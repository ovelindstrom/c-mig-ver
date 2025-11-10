package se.csn.ark.common.dt.integer;

import se.csn.ark.common.dt.CsnDataTransferObjectImpl;


/**
 * Datatransport-objekt för en integer.
 *
 * @author Joakim Olsson
 * @since 20050221
 * @version 0.1 skapad
 *
 */
public class DTOInteger extends CsnDataTransferObjectImpl {
	private Integer value;

    /**
     * skapa DTO med värde null
     */
	public DTOInteger() {
		this.value = null;
	}



    /**
     * @param value skapa DTO med värde
     */
	public DTOInteger(int value) {
		this.value = new Integer(value);
	}




	/**
	 * @param value skapa DTO med värde
	 */
	public DTOInteger(Integer value) {
		this.value = value;
	}

	/**
	 * @return värdet
	 */
	public Integer getValue() {
		return value;
	}




	/**
	 * @param integer värdet
	 */
	public void setValue(Integer integer) {
		value = integer;
	}




	/**
     * Strängrepresentationen av detta objekt.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "" + value;
	}
}