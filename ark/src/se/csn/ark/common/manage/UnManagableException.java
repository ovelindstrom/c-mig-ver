package se.csn.ark.common.manage;

import se.csn.ark.common.CsnApplicationException;

/**
 * Undantag som signalerar att något är fel med det som hanteras.<br>
 * 
 * <b>OBS!!! Skall endast kastas om det inte går att hantera tjänsten/funktionen längre.</b><br>
 * 
 * Ett enstaka fel som inte hindrar exekveringen att fortsätta skall inte kasta detta.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041130
 * @version 1 skapad
 *
 */
public class UnManagableException extends CsnApplicationException {

    /**
     * @param message felmeddelande
     */
	public UnManagableException(String message) {
		super(message);
	}

    /**
     * @param message felmeddelande
     * @param errorId id på felet
     */
	public UnManagableException(String message, Integer errorId) {
		super(message, errorId);
	}

    /**
     * @param message felmeddelande
     * @param cause exception som orsakade felet
     */
	public UnManagableException(String message, Throwable cause) {
		super(message, cause);
	}

    /**
     * @param cause exception som orsakade felet
     */
	public UnManagableException(Throwable cause) {
		super(cause);
	}

    /**
     * @param message felmeddelande
     * @param errorId id på felet
     * @param cause exception som orsakade felet
     */
	public UnManagableException(
		String message,
		Integer errorId,
		Throwable cause) {
		super(message, errorId, cause);
	}

}
