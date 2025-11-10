package se.csn.ark.common.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * @author Tobias Larsson
 * @since 20041204
 * @version 1 skapad
 * 
 * Custom klass för att sätta svenska inställningar för GregorianCalendar
 */
public class CustomGregorianCalendar extends GregorianCalendar {
	public static final TimeZone ZONE_SWE = TimeZone.getTimeZone("Europe/Stockholm");
	public static final Locale LOCAL_SWE = new Locale("sv", "SE");
    private static final int MIN_DAYS_FIRST_WEEK = 4;

	/**
	 * Default constructor
	 * Sätter TimeZone till Europe/Stockholm
	 * Locale "sv", "SE"
	 * setFirstDayOfWeek = 1
	 * setMinimalDaysInFirstWeek = 4
	 */
	public CustomGregorianCalendar() {
        
		TimeZone.setDefault(ZONE_SWE);
		Locale.setDefault(LOCAL_SWE);

		this.setFirstDayOfWeek(Calendar.MONDAY);
		this.setMinimalDaysInFirstWeek(MIN_DAYS_FIRST_WEEK);
	}




	/**
	 * Constructor
	 * @param year skapa med år
	 * @param month skapa med månad
	 * @param day skapa med dag
	 */
	public CustomGregorianCalendar(int year, int month, int day) {
		TimeZone.setDefault(ZONE_SWE);
		Locale.setDefault(LOCAL_SWE);

		this.setFirstDayOfWeek(Calendar.MONDAY);
		this.setMinimalDaysInFirstWeek(MIN_DAYS_FIRST_WEEK);
		this.set(year, month, day);
	}




	/**
	 * Constructor
     * @param year skapa med år
     * @param month skapa med månad
     * @param day skapa med dag
	 * @param hour skapa med timme
	 * @param minute skapa med minut
	 * @param second skapa med sekund
	 */
	public CustomGregorianCalendar(
	                               int year,
	                               int month,
	                               int day,
	                               int hour,
	                               int minute,
	                               int second) {
		TimeZone.setDefault(ZONE_SWE);
		Locale.setDefault(LOCAL_SWE);

		this.setFirstDayOfWeek(Calendar.MONDAY);
		this.setMinimalDaysInFirstWeek(MIN_DAYS_FIRST_WEEK);
		this.set(year, month, day, hour, minute, second);
	}
}