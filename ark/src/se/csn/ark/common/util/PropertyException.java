/*
 * Created on 2005-jan-21
 *
 */
package se.csn.ark.common.util;

import se.csn.ark.common.CsnApplicationException;

/**
 * @author K-G Sjöström - AcandoFrontec
 * @since 20050121
 * @version 1 skapad
 *
 */
public class PropertyException extends CsnApplicationException {

    /**
     * @param message felmeddelande
     */
	public PropertyException(String message) {
		super(message);
	}

    /**
     * @param message felmeddelande
     * @param errorId id på felet
     */
	public PropertyException(String message, Integer errorId) {
		super(message, errorId);
	}

    /**
     * @param message felmeddelande
     * @param cause exception som orsakade felet
     */
	public PropertyException(String message, Throwable cause) {
		super(message, cause);
	}

    /**
     * @param cause exception som orsakade felet
     */
	public PropertyException(Throwable cause) {
		super(cause);
	}

    /**
     * @param message felmeddelande
     * @param errorId id på felet
     * @param cause exception som orsakade felet
     */
	public PropertyException(
		String message,
		Integer errorId,
		Throwable cause) {
		super(message, errorId, cause);
	}

}
