/*
 * Created on 2005-feb-23
 *
 */
package se.csn.ark.common.jms;

import se.csn.ark.common.CsnSystemException;

/**
 * Felmeddelande som uppkommer vid JMS/köanslutning
 * @author K-G Sjöström - AcandoFrontec
 * @version 1.01
 *
 */
public class QueueException extends CsnSystemException {

	/**
	 * Konstruktor
	 * @param message Felmeddelande
	 */
	public QueueException(String message) {

		super(message);
	}

	/**
	 * Konstruktor
	 * @param message Felmeddelande
	 * @param cause Orsak
	 */
	public QueueException(String message, Throwable cause) {

		super(message, cause);
	}

	/**
	 * Konstruktor
	 * @param message Felmeddelande
	 * @param errorId Identitet för att kunna hämta feltext
	 */
	public QueueException(String message, Integer errorId) {

		super(message, errorId);
	}

	/**
	 * Konstruktor
	 * @param message Felmeddelande
	 * @param errorId Identitet för att kunna hämta feltext
	 * @param cause Orsak
	 */
	public QueueException(String message, Integer errorId, Throwable cause) {

		super(message, errorId, cause);
	}

}
