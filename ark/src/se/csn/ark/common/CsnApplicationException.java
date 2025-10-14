package se.csn.ark.common;


/**
 *
 * Basklass för applikations fel. Skall användas för att indikera fel som
 * hanteras av applikationen som t.ex. felaktig inmatning m.m.
 *
 * @author K-G Sjöström
 * @since 040809
 * @version 1 Skapad
 * @version 2 Förändringar av Jacob Nordin 2005-09-16. Ändrat konstruktorerna från protected
 *            till public så att instanser av CsnApplicationException kan skapas direkt utan
 *            att behöva skapa en subklass.
 * @see se.csn.ark.common.CsnSystemException
 *
 */
public class CsnApplicationException extends Exception implements CsnException {
	public static final Integer APPLICATION_EXCEPTION = new Integer(2000);
	public static final Integer KUND_EXCEPTION = new Integer(2001);
	public static final Integer ANMALAN_EXCEPTION = new Integer(2002);
	public static final Integer ANSOKAN_EXCEPTION = new Integer(2003);
	public static final Integer ANSOKAN_EXCEPTION_BERAKNASOKTAPERIODER =
		new Integer(1);
	public static final Integer ANDRING_EXCEPTION_ARENDE = new Integer(151);
	public static final Integer ANDRING_EXCEPTION_INKOMST = new Integer(152);
	public static final Integer ANDRING_EXCEPTION_UPPGIFTER = new Integer(153);
	private Integer errorId = null;

	/**
	 * Skapar ett applikations fel.
	 *
	 * @param message Felmeddelande
	 */
	public CsnApplicationException(String message) {
		this(message, APPLICATION_EXCEPTION, null);
	}




	/**
	 * Skapar ett applikations fel.
	 *
	 * @param message Felmeddelande
	 * @param errorId Identitiet för att kunna hämta feltext
	 */
	public CsnApplicationException(String message, Integer errorId) {
		this(message, errorId, null);
	}




	/**
	 * Skapar ett applikations fel.
	 *
	 * @param message Felmeddelande
	 * @param cause Orsak
	 */
	public CsnApplicationException(String message, Throwable cause) {
		this(message, APPLICATION_EXCEPTION, cause);
	}




	/**
	 * Skapar ett applikations fel.
	 *
	 * @param cause Orsak
	 */
	public CsnApplicationException(Throwable cause) {
		this(null, APPLICATION_EXCEPTION, cause);
	}




	/**
	 * Skapar ett applikations fel.
	 *
	 * @param message Felmeddelande
	 * @param errorId Identitiet för att kunna hämta feltext
	 * @param cause Orsak
	 */
	public CsnApplicationException(String message,
	                                  Integer errorId,
	                                  Throwable cause) {
		super(message, cause);
		if (errorId == APPLICATION_EXCEPTION) {
			// Om errorid är satt till APPLICATION_EXCEPTION dvs 
			// det fanns inget eget id för det sista exception så sätter
			// vi id till föregående exceptions id.
			if ((cause != null) && (cause instanceof CsnApplicationException))  {
				CsnException csnException = (CsnException) cause;
				this.errorId = csnException.getErrorId();
                			
			} else {
				this.errorId = errorId;
			}
            			
		} else {
			this.errorId = errorId;
		}
	}

	/**
	 * @see se.csn.ark.common.CsnException#getType()
	 */
	public Integer getType() {
		return CsnException.APP;
	}




	/**
	 * Fel id för detta applikations fel som kan användas för att hämta motsvarande
	 * felmeddelande från fil eller databas.
	 *
	 * @see se.csn.ipl.webbansokan.arkitektur.CsnException#getFelId()
	 * @return Returnerar <code>null</code> om id ej definierat
	 */
	public Integer getErrorId() {
		return errorId;
	}




	/**
	 * Returnerar en sträng representation av detta objekt innehållande
	 * alla eventuellt nästade <code>Exception</code>
	 *
	 * @return En sträng med all felbeskrivning
	 */
	public String toString() {
		String msg =
			this.getClass().getName() + " (Typ= " + getType() + ", Id="
			+ getErrorId() + "): " + getMessage();

		if (getCause() != null) {
			msg += (", orsakat av -> " + getCause().toString());
		}

		return msg;
	}




    /**
     * Återskapar CsnException utifrån indata
     *
     * @param message exception-meddelande
     * @param errorId id på CsnException
     * @return återskapt exception
     */
	public static CsnException reCreateCsnException(String message,
	                                                Integer errorId) {
		CsnException csnException;

		csnException = new CsnApplicationException(message, errorId, null);

		return csnException;
	}
}