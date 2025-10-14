/*
 * Created on 2005-apr-21
 *
 */
package se.csn.ark.common;

import java.io.Serializable;

import se.csn.ark.common.util.logging.trace.TraceRecord;
import se.csn.ark.common.util.logging.activity.ActivityRecord;

/**
 * Grundklassernas grundgränssnitt för CSN:s arkitekturramverk.
 * <br>
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 2005-04-21
 * @version 1 skapad
 * @version 2 2007-04-13 E-tjänster 2007 Lagt till aktivitetsloggning csn7571
 *
 */
public interface CsnArkBaseObject {
	
	/**
	 * Indikerar om traceloggning är aktiverad.
	 *
	 * @return true om Traceloggen är påslagen.
	 */
	public boolean isTraceing();

	/**
	 * Skriver spårbarhetsinformation till loggen.
	 * 
	 * @param traceRecord Innehåller allt som skall loggas.
	 */
	public void trace(TraceRecord traceRecord);

	/**
	 * Indikerar om aktivitetsloggning är aktiverad.
	 *
	 * @return true om Activityloggen är påslagen.
	 */
	public boolean isTraceingActivities();

	/**
	 * Skriver spårbarhetsinformation till aktivitetsloggen.
	 * 
	 * @param activityRecord Innehåller allt som skall loggas.
	 */
	public void activity(ActivityRecord activityRecord);

	/**
	 * Indikerar om prestandaloggning är aktiverad.
	 *
	 * @return true om Prestandaloggen är påslagen.
	 */
	public boolean isTiming();

	/**
	 * Startar klockan för prestandaloggningen. Tick, tack, ...
	 *
	 * @param loggingObject Det objekt som loggar.
	 * @param methodName Namnet på metoden som loggar.
	 */
	public void startClock(CsnArkBaseObjectImpl loggingObject, String methodName);

	/**
	 * Stoppar klockan för prestandaloggningen.
	 *
	 */
	public void stopClock();
	

}
