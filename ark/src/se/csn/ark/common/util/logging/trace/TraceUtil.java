package se.csn.ark.common.util.logging.trace;

import javax.servlet.http.HttpSession;

/**
 * <P>Namn: TraceUtil </P><br>
 *
 * <P>Beskrivning: <br>
 * Gemensam funktion för att TransactionID.
 *
 * @author Joakim Olsson
 * @since 20050223
 * @version 1 skapad
 */

public final class TraceUtil {

    /**
     * Privat konstruktor, endast statisk åtkomst
     */
    private TraceUtil() {
    }




	/**
	 * @param session för att skapa unikt transaktionsid
	 * @return unikt transkationsid
	 */
	public static String createCsnTxId(HttpSession session) {
		return session.getId() + "-" + session.getLastAccessedTime();
	}

	/**
	 * @param csnTxId transaktionsid byggt från sessionen
	 * @param iipaxTxId transaktionsid från iipax-broker
	 * @return 'csnTxId'/'iipaxTxId'
	 */
	public static String combineTxId(String csnTxId, String iipaxTxId) {
		return csnTxId + "/" + iipaxTxId;
	}

    /**
     * @param csnEvent händelse byggt från jsp-sidan
     * @param xmlEvent transaktionsid från xml-transaktionen
     * @return 'csnEvent'/'xmlEvent'
     */
	public static String combineEvent(String csnEvent, String xmlEvent) {
		return csnEvent + "/" + xmlEvent;
	}
}
