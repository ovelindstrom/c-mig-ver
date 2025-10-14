/**
 * Skapad 2007-maj-16
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.integration;

import se.csn.webservice.bas.notmotor.skicka.DTOMeddelande;
import se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat;

/**
 * Klass för att skicka många testmail via web service.
 * @author csn7821
 *
 */
public class IntegrationTestSMS extends IntegrationTestSkeleton {

    public void testSkickaSMS() {
        long cnt = System.currentTimeMillis();

        try {
            DTOMeddelande meddelande = skapaTestMeddelande("Meddelande", "Test av sändning.", "SMS");
            DTONotifieringResultat resultat = client.skickaMeddelande(meddelande);
            if (resultat.getResultat().intValue() != 0) {
                System.out.println("Fel i meddelande: " + resultat.getInfo());
                fail();
            }
        } catch (Exception e) {
            System.out.println("Exception för meddelande: " + e);
            fail();
        }
        System.out.println("Sänt meddelande på " + (System.currentTimeMillis() - cnt) + " millis");
    }
    
    
    public void testSkickaSMSochEpost() {
        long cnt = System.currentTimeMillis();

        try {
            DTOMeddelande meddelande = skapaTestMeddelandeMedTvaMottagare("Meddelande", "Test av sändning.");
            DTONotifieringResultat resultat = client.skickaMeddelande(meddelande);
            if (resultat.getResultat().intValue() != 0) {
                System.out.println("Fel i meddelande: " + resultat.getInfo());
                fail();
            }
        } catch (Exception e) {
            System.out.println("Exception för meddelande: " + e);
            fail();
        }
        System.out.println("Sänt meddelande på " + (System.currentTimeMillis() - cnt) + " millis");
    }
    
    public void testSkickaSMSLangreAn160Tecken() {
    	long cnt = System.currentTimeMillis();
    	
    	try {
    		final String meddelande161Tecken = "abcdefghijklmnopqrstuvxyz0123456789abcdefghijklmnopqrstuvxyz0123456789abcdefghijklmnopqrstuvxyz0123456789abcdefghijklmnopqrstuvxyz0123456789AAAAAAAAAAAAAAAAAAAAA";
    		
    		DTOMeddelande meddelande = skapaTestMeddelande("Meddelande", meddelande161Tecken, "SMS");
    		DTONotifieringResultat resultat = client.skickaMeddelande(meddelande);
    		if (resultat.getResultat().intValue() != 0) {
    			System.out.println("Resultatkod: " + resultat.getResultat());
                System.out.println("Fel i meddelande: " + resultat.getInfo());
                fail();
            }
    	} catch (Exception e) {
    		System.out.println("Exception för meddelande: " + e);
    		fail();
    	}
    	System.out.println("Sänt meddelande på " + (System.currentTimeMillis() - cnt) + " millis");
    }
    
    public void testSkickaEpost() {
        long cnt = System.currentTimeMillis();

        try {
            DTOMeddelande meddelande = skapaTestMeddelande("Meddelande", "Test av sändning.", "EPOST");
            DTONotifieringResultat resultat = client.skickaMeddelande(meddelande);
            if (resultat.getResultat().intValue() != 0) {
                System.out.println("Fel i meddelande: " + resultat.getInfo());
                fail();
            }
        } catch (Exception e) {
            System.out.println("Exception för meddelande: " + e);
            fail();
        }
        System.out.println("Sänt meddelande på " + (System.currentTimeMillis() - cnt) + " millis");
    }
    
}
