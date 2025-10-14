package se.csn.ark.common.util;

import se.csn.ark.common.CsnApplicationException;

/**
 * Denna klass indikerar att fel uppstått vid tolkning.
 * 
 * @author K-G Sjöström
 * @since 20041020
 * @version 1 skapad
 *
 */
public class ParseException extends CsnApplicationException {


    /**
     * @param message felmeddelande
     */
	public ParseException(String message) {
		super(message);
	}

    /**
     * @param message felmeddelande
     * @param cause exception som orsakade felet
     */
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}


    /**
     * @param cause exception som orsakade felet
     */
	public ParseException(Throwable cause) {
		super(cause);
	}

}
