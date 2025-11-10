/*
 * Created on 2004-okt-21
 *
 */
package se.csn.common.fam;

import java.io.Serializable;


/**
 * @author csn7478
 * @version 1.01
 *
 */
public class DTORetrievedDocument implements Serializable  {
	private byte[] bBuf = new byte[0];

	/**
	 * @return byte[]
	 */
	public byte[] getBBuf() {
		return bBuf;
	}


	/**
	 * @param bs byte[]
	 */
	public void setBBuf(byte[] bs) {
		bBuf = bs;
	}
}