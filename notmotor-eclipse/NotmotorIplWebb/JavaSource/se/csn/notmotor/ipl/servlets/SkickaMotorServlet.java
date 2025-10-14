package se.csn.notmotor.ipl.servlets;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.Log;
import se.csn.common.servlet.RunControl;
import se.csn.notmotor.ipl.Notmotor;

//import com.ibm.websphere.servlet.event.ServletEvent;
//import com.ibm.websphere.servlet.event.ServletListener;

/**
 * Denna servlet tillhandahåller den tråd som notmotorn går i. 
 * Servleten hanterar två typer av anrop: ett testanrop (GET 
 * eller POST utan speciella parametrar) och ett starta-anrop
 * (parametern 'start' satt till 'true').
 * Vid testanropet returnerar servleten ett enkelt, html-formaterat
 * OK-meddelande. 
 * Vid start-anropet kommer servleten att flusha buffern för att 
 * skicka meddelande tillbaka till anropande tråd. Därefter startas 
 * notmotorn. 
 *   
 */
public class SkickaMotorServlet extends HttpServlet implements Servlet { //, ServletListener {
	private static final long serialVersionUID = 1L;
	private static final String PROPERTIESFIL = "notmotor-ipl";
    private Log log = Log.getInstance(SkickaMotorServlet.class);
    private RunControl runControl;
    
    public SkickaMotorServlet() {
		super();
		runControl = new RunControl();
	}

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest arg0, HttpServletResponse arg1)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    log.debug("SkickaMotorServlet doGet");
	    hanteraAnrop(req, resp);
	}

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest arg0, HttpServletResponse arg1)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    log.debug("SkickaMotorServlet doPost");
	    hanteraAnrop(req, resp);
	}
	
	private void hanteraAnrop(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	    String start = req.getParameter("start");
	    skickaSvar(req, resp);
	    if ((start != null) && (start.equalsIgnoreCase("true"))) {
	        startaNotmotor(req.getRequestURL().toString());
	    } else {
	        log.debug("Skickat svar på förfrågan från " + req.getRemoteHost());
	    }
	}
	
	private void skickaSvar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	    resp.getWriter().write("<html><body>");
	    resp.getWriter().write("SkickaMotorServlet<br/>");
	    resp.getWriter().write("Context path: " + req.getContextPath() + "<br/>");
	    resp.getWriter().write("URI: " + req.getRequestURI() + "<br/>");
	    resp.getWriter().write("Server: " + req.getServerName() + "<br/>");
	    resp.getWriter().write("Port: " + req.getServerPort() + "<br/>");
	    resp.getWriter().write("URL: " + req.getRequestURL() + "<br/>");
	    resp.getWriter().write("</body></html>");
	}
	
	public void init(ServletConfig config) throws ServletException {
	    log.debug("init");
	    // Kontrollera om autostart är aktiverad och isåfall hur många instanser av Notmotorn
	    // som ska startas
	    int autostart = Properties.getIntProperty(PROPERTIESFIL, "notmotor.autostart", 0);
	    // Om autostart > 0, starta önskat antal instanser av Notmotorn
	    if (autostart > 0) {
	    	log.info("Autostart av " + autostart + " Notmotor-instanser ...");
	    	String url = Properties.getProperty(PROPERTIESFIL, "notmotor.url");
	    	for (int i = 0; i < autostart; i++) {
	    		startaNotmotor(url);
	    		try {
	    			// Vänta 1000 millisekunder innan nästa instans startas.
	    			// För att jobb-namnet för tråden ska bli unik får inte två instanser
	    			// startas inom samma millisekund.
	    			Thread.sleep(1000);
	    		} catch (InterruptedException e) {
	    			log.warn("sleep avbröts", e);
					// Fortsätt ändå
				}
	    	}
	    } else {
	    	log.info("Autostart inaktiverad. Ingen Notmotor-instans startas.");
	    }
	}
	
    
    void startaNotmotor(String anropadURL) {
        log.info("Startar ny notmotorinstans");
        long tid = Calendar.getInstance().getTimeInMillis();
        try {
	        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
	        Scheduler sched = schedFact.getScheduler();
	        sched.start();
	
	        JobDetail jobDetail = new JobDetail("myJob" + tid,
	                                            null,
	                                            Notmotor.class);
	        jobDetail.getJobDataMap().put("url", anropadURL);
	        jobDetail.getJobDataMap().put("runCon", runControl);
	        
	        Trigger trigger = TriggerUtils.makeImmediateTrigger(0, 0);
	        trigger.setName("myTrigger" + tid);
	        
	        sched.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException se) {
        	log.error("Kunde inte starta ny instans av notmotorn", se);
        }
    }
    
	public void destroy() {
		log.info("SkickaMotorServlet Destroying...");
		if (runControl != null) {
			runControl.setRunning(false);
		}
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
