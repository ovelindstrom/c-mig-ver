package se.csn.ark.common.util.logging.time;

import java.util.Hashtable;

import org.apache.log4j.Category;

import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.LogLevel;


/**
 *
 * Enkel prestandalogg.
 *
 * @author K-G sjöström - AcandoFrontec
 * @since 20041129
 * @version 1 skapad
 *
 */

public final class TimeLog {

	private String className;
	private static int nextLogId = 0;
	private static Hashtable logRecords = new Hashtable();
	
	private static boolean showLogId = false;
	private static boolean showOnlyTime = false;
	private static boolean timeLog = false;

	static {
		
		init();
	}
	
	/**
	 * initierar loggen
	 */
	private static void init() {
		
		timeLog = Properties.getBooleanProperty("ark_log4j", 
			"timelog", false);		
		showLogId = Properties.getBooleanProperty("ark_log4j", 
			"timelog.showLogId", false);		
		showOnlyTime = Properties.getBooleanProperty("ark_log4j", 
			"timelog.showOnlyTime", false);		
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
	private static Category log = Category.getInstance(TimeLog.class);

	/**
	 * Skapar en prestandaloggerinstans utifrån det objekt som vill tidslogga.
	 *
	 * @param loggingObject Det objekt som vill logga.
	 */
	private TimeLog(Object loggingObject) {
	
		String name = loggingObject.getClass().getName();
		this.className = name;
	}

	/**
	 * Ger en prestandaloggerinstans utifrån det objekt som vill tidslogga.
	 *
	 * @param loggingObject Det objekt som vill logga.
     * @return tids-logger
	 */
	public static TimeLog getLogger(Object loggingObject) {
		TimeLog tLog = null;

		tLog = new TimeLog(loggingObject);

		return tLog;
	}

	/**
	 * Startar klockan, tick, tack ....
	 *
	 * @param methodName Namnet på den metod som loggar.
	 * @return Ett id som skall användas när man stoppar klockan.
	 */
	public synchronized Integer startClock(String methodName) {
		Integer id = new Integer(-1);
		long startTime;

		// Intern koll så att inte  någon försöker någon använda
		//  loggen fast den är avslagen?
		if (isTiming()) {
			// Hämta id och ...
			id = getLogId();
			// ... fixa starttiden så ...
			startTime = System.currentTimeMillis();
			// ... trådar vi så fort det bara går.
			new Starter(logRecords, id, startTime, methodName);
			// Låt tråden fixa loggning m.m. så returnerar vi.
		}
		return id;
	}

	/**
	 * Stoppar klockan med det id som erhölls vi startClock.
	 *
	 * @param logId id var tid ska stoppas
	 */
	public synchronized void stopClock(Integer logId) {
		long stopTime;
		
		// Intern koll så att inte  någon försöker någon använda
		//  loggen fast den är avslagen?
		if (isTiming()) {
			// Fixa stoptiden så ...
			stopTime = System.currentTimeMillis();
			// ... trådar vi så fort det bara går.
			new Stopper(logRecords, logId, stopTime);
			// Låt tråden fixa loggning m.m. så returnerar vi.
		}
	}

	/**
	 * Indikerar om prestandaloggning är aktiverad.
	 *
	 * @return true om Prestandaloggen är påslagen.
	 */
	public static boolean isTiming() {
		
		return timeLog;
	}
	
	/**
	 * Ger en strängrepresentation vilka tidloggningar som är under behandling just nu.
     * @return strängrepresentaiton av klassen
	 */
	public String toString() {
	
		String str = "Föjande tidloggningsposter är under behandling:\n"
			+ logRecords;
		return str;
	}

	/**
	 * @param logStr sträng som loggas till timeloggen
	 */
	private static synchronized void log(String logStr) {
		
		log.log(LogLevel.TIME_LOG, logStr);
	}

	/**
	 * @return nytt unikt loggid
	 */
	private static synchronized Integer getLogId() {
		Integer nextLogIdInteger;
		
		nextLogId++;
		if (nextLogId == Integer.MAX_VALUE) {
			nextLogId = 0;
		}
		nextLogIdInteger = new Integer(nextLogId);
		return nextLogIdInteger;
	}

	/**
	 * @return namn på instansen
	 */
	private String getInstanceName() {
		
		return this.getClass().getName() + this.hashCode();
	}

	/**
     * data som time-loggen skriver
	 */
	public class TimeLogRecord {
		private Integer id;
		private String className;
		private String method;
		private long startTime;

		/**
         * Skapa record
         * 
		 * @param className klass som loggas
		 * @param method metod som loggas
		 * @param id id på tidsmätningen
		 * @param startTime mätning startad
		 */
		public TimeLogRecord(String className, String method, Integer id, long startTime) {
			this.method = method;
			this.className = className;
			this.id = id;
			this.startTime = startTime;
		}

		/**
		 * @param stopTime mätning avslutad
		 */
		void stop(long stopTime) {
			long totalTime;

			totalTime = stopTime - startTime;
			String logStr = className + "." + method + " ";
			if (!showOnlyTime) { 
				logStr += "<<< ";
			}
			logStr += totalTime + " ms";
			if (showLogId) {
				logStr += " [" + id + "]";
			}
			log(logStr);
		}

		/** 
		 * @return Strängrepresentation av klassen
		 */
		public String toString() {
			String str = "[";
		
			str += className + "." + method + ", id=" + id + ", startTime=" + startTime + "]";	
			return str;
		}
		
	}
	
	/**
     * Tråd som loggar och lagrar start av mätning
	 */
	public class Starter implements Runnable {
		private Hashtable logRecords; 
		private Integer id; 
		private String methodName;
		private long startTime;
				
		/**
		 * @param logRecords lagring av records
		 * @param id id på denna loggning
		 * @param startTime mätning startad
		 * @param methodName metod som loggas
		 */
		public Starter(Hashtable logRecords, Integer id, long startTime, String methodName) {
					
			this.logRecords = logRecords;
			this.id = id;
			this.startTime = startTime;
			this.methodName = methodName;
			Thread t = new Thread(this);
			t.start();
		}
				
		/**
		 * Loggar och lagrar start av mätning
		 */
		public void run() {

			// Ska start loggas eller loggas bara stop(summan) ?
			if (!showOnlyTime) {
				String logStr = className + "." + methodName + " >>>";
				if (showLogId) {
					logStr += " [" + id + "]";
				}		
				log(logStr);
			}
			// Start skall i vilket fall in i records.
			logRecords.put(id, new TimeLogRecord(className, methodName, id, startTime));
		}

	}
	
	/**
     * Tråd som loggar avslutad mätning
	 */
	public class Stopper implements Runnable {
		private Hashtable logRecords; 
        private Integer logId;
        private long stopTime;
        private TimeLogRecord timeLogRecord;

        /**
         * @param logRecords lagring av records
         * @param logId id på denna loggning
         * @param stopTime mätning avslutad
         */
		public Stopper(Hashtable logRecords, Integer logId, long stopTime) {
			
			this.logRecords = logRecords;
			this.logId = logId;
			this.stopTime = stopTime;	
			Thread t = new Thread(this);
			t.start();
		}
				
        /**
         * Loggar och lagrar avslutad mätning
         */
		public void run() {

			timeLogRecord = (TimeLogRecord)logRecords.get(logId);
			if (timeLogRecord != null) {
				timeLogRecord.stop(stopTime);
				logRecords.remove(logId);
			} else {
				log.error("Felaktigt logId " + logId + ". Följande log id finns:" + logRecords);
			}
		}

	}
	
}