package se.csn.ark.common;

import java.io.Serializable;


/**
 *
 * <p><b>ALLA</b> <code>Exception</code> i CSN:s java ramverk <u>skall</u>
 * implementera detta gränssnitt !</p>
 *
 * <p>Detta gränsnsitt är designat för att vara kompatibelt med stödet för
 * <a href=
 * "http://java.sun.com/j2se/1.4.2/docs/guide/lang/chained-exceptions.html">"
 * chained exceptions"</a> i JDK 1.4. om än i en försvenskad variant
 * (Cause => Orsak).</p>
 *
 * @author K-G Sjöström
 * @since 040809 K-G Sjöström
 * @version 1 skapad
 *
 */
public interface CsnException extends Serializable {
	public static final Integer SYSTEM = new Integer(0);
	public static final Integer APP = new Integer(1);
    public static final Integer LOGICAL = new Integer(2);
	public static final String SOURCE_IPL = "IPL";
	public static final String SOURCE_IPL_CLIENT = "IPL_CLIENT";
	public static final String SOURCE_BROKER_PLUGIN = "BROKER";


    /**
     * @return exception typ
     */
	public Integer getType();




	/**
	 *
	 * Fel id kan användas för att hämta motsvarande felmeddelande från fil
	 * eller databas.
	 *
	 * @return Fel identiteten för den situationen som detta fel uppstod i.
	 * Returnerar <code>null</code> om id ej definierat
	 */
	public Integer getErrorId();




	/**
	 *
	 * Ger orsaken till detta fel dvs ursprungliga felet som eventuellt
	 * kan ha gett en kedja av fel.
	 * Detta är en försvenskad variant av av metodsignaturen <code>getCause</code> som
	 * infördes för <code>Throwable</code> i JDK 1.4 för att hantera "nested exceptions".
	 *
	 * @return Ursprungsfelet
	 */
	public Throwable getCause();
	




	/**
	 * Returnerar felmeddelandet för detta objekt.
	 *
	 * @return Felmeddelandet för detta objekt om det blev skapat med ett
	 * message annars resturneras <code>null</code>
	 */
	public String getMessage();
}