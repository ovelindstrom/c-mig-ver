package se.csn.ark.common.servlet.jsp.tagext;

import java.io.Serializable;

import javax.servlet.jsp.tagext.TagSupport;

import se.csn.ark.common.CsnArkBaseObject;
import se.csn.ark.common.CsnArkBaseObjectImpl;
import se.csn.ark.common.util.logging.trace.TraceRecord;
import se.csn.ark.common.util.logging.activity.ActivityRecord;

/**
 * Basklass för taggar. Är av denna för att skape en ny Tag.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20050225
 * @version 1 skapad
 * @version 2 2007-04-27 Uppdaterad av csn7571 E-tjänster, activitylogg
 *
 */
public class CsnTagSupport extends TagSupport implements CsnArkBaseObject {

	/**
	 * 
	 */
	public CsnTagSupport() {
		super();
	}

	//*** Implementation av se.csn.ark.common.CsnArkBaseObject - start ***

	private CsnArkBaseObjectImpl arkBaseObjectImpl = 
			new  CsnArkBaseObjectImpl();

	/**
	 * @return true om trace-loggning är påslagen
	 */
	public boolean isTraceing() {
		
		return arkBaseObjectImpl.isTraceing();
	}

	/**
	 * @param traceRecord data att traca
	 */
	public void trace(TraceRecord traceRecord) {

		arkBaseObjectImpl.trace(traceRecord);
	}
	/**
	 * @return true om trace-loggning är påslagen
	 */
	public boolean isTraceingActivities() {
		
		return arkBaseObjectImpl.isTraceingActivities();
	}

	/**
	 * @param traceRecord data att traca
	 */
	public void activity(ActivityRecord activityRecord) {

		arkBaseObjectImpl.activity(activityRecord);
	}

	/**
	 * @return true om time-loggning är påslagen
	 */
	public boolean isTiming() {
		
		return arkBaseObjectImpl.isTiming();
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


	//*** Implementation av se.csn.ark.common.CsnArkBaseObject - slut ***


}
