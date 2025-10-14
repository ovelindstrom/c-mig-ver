package se.csn.ark.common.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import se.csn.ark.common.util.logging.Log;

/**
 * Klass med hjälpmetoder för generell formatering.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041020
 * @version 1 skapad
 *
 */
public final class FormatDate {
	protected static final Log log = Log.getInstance(FormatDate.class);

	private static final String NORMAL_DATE_FORMAT = "yyyy-MM-dd";
	private static final String NORMAL_DATE_FORMAT2 = "yyyyMMdd";
	private static final String NORMAL_TIME_SEPARETED_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
	private static final String NORMAL_TIME_SEPARETED_FORMAT2 = "yyyy-MM-dd HH:mm:ss";
	private static final String SHORT_DATE_FORMAT = "yyMMdd";
	private static final String CURRENT_TIME_FORMAT = "yyMMdd-HHmmss";
	private static final String CURRENT_TIMESTAMP = "yyyyMMddHHmmssSSS";
	private static final String PNR_FORMAT = "0000000000";

	// Vi använder DecimalFormat även om vi inte vill ha 
	// decimaler för den ger möjlighet att speca formatet.
	private static DecimalFormat pnrFormatter;
	private static SimpleDateFormat normalDateFormatter;
	private static SimpleDateFormat normalDateFormatter2;
	private static SimpleDateFormat normalTimeSepFormatter;
	private static SimpleDateFormat normalTimeSepFormatter2;
	private static SimpleDateFormat currentTimeFormatter;
	private static SimpleDateFormat currentTimeStampFormatter;
	private static SimpleDateFormat shortDateFormatter;

	static {
		init();
	}

	/**
	 * Privat konstruktor, endast statisk åtkomst
	 */
	private FormatDate() {
    }

	/**
	 * Initiera formaterare
	 */
	public static void init() {
		normalDateFormatter = new SimpleDateFormat(NORMAL_DATE_FORMAT);
		shortDateFormatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
		normalDateFormatter2 = new SimpleDateFormat(NORMAL_DATE_FORMAT2);
		normalTimeSepFormatter = new SimpleDateFormat(NORMAL_TIME_SEPARETED_FORMAT);
		normalTimeSepFormatter2 = new SimpleDateFormat(NORMAL_TIME_SEPARETED_FORMAT2);
		currentTimeFormatter = new SimpleDateFormat(CURRENT_TIME_FORMAT);
		currentTimeStampFormatter = new SimpleDateFormat(CURRENT_TIMESTAMP);
		pnrFormatter = new DecimalFormat(PNR_FORMAT);
	}

	/**
	 * Generell metod för formatering av datum.
	 *
	 * @param date Datumet som skall formateras.
	 * @return Sträng med korrekt datumformat eller "null" om datum var null.
	 * @throws FormatException Om datumet inte kunde formateras pga felaktigt format.
	 */
	public static String toDateFormatStringYYMMDD(Date date) throws FormatException {
		String str = "null";

		if (date == null) {
			throw new FormatException("Kunde ej formatera Date=null");

		} else {
			try {
				str = shortDateFormatter.format(date);

			} catch (Exception e) {
				throw new FormatException("Kunde ej formatera Date=" + date, e);
			}
		}

		return str;
	}

	/**
	 * Generell metod för formatering av datum.
	 *
	 * @param gregCal Kalendern som skall formateras.
	 * @return Sträng med korrekt datumformat.
	 * @throws FormatException Om kalendern inte kunde formateras pga felaktigt format.
	 */
	public static String toDateFormatString(Calendar gregCal) throws FormatException {
		String str = "null";

		if (gregCal == null) {
			throw new FormatException("Kunde ej formatera GregorianCalendar=null");
		} else {
			try {
				str = normalDateFormatter.format(gregCal.getTime());
			} catch (Exception e) {
				throw new FormatException("Kunde ej formatera GregorianCalendar=" + gregCal, e);
			}
		}

		return str;
	}

	/**
	 * Generell metod för formatering av datum.
	 *
	 * @param date Datumet som skall formateras.
	 * @return Sträng med korrekt datumformat eller "null" om datum var null.
	 * @throws FormatException Om datumet inte kunde formateras pga felaktigt format.
	 */
	public static String toDateFormatString(Date date) throws FormatException {
		String str = "null";

		if (date == null) {
			throw new FormatException("Kunde ej formatera Date=null");
		} else {
			try {
				str = normalDateFormatter.format(date);
			} catch (Exception e) {
				throw new FormatException("Kunde ej formatera Date=" + date, e);
			}
		}

		return str;
	}

	/**
	 * Forrmaterar tid till ett en sträng med avskiljare
	 *
	 * @param date Datumet som skall formateras.
	 * @return Sträng med korrekt datumformat eller "null" om datum var null.
	 * @throws FormatException Om datumet inte kunde formateras pga felaktigt format.
	 */
	public static String toTimeSeparatedFormatString(Date date) throws FormatException {
		String str = "null";

		if (date == null) {
			throw new FormatException("Kunde ej formatera Date=null");
		} else {
			try {
				str = normalTimeSepFormatter.format(date);
			} catch (Exception e) {
				throw new FormatException("Kunde ej formatera Date=" + date, e);
			}
		}

		return str;
	}

	/**
	 * Generell metod för formatering av personnummer.
	 *
	 * @param d personnummer
	 * @return Sträng med korrekt personnummerformat.
	 * @throws FormatException Om formateringen misslyckades.
	 */
	public static String toPnrString(Double d) throws FormatException {
		String str = "null";

		if (d != null) {
			try {
				// Vi använder DecimalFormat även om vi inte vill ha 
				// decimaler för den ger möjlighet att speca formatet.
				pnrFormatter = new DecimalFormat(PNR_FORMAT);
				str = pnrFormatter.format(d.doubleValue());
			} catch (Exception e) {
				throw new FormatException(e);
			}
		}

		return str;
	}

	/**
	 * Tolkar datumsträng till ett kalenderobjekt.
	 *
	 * @param gregStr Datumsträng
	 * @return En kalender som motsvarar datumsträngen.
	 * @throws ParseException Datumsträngen kunde ej tolkas.
	 */
	public static GregorianCalendar parseGregorianString(String gregStr) throws ParseException {
		CustomGregorianCalendar cal = null;

		cal = new CustomGregorianCalendar();
		cal.setTime(parseDateString(gregStr));

		return cal;
	}

	/**
	 * Tolkar datumsträng till ett datumobjekt.
	 *
	 * @param dateStr Datumsträng
	 * @return Ett datum som motsvarar datumsträngen.
	 * @throws ParseException Datumsträngen kunde ej tolkas.
	 */
	public static Date parseDateString(String dateStr) throws ParseException {
		Date date = null;

		try {
			date = normalDateFormatter.parse(dateStr);
		} catch (java.text.ParseException pe) {
			throw new ParseException(pe);
		}

		return date;
	}

	/**
	 * Tolkar datumsträng till ett datumobjekt.
	 *
	 * @param dateStr Datumsträng
	 * @return Ett datum som motsvarar datumsträngen.
	 * @throws ParseException Datumsträngen kunde ej tolkas.
	 */
	public static Date parseDateString2(String dateStr) throws ParseException {
		Date date = null;

		try {
			date = normalDateFormatter2.parse(dateStr);
		} catch (java.text.ParseException pe) {
			throw new ParseException(pe);
		}

		return date;
	}

	/**
	 * Ger sträng med datum & tid för detta ögonblick.
	 *
	 * @return Sträng med format yyMMdd-HHmmss
	 */
	public static String getCurrentTimeString() {
		return currentTimeFormatter.format(new Date());
	}

	/**
	 * Ger sträng med datum & tid för detta ögonblick.
	 *
	 * @return Sträng med format yyyy-MM-dd HH:mm:ss:SSS
	 */
	public static String getCurrentTimeSeparatedString() {
		return normalTimeSepFormatter.format(new Date());
	}
	
	/**
	 * Ger sträng med datum & tid för detta ögonblick.
	 *
	 * @return Sträng med format yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTimeSeparatedString2() {
		return normalTimeSepFormatter2.format(new Date());
	}

	/**
	 * Ger sträng med datum & tid för detta ögonblick.
	 *
	 * @return Sträng med format yyyyMMddHHmmssSSS
	 */
	public static String getCurrentTimeStamp() {
		return currentTimeStampFormatter.format(new Date());
	}

	/**
	 * @param iToParse int att parsa
	 * @return sträng-representation av int
	 * @throws NullPointerException gick ej att formatera
	 */
	public static String parseFromIntToString(int iToParse) throws NullPointerException {
		try {
			return String.valueOf(iToParse);
		} catch (NullPointerException ne) {
			throw new NullPointerException("Kunde inte formater om från tal till integer");
		}
	}

	/**
	 * Gör replace på en sträng.
	 * 
	 * @param source original-sträng
	 * @param replace substräng skall ersättas
	 * @param with ny substräng
	 * @return den nya strängen
	 * @throws FormatException gick ej att utföra
	 * @author Tobias Larsson
	 * @since 20055027
	 * @version 1 skapad
	 */
	public static String replace(String source, String replace, String with) throws FormatException {
		try {
			int i = source.indexOf(replace);

			if (i == -1) {
				return source;
			}

			StringBuffer sb = new StringBuffer();

			sb.append(source.substring(0, i));
			sb.append(with);

			int j = source.indexOf(replace, i + replace.length());

			while (j != -1) {
				sb.append(source.substring(i + replace.length(), j));
				sb.append(with);
				i = j;
				j = source.indexOf(replace, j + replace.length());
			}

			sb.append(source.substring(i + replace.length()));

			return sb.toString();
		} catch (Exception ex) {
			throw new FormatException("Problem med replace funktionen", ex);
		}
	}

	/**
	 * Tar ut vilken vecka det är på året
	 * @param date datum
	 * @param cal kalender
	 * @return int veckonummer som datumet infaller i
	 * @author Tobias Larsson
	 * @since 20055027
	 * @version 1 skapad
	 */
	public static int getWeekOfYear(GregorianCalendar date, Calendar cal) {
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * Tar ut vilken vecka det är på året
	 * Förenklad.
	 * @param date datum
	 * @return int veckonummer som datumet infaller i
	 * @author Tobias Larsson
	 * @since 20055027
	 * @version 1 skapad
	 */
	public static int getWeekOfYear2(CustomGregorianCalendar date) {
		CustomGregorianCalendar gregTemp = new CustomGregorianCalendar(date.get(Calendar.YEAR), 
                                                                       date.get(Calendar.MONTH), 
                                                                       date.get(Calendar.DAY_OF_MONTH));

		;

		return gregTemp.get(Calendar.WEEK_OF_YEAR);
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
	 * @author Tobias Larsson
	 * @since 20055027
	 * @version 1 skapad
	 */
	public static int calculateIfFourHalfYearIsBetweenDates(int iStartYear, 
                                                            int iStartMonth, 
                                                            int iStartDay, 
                                                            int iStopYear, 
                                                            int iStopMonth, 
                                                            int iStopDay) {
		final int iTempDefault = 999;

		int iHalfYear = 0;
		int iTemp = iTempDefault;
		int iCounter = 0;

		try {
			CustomGregorianCalendar start = new CustomGregorianCalendar(iStartYear, iStartMonth, iStartDay);
			CustomGregorianCalendar end = new CustomGregorianCalendar(iStopYear, iStopMonth, iStopDay);

			while (start.getTime().compareTo(end.getTime()) < 0) {
				//start.add(Calendar.WEEK_OF_YEAR, 1);    
				start.add(Calendar.DAY_OF_WEEK, 1);

				/**
				 * Hämtar upp halvår, 0 eller 1
				 */
				iHalfYear = getStudyPeriod(getWeekOfYear2(start));

				//System.out.println(getWeekOfYear2(start));

				/**
				 * Om iHalfYear skiljer sig från iTemp så är det ett nytt halvår.
				 */
				if (iHalfYear != iTemp) {
					iTemp = iHalfYear;
					iCounter++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			iCounter = 0;
		}

		//System.out.println("Veckor mellan datum..: " + weeks);
		return iCounter;
	}

	/**
	 * Formaterar datum sträng till yyyy-mm-dd
	 * @param dtDate datum
	 * @return strän-representation av datum
	 */
	public static String formatDateToString(Date dtDate) {
		CustomGregorianCalendar greg = new CustomGregorianCalendar();
		Calendar cal = Calendar.getInstance(CustomGregorianCalendar.ZONE_SWE);
		
		greg.setTime(dtDate);

		String sDayOfMonth = parseFromIntToString(greg.get(Calendar.DAY_OF_MONTH));

		if (sDayOfMonth.length() == 1) {
			sDayOfMonth = "0" + sDayOfMonth;
		}

		String sMonth = parseFromIntToString(greg.get(Calendar.MONTH)+1);

		if (sMonth.length() == 1) {
			sMonth = "0" + sMonth;
		}

		String sYear = parseFromIntToString(greg.get(Calendar.YEAR));

		return sYear + "-" + sMonth + "-" + sDayOfMonth;

		//return greg.get(Calendar.YEAR) + "-" + greg;
	}

	/**
	 * Formaterar till formatet yyyymmdd
	 * @param dtDate datum
	 * @return sträng-representation av datum
	 * @author Tobias Larsson
	 * @since 20055027
	 * @version 1 skapad
	 */
	public static String formatDateToString2(Date dtDate) {
		CustomGregorianCalendar greg = new CustomGregorianCalendar();
		Calendar cal = Calendar.getInstance(CustomGregorianCalendar.ZONE_SWE);

		greg.setTime(dtDate);

		String sDayOfMonth = parseFromIntToString(greg.get(Calendar.DAY_OF_MONTH));

		if (sDayOfMonth.length() == 1) {
			sDayOfMonth = "0" + sDayOfMonth;
		}

		String sMonth = parseFromIntToString(greg.get(Calendar.MONTH)+1);

		if (sMonth.length() == 1) {
			sMonth = "0" + sMonth;
		}

		String sYear = parseFromIntToString(greg.get(Calendar.YEAR));

		return sYear + sMonth + sDayOfMonth;
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
	 * @author Tobias Larsson
	 * @since 20055027
	 * @version 1 skapad
	 */
	public static int calculateWeeksBetweenDates(int iStartYear, 
                                                 int iStartMonth, 
                                                 int iStartDay, 
                                                 int iStopYear, 
                                                 int iStopMonth, 
                                                 int iStopDay) {
		final int daysPerWeek = 7;
		int iWeeks = 0;

		try {
			CustomGregorianCalendar start = new CustomGregorianCalendar(iStartYear, iStartMonth, iStartDay);
			CustomGregorianCalendar end = new CustomGregorianCalendar(iStopYear, iStopMonth, iStopDay);

			int iTemp = 0;

			while (start.getTime().compareTo(end.getTime()) < 0) {
				//start.add(Calendar.WEEK_OF_YEAR, 1);    
				start.add(Calendar.DAY_OF_WEEK, 1);
				iTemp++;

				if (iTemp == daysPerWeek) {
					iTemp = 0;
					iWeeks++;
				}
			}
		} catch (Exception e) {
			//System.out.println("Veckor mellan datum..: " + e.toString());
			e.printStackTrace();
			iWeeks = 0;
		}

		//System.out.println("Veckor mellan datum..: " + iWeeks);
		return iWeeks;
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
	 * @author Tobias Larsson
	 * @since 20055027
	 * @version 1 skapad
	 */
	public static int calculateDaysBetweenDates(int iStartYear, 
                                                int iStartMonth, 
                                                int iStartDay, 
                                                int iStopYear, 
                                                int iStopMonth, 
                                                int iStopDay) {
		int iDays = 0;

		try {
			CustomGregorianCalendar start = new CustomGregorianCalendar(iStartYear, iStartMonth, iStartDay);
			CustomGregorianCalendar end = new CustomGregorianCalendar(iStopYear, iStopMonth, iStopDay);

			while (start.getTime().compareTo(end.getTime()) < 0) {
				//start.add(Calendar.WEEK_OF_YEAR, 1);    
				start.add(Calendar.DAY_OF_WEEK, 1);
				iDays++;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("calculateDaysBetweenDates",e);
			iDays = 0;
		}

		//System.out.println("Veckor mellan datum..: " + weeks);
		return iDays;
	}

	/**
	 * Hämtar ut studieperiod.
	 * Om det är vecka 1-26 returneras 0 annars 1
	 * @param iWeek vecka
	 * @return int 0 för vecka 1-26, annar 1
	 * @author Tobias Larsson
	 * @since 20055027
	 * @version 1 skapad
	 */
	public static int getStudyPeriod(int iWeek) {
		final int startPeriod1 = 27;
		if ((iWeek > 0) && (iWeek < startPeriod1)) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * @param dtStart från datum
	 * @param dtEnd till datum
	 * @return true om det är mer än 3 veckor mellan från och till-datum
	 * @author Tobias Larsson
	 * @since 20055027
	 * @version 1 skapad
	 */
	public static boolean isMoreThanThreeWeeks(GregorianCalendar dtStart, GregorianCalendar dtEnd) {
		final int maxNoWeeks = 2;

		if (log.isDebugEnabled()) {
			log.debug("STARTED isMoreThanThreeWeeks===>Start datum: " + dtStart.getTime().toString());
			log.debug("Stopp datum: " + dtEnd.getTime().toString());
		}

		CustomGregorianCalendar start = new CustomGregorianCalendar(dtStart.get(Calendar.YEAR), 
                                                                    dtStart.get(Calendar.MONTH), 
                                                                    dtStart.get(Calendar.DAY_OF_MONTH));
		CustomGregorianCalendar end = new CustomGregorianCalendar(dtEnd.get(Calendar.YEAR), 
                                                                  dtEnd.get(Calendar.MONTH), 
                                                                  dtEnd.get(Calendar.DAY_OF_MONTH));

		int iWeeks = 0;
		int iWeek = 0;
		int iTemp = 0;
		int x = 0;

		/**
		 * Denna addar 1 annars så blir det 1 dag för lite i loopen...kan missa en vecka annars.
		 */
		end.add(Calendar.DAY_OF_WEEK, 1);

		while (start.getTime().compareTo(end.getTime()) < 0) {
			if (x == 0) {
				iWeek = getWeekOfYear2(start);
				start.add(Calendar.DAY_OF_WEEK, 1);
				x++;
			} else {
				iWeek = getWeekOfYear2(start);
				start.add(Calendar.DAY_OF_WEEK, 1);
			}

			if (iWeek != iTemp) {
				iTemp = iWeek;
				iWeeks++;

				if (iWeeks > maxNoWeeks) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @param dtStart från datum
	 * @param dtEnd till datum
	 * @return true om det är mer än 53 veckor mellan från och till-datum
	 * @author Tobias Larsson
	 * @since 20055027
	 * @version 1 skapad
	 */
	public static boolean isMoreThanFiftyThreeWeeks(GregorianCalendar dtStart, GregorianCalendar dtEnd) {
		final int maxNoWeeks = 53;

		if (log.isDebugEnabled()) {
			log.debug("STARTED isMoreThanFiftyThreeWeeks===>Start datum: " + dtStart.getTime().toString());
			log.debug("Stopp datum: " + dtEnd.getTime().toString());
		}

		CustomGregorianCalendar start = new CustomGregorianCalendar(dtStart.get(Calendar.YEAR), 
                                                                    dtStart.get(Calendar.MONTH), 
                                                                    dtStart.get(Calendar.DAY_OF_MONTH));
		CustomGregorianCalendar end = new CustomGregorianCalendar(dtEnd.get(Calendar.YEAR), 
                                                                  dtEnd.get(Calendar.MONTH), 
                                                                  dtEnd.get(Calendar.DAY_OF_MONTH));

		int iWeeks = 0;
		int iWeek = 0;
		int iTemp = 0;
		int x = 0;

		/**
		 * Denna addar 1 annars så blir det 1 dag för lite i loopen...kan missa en vecka annars.
		 */
		end.add(Calendar.DAY_OF_WEEK, 1);

		while (start.getTime().compareTo(end.getTime()) < 0) {
			if (x == 0) {
				iWeek = getWeekOfYear2(start);
				start.add(Calendar.DAY_OF_WEEK, 1);
				x++;
			} else {
				iWeek = getWeekOfYear2(start);
				start.add(Calendar.DAY_OF_WEEK, 1);
			}

			if (iWeek != iTemp) {
				iTemp = iWeek;
				iWeeks++;

				if (iWeeks > maxNoWeeks) {
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * Konverterar datum från java.sql.Date till GregorianCalendar
	 * 
	 * @param java.sql.Date - datum som ska konverteras
	 * @return GregorianCalendar - konverterat datum 
	 * @throws ParseException om konverteringen inte gick bra 
	 * @author Andreas Hellström
	 * @since 20051027
	 * @version 1 skapad
	 */
	public static GregorianCalendar formatSqlDateToGregorianCalendar(java.sql.Date sqlDate)	
		throws ParseException {								
		
		String strDatum = formatDateToString(sqlDate);											 			
		return (parseGregorianString(strDatum));		
	}

    /**
     * @param gregCal datum som ska konverteras
     * @return konverterat datum
	 * @author Tobias Larsson
	 * @since 20051027
	 * @version 1 skapad
     */
	public static java.sql.Date formatGregorianCalendarToSqlDate(Calendar gregCal)	{
		java.sql.Date date = new java.sql.Date
							(
							new GregorianCalendar(
								gregCal.get(Calendar.YEAR),	
								gregCal.get(Calendar.MONTH),
								gregCal.get(Calendar.DAY_OF_MONTH)
								).getTime().getTime()
							);
		return date;	
	}
	
	
}