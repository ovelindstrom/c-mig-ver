package se.csn.ark.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import se.csn.ark.common.CsnApplicationException;
import se.csn.ark.common.CsnException;
import se.csn.ark.common.CsnSystemException;
import se.csn.ark.common.DTOException;
import se.csn.ark.common.util.logging.Log;

/**
 * Klass med hjälpmetoder för generell formatering.
 * Inga datum klasser skall finnas i denna fil, dessa har blivit flyttade till FormatDate.	(Tobias)
 * @author K-G Sjöström - AcandoFrontec
 * @deprecated  använd metoder i FormatDate, FormatNumber, FormatException.
 * @since 20041020
 * @version 1 skapad
 * @version 2 skapad Tobias Larsson 2005-09-19
 * 
 *
 */
public final class Format {
	protected static final Log log = Log.getInstance(Format.class);

	static {
		FormatDate.init();
	}

	/**
	 * Privat konstruktor, endast statisk åtkomst
	 */
	private Format() {
    }



	/**
	 * Generell metod för formatering av datum.
	 *
	 * @param date Datumet som skall formateras.
	 * @return Sträng med korrekt datumformat eller "null" om datum var null.
	 * @throws FormatException Om datumet inte kunde formateras pga felaktigt format.
	 */
	public static String toDateFormatStringYYMMDD(Date date) throws FormatException {

		return FormatDate.toDateFormatStringYYMMDD(date);

	}

	/**
	 * Generell metod för formatering av datum.
	 *
	 * @param gregCal Kalendern som skall formateras.
	 * @return Sträng med korrekt datumformat.
	 * @throws FormatException Om kalendern inte kunde formateras pga felaktigt format.
	 */
	public static String toDateFormatString(GregorianCalendar gregCal) throws FormatException {

		return FormatDate.toDateFormatString(gregCal);
	}

	/**
	 * Generell metod för formatering av datum.
	 *
	 * @param date Datumet som skall formateras.
	 * @return Sträng med korrekt datumformat eller "null" om datum var null.
	 * @throws FormatException Om datumet inte kunde formateras pga felaktigt format.
	 */
	public static String toDateFormatString(Date date) throws FormatException {

		return FormatDate.toDateFormatString(date);
	}

	/**
	 * Forrmaterar tid till ett en sträng med avskiljare
	 *
	 * @param date Datumet som skall formateras.
	 * @return Sträng med korrekt datumformat eller "null" om datum var null.
	 * @throws FormatException Om datumet inte kunde formateras pga felaktigt format.
	 */
	public static String toTimeSeparatedFormatString(Date date) throws FormatException {
		return FormatDate.toTimeSeparatedFormatString(date);
	}

	/**
	 * Generell metod för formatering av personnummer.
	 *
	 * @param d personnummer
	 * @return Sträng med korrekt personnummerformat.
	 * @throws FormatException Om formateringen misslyckades.
	 */
	public static String toPnrString(Double d) throws FormatException {
		return FormatDate.toPnrString(d);
	}

	/**
	 * Tolkar datumsträng till ett kalenderobjekt.
	 *
	 * @param gregStr Datumsträng
	 * @return En kalender som motsvarar datumsträngen.
	 * @throws ParseException Datumsträngen kunde ej tolkas.
	 */
	public static GregorianCalendar parseGregorianString(String gregStr) throws ParseException {
		return FormatDate.parseGregorianString(gregStr);
	}

	/**
	 * Tolkar datumsträng till ett datumobjekt.
	 *
	 * @param dateStr Datumsträng
	 * @return Ett datum som motsvarar datumsträngen.
	 * @throws ParseException Datumsträngen kunde ej tolkas.
	 */
	public static Date parseDateString(String dateStr) throws ParseException {

		return FormatDate.parseDateString(dateStr);
	}

	/**
	 * Tolkar datumsträng till ett datumobjekt.
	 *
	 * @param dateStr Datumsträng
	 * @return Ett datum som motsvarar datumsträngen.
	 * @throws ParseException Datumsträngen kunde ej tolkas.
	 */
	public static Date parseDateString2(String dateStr) throws ParseException {
		return FormatDate.parseDateString2(dateStr);
	}

	/**
	 * Ger sträng med datum & tid för detta ögonblick.
	 *
	 * @return Sträng med format yyMMdd-HHmmss
	 */
	public static String getCurrentTimeString() {
		return FormatDate.getCurrentTimeStamp();

	}

	/**
	 * Ger sträng med datum & tid för detta ögonblick.
	 *
	 * @return Sträng med format yyyy-MM-dd HH:mm:ss:SSS
	 */
	public static String getCurrentTimeSeparatedString() {
		return FormatDate.getCurrentTimeSeparatedString();
	}

	/**
	 * Ger sträng med datum & tid för detta ögonblick.
	 *
	 * @return Sträng med format yyyyMMddHHmmssSSS
	 */
	public static String getCurrentTimeStamp() {
		return FormatDate.getCurrentTimeStamp();

	}

	/**
	 * @param iToParse int att parsa
	 * @return sträng-representation av int
	 * @throws NullPointerException gick ej att formatera
	 */
	public static String parseFromIntToString(int iToParse) throws NullPointerException {
		return FormatDate.parseFromIntToString(iToParse);
	}

	/**
	 * konvertera från exception till dto
	 * @param csnException exception att konvertera
	 * @return dto-representation av exception
	 */
	public static DTOException csnExceptioToDTOException(CsnException csnException) {

		return FormatConvertException.csnExceptioToDTOException(csnException);
//		DTOException dtoException = new DTOException();
//
//		if (csnException.getCause() == null) {
//			dtoException.setCauseName(null);
//		
//        } else {
//			dtoException.setCauseName(csnException.getCause().getClass().getName());
//		}
//
//		dtoException.setErrorId(csnException.getErrorId());
//		dtoException.setMessage(csnException.getMessage());
//		dtoException.setType(csnException.getType());
//
//		return dtoException;
	}

	/**
	 * konvertera från dto till exception
	 * @param dtoException dto att konvertera
	 * @return exception-representation av dto
	 */
	public static CsnException dtoExceptionToCsnException(DTOException dtoException) {
		
		return FormatConvertException.dtoExceptionToCsnException(dtoException);
//		CsnException csnException = null;
//
//		if (dtoException.getType().equals(CsnException.APP)) {
//			csnException = CsnApplicationException.reCreateCsnException(dtoException.getMessage(), dtoException.getErrorId());
//
//		} else if (dtoException.getType().equals(CsnException.SYSTEM)) {
//			csnException = CsnSystemException.reCreateCsnException(dtoException.getMessage(), dtoException.getErrorId());
//		}
//
//		return csnException;
	}

	/**
	 * Gör replace på en sträng.
	 * @deprecated  använd metod i FormatDate
	 * @param source original-sträng
	 * @param replace substräng skall ersättas
	 * @param with ny substräng
	 * @return den nya strängen
	 * @throws FormatException gick ej att utföra
	 */
	public static String replace(String source, String replace, String with) throws FormatException {

		return FormatDate.replace(source, replace, with);

	}

	/**
	 * Tar ut vilken vecka det är på året
	 * @param date datum
	 * @param cal kalender
	 * @return int veckonummer som datumet infaller i
	 */
	public static int getWeekOfYear(GregorianCalendar date, Calendar cal) {
		return FormatDate.getWeekOfYear(date, cal);
	}

	/**
	 * Tar ut vilken vecka det är på året
	 * Förenklad.
	 * @param date datum
	 * @return int veckonummer som datumet infaller i
	 */
	public static int getWeekOfYear2(CustomGregorianCalendar date) {
		return FormatDate.getWeekOfYear2(date);

	}

	/**
	 * Funktion som kollar hur många halvår det är mellan 2 datum.
	 * @param iStartYear datum 1, år
	 * @param iStartMonth datum 1, månad
	 * @param iStartDay datum 1, dag
	 * @param iStopYear datum 2, år
	 * @param iStopMonth datum 2, månad
	 * @param iStopDay datum 2, dag
	 * @return int antal halvår mellan datum 1 och datum 2
	 */
	public static int calculateIfFourHalfYearIsBetweenDates(int iStartYear, 
                                                            int iStartMonth, 
                                                            int iStartDay, 
                                                            int iStopYear, 
                                                            int iStopMonth, 
                                                            int iStopDay) {

		return FormatDate.calculateIfFourHalfYearIsBetweenDates(iStartYear, iStartMonth, iStartDay, iStopYear, iStopMonth, iStopDay);

	}

	/**
	 * Formaterar datum sträng till yyyy-mm-dd
	 * @param dtDate datum
	 * @return strän-representation av datum
	 */
	public static String formatDateToString(Date dtDate) {
		return FormatDate.formatDateToString(dtDate);
	}

	/**
	 * Formaterar till formatet yyyymmdd
	 * @param dtDate datum
	 * @return sträng-representation av datum
	 */
	public static String formatDateToString2(Date dtDate) {

		return FormatDate.formatDateToString2(dtDate);
	}

	/**
	 * Räknar ut veckor mellan datum
	 * @param iStartYear datum 1, år
	 * @param iStartMonth datum 1, månad
	 * @param iStartDay datum 1, dag
	 * @param iStopYear datum 2, år
	 * @param iStopMonth datum 2, månad
	 * @param iStopDay datum 2, dag
	 * @return int antal veckor mellan datum 1 och datum 2
	 */
	public static int calculateWeeksBetweenDates(int iStartYear, 
                                                 int iStartMonth, 
                                                 int iStartDay, 
                                                 int iStopYear, 
                                                 int iStopMonth, 
                                                 int iStopDay) {

		return FormatDate.calculateWeeksBetweenDates(iStartYear, iStartMonth, iStartDay, iStopYear, iStopMonth, iStopDay);

	}

	/**
	 * Räknar ut dagar mellan datum
	 * @param iStartYear datum 1, år
	 * @param iStartMonth datum 1, månad
	 * @param iStartDay datum 1, dag
	 * @param iStopYear datum 2, år
	 * @param iStopMonth datum 2, månad
	 * @param iStopDay datum 2, dag
	 * @return int antal dagar mellan datum 1 och datum 2
	 */
	public static int calculateDaysBetweenDates(int iStartYear, 
                                                int iStartMonth, 
                                                int iStartDay, 
                                                int iStopYear, 
                                                int iStopMonth, 
                                                int iStopDay) {

		return FormatDate.calculateDaysBetweenDates(iStartYear, iStartMonth, iStartDay, iStopYear, iStopMonth, iStopDay);

	}

	/**
	 * Hämtar ut studieperiod.
	 * Om det är vecka 1-26 returneras 0 annars 1
	 * @param iWeek vecka
	 * @return int 0 för vecka 1-26, annar 1
	 */
	public static int getStudyPeriod(int iWeek) {
		return FormatDate.getStudyPeriod(iWeek);
	}

	/**
	 * @param dtStart från datum
	 * @param dtEnd till datum
	 * @return true om det är mer än 3 veckor mellan från och till-datum
	 */
	public static boolean isMoreThanThreeWeeks(GregorianCalendar dtStart, GregorianCalendar dtEnd) {

		return FormatDate.isMoreThanThreeWeeks(dtStart, dtEnd);

	}

	/**
	 * @param dtStart från datum
	 * @param dtEnd till datum
	 * @return true om det är mer än 53 veckor mellan från och till-datum
	 */
	public static boolean isMoreThanFiftyThreeWeeks(GregorianCalendar dtStart, GregorianCalendar dtEnd) {
		return FormatDate.isMoreThanFiftyThreeWeeks(dtStart, dtEnd);
	}
}