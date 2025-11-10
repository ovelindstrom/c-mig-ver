/*
 * Created on 2004-nov-18
 *
 */
package se.csn.ark.common;

import java.io.Serializable;

/**
 * Datatransport objekt för klasser som ärver av CsnException.
 * 
 * @author K-G Sjöström - AcandoFrontec.
 * @since 041118
 * @version 1
 *
 */
public class DTOException implements Serializable {

	private Integer type;
	private Integer errorId;
	private String cause;
	private String message;

	/**
	 * @see se.csn.ark.common.CsnException#getType()
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @see se.csn.ark.common.CsnException#getErrorId()
	 */
	public Integer getErrorId() {
		return errorId;
	}

	/**
	 * @see se.csn.ark.common.CsnException#getCause()
	 */
	public String getCauseName() {
		return cause;
	}
	/**
	 * @see se.csn.ark.common.CsnException#getMessage()
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param name felorsak
	 */
	public void setCauseName(String name) {
		cause = name;
	}

	/**
	 * @param id fel id
	 */
	public void setErrorId(Integer id) {
		errorId = id;
	}

	/**
	 * @param string felmeddelande
	 */
	public void setMessage(String string) {
		message = string;
	}

	/**
	 * @param i exception typ
	 */
	public void setType(Integer i) {
		type = i;
	}
	
}
