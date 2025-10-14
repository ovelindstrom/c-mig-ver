package se.csn.ark.common.manage;

/**
 * Gränssnitt för något som skall kunna vara hanterbart.
 * Innehåller det som en hanterare skall kunna anropa mot
 * den resurs som skall hanteras.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041111
 * @version 1 skapad
 *
 */
public interface CsnManagable {

	
	/**
	 * Ej initierad - Har inte använts eller har blivit nerstängd (close).
	 */
	public int STATE_NOT_INITIATED = 0;

	/**
	 * Initierad - Redo för start.
	 */
	public int STATE_INITIATED = 1;

	/**
	 * Kör - Anrop till start har gjorts.
	 */
	public int STATE_RUNNING = 2;

	/**
	 * Stoppad - Redo att startas igen
	 */
	public int STATE_STOPPED = 3;

	/**
	 * Stängd - Måste initieras igen för att kunna användas igen.
	 */
	public int STATE_CLOSED = 4;

	/**
	 * Texter som beskriver tillstånden. Hämtas ur arrayen med motsvarande 
	 * STATE_ konstant.
	 */
	public String[] STATES_TEXTS = {"NOT INITIATED", "INITIATED", "RUNNING", 
			"STOPPED", "CLOSED"};

	/**
	 * Initierar det som skall hanteras.
	 * 
	 * @throws UnManagableException Indikerar att något gått så pass fel att tjänsten 
     * inte kan initieras. Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 * 
	 */
	public void init() throws UnManagableException;
	
	/**
	 * Startar exekveringen.
	 * 
	 * @throws UnManagableException Indikerar att något gått så pass fel att tjänsten 
     * inte kan startas. Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 * 
	 */
	public void start() throws UnManagableException;
	
	/**
	 * Stoppar exekveringen utan att stänga ner resurser.
	 * 
	 * @throws UnManagableException Indikerar att något gått så pass fel att tjänsten 
     * inte kan stoppas. Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 * 
	 */
	public void stop() throws UnManagableException;
	
	/**
	 * Stänger ner alla resurser för det som hanteras.
	 * 
	 * @throws UnManagableException Indikerar att något gått så pass fel att tjänsten 
     * inte kan stängas. Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 * 
	 */
	public void close() throws UnManagableException;
	
	/**
	 * Ger tillståndet för denna tjänst enligt de metoder som kan anropas 
	 * i detta gränssnitt - init, start, stop och close. 
	 * 
	 * @return Returnerar värden enligt någon av de "konstanter" som finns
	 * i detta gränssnitt och börjar på STATE_ .
	 * 
	 * @throws UnManagableException Indikerar att något gått så pass fel att det inte går att
	 * fråga efter tillståndet för tjänsten.
	 * Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 * 
	 */
	public int getState() throws UnManagableException;

	/**
	 * Strängrepresentation av status för det som hanteras.
	 * 
	 * @return Information som talar status för denna tjänst.
	 * 
	 * @throws UnManagableException Indikerar att något gått så pass fel att det inte går att
	 * fråga efter status för tjänsten.
	 * Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 * 
	 */
	public String getStatus() throws UnManagableException;

	/**
	 * Ger klassreferens för loggning m.m.
	 * 
	 * @return Klassen som implementerar detta gränssnitt.
	 */
	public Class getManagableClass();
	
}
