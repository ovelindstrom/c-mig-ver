package se.csn.ark.common.util.logging;

/**
 * Det fungerar inte att använda Log klassen i alla lägen.
 * T.ex. i SQLJ-klasser som innehåller en del statiska saker så
 * går det inte. Använd då denna Wrapper klass som kapslar in 
 * Log klassen.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20050214
 * @version 1 skapad
 *
 * @see se.csn.ark.common.util.logging.Log
 */
public class NonStaticLog {

	private Log log; 

	/**
	 * Skapar ny logger.
	 * 
	 *@param instanceClass Den klass som skall logga.
	 */
	public NonStaticLog(Class instanceClass) {

		log = new Log(instanceClass.getName());
	}

	/**
	 * @param message meddelande att logga
	 */
	public void debug(Object message) {
		log.debug(message);
	}

	/**
	 * @param message meddelande att logga
	 * @param throwable exception att logga
	 */
	public void debug(Object message, Throwable throwable) {
		log.debug(message, throwable);
	}

	/**
	 * @param message meddelande att logga
	 */
	public void error(Object message) {
		log.error(message);
	}

	/**
	 * @param message meddelande att logga
	 * @param throwable exception att logga
	 */
	public void error(Object message, Throwable throwable) {
		log.error(message, throwable);
	}

	/**
	 * @param message meddelande att logga
	 */
	public void fatal(Object message) {
		log.fatal(message);
	}

	/**
	 * @param message meddelande att logga
	 * @param throwable exception att logga
	 */
	public void fatal(Object message, Throwable throwable) {
		log.fatal(message, throwable);
	}

	/**
	 * @param message meddelande att logga
	 */
	public void info(Object message) {
		log.info(message);
	}

	/**
	 * @param message meddelande att logga
	 * @param throwable exception att logga
	 */
	public void info(Object message, Throwable throwable) {
		log.info(message, throwable);
	}

	/**
	 * @return true om debug-loggning är påslagen
	 */
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	/**
	 * @return true om info-loggning är påslagen
	 */
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	/**
	 * @param loggNiva nivå att logga på
	 * @param message meddelande att logga
	 */
	public void log(LogLevel loggNiva, Object message) {
		log.log(loggNiva, message);
	}

	/**
	 * @param loggNiva nivå att logga på
	 * @param message meddelande att logga
	 * @param throwable exception att logga
	 */
	public void log(LogLevel loggNiva, Object message, Throwable throwable) {
		log.log(loggNiva, message, throwable);
	}

	/**
	 * @param message meddelande att logga
	 */
	public void warn(Object message) {
		log.warn(message);
	}

	/**
	 * @param message meddelande att logga
	 * @param throwable exception att logga
	 */
	public void warn(Object message, Throwable throwable) {
		log.warn(message, throwable);
	}

}
