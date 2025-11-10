
package se.csn.ark.common.util;

/**
 * Klass med hjälpmetoder för generell formatering av nummer.
 *
 * @author 
 * @since 20050920
 * @version 1 skapad
 *
 */
public final class FormatNumber {
    
    /**
     * Privat konstruktor för en utility-klass.
     * Dvs med endast statisak metoder.
     *
     */
    private FormatNumber() {
    }


	/**
	 * Formaterar ett givet belopp till en sträng med tusenavskiljare.
	 * Som tecken för avskiljare används ett mellanslag.
	 * Metoden klarar alla belopp inom intervallet Integer (även negativa).
	 * 
	 * @param belopp som ska formateras
	 * @return String en sträng med ett mellangslag varje tusendel.
	 *         Om beloppet in är null returneras en tom sträng.
	 * 
	 * @author Andreas Hellström
	 * @since 20050920
	 * @version 1 skapad
	 */
	public static String formateraBeloppMedTusenAvskiljare(Integer belopp) {
	   final int separatorPosition = 3;
       
		if (belopp == null) {
            return "";
		}
		
		StringBuffer stringBuffer = new StringBuffer();
	
		stringBuffer.append(belopp.toString()); 
	
		// Gå från slutet av strängen och skjut in ett mellanslag var tredje position
		int sistaPos = stringBuffer.length();
		for (int i = sistaPos - separatorPosition; i >= 0; i -= separatorPosition) {
			stringBuffer.insert(i, " ");
		}		
	
		// Algoritmen ovan tar ingen hänsyn till att det blir ett mellanslag som första tecken.
		// Därför måste vi trimma till den lite grann, innan vi kan returnera en korrekt
		// tusenavkiljd beloppssträng.		
		String beloppMedTusenAvskiljare = stringBuffer.toString();			
		beloppMedTusenAvskiljare = beloppMedTusenAvskiljare.trim();
		beloppMedTusenAvskiljare = beloppMedTusenAvskiljare.replaceFirst("- ", "-");
	
		return beloppMedTusenAvskiljare;		
	}
	
	/**
	 * Kollar om det är ett nummer av en sträng.
	 * @param n String
	 * @return boolean
	 * @author Tobias Larsson
	 * @since 20051027
	 * @version 1 skapad
	 */
	public static boolean isNumber(String n) {
	  try {
		double d = Double.valueOf(n).doubleValue();
		return true;
		}
	  catch (NumberFormatException e) {
		e.printStackTrace();
		return false;
		}
	}


}
