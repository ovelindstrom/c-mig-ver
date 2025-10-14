package se.csn.ark.common.servlet;

import java.io.Serializable;

import javax.servlet.http.HttpServlet;

import se.csn.ark.common.CsnArkBaseObjectImpl;
import se.csn.ark.common.util.logging.activity.ActivityRecord;
import se.csn.ark.common.util.logging.trace.TraceRecord;
/**
 * Basklass för Servlets.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041212
 * @version 1 skapad
 * @version 2 2007-04-27 Uppdaterad av csn7571 E-tjänster, activitylogg
 *
 */
public class CsnServletImpl extends HttpServlet implements CsnServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5220524143492355765L;

	/**
	 * skapa instans
	 */
	public CsnServletImpl() {
		super();
	}

	//*** Implementation av se.csn.ark.common.CsnArkBaseObject - start ***

	private CsnArkBaseObjectImpl arkBaseObjectImpl = 
			new  CsnArkBaseObjectImpl();

	/**
	 * @return true om time-loggning är påslagen
	 */
	public boolean isTiming() {
		
		return arkBaseObjectImpl.isTiming();
	}

	/**
	 * @return true om trace-loggning är påslagen
	 */
	public boolean isTraceing() {
		
		return arkBaseObjectImpl.isTraceing();
	}
	/**
	 * @return true om trace-loggning är påslagen
	 */
	public boolean isTraceingActivities() {
		
		return arkBaseObjectImpl.isTraceingActivities();
	}

	
	/**
     * Starta time-log-klocka
     * 
     * @param loggingObject objektet som loggar 
     * @param methodName metoden som klockas
	 */
	public void startClock(CsnArkBaseObjectImpl loggingObject, String methodName) {		
		arkBaseObjectImpl.startClock(loggingObject, methodName);
	}

	
	/**
     * stoppa time-log-klocka
	 */
	public void stopClock() {
		
		arkBaseObjectImpl.stopClock();
	}

	/**
     * @param traceRecord data att traca
	 */
	public void trace(TraceRecord traceRecord) {

		arkBaseObjectImpl.trace(traceRecord);
	}

	/**
     * @param traceRecord data att traca
	 */
	public void activity(ActivityRecord activityRecord) {

		arkBaseObjectImpl.activity(activityRecord);
	}
	//*** Implementation av se.csn.ark.common.CsnArkBaseObject - slut ***

}
