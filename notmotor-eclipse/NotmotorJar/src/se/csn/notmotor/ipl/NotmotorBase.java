/**
 * Skapad 2007-mar-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl;

import javax.sql.DataSource;

import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.db.QueryListenerImpl;
import se.csn.notmotor.ipl.db.QueryProcessor;
import se.csn.notmotor.ipl.db.SingleThreadConnectionQueryProcessor;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;

/**
 * Huvudklassen, ingangsklassen for notifieringsmotorn. Denna klass
 * instantierar och kopplar ihop alla andra klasser. 
 */
public class NotmotorBase {

    private Log log = Log.getInstance(NotmotorBase.class);

    protected static final String PROPERTYFIL = "notmotor-ipl";
    protected static final String PROPERTYFIL_ARK = "ark";

    protected static final String NOTMOTOR_DS_JNDI_PROP = "notmotor.ds.jndinamn";
    protected static final String REFRESHTID_PROP = "param.refreshtid";

    /**
     * Skapar ny instans av NotmotorBase. 
     *
     */
    public NotmotorBase() {

    }

    protected QueryProcessor getQP(DataSource ds, String engine) {
        QueryProcessor qp = new SingleThreadConnectionQueryProcessor(ds);
        qp.addQueryListener(new QueryListenerImpl(engine));
        return qp;
    }

    /**
     * Metoden satter alla statusrader som matchar URL:en till STOPPED.
     * Nodvandigt ifall nagon process dor okontrollerat.
     * Ska alltsa bara anropas en gang per webbapplikationsinstans.
     * @param anropadURL anropande process
     * @param qp QueryProcessor
     */
    protected void stangOppnaInstanser(String anropadURL, QueryProcessor qp) {
        String sql = "UPDATE STATUS SET STATUS="
            + MeddelandeStateMachineBase.STOPPED
            + " WHERE SERVER="
            + "(SELECT ID FROM SERVER WHERE NOTMOTORSERVLETURL='" + anropadURL + "')"
            + " AND STATUS <>" + MeddelandeStateMachineBase.STOPPED;
        log.info("Stänger ej stangda instanser, sql: " + sql);

        int result = qp.executeThrowException(sql);

        log.debug("Stängde " + result + " instanser.");
    }

    /**
    * Soker ut alla meddelanden med negativ status (dvs. meddelanden
    * som ar markerade for pagaende sandning) och satter om dem till 
    * status MOTTAGET.
    * Ska alltsa bara anropas en gang per webbapplikationsinstans.
    * @param qp QueryProcessor
    */
    protected void aterstallAvbrutnaSandningar(QueryProcessor qp) {
        String sql = "UPDATE MEDDELANDE SET STATUS = " + MeddelandeHandelse.MOTTAGET
        	+ " WHERE STATUS < 0";
        log.info("Återställer meddelandestatus för avbrutna sändningar, sql: " + sql);

        int result = qp.executeThrowException(sql);

        log.debug("Återställde " + result + " meddelanden.");
    }

}
