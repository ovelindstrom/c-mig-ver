package se.csn.ark.common.util.logging;

import org.apache.log4j.Level;

/**
 * <P>Namn: LogLevel </P><br>
 *
 * <P>Beskrivning: <br>
 * Med denna klass skapas nivå för loggning med Log klassen.
 * 
 * @see se.csn.ark.common.util.logging.Log 
 *
 * @author K-G Sjöström 
 * @since 20040805 
 * @version 1 skapad
 * @verssion 2 2007-04-27 E-tjänster csn7571 Laggt till Level ACTIVITY
 *
 */
public class LogLevel extends Level {

	/** 
	 * Loggnivå för tidsloggningen. Satt till högre nivå än FATAL eftersom 
	 * tidsloggningen inte alltid kan kopplas till de valiga loggnivåerna
	 * och därmed måste slås av/på med egen flagga. 
	 *  
	 */
	public static final LogLevel TIME_LOG =
		new LogLevel(FATAL_INT + 1, "TIMELOG", FATAL_INT + 1);

	/** 
	 * Loggnivå för kritiska systemfel. Satt till högre nivå än FATAL eftersom 
	 * dessa fel alltid ska loggas.
	 *  
	 */
	public static final LogLevel CRITICAL =
		new LogLevel(FATAL_INT + 1, "CRITICAL", FATAL_INT + 1);

	/** 
	 * Loggnivå för Trace. Satt till högre nivå än FATAL eftersom 
	 * dessa fel alltid ska loggas.
	 *  
	 */
	public static final LogLevel TRACE_LOG =
		new LogLevel(FATAL_INT + 1, "TRACELOG", FATAL_INT + 1);
	    

	/** 
	 * Loggnivå för Aktivitetslogg. Satt till högre nivå än FATAL eftersom 
	 * dessa  alltid ska loggas.
	 *  
	 */
	public static final LogLevel ACTIVITY =
		new LogLevel(FATAL_INT + 1, "ACTIVITY", FATAL_INT + 1);
	/**
	 * Konstruerar ny LoggNiva.
	 * @param level Bör passa in i den befintliga nivåskalan.
	 * @param levelStr Text för denna nivå som visas i loggen
	 * @param syslogEquivalent Nivå i sysloggen (om den används)
	 * som denna nivå skall matchas emot.
	 */
	public LogLevel(int level, String levelStr, int syslogEquivalent) {
		super(level, levelStr, syslogEquivalent);
	}

}
