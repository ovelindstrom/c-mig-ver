/*
 * Created on 2005-feb-23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package se.csn.ark.common.message.mail;

import se.csn.ark.common.CsnApplicationException;

/**
 *    Namn : MailException <br>
 *
 *    Beskrivning: Fel som uppkommer i samband med Epost.
 * @author CSN7504
 * @version 1.01
 */
public class MailException extends CsnApplicationException {
	public static final Integer FELAKTIGT_ADRESSFORMAT = new Integer(300);
	public static final Integer EPOSTRUBRIK_ERR  = new Integer(301);
	public static final Integer EPOSTDATUM_ERR = new Integer(302);
	public static final Integer EPOSTMEDDELANDETEXT_ERR = new Integer(303);
	
	/**
    * @param arg0 Felmeddelande
    */
   public MailException(String arg0) {

		super(arg0);
	}	

	/**
	 * @param message Felmeddelande.
	 * @param errorId Identitet för att kunna hämta feltext.
	 */
	public MailException(String message, Integer errorId) {
		super(message, errorId);

	}

	/**
	 * @param message Felmeddelande
	 * @param cause Orsak
	 */
	public MailException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause Orsak
	 */
	public MailException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message Felmeddelande.
	 * @param errorId Identitet för att kunna hämta feltext.
	 * @param cause Orsak
	 */
	public MailException(String message, Integer errorId, Throwable cause) {
		super(message, errorId, cause);
	
	}

	/**
	* @param arg0 Felmeddelande
	* @param e Orsak
	*/
   public MailException(String arg0, Exception e) {
	   super(arg0, e);
   }


}
