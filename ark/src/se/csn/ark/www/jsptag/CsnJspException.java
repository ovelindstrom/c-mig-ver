package se.csn.ark.www.jsptag;

import se.csn.ark.common.CsnException;

import javax.servlet.jsp.JspException;


/**
 * jsp-excepion.
 *
 * @author Joakim Olsson
 * @since 20050103
 * @version 0.1 skapad
 */
public class CsnJspException extends JspException implements CsnException {
	public static final Integer HELPER_EXCEPTION = new Integer(3002);
	public static final Integer ADRESS_EXCEPTION = new Integer(3003);
	public static final Integer EKUNDSTATUS_EXCEPTION = new Integer(3004);
	public static final Integer ANSOKAN_EXCEPTION = new Integer(3005);
	private Integer errorId = null;

	/**
	 * Skapar ett applikations fel.
	 *
	 * @param message Felmeddelande
	 * @param errorId Identitiet för att kunna hämta feltext
	 */
	protected CsnJspException(String message, Integer errorId) {
		this(message, errorId, null);
	}




	/**
	 * Skapar ett applikations fel.
	 *
	 * @param message Felmeddelande
	 * @param errorId Identitiet för att kunna hämta feltext
	 * @param cause Orsak
	 */
	protected CsnJspException(String message, Integer errorId, Throwable cause) {
		super(message, cause);
		this.errorId = errorId;
	}




	/**
	 * @see se.csn.ark.common.CsnException#getType()
     * return application type
	 */
	public Integer getType() {
		return CsnException.APP;
	}




	/**
	 * Fel id för detta applikations fel som kan användas för att hämta motsvarande
	 * felmeddelande från fil eller databas.
	 *
	 * @see se.csn.ipl.webbansokan.arkitektur.CsnException#getFelId()
	 * @return Returnerar <code>null</code> om id ej definierat
	 */
	public Integer getErrorId() {
		return errorId;
	}
}