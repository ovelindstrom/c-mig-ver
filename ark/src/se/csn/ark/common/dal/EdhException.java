/*
 * Created on 2004-sep-16
 *
 */
package se.csn.ark.common.dal;

import se.csn.ark.common.CsnSystemException;

/**
 * Basklass för Edh fel.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20040916
 * @version 1 skapad
 *
 */
public class EdhException extends CsnSystemException {

	/**
	 * @param message felmeddelande
	 */
	public EdhException(String message) {

		super(message, EDH_ERROR);
	}

	/**
	 * @param message felmeddelande
	 * @param cause exception som orsakade felet
	 */
	public EdhException(String message, Throwable cause) {

		super(message, EDH_ERROR, cause);
	}

	/**
	 * @param message felmeddelande
	 * @param errorId id på felet
	 * @param cause exception som orsakade felet
	 */
	public EdhException(
		String message,
		Integer errorId,
		Throwable cause) {
		super(message, errorId, cause);
	}


}
