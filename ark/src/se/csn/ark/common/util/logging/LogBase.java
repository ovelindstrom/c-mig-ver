package se.csn.ark.common.util.logging;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.net.SyslogAppender;
import org.apache.log4j.nt.NTEventLogAppender;

import se.csn.ark.common.CsnException;

/**
 * <P>Namn: Logg </P><br>
 *
 * <P>Beskrivning: <br>
 * Tillhandahåller loggnings basfunktionalitet för CSN:s java ramverk.
 *
 * @author K-G Sjöström
 * @since 20040805 
 * @version 1 skapad
 */

public class LogBase {

	protected static final String LOG_CONFIG = "ark_log4j";
	protected static final String NT_LOG = "nt_event_logging";
	protected static final String UNIX_LOG = "unix_event_logging";
	protected static final String APP_NAMN = "E-BSM";

	private static Properties logProperties;

	/**
	 * Sätts till true om Windows NT system log skall användas för loggning.
	 */
	private static boolean eventLoggingNTEnabled = false;

	/**
	 * Log appender för Windows NT system log.
	 */
    private static NTEventLogAppender logNT;

	/**
	 * Sätts till <code>true</code> om loggning med Unix event log skall användas.
	 */
    private static boolean eventLoggingUnixEnabled = false;

	/**
	 * Log appendeder för Unix event log(remote UNIX Syslog daemon).
	 */
    private static SyslogAppender logUnix;


	static {
		initLogging();
	}

    
	/**
	 * Endast subklasser kan skapa
	 */
	protected LogBase() {
		
	}

	/**
	 * Indikerar om loggning till Windows NT event Log är påslagen.
	 *
	 * @return <code>true</code> om Windows NT event loggning är påslagen.
	 */
	public static boolean isNtEventLoggingEnabled() {
		return eventLoggingNTEnabled;
	}

	/**
	 * Indikerar om logging till Unix system Logg är påslagen.
	 *
	 * @return <code>true</code> om Unix system logging är påslagen.
	 */
	public static boolean isUnixEventLoggingEnabled() {
		return eventLoggingUnixEnabled;
	}

    /**
     * @return log-appender för unix event-logg
     */
    protected static NTEventLogAppender getLogNT() {
        return logNT;
    }

    /**
     * @return log-appender för NT system-logg
     */
    protected static SyslogAppender getLogUnix() {
        return logUnix;
    }

	/**
	 * Visar loggens konfiguration
     * @return loggens konfiguration
	 */
	public String toString() {
		String str = "se.csn.ark.common.util.logging.Log\n";

		str += "\tproperties=" + logProperties;
		return str;
	}
	
	/**
	 * Laddar om log ramverket
	 */
	public static void reload() {
		initLogging();
	}

	/**
	 * Initierar log ramverket.
	 */
	private static void initLogging() {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(LOG_CONFIG);

			// Läs alla nycklar
			Enumeration nycklar = bundle.getKeys();
			logProperties = new Properties();

			// Lagra alla nycklar och värden.
			while (nycklar.hasMoreElements()) {
				String key = (String) nycklar.nextElement();
				String value = bundle.getString(key);
				logProperties.put(key, value);
			}

			//Initiera Log4J
			PropertyConfigurator.configure(logProperties);

			// Kolla om några specialla Logg egenskaper har satts.
			// Den här delen skulle kunna använda boolean grejor i Properties.
			String ntEventLoggingEnabledStr =
				logProperties.getProperty(NT_LOG);
			if (ntEventLoggingEnabledStr != null) {
				eventLoggingNTEnabled =
					(Boolean.valueOf(ntEventLoggingEnabledStr)).booleanValue();
				if (eventLoggingNTEnabled) {
					logNT = new NTEventLogAppender();
				}
			}
			String unixEventLoggingEnabled =
				logProperties.getProperty(UNIX_LOG);
			if (unixEventLoggingEnabled != null) {
				eventLoggingUnixEnabled =
					(Boolean.valueOf(unixEventLoggingEnabled)).booleanValue();
				if (eventLoggingUnixEnabled) {
					logUnix = new SyslogAppender();
				}
			}

			java.net.URL u =
				LogBase.class.getClassLoader().getResource(LOG_CONFIG + ".properties");
			String url = "";
			if (null != u) url = u.toString();
			skrivParametrarTillSystemOut(url);
		} catch (Exception e) {
			// Om det blir fel i log initieringen så skickar vi det till System.out ...
            java.io.PrintStream out = System.out; 
			out.println(
				"[LogBase init] kunde inte öppna log4j inställnings filen, "
					+ "återgår till standardinställningen. "
					+ e);
			out.println(
				"[LogBase init] system inställningar = "
					+ System.getProperties());
			//...och till System.err
			System.err.println(
				"[LogBase init] kunde inte öppna log4j inställnings filen, "
					+ "återgår till standardinställningen. "
					+ e);
			System.err.println(
				"[LogBase init] system inställningar = "
					+ System.getProperties());
		}
	}

	private static void skrivParametrarTillSystemOut(String url) {

		StringBuffer loggMsg = new StringBuffer();
		Enumeration props = logProperties.propertyNames();
		loggMsg.append("se.csn.ark.common.util.logging.Log initierad från '" + url + "' [");
		while (props.hasMoreElements()) {
			String key = (String)props.nextElement();
			if (null != key  &&  key.indexOf(".File") > 0) {
				loggMsg.append("\n  " + key + " -> '" + logProperties.getProperty(key) + "'");
			}
		}
		loggMsg.append("\n ]");
		System.out.println(loggMsg);
	}

	/**
	 *
	 * Skapar ett message från det givna meddelandet och fel objektet.
	 * Meddelandet innehåller meddelanden från alla "nested exceptions", och
	 * den ursprungliga "stacktracen" (från det djupaste felet).
	 *
	 * @param message Top nivå message
	 * @param fel Ett fel som kan innehålla andra fel
	 *
	 * @return Alla meddelanden och huvudorsaken.
	 */
	protected LogMessage createMessage(Object message, Throwable fel) {

		String firstMessage = String.valueOf(message);
		LogMessage loggMeddelande = new LogMessage(firstMessage, fel);

		if (fel instanceof CsnException) {
            final int defaultSize = 250;
			StringBuffer messageBuffer = new StringBuffer(defaultSize);
			messageBuffer.append(firstMessage);

			// get a message if there are no nested exceptions.
			// we don't want to include the last message in the message ladder
			// because it's printed as part of the stacktrace

			// Make sure we always return the first message even if there are no nested exceptions.
			int lastMessageStart = firstMessage.length();

			// iterate over each nested exception and extract the message
			do {
				fel = findNestedException(fel);
				if (fel != null) {
					loggMeddelande.setOrsak(fel);
					lastMessageStart = messageBuffer.length();
					messageBuffer
						.append("\n ")
						.append(fel.getClass().getName())
						.append(": ")
						.append(fel.getMessage());
				}
			} while (fel != null);

			// drop the very last message since it's in the stacktrace
			messageBuffer.setLength(lastMessageStart);

			// save the end message
			loggMeddelande.setMeddelande(messageBuffer.toString());
		}
		return loggMeddelande;
	}

	/**
	 * Försöker att hitta nästade exceptions.
	 *
	 *
	 * @param exception An exception that may or may not include a
	 *    nested exception
	 *
	 * @return det nästade felet, eller <code>null</code> om inget hittades.
	 */
	protected Throwable findNestedException(Throwable exception) {

		// Kolla först om det är ett CsnException - i så fall behöver
		// vi inte göra något mer än returnera.
		if (exception instanceof CsnException) {
			return ((CsnException) exception).getCause();
		}
		// Annars måste vi titta lite närmare.
		final String[] getterNames =
			{
				"getCause",
				"getRootException",
				"getException",
				"getNestedException" };
		final Class[] noArgsTypes = new Class[0];
		final Object[] noArgs = new Object[0];
		final Class exceptionKlass = exception.getClass();
		for (int i = 0; i < getterNames.length; ++i) {
			try {
				Method getNestedMethod =
					exceptionKlass.getMethod(getterNames[i], noArgsTypes);
				Object nestedObject = getNestedMethod.invoke(exception, noArgs);
				if (nestedObject instanceof Throwable) {
					return (Throwable) nestedObject;
				}
			} catch (Throwable e) {
				// Helt OK, vi prövar nästa metod ...
			}
		}

		return null;
	}

	/**
	 * <P>Namn: Meddelande </P><br>
	 *
	 * <P>Beskrivning: <br>
	 * Denna klass representerar ett message, med eller utan fel och dess cause,
	 * som skall skrivas till loggen.
	 *
	 * @since 040805 K-G Sjöström Version 1
	 *
	 */
	class LogMessage {

		/** Ettt message att skriva till loggen. */
		private String message;

		/** Ett undantag att skriva till loggen. */
		private Throwable cause;

		/**
		 * Konstruerar nytt log message.
         * @param message meddelande att logga
         * @param cause exception att logga
		 */
		public LogMessage(String message, Throwable cause) {
			this.message = message;
			this.cause = cause;
		}

		/**
		 * Returnerar felmeddelandet.
         * @return meddelande att logga
		 */
		public String getMeddelande() {
			return message;
		}

		/**
		 * Sätter log meddelandet.
         * @param msg meddelande att logga
		 */
		public void setMeddelande(String msg) {
			this.message = msg;
		}

		/**
		 * Returnerar felet som skall loggas.
         * @return exception att logga
		 */
		public Throwable getOrsak() {
			return cause;
		}

		/**
		 * Sätter felet som skall loggas med meddelandet.
         * @param theCause exception att logga
		 */
		public void setOrsak(Throwable theCause) {
			this.cause = theCause;
		}
	}

}
