/**
 * Skapad 2007-maj-23
 * @author Jonas åhrnell (csn7821)
 * @author Petrus Bergman (csn7820)
 */
package se.csn.common.mail;

import se.csn.ark.common.util.logging.Log;


/**
 * Validerar en e-postadress.
 * <p>
 * En e-postadress får inte vara tom och måste ha formen av en korrekt e-postadress.
 * <p>
 * &quot;local-part&quot; har en maxlängd på 64 tecken.<br>
 * &quot;domain name&quot; har en maxlängd på 255 tecken.
 * <p>
 * <i>&quot;According to RFC 2822, the local-part of the address may use any of these ASCII characters:
 * <ul>
 * <li>Uppercase and lowercase letters (case insensitive)</li>
 * <li>The digits 0 through 9</li>
 * <li>The characters ! # $ % & ' * + - / = ? ^ _ ` { | } ~</li>
 * <li>The character . provided that it is not the first or last character in the local-part.</li>
 * </ul>
 * The dot separated domain labels are limited to letters, digits, and hyphens drawn from the
 * ASCII character set.&quot;</i>
 * <p>
 * Källa: http://en.wikipedia.org/wiki/E-mail_address  (2006-12-28)
 */
public final class EmailAdressValidator {
	
	/**
	 * Hämta logg.
	 */
	private static Log log = Log.getInstance(EmailAdressValidator.class);
	
	
	/**
	 * <code>EmailAdressValidator</code> är en utility-klass
	 * och ska inte instansieras.
	 */
	private EmailAdressValidator() {
		// Utility class
	}
	
	/**
	 * 
	 * @param adress den e-postadress som ska valideras
	 * @return <code>true</code> om e-postadressen är korrekt formaterad, annars <code>false</code>
	 */
	public static boolean isValid(String adress) {
		if (log.isDebugEnabled()) { log.debug("Verifierar adress: " + adress); }
		if (adress != null) {
			// Kontrollera att e-postadressen följer mönstret för
			// e-postadresser enligt RFC 2821 och RFC 2822
			String e = adress.trim().toLowerCase();
			String atext = "[a-z0-9!#$%&'*+\\-/=?^_`{|}~]";
			String local = atext + "+(\\." + atext + "+)*";
			String dtext = "[a-z0-9\\-]";
			String domain = dtext + "+(\\." + dtext + "+)+";
			String epostReg = "^" + local + "@" + domain + "$";
			
			// Enligt RFC 2821 (4.5.3.1) http://www.ietf.org/rfc/rfc2821.txt gäller:
			// "The maximum total length of a user name or other local-part is 64 characters."
			// "The maximum total length of a domain name or number is 255 characters."
			String lengthReg = "^[^@]{1,64}@[^@]{4,255}$";
			
			if (log.isDebugEnabled()) {
				log.debug("epostRegExp: " + epostReg);
				log.debug("lengthRegExp: " + epostReg);
			}
						
			if (e.matches(epostReg) && e.matches(lengthReg)) {
				// E-postadressen ser ut att vara korrekt
				if (log.isDebugEnabled()) { log.debug("OK"); }
				return true;
			} else {
				// Felaktigt formaterad e-postadress
				if (log.isDebugEnabled()) { log.debug("Ogiltig adress"); }
			}
		} else {
			// E-postadress saknas
			if (log.isDebugEnabled()) { log.debug("Adress saknas"); }
		}
		return false;
	}
	
}
