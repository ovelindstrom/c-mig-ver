/*
 * Created on 2004-sep-16
 *
 */
package se.csn.ark.common.dal;

import se.csn.ark.common.CsnSystemException;

/**
 * Basklass för Web Service fel.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041116
 * @version 1 skapad
 *
 */
public class WebServiceException extends CsnSystemException {

    /**
     * @param message felmeddelande
     */
	public WebServiceException(String message) {

		super(message, WEBSERVICE_ERROR);
	}

    /**
     * @param message felmeddelande
     * @param cause exception som orsakade felet
     */
	public WebServiceException(String message, Throwable cause) {

		super(message, WEBSERVICE_ERROR, cause);
	}

    /**
     * @param message felmeddelande
     * @param errorId id på felet
     * @param cause exception som orsakade felet
     */
	public WebServiceException(
		String message,
		Integer errorId,
		Throwable cause) {
		super(message, errorId, cause);
	}


}
