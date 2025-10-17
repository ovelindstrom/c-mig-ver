/**
 * @since 2007-mar-02
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.ft;

import java.util.Date;

import se.csn.notmotor.ipl.model.Avsandare;
import se.csn.notmotor.ipl.model.Bilaga;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.NotifieringResultat;


public interface NotifieringProxy {
    
    
    
    /**
     * @param meddelande Det meddelande som ska skickas. 
     * @param callbackEndpoint URL till den webservice som ska
     *        anropas när meddelandet behandlas i en händelse som 
     * 		  definierats i callbackHandelser. Kan vara null.
     * 		  Den utpekade URL:en ska peka på en webservice som 
     * 		  implementerar interfacet NotifieringCallback.
     * @param callbackHandelser En mask/flagga som pekar ut för vilka
     *        händelser som callbackanrop ska göras till den utpekade
     * 		  webservicen.
     * @throws IllegalArgumentException om någon av inparametrarna
     *        i meddelandet är ogiltig.
     * @throws RuntimeException om tjänsten har någon form av internt fel. 		   
     */
    NotifieringResultat skickaMeddelande(Meddelande meddelande);
    
    /**
     * @param meddelandeId Id för det meddelande som ska hämtas.
     * @return Ett meddelande om det fanns ett meddelande med det angivna 
     * 	       id:t, annars null. 
     * @throws IllegalArgumentException om någon av inparametrarna
     *        i meddelandet är ogiltig.
     * @throws RuntimeException om tjänsten har någon form av internt fel. 		   
     */
    Meddelande hamtaMeddelande(Long meddelandeId);

    /**
     * Sätter om status för meddelandet till BORTTAGET. Meddelandet
     * ligger alltså kvar i databasen men kommer inte att sändas. 
     * @param meddelandeId Meddelande-id
     */
    NotifieringResultat taBortMeddelande(Long meddelandeId);
    
    /**
     * Den huvudsakliga söktjänsten för meddelanden. 
     * 
     * 
     * @return En lista av meddelanden som matchar sökningen. Om 
     * 		   inga meddelanden hittas returneras en tom lista. 
     * @throws IllegalArgumentException om någon av inparametrarna
     *        i meddelandet är ogiltig.
     * @throws RuntimeException om tjänsten har någon form av internt fel. 		   
     */
    Meddelande[] sokMeddelanden(
            Date from, 
            Date tom, 
            Avsandare[] avsandare, 
            Mottagare[] mottagare, 
            String textinnehall,
            Integer minstorlek, 
            Integer maxstorlek, 
            Integer handelseMask,
            Integer felmask,
            Bilaga[] bilagor
        );    
    
    Avsandare[] sokAvsandare(
            String namndel,
            String applikationsdel,
            String kategoridel,
            String adressdel,
            String replytoDel
    );
    
    Mottagare[] sokMottagare(
            String namndel,
            String adressdel,
            String typ,
            Integer csnnrFrom,
            Integer csnnrTom
    );
    
}
