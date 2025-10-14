/**
 * Skapad 2007-jun-18
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.integration;

import java.util.Calendar;

import se.csn.webservice.bas.notmotor.skicka.DTOMeddelande;
import se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat;

public class IntegrationTestCRUD extends IntegrationTestSkeleton {

    public void testSkickaEnkelHamta() {
        DTOMeddelande m = skapaTestMeddelande("testSkickaEnkel");
        DTONotifieringResultat res;
		try {
			res = client.skickaMeddelande(m);
			assertEquals(res.getResultat().intValue(), 0);

	        DTOMeddelande hamta = client.hamtaMeddelande(res.getMeddelandeId());

	        assertEquals(m.getRubrik(), hamta.getRubrik());
	        assertEquals(m.getMeddelandetext(), hamta.getMeddelandetext());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
    }

    public void testSkickaMedBilagor() {
        DTOMeddelande m = skapaTestMeddelande("testSkickaMedBilagor");
        // TODO: bygg test
    }

    public void testSkickaSandSenare() {
        // Skapa meddelande som ska skickas om 1 minut
        // (får inte vara för kort tid, måste hinna refresha i notmotorn etc)
        DTOMeddelande m = skapaTestMeddelande("testSkickaSandSenare");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);
        m.setSkickaTidigast(cal);

        // Skicka. Hämta efter 40 sek. Kolla att inte sänt.
        try {
	        DTONotifieringResultat res = client.skickaMeddelande(m);
	        vantaOchSkriv(5, 8);
	        DTOMeddelande hamta = client.hamtaMeddelande(res.getMeddelandeId());
	        assertNull(hamta.getSkickat());
	        assertNotNull(hamta.getSkickaTidigast());

	        // Vänta ytterligare 40 sekunder. Nu ska meddelandet vara skickat.
	        vantaOchSkriv(5, 8);
	        hamta = client.hamtaMeddelande(res.getMeddelandeId());
	        assertNotNull(hamta.getSkickat());
        } catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void testTaBort() {
        // Skapa meddelande som ska skickas om 20 sekunder
        DTOMeddelande m = skapaTestMeddelande("testSkickaSandSenare");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        m.setSkickaTidigast(cal);

        // Skicka. Ta bort. Hämta efter 40 sek. Kolla att inte sänt.
        try {
	        DTONotifieringResultat res = client.skickaMeddelande(m);
	        Long id = res.getMeddelandeId();
	        res = client.taBortMeddelande(id);
	        vantaOchSkriv(5, 8);
	        DTOMeddelande hamta = client.hamtaMeddelande(id);
	        assertNull(hamta.getSkickat());
	    } catch (Exception e) {
			e.printStackTrace();
			fail();
		}
    }

}
