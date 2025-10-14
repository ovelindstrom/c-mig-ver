/*
 * Created on 2004-nov-03
 *
 */
package se.csn.ark.common.dal;

import se.csn.ark.common.CsnApplicationException;

/**
 * Basklass för fel vid filhantering.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041103
 * @version 1 skapad
 *
 */
public class FileException extends CsnApplicationException {

    /**
     * @param message felmeddelande
     */
	public FileException(String message) {

		super(message);
	}

    /**
     * @param message felmeddelande
     * @param errorId id på felet
     */
	public FileException(String message, Integer errorId) {

		super(message, errorId);
	}

    /**
     * @param message felmeddelande
     * @param cause exception som orsakade felet
     */
	public FileException(String message, Throwable cause) {

		super(message, cause);
	}

    /**
     * @param message felmeddelande
     * @param errorId id på felet
     * @param cause exception som orsakade felet
     */
	public FileException(
		String message,
		Integer errorId,
		Throwable cause) {
		super(message, errorId, cause);
	}

	/**
	 * @param cause exception som orsakade felet
	 */
	public FileException(Throwable cause) {

		super(cause);
	}


}
