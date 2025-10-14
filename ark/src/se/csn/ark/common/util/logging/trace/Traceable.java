package se.csn.ark.common.util.logging.trace;

/**
 *
 * Gränssnitt för det som skall kunna loggas av spårbarhetsloggningen 
 * som används för att kunna logga alla tjänster för att kunna plocka
 * ut statistik/spårbarhet för en person.
 *   
 * @author Hasse Zetterberg
 * @since 2005-02-04
 * @version 2 Förbättringar - K-G Sjöström AcandoFrontec
 *          Utökat gränssnitt. 
 * @version 1 Skapad - Hasse Zetterberg
 */
public interface Traceable {
	
	/**
	 * Ger kundens CSN nummer.
	 * 
	 * @return CSN nummer
	 */
	public Integer getCsnNummer();

	/**
	 * Ger kundens personnummer.
	 * 
	 * @return Personnummer
	 */
	public Double getPersonNummer();

	/**
	 * Ger TransactionsId.
	 * 
	 * @return TransactionsId
	 */
	public String getTransactionId();

	/**
	 * Ger händelsen som skall loggas.
	 * 
	 * @return Händelsen.
	 */
	public String getHandelse();


	/**
	 * 
	 * @return identifierings-typ
	 */
	public String getAuthType();

	/**
	 * Ger felmeddelande om transaktionen inte gick bra.
	 * 
	 * @return Beskrivning av felet.
	 */
	public String getFelMeddelande();


	
}
