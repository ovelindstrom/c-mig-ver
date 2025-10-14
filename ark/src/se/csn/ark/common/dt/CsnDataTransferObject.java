/*
 * Created on 2004-sep-14
 *
 */
package se.csn.ark.common.dt;

import java.io.Serializable;

import se.csn.ark.common.DTOException;

/**
 * Basgränsnsitt för datatransport objekt.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20040914
 * @version 1 skapad
 *
 */
public interface CsnDataTransferObject extends Serializable {

	/**
	 * Sätter datatransport objekt för exception.
	 * 
	 * @param de Exception för transport.
	 */
	public void setDTOException(DTOException de);
	
	/**
	 * Ger datatransport objekt för exception.
	 * 
	 * @return Transporterat exception.
	 */
	public DTOException getDTOException();
	
	
	/**
	 * Sätter transactionsid.
	 * Används i första hand för loggning.
	 * 
	 * @param transactionId Id för aktuell trnsaktion.
	 */
	public void setTransactionId(String transactionId);
	
	/**
	 * Ger transaktions id för pågående trsaktion.
	 * Används i första hand för loggning.
	 * 
	 * @return Id för aktuell transaktion.
	 */
	public String getTransactionId();
	
	
	/**
	 * Ger händelse.
	 * Används i första hand för loggning.
	 * 
	 * @return  Beskrivning av den händelse som skall loggas.
	 */
	public String getEvent();

	/**
	 * Sätter händelse.
	 * Används i första hand för loggning.
	 * 
	 * @param event Beskrivning av den händelse som skall loggas.
	 */
	public void setEvent(String event);
	
	
}
