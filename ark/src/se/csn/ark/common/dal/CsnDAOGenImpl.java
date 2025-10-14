/*
 * Created on 2004-sep-14
 *
 */
package se.csn.ark.common.dal;


/**
 * Grundklass för access mot COOL tjänster.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20040914
 * @version 1 skapad
 *
 */
public class CsnDAOGenImpl extends CsnDataAccessObjectImpl implements CsnDAOGen {
	/**
	 * COOL tjänsterna levererar data med en massa blanktecken
	 * dvs strängar verkar ha fyllts ut. Med denna metod tar vi
	 * bort detta även om data egentligen skall formateras i
	 * presentationskiktet och inte här.
	 *
	 * @param stringWithLotsOfSpaces sträng som ska trimmas
	 * @return En sträng utan onödiga blanktecken.
	 */
	protected String trim(String stringWithLotsOfSpaces) {
		return stringWithLotsOfSpaces.trim();
	}




	/**
	 * Bygger en felstäng från inparametrarna
	 *
	 * @param status från cool
	 * @param beskrivning från cool
	 * @param modul från cool
	 * @param nyckel från cool
	 * @param indata från cool
	 * @return En sträng från parametrarna ovan
	 */
	public String buildCoolErrorMessage(
	                                    short status,
	                                    String beskrivning,
	                                    String modul,
	                                    String nyckel,
	                                    String indata) {
		final String nl = "\n";

		StringBuffer msg = new StringBuffer();

		msg.append(nl + "COOL-fel=[");
		msg.append(nl + "  Status=" + status);
		msg.append(nl + "  Beskrivning=" + trim(beskrivning));
		msg.append(nl + "  Modul=" + trim(modul));
		msg.append(nl + "  Nyckel=" + trim(nyckel));
		msg.append(nl + "]");
		msg.append(nl + "Indata=[" + indata);
		msg.append(nl + "]");

		return msg.toString();
	}
}