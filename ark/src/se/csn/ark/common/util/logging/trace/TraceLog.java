package se.csn.ark.common.util.logging.trace;

import java.text.NumberFormat;

import org.apache.log4j.Category;

import se.csn.ark.common.util.FormatDate;
import se.csn.ark.common.util.FormatException;
import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.LogLevel;

/**
 *
 * Spårbarhetsloggningn som används för att kunna logga alla tjänster
 * för att kunna plocka ut statistik/spårbarhet för en person.
 * 
 * Används också för att logga inloggningen.
 * Både om det går bra vilken typ av e-legitimation som används och
 * vid fel, vad felet beror på.
 *   
 * @author Hasse Zetterberg
 * @since 2005-02-04
 * @version 2 Förbättringar - K-G Sjöström AcandoFrontec
 *          Nu trådsäker och inget attribut sätts i själva loggklassen. 
 *          Enklare gränssnitt då man loggar TraceRecord.
 * @version 1 Skapad - Hasse Zetterberg
 */
public final class TraceLog {
		
	private static boolean traceLog = false;

	static {
		
		init();
	}
	
	/**
	 * initiera
	 */
	private static void init() {
		
		traceLog = Properties.getBooleanProperty("ark_log4j", 
			"tracelog", false);		
	}
	
	/**
	 * Laddar in egenskaper för denna loggklass på nytt.
	 */
	public static void reloadProperties() {
		
		init();
	}
	
	/**
	 * Category är log4j klassen som används för loggningen av denna log wrapper.
	 */
	private static Category log;
		
	private static TraceLog traceLogger;
	
	/**
	 * Instans endast via getLoggerInstance
	 */
	private TraceLog() {

		log = Category.getInstance(TraceLog.class);
	}

	/**
	 * Indikerar om spårbarhetsloggning är aktiverad.
	 *
	 * @return true om Spårbarhetsloggen är påslagen.
	 */
	public static boolean isTraceing() {
		
		return traceLog;
	}
		
	/**
	 * Ger en trace-logger.
	 * 
	 * @return trace-logger
	 */
	public static synchronized TraceLog getLoggerInstance() {
		
		if (traceLogger == null) {
			traceLogger = new TraceLog();
		}
		return traceLogger;
	}
			
	/**
	 * Skriver spårbarhetsinformation till loggen.
	 * 
	 * @param traceRecord Innehåller allt som skall loggas.
	 */
	public synchronized void trace(TraceRecord traceRecord) {
		
		if (isTraceing()) {
			// Tråda av en loggskrivare så kan exekveringen fortsätta.
			new LogWriter(traceRecord);
		}
	}

	/**
     * Tråd som sköter skrivandet till fil
	 */
	class LogWriter implements Runnable {
		private TraceRecord traceObjekt;
				
		/**
         * Skapa tråd
		 * @param traceObjekt det som ska loggas
		 */
		LogWriter(TraceRecord traceObjekt) {
			
			// Spara data.
			this.traceObjekt = traceObjekt;
			// Skapa en tråd med detta objekt.
			Thread t = new Thread(this);
			// Kör
			t.start();
		}
		
		/**
		 * Sköter loggandet
		 */
		public void run() {
            String pnr = null;
            if (traceObjekt.getPersonNummer() != null) {
                NumberFormat nf = NumberFormat.getInstance();
                try {
                    pnr = FormatDate.toPnrString(traceObjekt.getPersonNummer());
                } catch (FormatException fe) {
                    pnr = traceObjekt.getPersonNummer().toString();
                }
                
            }

			String logStr = traceObjekt.getTransactionId()
					+ ";" + traceObjekt.getCsnNummer() 
					+ ";" +	pnr  
					+ ";" +	traceObjekt.getAuthType()
					+ ";" +	traceObjekt.getHandelse()
					+ ";" +	traceObjekt.getFelMeddelande();
			log.log(LogLevel.TRACE_LOG, logStr);
		}

	}
}
