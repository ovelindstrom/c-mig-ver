/*
 * Created on 2004-nov-25
 *
 */
package se.csn.common.fam;

import java.io.Serializable;


/**
 * @author csn7478
 * @version 1.01
 *
 * Identifikatonsnummer på ett dokument i edh.
 */
public class DTOFamId implements Serializable {
	private String sFamId;

	/**
	 * @return Dokumentets Id i strängform
	 */
	public String getSFamId() {
		return sFamId;
	}




	/**
	 * @param string Dokumentets Id i strängform
	 */
	public void setSFamId(String string) {
		sFamId = string;
	}
}