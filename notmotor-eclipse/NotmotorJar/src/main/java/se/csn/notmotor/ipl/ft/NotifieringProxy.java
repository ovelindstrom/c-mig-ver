package se.csn.notmotor.ipl.ft;

import java.util.Date;

import se.csn.notmotor.ipl.model.Avsandare;
import se.csn.notmotor.ipl.model.Bilaga;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.NotifieringResultat;

public interface NotifieringProxy {

    /**
     * Skicka ett meddelande. RuntimeException kastas vid fel i meddelandent eller vid serviceavbrott.
     * 
     * @param meddelande Det meddelande som ska skickas.
     */
    NotifieringResultat skickaMeddelande(Meddelande meddelande);

    /**
     * Hamta ett meddelande.
     * 
     * @param meddelandeId Id för det meddelande som ska hämtas.
     * @return Ett meddelande om det fanns ett meddelande med det angivna
         *         id:t, annars null.
     */
    Meddelande hamtaMeddelande(Long meddelandeId);

    /**
     * Satter om status for meddelandet till BORTTAGET. Meddelandet
     * ligger alltsa kvar i databasen men kommer inte att sandas.
     * 
     * @param meddelandeId Meddelandeets id
     */
    NotifieringResultat taBortMeddelande(Long meddelandeId);

    /**
     * Den huvudsakliga soktjansten for meddelanden.
     * 
     * @param from         Från-datum för sökningen
     * @param tom          Till-datum för sökningen
     * @param avsandare    Array med avsändare att söka efter
     * @param mottagare    Array med mottagare att söka efter
     * @param textinnehall Textinnehåll att söka efter
     * @param minstorlek   Minsta storlek på meddelanden
     * @param maxstorlek   Största storlek på meddelanden
     * @param handelseMask Händelsemask för filtrering
     * @param felmask      Felmask för filtrering
     * @param bilagor      Array med bilagor att söka efter
     * @return En lista av meddelanden som matchar sökningen. Om
         *         inga meddelanden hittas returneras en tom lista.
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
                        Bilaga[] bilagor);

    Avsandare[] sokAvsandare(
                        String namndel,
                        String applikationsdel,
                        String kategoridel,
                        String adressdel,
                        String replytoDel);

    Mottagare[] sokMottagare(
                        String namndel,
                        String adressdel,
                        String typ,
                        Integer csnnrFrom,
                        Integer csnnrTom);

}
