package se.csn.notmotor.ipl.servlets;

import java.util.Iterator;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.Log;
import se.csn.common.config.ClassDependencyTester;
import se.csn.common.config.PropertyDependencyTester;
import se.csn.common.jndi.ServiceLocator;
//import se.csn.notmotor.ipl.db.DAOAvsandareImpl;
//import se.csn.notmotor.ipl.db.DAOBilagaImpl;
//import se.csn.notmotor.ipl.db.DAOHandelseImpl;
//import se.csn.notmotor.ipl.db.DAOMeddelande;
//import se.csn.notmotor.ipl.db.DAOMeddelandeImpl;
//import se.csn.notmotor.ipl.db.DAOMottagareImpl;
import se.csn.notmotor.ipl.db.DAOServer;
import se.csn.notmotor.ipl.db.DAOServerImpl;
import se.csn.notmotor.ipl.db.QueryListenerImpl;
import se.csn.notmotor.ipl.db.QueryProcessor;
import se.csn.notmotor.ipl.db.QueryProcessorImpl;
import se.csn.notmotor.ipl.ft.NotifieringProxyFactory;
import se.csn.notmotor.ipl.ft.NotifieringProxyFactoryImpl;
import se.csn.notmotor.ipl.model.Server;
import se.csn.notmotor.ipl.webservice.SkickaService;

//import com.ibm.websphere.servlet.event.ServletEvent;
//import com.ibm.websphere.servlet.event.ServletListener;

/**
 * Servlet som ska koras vid startup; anvands for att starta notmotorinstans.
 */
public class InitServlet extends HttpServlet implements Servlet { //, ServletListener {
	private static final long serialVersionUID = 1L;
    private static final String PROPERTIESFIL = "notmotor-ipl";
    private static Log log = Log.getInstance(InitServlet.class);
    
	public InitServlet() {
		super();
	}

	/**
	 * Testar externa beroenden, satter upp beroenden, 
	 * startar en notmotorinstans. Detta gors mha en trad. 
	 */
	public void init(ServletConfig config) throws ServletException {
	    checkExternalDependencies();
	    setupDependencies();
	    
	    // Lägger upp serverrad i DB om den inte redan fanns:
	    lagraServerIDB(Properties.getProperty(PROPERTIESFIL, "notmotor.url"));
	}

	private void setupDependencies() {
	    System.out.println("Sätter upp dependencies för Notmotor");
        ServiceLocator sl = new ServiceLocator();
        String datasourceJndi = Properties.getProperty(PROPERTIESFIL, "notmotor.ds.jndinamn");
        DataSource ds = sl.getDatasource(datasourceJndi);

        QueryProcessor qp = new QueryProcessorImpl(ds);
        qp.addQueryListener(new QueryListenerImpl("WEBSERVICE"));
        NotifieringProxyFactory factory = new NotifieringProxyFactoryImpl(ds);
        SkickaService.setFactory(factory);
	}
	
    public void checkExternalDependencies() {
	    System.out.println("Kontrollerar dependencies för Notmotor");
        try {
            // 0. Kolla att utils finns:
            Class.forName("se.csn.common.config.ConfigException");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Kunde inte ens hitta utils, klassen se.csn.common.config.ConfigException finns inte...", e);
        }
        String[][] CLASSTEST = new String[][] {
                ClassDependencyTester.LOG4J,
                ClassDependencyTester.ARKALL,
                ClassDependencyTester.COMMONS_LANG,
        };
        ClassDependencyTester.findClassesThrowException(CLASSTEST);
        
        String[] properties = new String[] {
                "mail.user",
                "mail.password",
                "mail.host",
                "mail.port",
                "mail.timeout",
                "param.refreshtid",
                "notmotor.url",
                "notmotor.autostart",
                "notmotor.ds.jndinamn",
                "sms.user",
                "sms.password", 
                "sms.endpoint",
        };
        
        // Anropa med propertiesfilen som argument för att sätta upp
        // Log-klassens interna cache:
        Properties.getProperty(PROPERTIESFIL, "mail.user");
        PropertyDependencyTester.dumpPropertiesThrowIfMissing(properties);
        
        log.info("Kontrollerat dependencies");
    }
    
    void lagraServerIDB(String url) {
        ServiceLocator sl = new ServiceLocator();
        String datasourceJndi = Properties.getProperty(PROPERTIESFIL, "notmotor.ds.jndinamn");
        DataSource ds = sl.getDatasource(datasourceJndi);

        QueryProcessor qp = new QueryProcessorImpl(ds);
        DAOServer dao = new DAOServerImpl(qp);
        List serverlist = dao.getAktiva(true);
        for (Iterator it = serverlist.iterator(); it.hasNext();) {
            Server server = (Server) it.next();
            if (url.equalsIgnoreCase(server.getServleturl())) {
                return;
            }
        }
        
        // Hittade ingen server: lägger upp.
        Server server = new Server();
        server.setAktiv(true);
        server.setPrestanda(1);
        server.setServleturl(url);
        dao.skapa(server);
        log.info("Lade upp serverrad i databasen, url: " + url);
    }
    
    public void destroy() {
		log.info("InitServlet Destroying...");
	}
    
//    public void onServletAvailableForService(ServletEvent arg0) {
//        log.debug(arg0.getServletClassName() + "availableForService()");
//    }
//    public void onServletFinishDestroy(ServletEvent arg0) {
//        log.debug(arg0.getServletClassName() + "finishDestroy()");
//    }
//    public void onServletFinishInit(ServletEvent arg0) {
//        log.debug(arg0.getServletClassName() + "finishInit()");
//    }
//    public void onServletStartDestroy(ServletEvent arg0) {
//        log.debug(arg0.getServletClassName() + "startDestroy()");
//    }
//    public void onServletStartInit(ServletEvent arg0) {
//        log.debug(arg0.getServletClassName() + "availableForService()");
//    }
//    public void onServletUnavailableForService(ServletEvent arg0) {
//        log.debug(arg0.getServletClassName() + "unavailableForService()");
//    }
//    public void onServletUnloaded(ServletEvent arg0) {
//        log.debug(arg0.getServletClassName() + "unloaded()");
//    }

}
