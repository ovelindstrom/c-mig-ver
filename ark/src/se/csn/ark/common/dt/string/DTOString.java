package se.csn.ark.common.dt.string;

import se.csn.ark.common.dt.CsnDataTransferObjectImpl;


/**
 * Datatransport-objekt för en sträng.
 *
 * @author Joakim Olsson
 * @since 20050221
 * @version 0.1 skapad
 *
 */
public class DTOString extends CsnDataTransferObjectImpl {
	private String value;

    /**
     * skapa DTO med värde null
     */
	public DTOString() {
		this.value = null;
	}




    /**
     * @param value skapa DTO med värde
     */
	public DTOString(String value) {
		this.value = value;
	}




    /**
     * @return värdet
     */
	public String getValue() {
		return value;
	}




	/**
	 * @param string värdet
	 */
	public void setValue(String string) {
		value = string;
	}




	/**
     * Strängrepresentationen av detta objekt.
     * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return value;
	}
}