package se.csn.ark.common.dt;

import java.util.GregorianCalendar;

import se.csn.ark.common.CsnException;
import se.csn.ark.common.DTOException;
import se.csn.ark.common.util.FormatConvertException;
import se.csn.ark.common.util.FormatDate;
import se.csn.ark.common.util.FormatException;


/**
 * Grundklass för alla typer av datatransportobjekt.
 *
 * @author K-G Sjöström
 * @since 20040914
 * @version 1 skapad
 *
 */
public class CsnDataTransferObjectImpl implements CsnDataTransferObject {
	/**
	 * Konstant för newline tecken lämpligt för användande i
	 * exempelvis toString metod.
	 */
	protected static final String NL = "\n";
	private DTOException dtoException;
	private String transactionId;
	private String event;

	/**
	 * Konstruktor som används om man inte vill använda transaktions id
	 */
	public CsnDataTransferObjectImpl() {
		this(false);
	}




	/**
	 * Konstruktor som används om man vill använda transaktions id
	 * @param useTransactionID id för trasaktion med denna dto
	 */
	public CsnDataTransferObjectImpl(boolean useTransactionID) {
		if (useTransactionID) {
			long tID = System.currentTimeMillis();

			setTransactionId(new String("" + tID));
		}
	}




	/**
	 * @return true om dto'n innehåller exception
	 */
	public boolean isException() {
		if (dtoException != null) {
			return true;
		}

		return false;
	}




	/**
	 * Bekvämlighetsmetod för att formatera datum i toString
	 * metoder. Eventuella Exceptions "sväljs" då detta är till
	 * för att skapa en sträng som skall användas för debug och
	 * loggning.
	 *
	 * @param gregCal Kalender som ska formateras.
	 * @return String sträng för användande i toString metod.
	 */
	protected String toStringDateFormat(GregorianCalendar gregCal) {
		String dateStr = null;

		try {
			dateStr = FormatDate.toDateFormatString(gregCal);
		} catch (FormatException fe) {
			// Gör ingenting alls.
		}

		return dateStr;
	}




	/**
	 * Ger datatransport objekt för exception.
	 *
	 * @return Transporterat exception.
	 */
	public DTOException getDTOException() {
		return dtoException;
	}




	/**
	 * Sätter datatransport objekt för exception.
	 *
	 * @param de Exception för transport.
	 */
	public void setDTOException(DTOException de) {
		this.dtoException = de;
	}




	/**
	 * @param ce exception för transport
	 */
	public void setCsnException(DTOException ce) {
		this.dtoException = ce;
	}




	/**
	 * Ger transaktions id för pågående trsaktion.
	 * Används i första hand för loggning.
	 *
	 * @return Id för aktuell transaktion.
	 */
	public String getTransactionId() {
		return transactionId;
	}




	/**
	 * Sätter transactionsid.
	 * Används i första hand för loggning.
	 *
	 * @param string Id för aktuell transaktion.
	 */
	public void setTransactionId(String string) {
		transactionId = string;
	}




	/**
	 * Ger händelse.
	 * Används i första hand för loggning.
	 *
	 * @return  Beskrivning av den händelse som skall loggas.
	 */
	public String getEvent() {
		return event;
	}




	/**
	 * Sätter händelse.
	 * Används i första hand för loggning.
	 *
	 * @param ev Beskrivning av den händelse som skall loggas.
	 */
	public void setEvent(String ev) {
		this.event = ev;
	}
}