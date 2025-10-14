package se.csn.ark.common.util;

import se.csn.ark.common.CsnApplicationException;

/**
 * Denna klass indikerar att fel uppstått vid formatering.
 * 
 * @author K-G Sjöström
 * @since 20041020
 * @version 1 skapad
 *
 */
public class FormatException extends CsnApplicationException {


    /**
     * @param message felmeddelande
     */
	public FormatException(String message) {
		super(message);
	}

    /**
     * @param message felmeddelande
     * @param cause exception som orsakade felet
     */
	public FormatException(String message, Throwable cause) {
		super(message, cause);
	}


    /**
     * @param cause exception som orsakade felet
     */
	public FormatException(Throwable cause) {
		super(cause);
	}

}
