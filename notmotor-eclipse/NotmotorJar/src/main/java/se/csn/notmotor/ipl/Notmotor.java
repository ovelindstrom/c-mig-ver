/**
 * @since 2007-mar-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import javax.sql.DataSource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.PropertyException;
import se.csn.ark.common.util.logging.Log;
import se.csn.common.jndi.ServiceLocator;
import se.csn.common.servlet.RunControl;
import se.csn.notmotor.ipl.db.DAOAvsandare;
import se.csn.notmotor.ipl.db.DAOAvsandareImpl;
import se.csn.notmotor.ipl.db.DAOBilaga;
import se.csn.notmotor.ipl.db.DAOBilagaImpl;
import se.csn.notmotor.ipl.db.DAOHandelse;
import se.csn.notmotor.ipl.db.DAOHandelseImpl;
import se.csn.notmotor.ipl.db.DAOMeddelande;
import se.csn.notmotor.ipl.db.DAOMeddelandeImpl;
import se.csn.notmotor.ipl.db.DAOMottagare;
import se.csn.notmotor.ipl.db.DAOMottagareImpl;
import se.csn.notmotor.ipl.db.DAOStatus;
import se.csn.notmotor.ipl.db.DAOStatusImpl;
import se.csn.notmotor.ipl.db.ParameterCache;
import se.csn.notmotor.ipl.db.ParameterKalla;
import se.csn.notmotor.ipl.db.QueryProcessor;
import se.csn.notmotor.ipl.db.SingleThreadConnectionQueryProcessor;
import se.csn.notmotor.ipl.model.Status;
import se.csn.notmotor.ipl.sms.SMSTjaenst;

/**
 * Huvudklassen, ingångsklassen för notifieringsmotorn. Denna klass
 * instantierar och kopplar ihop alla andra klasser. 
 */
public class Notmotor extends NotmotorBase implements Job {

    private Log log = Log.getInstance(Notmotor.class);

    private static final String MAIL_USER_PROP = "mail.user",
    							MAIL_PASSWORD_PROP = "mail.password",
    							MAIL_HOST_PROP = "mail.host",
    							MAIL_PORT_PROP = "mail.port",
								MAIL_TIMEOUT_PROP = "mail.timeout",
								SMS_USER_PROP = "sms.user",
								SMS_PASSWORD_PROP = "sms.password", 
								SMS_ENDPOINT_PROP = "sms.endpoint",
    							MMM_ENDPOINT_PROP = "tieto.endpoint",
    							MMM_CERTIFIKAT_PEM = "tieto.certifikat",
    							MMM_NYCKEL_PKCS8 = "tieto.nyckel";
    
    private static final String INSTANSNAMN = "NOTMOTOR #";
    private static final String TYP = "NOTMOTOR";
    private static int engineCount = 0;
    private static boolean firstCall = true;
    
    /**
     * Konstruktor.
     */
    public Notmotor() {
        super();
    }
    
    
    /**
     * Skapar ny instans av Notmotorn. 
     * Detta konstruktoranrop kommer inte att terminera i vanlig ordning. 
     * Tråden kommer att gå in i en loop som bara kan brytas om databasen sätter 
     * status till STOPPING eller STOPPED eller om det booleska värdet i runControl 
     * sätts till false
     * 
     * anropadURL - Den URL som anropades för att starta anropande servlet. 
     *     Denna parameter används för att koppla notmotorn till en Server.
     * runControl - Handle som anropande kod kan använda för att stoppa denna
     *     notmotor.
     * @param context JobExecutionContext
     * @throws JobExecutionException e
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	String anropadURL = context.getJobDetail().getJobDataMap().getString("url");
    	RunControl runControl = (RunControl) context.getJobDetail().getJobDataMap().get("runCon");
    	init(anropadURL, runControl);
    }
    
    
    /**
     * Sätter upp ny notmotorinstans.
     * @param anropadURL anropande process
     * @param runControl RunControl
     */
    public void init(String anropadURL, RunControl runControl) {
        engineCount++;
        log.info("Sätter upp ny instans, nr " + engineCount);
        
		// Hämta och kontrollera kopplingar till databas
        ServiceLocator sl = new ServiceLocator();
        String datasourceJndi = Properties.getProperty(NOTMOTOR_DS_JNDI_PROP);
        DataSource ds = sl.getDatasource(datasourceJndi);
        
        long refreshtid;
        try {
            refreshtid = Properties.getLongProperty(REFRESHTID_PROP);
        } catch (PropertyException e1) {
            throw new IllegalStateException("Kunde inte läsa property 'param.refreshtid', var inte ett giltigt tal");
        }
        log.debug("Läst refreshtid: " + refreshtid);
        
        // Skapa DAO-objekt
        ParameterKalla paramkalla = new ParameterCache(getQP(ds, INSTANSNAMN + engineCount), refreshtid);
        DAOHandelse daohandelse = new DAOHandelseImpl(getQP(ds, INSTANSNAMN + engineCount));
        DAOAvsandare daoavs = new DAOAvsandareImpl(getQP(ds, INSTANSNAMN + engineCount));
        DAOMottagare daomottagare = new DAOMottagareImpl(getQP(ds, INSTANSNAMN + engineCount));
        DAOBilaga daobilaga = new DAOBilagaImpl(getQP(ds, INSTANSNAMN + engineCount));
        
        DAOMeddelande daomeddelande = new DAOMeddelandeImpl(getQP(ds, INSTANSNAMN + engineCount), 
                daoavs, daomottagare, daobilaga, daohandelse); 
        
        log.debug("Skapat dao-objekt");

        // Skapa kopplingar till mailtjänst
        String user = Properties.getProperty(PROPERTYFIL, MAIL_USER_PROP);
        String password = Properties.getProperty(PROPERTYFIL, MAIL_PASSWORD_PROP);
        String server = Properties.getProperty(PROPERTYFIL, MAIL_HOST_PROP);
        int port;
        try {
            port = Properties.getIntProperty(PROPERTYFIL, MAIL_PORT_PROP);
        } catch (PropertyException e) {
            throw new IllegalStateException("Kunde inte läsa property '" + MAIL_PORT_PROP 
                    + "', var inte ett giltigt tal");
        }
        int timeout;
        try {
            timeout = Properties.getIntProperty(PROPERTYFIL, MAIL_TIMEOUT_PROP);
        } catch (PropertyException e) {
            throw new IllegalStateException("Kunde inte läsa property '" + MAIL_TIMEOUT_PROP 
                    + "', var inte ett giltigt tal");
        }
        MeddelandeSender epostSender = new EpostMeddelandeSenderImpl(user, password, server, port, timeout);
        log.debug("Skapat epost-sender");
        
        // Skapa kopplingar till sms-tjänst
        String smsuser = Properties.getProperty(PROPERTYFIL, SMS_USER_PROP);
        String smspassword = Properties.getProperty(PROPERTYFIL, SMS_PASSWORD_PROP);
        String smsEndpoint = Properties.getProperty(PROPERTYFIL, SMS_ENDPOINT_PROP);
        
        SMSTjaenst smstjaenst = new SMSTjaenst(smsEndpoint, smsuser, smspassword);
        MeddelandeSender smsSender = new SMSMeddelandeSenderImpl(smstjaenst);
        log.debug("Skapat sms-sender");
        
        //CombinedMeddelandeSenderImpl sender = new CombinedMeddelandeSenderImpl();
        //sender.addSender(epostSender);
        //sender.addSender(smsSender);
        
        //Lägg till kopplingar som behövs för anrop mot MINAMEDDELANDEN +++
        
        //String mmmEndpoint = Properties.getProperty(PROPERTYFIL, MMM_ENDPOINT_PROP);
        /* Disable code for MMM sender as these type of messages is no longer handled by Notmotor
         * Petrus Bergman 2022-08-17
        URI certifikatPEM;
		try {
			certifikatPEM = new URI(Properties.getProperty(PROPERTYFIL, MMM_CERTIFIKAT_PEM));
		} catch (URISyntaxException e1) {
			throw new IllegalStateException("Kunde inte läsa property '" + MMM_CERTIFIKAT_PEM 
                    + "', var inte en giltig adress");
		}
        URI nyckelPKCS8;
        
		try {
			nyckelPKCS8 = new URI(Properties.getProperty(PROPERTYFIL, MMM_NYCKEL_PKCS8));
		} catch (URISyntaxException e1) {
			throw new IllegalStateException("Kunde inte läsa property '" + MMM_NYCKEL_PKCS8 
                    + "', var inte en giltig adress");
		}
        
        String mmmEndpoint = Properties.getProperty(PROPERTYFIL, MMM_ENDPOINT_PROP);
        log.debug("mmmEndpoint " + mmmEndpoint);
        
        String dataServiceEndpoint = Properties.getProperty(PROPERTYFIL, "dataservice.hamtapersonnummer");
        
        MeddelandeSender mmmSender = new MMMeddelandeSenderImpl(mmmEndpoint, dataServiceEndpoint, certifikatPEM, nyckelPKCS8);
        log.debug("Skapat mmm-sender");
        */

        // Stäng öppna instanser om första anropet:
        QueryProcessor qp = getQP(ds, INSTANSNAMN + engineCount);
        if (firstCall) {
            stangOppnaInstanser(anropadURL, qp);
            aterstallAvbrutnaSandningar(qp);
            firstCall = false;
        }
        
        // Sätt statusrad i databas
        // Måste söka ut servernummer:
        //qp.addQueryListener(new QueryListenerImpl("NOTMOTOR:"));
        int servernr = qp.getInt("SELECT ID FROM SERVER WHERE NOTMOTORSERVLETURL='" + anropadURL + "'", -1);
        if (servernr == -1) {
            log.error("Kunde inte hitta server med URL " + anropadURL + " - sätter servernr till -1");
        }
        Status status = new Status(-1, SkickaMeddelandeStateMachine.INIT, servernr, 
                					new Date(), null, new Date(), TYP);
        DAOStatus daostatus = new DAOStatusImpl(qp);
        daostatus.skapa(status);
        log.debug("Sparat statusrad");
        
        SkickaMeddelandeServicesImpl services = new SkickaMeddelandeServicesImpl(
                new SingleThreadConnectionQueryProcessor(ds), 
                paramkalla, daomeddelande, daohandelse, daomottagare, status.getInstans());
        // Lägg till senders:
        services.addMeddelandeSender(epostSender);
        services.addMeddelandeSender(smsSender);
        //services.addMeddelandeSender(mmmSender);
        
        SkickaMeddelandeStateMachine sm = new SkickaMeddelandeStateMachine(services, runControl);

        log.debug("Skapat state machine, instansnummer " + status.getInstans());
        
        log.info("Startar state machine");
        sm.run();
    }
    
}
