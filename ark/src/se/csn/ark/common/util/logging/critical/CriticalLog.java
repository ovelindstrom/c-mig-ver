/*
 * Created on 2005-jan-24
 *
 */
package se.csn.ark.common.util.logging.critical;

import org.apache.log4j.Category;

import se.csn.ark.common.util.logging.LogLevel;

/**
 * Logger för att skriva kritiska systemfel till en egen loggfil.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20050124
 * @version 1 skapad
 *
 */
public final class CriticalLog {

	//private static final String CRITICAL_LOGGER = "Critical Logger";

	private static CriticalLog criticalLogger;
	
	/**
	 * privat konstruktor, endast static access
	 */
	private CriticalLog() {

		log = Category.getInstance(CriticalLog.class);
	}

	/**
	 * Category är log4j klassen som används för loggningen av denna log wrapper.
	 */
	private static Category log;
	
	/**
	 * Ger en critical-logger-instans.
	 * 
	 * @return logger för critical-fel.
	 */
	public static CriticalLog getLoggerInstance() {
		
		if (criticalLogger == null) {
			criticalLogger = new CriticalLog();
		}
		return criticalLogger;
	}
		
	/**
	 * Loggar ett meddelande med KRITISK prioritet.
	 *
	 *
	 * @param message Objektet kommer att konverteras till String innan det skrivs till loggen.
	 */
	public void critical(Object message) {
	
		log.log(LogLevel.CRITICAL, message);
	}

	/**
	 * Loggar ett meddelande med KRITISK prioritet.
	 *
	 * @param message Objektet kommer att konverteras till String innan det skrivs till loggen.
	 * @param throwable Throwable som skall loggas
	 */
	public void critical(Object message, Throwable throwable) {
	
		log.log(LogLevel.CRITICAL, message, throwable);
	}

}
