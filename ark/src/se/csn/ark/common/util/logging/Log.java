package se.csn.ark.common.util.logging;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.apache.log4j.Category;
import org.apache.log4j.MDC;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

import se.csn.ark.common.util.logging.critical.CriticalLog;


/**
 * <P>Namn: Logg </P><br>
 *
 * <P>Beskrivning: <br>
 * Tillhandahåller loggnings funktionalitet för CSN:s java ramverk.
 *
 * @author K-G Sjöström
 * @since 20040805  
 * @version 1 skapad
 * @version 2 Ändrad av csn7571 för E-tjänster
 */
public class Log extends LogBase {
	/** Collection av singleton Log objects, en per klass. */
	private static HashMap logInstances = new HashMap();

	/** Singleton instans av logger för kritiska systemfel. */
	private static CriticalLog criticalLog = CriticalLog.getLoggerInstance();

	/**
	 * Category är log4j klassen som används för loggningen av denna log wrapper.
	 */
	private Category log;

	/** Namnet som log instansen är kopplad till. */
	private String instanceName;
	/** Servern som log instansen är kopplad till. */
	private String server;
	/** Systemet, tex E-BSM, som log instansen är kopplad till. */
	private String system;

	/**
	 * Skapar en log instans
	 *
	 * @param instanceName Ett namn att binda instansen till.
	 */
	Log(String instanceName) {
		log = Category.getInstance(instanceName);
		this.instanceName = instanceName; 
		try {
			  InetAddress me = InetAddress.getLocalHost();
			  //this.server = me.getCanonicalHostName();
			  this.server = me.getHostName();
			}
			catch (UnknownHostException e) {
			  this.server ="unknown";
			} 	
		
		 this.system=APP_NAMN; //från LogBase
		 //Dessa kan loggas i log4j i ConversionPattern/sql
         //%X{server} för FileAppender / @MDC:server@ för JDBCAppender
		 MDC.put("server", this.server);
		 MDC.put("system", this.system);

			
	}

	/**
	 * Returnerar en log instans för den log som idenifieras av det givna namnet..
	 *
	 * @param instanceName Instansnamnet
	 *
	 * @return En log instans
	 */
	private static Log getInstance(String instanceName) {
		synchronized (logInstances) {
			Log obj = (Log)logInstances.get(instanceName);

			if (obj == null) {
				obj = new Log(instanceName);
				logInstances.put(instanceName, obj);
			}


			return obj;
		}
	}




	/**
	 * Returnerar en Logg instans för det Logg objekt som identifieras av den givna klassen
	 *
	 * @param instanceClass Klass som identifierar vilken log som skall användas.
	 *
	 * @return En log instans
	 */
	public static Log getInstance(Class instanceClass) {

		return getInstance(instanceClass.getName());
	}




	/**
	 * Loggar ett meddelande med KRITISK prioritet.
	 *
	 *
	 * @param message Objektet kommer att konverteras till String innan det skrivs till loggen.
	 */
	public void fatal(Object message) {
		log.fatal(message);

		// Logga även till den egna loggfilen för kritiska systemfel.
		criticalLog.critical(instanceName + " " + message);

		if (isNtEventLoggingEnabled()) {
			// NT Event Log har ERROR som högsta nivå
			LoggingEvent e =
				new LoggingEvent(APP_NAMN, log, Priority.ERROR, message, null);

			getLogNT().append(e);
		} else if (isUnixEventLoggingEnabled()) {
			LoggingEvent e =
				new LoggingEvent(APP_NAMN, log, Priority.ERROR, message, null);

			getLogUnix().append(e);
		}
	}




	/**
	 * Loggar ett meddelande med KRITISK prioritet.
	 *
	 * @param message Objektet kommer att konverteras till String innan det skrivs till loggen.
	 * @param throwable Throwable som skall loggas
	 */
	public void fatal(Object message, Throwable throwable) {
		log.fatal(message, throwable);

		// Logga även till den egna loggfilen för kritiska systemfel.
		criticalLog.critical(instanceName + " " + message, throwable);

		if (isNtEventLoggingEnabled()) {
			// NT Event Log har ERROR som högsta nivå
			LoggingEvent e =
				new LoggingEvent(
				                 APP_NAMN,
				                 log,
				                 Priority.ERROR,
				                 message,
				                 throwable);

			getLogNT().append(e);
		} else if (isUnixEventLoggingEnabled()) {
			LoggingEvent e =
				new LoggingEvent(
				                 APP_NAMN,
				                 log,
				                 Priority.ERROR,
				                 message,
				                 throwable);

			getLogUnix().append(e);
		}
	}




	/**
	 * Loggar ett objekt med DEBUG prioritet
	 *
	 * @param message Objektet kommer att konverteras till String innan det skrivs till loggen.
	 */
	public void debug(Object message) {
		log.debug(message);
	}




	/**
	 * Loggar ett objekt med DEBUG prioritet
	 *
	 * @param message Objektet kommer att konverteras till String innan det skrivs till loggen.
	 * @param throwable Throwable som skall loggas
	 */
	public void debug(Object message, Throwable throwable) {
		if (log.isDebugEnabled()) {
			LogMessage loggMeddelande = createMessage(message, throwable);

			log.debug(
			          loggMeddelande.getMeddelande(),
			          loggMeddelande.getOrsak());
		}
	}




	/**
	 * Loggar ett objekt med prioritet FEL.
	 *
	 * @param message The Logg message.
	 */
	public void error(Object message) {
		log.error(message);
	}




	/**
	 * Loggar ett objekt med prioritet FEL.
	 *
	 * @param message Objektet kommer att konverteras till String innan det skrivs till loggen.
	 * @param throwable Throwable som skall loggas
	 */
	public void error(Object message, Throwable throwable) {
		LogMessage loggMeddelande = createMessage(message, throwable);

		log.error(loggMeddelande.getMeddelande(), loggMeddelande.getOrsak());
	}




	/**
	 * Loggar ett objekt med prioritet INFO.
	 *
	 * @param message Objektet kommer att konverteras till String innan det skrivs till loggen.
	 */
	public void info(Object message) {
		log.info(message);
	}




	/**
	 * Loggar ett objekt med prioritet INFO.
	 *
	 * @param message Objektet kommer att konverteras till String innan det skrivs till loggen.
	 * @param throwable Throwable som skall loggas
	 */
	public void info(Object message, Throwable throwable) {
		if (log.isInfoEnabled()) {
			LogMessage loggMeddelande = createMessage(message, throwable);

			log.info(loggMeddelande.getMeddelande(), loggMeddelande.getOrsak());
		}
	}




	/**
	 * Loggar ett objekt med prioritet VARNING.
	 *
	 * @param message Objektet kommer att konverteras till String innan det skrivs till loggen.
	 */
	public void warn(Object message) {
		log.warn(message);
	}




	/**
	 * Loggar ett meddelande med angiven loggnivå.
	 *
	 * @param loggNiva Aktuell loggnivå för detta message.
	 * @param message Meddelande som skall loggas.
	 */
	public void log(LogLevel loggNiva, Object message) {
		log.log(loggNiva, message); 
		
	}




	/**
	 * Loggar ett meddelande med angiven loggnivå.
	 *
	 * @param loggNiva Aktuell loggnivå för detta message.
	 * @param message Meddelande som skall loggas.
	 * @param throwable Fel som skall loggas.
	 */
	public void log(LogLevel loggNiva, Object message, Throwable throwable) {
		log.log(loggNiva, message, throwable);
		
	}




	/**
	 * Loggar ett objekt med prioritet VARNING.
	 *
	 * @param message Objektet kommer att konverteras till String innan det skrivs till loggen.
	 * @param throwable Throwable som skall loggas
	 */
	public void warn(Object message, Throwable throwable) {
		LogMessage loggMeddelande = createMessage(message, throwable);

		log.warn(loggMeddelande.getMeddelande(), loggMeddelande.getOrsak());
	}




	/**
	 * Kollar om debug loggning är påslaget.
	 *
	 * <p>Med denna metod kan man minska den mängd datorkraft som går åt
	 * till debug loggning.</p>
	 *
	 * <p>Om man skriver koden som ...</p>
	 *
	 * <pre>
	 *   log.debug("Detta är nummer: " + i );
	 * </pre>
	 *
	 * <p>... så kommer strängen för debug meddelandet att byggas ihop
	 * oavsett om meddelandet kommer att log eller ej. </p>
	 *
	 * <p>Skriv istället</p>
	 *
	 * <pre>
	 *   if(log.isDebugEnabled() {
	 *     log.debug("Detta är nummer: " + i );
	 *   }
	 * </pre>
	 *
	 * <p>På detta sätt kommer det inte finnas någon onödig parameter
	 * konstruktion för meddelanden. Å andra sidan kommer kontrollen om
	 * debug är påslaget att utföras 2 gånger. En gång i
	 * <code>isDebugEnabled</code> och en gång till i <code>debug</code>.
	 * Detta är dock försumbart då det tar 1% av tiden att kontrollera
	 * om det skall loggas jämfört med tiden att skriva till loggen.
	 *
	 * @return <code>true</code> om debug loggning är påslaget,
	 *    <code>false</code> annars.
	 */
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}




	/**
	 * Kollar om info loggning är påslaget.
	 *
	 * <p>Med denna metod kan man minska den mängd datorkraft som går åt
	 * till info loggning.</p>
	 *
	 * <p>Om man skriver koden som ...</p>
	 *
	 * <pre>
	 *   log.info("Detta är nummer: " + i );
	 * </pre>
	 *
	 * <p>... så kommer strängen för debug meddelandet att byggas ihop
	 * oavsett om meddelandet kommer att log eller ej. </p>
	 *
	 * <p>Skriv istället</p>
	 *
	 * <pre>
	 *   if(log.isInfoEnabled() {
	 *     log.info("Detta är nummer: " + i );
	 *   }
	 * </pre>
	 *
	 * <p>På detta sätt kommer det inte finnas någon onödig parameter
	 * konstruktion för meddelanden. Å andra sidan kommer kontrollen om
	 * debug är påslaget att utföras 2 gånger. En gång i
	 * <code>isDebugEnabled</code> och en gång till i <code>debug</code>.
	 * Detta är dock försumbart då det tar 1% av tiden att kontrollera
	 * om det skall loggas jämfört med tiden att skriva till loggen.
	 *
	 * @return <code>true</code> om debug loggning är påslaget,
	 *    <code>false</code> annars.
	 */
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}




	/**
	 * @return strängrepresentation av klassen
	 */
	public String toString() {
		return super.toString();
	}
	
	/**
	 * @return Returns the server.
	 */
	public String getServer() {
		return server;
	}
	
	/**
	 * @return Returns the system.
	 */
	public String getSystem() {
		return system;
	}
}