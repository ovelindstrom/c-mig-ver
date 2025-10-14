package se.csn.ark.common;

import java.io.Serializable;

import se.csn.ark.common.util.logging.time.TimeLog;
import se.csn.ark.common.util.logging.trace.TraceLog;
import se.csn.ark.common.util.logging.trace.TraceRecord;
import se.csn.ark.common.util.logging.activity.ActivityLog;
import se.csn.ark.common.util.logging.activity.ActivityRecord;
/**
 * Grundklassernas grundklass för CSN:s arkitekturramverk.
 * <br>
 * Basklasser i arkitekturen kan ärva av denna klass för att
 * därmed enkelt kunna använda grundläggande funktionalitet.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 2005-01-22
 * @version 1 Skapad K-G Sjöström
 * @version 2 Förändringar K-G Sjöström
 * 			Ändringar i trace gränssnittet.
 * @version 3 2007-04-27 Uppdaterad av csn7571 E-tjänster, activitylogg
 *
 */
public class CsnArkBaseObjectImpl implements CsnArkBaseObject {
	
	private Integer timeLogId;
	private TimeLog timeLog;
	private TraceLog traceLog = TraceLog.getLoggerInstance();
	private ActivityLog activityLog = ActivityLog.getLoggerInstance();
	private Object loggingObject;

	/**
	 * Indikerar om traceloggning är aktiverad.
	 *
	 * @return true om Traceloggen är påslagen.
	 */
	public boolean isTraceing() {

		return TraceLog.isTraceing();
	}

	/**
	 * Skriver spårbarhetsinformation till loggen.
	 * 
	 * @param traceRecord Innehåller allt som skall loggas.
	 */
	public void trace(TraceRecord traceRecord) {

		traceLog.trace(traceRecord);
	}

	/**
	 * Indikerar om activityloggning är aktiverad.
	 *
	 * @return true om Aktivitetsloggen är påslagen.
	 */
	public boolean isTraceingActivities() {

		return ActivityLog.isTraceingActivities();
	}

	/**
	 * Skriver spårbarhetsinformation till aktivitetsloggen.
	 * 
	 * @param traceRecord Innehåller allt som skall loggas.
	 */
	public void activity(ActivityRecord activityRecord) {

		activityLog.activity(activityRecord);
	}

	/**
	 * Indikerar om prestandaloggning är aktiverad.
	 *
	 * @return true om Prestandaloggen är påslagen.
	 */
	public boolean isTiming() {
		
		return TimeLog.isTiming();
	}

	/**
	 * Startar klockan för prestandaloggningen. Tick, tack, ...
	 *
	 * @param object Det objekt som loggar.
	 * @param methodName Namnet på metoden som loggar.
	 */
	public void startClock(CsnArkBaseObjectImpl object, String methodName) {
		
		this.loggingObject = object;
		timeLog = TimeLog.getLogger(object);
		timeLogId = timeLog.startClock(methodName);
	}

	/**
	 * Stoppar klockan för prestandaloggningen.
	 *
	 */
	public void stopClock() {
		
		timeLog.stopClock(timeLogId);
	}

}