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
     * @throws IllegalArgumentException om någon av inparametrarna i meddelandet är ogiltig.
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
     * @param from Från-datum för sökningen
     * @param tom Till-datum för sökningen
     * @param avsandare Array med avsändare att söka efter
     * @param mottagare Array med mottagare att söka efter
     * @param textinnehall Textinnehåll att söka efter
     * @param minstorlek Minsta storlek på meddelanden
     * @param maxstorlek Största storlek på meddelanden
     * @param handelseMask Händelsemask för filtrering
     * @param felmask Felmask för filtrering
     * @param bilagor Array med bilagor att söka efter
     * @return En lista av meddelanden som matchar sökningen. Om 
     * 		   inga meddelanden hittas returneras en tom lista. 
     * @throws IllegalArgumentException om någon av inparametrarna i meddelandet är ogiltig.
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
