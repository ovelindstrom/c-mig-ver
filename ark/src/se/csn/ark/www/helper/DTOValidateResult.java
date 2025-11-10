package se.csn.ark.www.helper;

import se.csn.ark.common.dt.CsnDataTransferObjectImpl;

import java.util.ArrayList;
import java.util.Collection;


/**
 * DTOValidateResult lagrar resultatet av en validering.
 *
 * @author Joakim Olsson
 * @since 20041026
 * @version 0.1 skapad
 */
public class DTOValidateResult extends CsnDataTransferObjectImpl {
	public static final int NOT_EXECUTED = 0;
	public static final int COULD_NOT_EXECUTE = 1;
	public static final int VALID = 2;
	public static final int NOT_VALID = 3;
	private ArrayList errorMessages = new ArrayList();
	private int status = VALID;
	private String sAlternativeUrlProperty;

	/**
	 * Sätter status på valideringen.
	 *
	 * @param  theStatus valideringsstatus
	 */
	public void setStatus(int theStatus) {
		this.status = theStatus;
	}




	/**
	 * Hämtar en alternativ url property.
	 * Om man behöver en alternativ url än default..
	 * @return nya url property, om inte default skall användas
	 */
	public String getSAlternativeUrlProperty() {
		return sAlternativeUrlProperty;
	}




	/**
	 * Sätter en alternativ url property.
	 * Om man behöver en alternativ url än default..
	 * @param string den nya url'en
	 */
	public void setSAlternativeUrlProperty(String string) {
		sAlternativeUrlProperty = string;
	}




	/**
	 * Lägger till ett nytt felmeddelande.
	 *
	 * @param errorMessage felmeddelande
	 */
	public void addErrorMessage(String errorMessage) {
		status = NOT_VALID;

		//Om error meddelande finns läggs inte detta in. /Tobias 
		if (!errorMessages.contains(errorMessage)) {
			errorMessages.add(errorMessage);
		}
	}




	/**
	 * Resultat som visar att det inte gick inte att köra valideringen.
	 *
	 * @param message felmeddelande
	 */
	public void couldNotExecute(String message) {
		errorMessages = new ArrayList();
		addErrorMessage(message);
		setStatus(DTOValidateResult.COULD_NOT_EXECUTE);
	}




	/**
	 * Hämta alla felmeddelanden från valideringen.
	 *
	 * @return  alla felmeddelanden
	 */
	public Collection getMessages() {
		return errorMessages;
	}




	/**
	 * Returnerar true om validering har utförts och den gick bra.
	 *
	 * @return  validering OK
	 */
	public boolean getValid() {
		return status == VALID;
	}




	/**
	 * Returnerar true om validering har utförts och den INTE gick bra.
	 *
	 * @return  validering NOK
	 */
	public boolean getNotValid() {
		return status == NOT_VALID;
	}




	/**
	 * Returnerar true om validering har utförts.
	 *
	 * @return  validering NOK
	 */
	public boolean getExecuted() {
		return ((status == VALID) || (status == NOT_VALID));
	}




	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String str = "";

		str += (NL + this.getClass().getName() + "[");
		str += (NL + "status=");

		if (status == NOT_EXECUTED) {
			str += "NOT_EXECUTED";
		} else if (status == COULD_NOT_EXECUTE) {
			str += "COULD_NOT_EXECUTE";
		} else if (status == VALID) {
			str += "VALID";
		} else if (status == NOT_VALID) {
			str += "NOT_VALID";
		} else {
			str += "UNKNOWN";
		}

		str += "]";

		return str;
	}
}