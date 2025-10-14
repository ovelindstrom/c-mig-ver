package se.csn.ark.www.helper;

import se.csn.ark.common.CsnApplicationException;


/**
 * ValidatorException används för validering.
 *
 * @author Joakim Olsson
 * @since 20041026
 * @version 0.1 skapad
 */
public class ValidatorException extends CsnApplicationException {
	/**
	 * @param message exception-meddelande
	 */
	public ValidatorException(String message) {
		super(message);
	}




	/**
	 * @param message exception-meddelande
	 * @param cause original-exception
	 */
	public ValidatorException(String message, Throwable cause) {
		super(message, cause);
	}




	/**
	 * @param cause original-exception
	 */
	public ValidatorException(Throwable cause) {
		super(cause);
	}
}