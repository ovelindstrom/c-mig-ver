/*
 * Created on 2004-sep-16
 *
 */
package se.csn.ark.common.dal.db;

import se.csn.ark.common.CsnSystemException;

/**
 * Basklass för databas fel.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20040916
 * @version 1 skapad
 *
 */
public class DatabaseException extends CsnSystemException {

    /**
     * @param message felmeddelande
     */
	public DatabaseException(String message) {

		super(message, DB_ERROR);
	}

    /**
     * @param message felmeddelande
     * @param cause exception som orsakade felet
     */
	public DatabaseException(String message, Throwable cause) {

		super(message, DB_ERROR, cause);
	}

    /**
     * @param message felmeddelande
     * @param errorId id på felet
     * @param cause exception som orsakade felet
     */
	public DatabaseException(
		String message,
		Integer errorId,
		Throwable cause) {
		super(message, errorId, cause);
	}


}
