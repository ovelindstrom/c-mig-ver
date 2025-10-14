/**
 * Skapad 2007-maj-16
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.integration;

import java.util.Calendar;

import se.csn.webservice.bas.notmotor.skicka.DTOMeddelande;
import se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat;


/**
 * Klass för att skicka många testmail via web service.
 * @author csn7821
 *
 */
public class IntegrationTestMassMail extends IntegrationTestSkeleton {

    public void testSkickaManga() {
        int antal = 1;
        int rapportintervall = 100;
        long cnt = System.currentTimeMillis();
        int ok = 0;
        int fel = 0;
        int error = 0;
        for (int i = 1; i <= antal; i++) {
            if ((i % rapportintervall) == 0) {
                System.out.println("Skickar meddelande nr " + i);
            }
            try {
	            DTOMeddelande meddelande = skapaTestMeddelande("skickaTestManga", i);
	            
	            // För test av mimetyp på meddelande:
	            // Observera att man sätter encoding (charset) i samma sträng.
	            //meddelande.setMimetyp("text/html;charset=iso-8859-1");
	            DTONotifieringResultat resultat = client.skickaMeddelande(meddelande);
	            if (resultat.getResultat().intValue() != 0) {
	                System.out.println("Fel i meddelande " + i + ": " + resultat.getInfo());
	                fel++;
	            } else {
	                ok++;
	            }
            } catch (Exception e) {
                System.out.println("Exception för meddelande " + i + ": " + e);
                error++;
            }
        }
        System.out.println("Sänt " + antal + " meddelanden på " + (System.currentTimeMillis() - cnt) + " millis");
        System.out.println("OK: " + ok + "  Fel: " + fel + "  Error: " + error);
        if (fel > 0) {
        	fail();
        }
    }
    
    
    public void testSkicka100SandSenare() {
        int antal = 100;
        int fordrojningIMinuter = 5;
        
        
        // Sänd om 5 mi
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, fordrojningIMinuter);
        long cnt = System.currentTimeMillis();
        int ok = 0;
        int fel = 0;
        int error = 0;
        for (int i = 0; i < antal; i++) {
            try {
	            DTOMeddelande meddelande = skapaTestMeddelande("testSkicka100SandSenare", i);
	            meddelande.setSkickaTidigast(cal);
	            DTONotifieringResultat resultat = client.skickaMeddelande(meddelande);
	            if (resultat.getResultat().intValue() != 0) {
	                System.out.println("Fel i meddelande " + i + ": " + resultat.getInfo());
	                fel++;
	            } else {
	                ok++;
	            }
            } catch (Exception e) {
                System.out.println("Exception för meddelande " + i + ": " + e);
                error++;
            }
        }
        System.out.println("Sänt " + antal + " meddelanden på " + (System.currentTimeMillis() - cnt) + " millis");
        System.out.println("OK: " + ok + "  Fel: " + fel + "  Error: " + error);
    }
    
}
