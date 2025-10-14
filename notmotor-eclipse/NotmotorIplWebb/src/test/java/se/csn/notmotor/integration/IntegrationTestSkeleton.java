/**
 * Skapad 2007-jun-18
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import se.csn.ark.common.dal.CsnDAOWebServiceImpl;
import se.csn.ark.common.util.logging.Log;
import se.csn.webservice.bas.notmotor.skicka.DTOAvsandare;
import se.csn.webservice.bas.notmotor.skicka.DTOMeddelande;
import se.csn.webservice.bas.notmotor.skicka.DTOMottagare;
import se.csn.webservice.bas.notmotor.skicka.Skicka_PortType;
import se.csn.webservice.bas.notmotor.skicka.Skicka_ServiceLocator;


public class IntegrationTestSkeleton extends TestCase {
	private static final Log log = Log.getInstance(IntegrationTestSkeleton.class);
    protected Skicka_PortType client;
    protected String mottagaradress, mottagarsms, avsandaradress;
    protected String avsandarnamn;
    
    public void setUp() {
    	try {
    		Skicka_ServiceLocator serviceLocator = new Skicka_ServiceLocator();
	    	CsnDAOWebServiceImpl daows = new CsnDAOWebServiceImpl();
    		client = serviceLocator.getSkickaSOAP(daows.getURL(serviceLocator.getPorts()));
    	} catch (Exception e) {
    		log.error(e.getMessage());
    	}
        
        // Byt ut mot rätt mailadress(er):
        mottagaradress = "test.ekund@csn.se";
        mottagarsms = "0701234";
        avsandaradress = "noreply.test@csn.se";
        avsandarnamn = "Notmotorn";
    }

    public DTOMeddelande skapaTestMeddelande() {
        return skapaTestMeddelande("Testmeddelande", "Skapat " + (new Date()).toString());
    }

    public DTOMeddelande skapaTestMeddelande(String metod) {
        return skapaTestMeddelande("Testmeddelande från " + metod, "Metod: " + metod + "\n Skapat " + (new Date()).toString());
    }
    
    
    public DTOMeddelande skapaTestMeddelande(String metod, int nummer) {
        return skapaTestMeddelande("Testmeddelande nr " + nummer + " från " + metod, "Metod: " + metod + "\nNr: " + nummer + "\nSkapat " + (new Date()).toString());
    }
    
    public DTOMeddelande skapaTestMeddelande(String metod, int nummer, String typ) {
        return skapaTestMeddelande("Testmeddelande nr " + nummer + " från " + metod, "Metod: " + metod + "\nNr: " + nummer + "\nMeddelandesätt: " + typ + "\nSkapat " + (new Date()).toString(), typ);
    }
    
    public DTOMeddelande skapaTestMeddelande(String rubrik, String text) {
        DTOMeddelande meddelande = new DTOMeddelande();
        meddelande.setRubrik(rubrik);
        meddelande.setMeddelandetext(text);
        
        DTOMottagare mott = new DTOMottagare();
        mott.setAdress(mottagaradress);
        List<DTOMottagare> list = new ArrayList<DTOMottagare>();
        list.add(mott);
        meddelande.setMottagare((DTOMottagare[]) list.toArray(new DTOMottagare[0]));
        
        DTOAvsandare avs = new DTOAvsandare();
        avs.setEpostadress(avsandaradress);
        avs.setNamn(avsandarnamn);
        meddelande.setAvsandare(avs);
        
        return meddelande;
    }
    
    public DTOMeddelande skapaTestMeddelande(String rubrik, String text, String typ) {
    	DTOMeddelande dto = new DTOMeddelande();
        dto.setRubrik(rubrik);
        dto.setMeddelandetext(text);
    	DTOMottagare mott = null;
		String meddelandesatt = typ.toUpperCase();
		if ("EPOST".equals(meddelandesatt)) {
			mott = new DTOMottagare();
			mott.setAdress(mottagaradress);
			mott.setTyp(meddelandesatt);
		} else if ("SMS".equals(meddelandesatt)) {
			mott = new DTOMottagare();
			mott.setAdress(mottagarsms);
			mott.setTyp(meddelandesatt);
		} else {
			String error = "Saknar mottagare vid leverans till notmotor (nyregistrering av e-kund)";
			System.err.println(error);
			throw new IllegalArgumentException(error);
		}
		List<DTOMottagare> list = new ArrayList<DTOMottagare>();
        list.add(mott);
        dto.setMottagare((DTOMottagare[]) list.toArray(new DTOMottagare[0]));
        
		DTOAvsandare avs = new DTOAvsandare();
		avs.setNamn(avsandarnamn);
		avs.setEpostadress(avsandaradress);
		avs.setApplikation("Junit test");
		avs.setKategori(null);
		dto.setAvsandare(avs);
		
        return dto;
    }
    
    public DTOMeddelande skapaTestMeddelandeMedTvaMottagare(String rubrik, String text) {
    	DTOMeddelande dto = new DTOMeddelande();
        dto.setRubrik(rubrik);
        dto.setMeddelandetext(text);
    	DTOMottagare mott = null;
    	List<DTOMottagare> list = new ArrayList<DTOMottagare>();
    	
		// Epost
		mott = new DTOMottagare();
		mott.setAdress(mottagaradress);
		list.add(mott);
		
		// SMS
		mott = new DTOMottagare();
		mott.setAdress(mottagarsms);
		mott.setTyp("SMS");
		list.add(mott);
        dto.setMottagare((DTOMottagare[]) list.toArray(new DTOMottagare[0]));
        
		DTOAvsandare avs = new DTOAvsandare();
		avs.setNamn(avsandarnamn);
		avs.setEpostadress(avsandaradress);
		avs.setApplikation("Junit test");
		avs.setKategori(null);
		dto.setAvsandare(avs);
		
        return dto;
    }
    
    public void vantaOchSkriv(int sekunder, int ganger) {
        for (int i = 0; i < ganger; i++) {
            try {
                Thread.sleep(sekunder * 1000);
                System.out.println("Sovit " + (sekunder * (i + 1)) + " sekunder");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
