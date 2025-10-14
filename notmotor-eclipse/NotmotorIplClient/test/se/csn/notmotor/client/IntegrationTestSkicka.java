package se.csn.notmotor.client;

import java.rmi.RemoteException;

import junit.framework.TestCase;
import se.csn.webservice.bas.notmotor.skicka.DTOAvsandare;
import se.csn.webservice.bas.notmotor.skicka.DTOMeddelande;
import se.csn.webservice.bas.notmotor.skicka.DTOMottagare;
import se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat;
import se.csn.webservice.bas.notmotor.skicka.SkickaProxy;

public class IntegrationTestSkicka extends TestCase {

	private SkickaProxy proxy;
	
	public void setUp() {
		proxy = new SkickaProxy();
		// Peka ut grön systemtest
		proxy.setEndpoint("http://gronintern.csn.se:8080/NotmotorIPL/services/SkickaSOAP");
	}
	
	public void testSkickaEnkel() throws RemoteException {
		DTOAvsandare avsandare = new DTOAvsandare();
		avsandare.setApplikation("Clienttest");
		avsandare.setNamn("Testsändare");
		avsandare.setEpostadress("joel.norberg@csn.se");
		
		DTOMottagare m1 = new DTOMottagare();
		m1.setAdress("joel.norberg@csn.se");
		m1.setNamn("Joel Norberg");
		m1.setTyp("epost");
		DTOMottagare m2 = new DTOMottagare();
		m2.setAdress("+46705976212");
		m2.setNamn("Jonas Mobil");
		m2.setTyp("sms");
		
		DTOMeddelande meddelande = new DTOMeddelande();
		meddelande.setAvsandare(avsandare);
		meddelande.setMottagare(new DTOMottagare[]{m1, m2});
		meddelande.setRubrik("Testrubrik");
		meddelande.setMeddelandetext("Detta är ett testmeddelande");
		
		DTONotifieringResultat res = proxy.skickaMeddelande(meddelande);
		System.out.println("Kod: " + res.getResultat() + " text: " + res.getInfo());
	}
	
}
