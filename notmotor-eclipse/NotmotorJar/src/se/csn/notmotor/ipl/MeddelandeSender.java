
package se.csn.notmotor.ipl;

import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.SandResultat;

/**
 * Interface som markerar alla sändartjänster.  
 * 
 * @author Jonas Öhrnell - csn7821
 */
public interface MeddelandeSender {
	
    /**
     * @param meddelande Det meddelande som ska sändas
     * @return En meddelandehändelse som markerar hur sändningen gick, eller 
     * 		null om denna sändare inte befattar sig med meddelanden som har det här
     * 		innehållet.
     */
	public SandResultat skickaMeddelande(Meddelande meddelande);
	
	/**
	 * Kontrollerar om detta meddelande kan skickas av denna sändare. 
	 * @param meddelande
	 * @return true om denna sändare KAN skicka meddelandet, dvs om meddelandet har 
	 * 		rätt adresstyp etc. Annars returneras false. 
	 */
	public boolean kanSkickaMeddelande(Meddelande meddelande);
}
