package se.csn.notmotor.ipl;

import java.util.Date;

import se.csn.ark.common.dal.db.DatabaseException;
import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.db.ControlledCommitQueryProcessor;
import se.csn.notmotor.ipl.db.DAOMeddelande;
import se.csn.notmotor.ipl.db.DAOStatus;
import se.csn.notmotor.ipl.db.DAOStatusImpl;
import se.csn.notmotor.ipl.db.ParameterKalla;
import se.csn.notmotor.ipl.db.SingleThreadConnectionQueryProcessor;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.Status;


public class MeddelandeServicesImplBase implements MeddelandeServicesBase {

    protected ControlledCommitQueryProcessor qp;
    protected ParameterKalla paramSource;
    protected int instansnummer;
    public static final int DEFAULT_SOVTID = 0;
    public static final int DEFAULT_TICKTID = 1;
    // Den längsta tid i sekunder som får förlöpa mellan uppdatering av watchdog-timern.
    // Denna tid styr hur ofta watchdogfältet uppdateras.
    public static final int DEFAULT_WATCHDOGTID = 120;
    // Denna tid styr den kortast tillåtna watchdogtiden. Den är satt så att de längsta operationerna
    // ska hinna fullbordas på kortare tid. Ändra inte utan att kolla upp hur lång tid som 
    // går åt t.ex. för att slå upp en batch meddelanden.
    public static final int MIN_WATCHDOGTID = 20;

    public static final int DEFAULT_BATCHSTORLEK = 100;

    private long senastUppdateradWatchdog;

    private MeddelandeSender meddelandeSender;

    protected DAOMeddelande daomeddelande;
    protected DAOStatus daostatus;

    private final Log log = Log.getInstance(MeddelandeServicesImplBase.class);

    public MeddelandeServicesImplBase(ControlledCommitQueryProcessor qp, ParameterKalla paramSource,
                                      DAOMeddelande meddelandehandler, int instansnummer) {
        if (qp == null) {
            throw new IllegalArgumentException("QueryProcessor får inte vara null");
        }
        if (paramSource == null) {
            throw new IllegalArgumentException("parameterkälla får inte vara null");
        }
        if (meddelandehandler == null) {
            throw new IllegalArgumentException("MeddelandeHandler får inte vara null");
        }

        this.qp = qp;
        this.paramSource = paramSource;
        this.daomeddelande = meddelandehandler;
        this.instansnummer = instansnummer;
        daostatus = new DAOStatusImpl(qp);
    }

    public void updateParameters() {
        paramSource.reload();
    }


    /*
     * 
     * @return true om nuvarande tidpunkt befinner sig i ett schemalagt avbrott 
     */
    public boolean inScheduledPause() {
        try {
            int radcount = qp.getInt("SELECT COUNT(*) FROM KORSCHEMA WHERE"
                + " STANGNINGSTID < CURRENT TIMESTAMP AND OPPNINGSTID > CURRENT TIMESTAMP", 0);
            return radcount > 0;
        } catch (Exception e) {
            log.error("inScheduledPause: Fångat exception, returnerar FALSE: ", e);
            return false;
        }
    }

    /*
     * Satter faltet WATCHDOGTIMESTAMP i tabellen STATUS for denna instans till nuvarande tid. 
     * Detta falt anvands for att kontrollera att alla instanser lever och gor det de ska.
     * En servlet laser faltet for alla aktiva instanser och kollar att de lever.
     */
    public void updateWatchdogFlag() {
        try {
            int wdtid = paramSource.getIntParam("WATCHDOGTID", DEFAULT_WATCHDOGTID) * 1000;
            if ((System.currentTimeMillis() - senastUppdateradWatchdog) >= wdtid) {
                String sql = "UPDATE STATUS SET WATCHDOGTSTAMP = CURRENT TIMESTAMP WHERE INSTANS = " + instansnummer;
                log.debug("UpdateWDT SQL: " + sql);
                qp.executeThrowException(sql);
                senastUppdateradWatchdog = System.currentTimeMillis();
            }
        } catch (Exception e) {
            log.error("updateWatchdogFlag: Fångat exception: ", e);
        }
    }

    /*
     * Kastar RuntimeException om sovandet misslyckades
     * @param milliseconds int
     */
    public void sleep(int milliseconds) {
        if (milliseconds < 0) {
            throw new IllegalArgumentException("Sovtiden måste vara >= 0");
        }
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // Logga till fellogg
            log.error("Avbruten under sleep()", e);
        }
    }

    void sleepAndUpdate(int sovtid, int watchdogtid) {
        try {
            if (watchdogtid < MIN_WATCHDOGTID) {
                throw new IllegalArgumentException("Orimligt kort watchdogtid: " + watchdogtid);
            }
            // Börja med att uppdatera watchdog, vi vet ju inte hur länge sen det var sist
            updateWatchdogFlag();

            // Om sovtiden är större än watchdogtiden:
            // Sov watchdogtiden
            // Uppdatera flaggan
            // Minska sovtiden med watchdogtiden
            // upprepa
            while (sovtid > watchdogtid) {
                //System.out.println("Sovtid kvar: " + sovtid);
                sleep(watchdogtid);
                updateWatchdogFlag();
                sovtid -= watchdogtid;
            }

            // Avsluta med att sova kvarstående sovtid
            sleep(sovtid);
        } catch (Exception e) {
            log.error("sleepAndUpdate: Fångat exception: ", e);
        }

    }

    public boolean makeTransition(int fromState, int toState) {
        // Skriv status till databas
        //log.debug("Sätter status för instans " + instansnummer + " till " + status);
        // Kontrollera att vi befinner oss i rätt tillstånd:
        if (getStatus() != fromState) {
            return false;
        }
        try {
            qp.executeThrowException("UPDATE STATUS SET STATUS = " + toState + " WHERE INSTANS = " + instansnummer);
            return true;
        } catch (DatabaseException de) {
            log.error("Kunde inte skriva status " + toState + " till databas", de);
            return false;
        }
    }


    /*
     * Laser den rad i STATUS-tabellen som matchar denna instans
     * @return aktuell status som den är satt i databasen för den här instansen
     */
    public int getStatus() {
        // Läs status i statustabellen
        int newStatus = MeddelandeStateMachineBase.UNKNOWN;
        try {
            newStatus = qp.getInt("SELECT STATUS FROM STATUS WHERE INSTANS = " + instansnummer, -1);
            if (newStatus == -1) {
                throw new DatabaseException("Kunde inte läsa status för instans " + instansnummer);
            }
        } catch (Exception e) {
            log.error("Kunde inte läsa status", e);
            return MeddelandeStateMachineBase.UNKNOWN;
        }
        return newStatus;
    }

    public void sleepTick() {
        try {
            int wdtid = paramSource.getIntParam("WATCHDOGTID", DEFAULT_WATCHDOGTID) * 1000;
            int sovtid = paramSource.getIntParam("TICKTID", DEFAULT_TICKTID) * 1000;

            sleepAndUpdate(sovtid, wdtid);
        } catch (Exception e) {
            log.error("sleepTick: Fångat exception: ", e);
        }
    }

    public void sleepWaittime() {
        try {
            int wdtid = paramSource.getIntParam("WATCHDOGTID", DEFAULT_WATCHDOGTID) * 1000;
            int sovtid = paramSource.getIntParam("SOVTID", DEFAULT_SOVTID) * 1000;

            sleepAndUpdate(sovtid, wdtid);
        } catch (Exception e) {
            log.error("sleepWaittime: Fångat exception: ", e);
        }
    }


    public int getInstansnummer() {
        return instansnummer;
    }

    public void setInstansnummer(int instansnummer) {
        this.instansnummer = instansnummer;
    }

    public MeddelandeSender getMeddelandeSender() {
        return meddelandeSender;
    }

    public void setMeddelandeSender(MeddelandeSender meddelandeSender) {
        this.meddelandeSender = meddelandeSender;
    }

    protected String getMeddelandeSummary(Meddelande meddelande) {
        if (meddelande == null) {
            return "NULL";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("ID: ");
        if (meddelande.getId() != null) {
            sb.append(meddelande.getId().longValue());
        } else {
            sb.append("NULL");
        }
        sb.append(" Från: ");
        if (meddelande.getAvsandare() != null) {
            sb.append(meddelande.getAvsandare().getEpostadress());
        } else {
            sb.append("NULL");
        }
        sb.append(" Till: ");
        Mottagare[] mott = meddelande.getMottagare();
        if (mott != null) {
            for (int i = 0;i < mott.length;i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(mott[i].getAdress());
            }
        } else {
            sb.append("NULL");
        }

        return sb.toString();
    }


    /*
     * Soker ut alla meddelanden for denna instans med negativ status 
     * (dvs. meddelanden som ar markerade for pagaende sandning) 
     * och satter om dem till status MOTTAGET.
    */
    private void aterstallMeddelanden() {
        String sql = "UPDATE MEDDELANDE SET STATUS = " + MeddelandeHandelse.MOTTAGET
            + " WHERE STATUS = " + (-instansnummer);
        log.info("Återställer meddelandestatus för avbrutna sändningar, sql: " + sql);

        int result = qp.executeThrowException(sql);

        log.debug("Återställde " + result + " meddelanden.");
    }


    public void shutdown() {
        try {
            DAOStatus dao = new DAOStatusImpl(qp);
            Status s = dao.getStatus(instansnummer);
            s.setStoppad(new Date());
            s.setStatus(MeddelandeStateMachineBase.STOPPED);
            dao.uppdatera(s);
            aterstallMeddelanden();

            SingleThreadConnectionQueryProcessor stp = (SingleThreadConnectionQueryProcessor) qp;
            stp.removeConnectionForThisThread();
            log.info("Stängt ner instans " + instansnummer);
        } catch (Exception e) {
            log.error("shutdown: Fångat exception: ", e);
        }

    }
}
