/**
 * @since 2007-maj-04
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl;

import junit.framework.TestCase;
import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.SandResultat;


public class MailTestEpostMeddelandeSenderImpl extends TestCase {

    private Log log = Log.getInstance(MailTestEpostMeddelandeSenderImpl.class);

    private EpostMeddelandeSenderImpl getImpl() {
        return new EpostMeddelandeSenderImpl("emaal-skv", "emaal-skv", "csnmail1.csnnet.int", 25, 30000);
    }

    public void testSkickaManga() {
        EpostMeddelandeSenderImpl impl = getImpl();

        int antal = 100;
        for (int i = 1;i <= antal;i++) {
            Meddelande meddelande = new Meddelande("Testmeddelande numro " + i, "Detta är meddelande nummer " + i + " från testSkickaManga", "jonas.ohrnell@csn.se", "noreply@csn.se", "Noreply");
            SandResultat resultat = impl.skickaMeddelande(meddelande);
            if (resultat.getKod() != MeddelandeHandelse.SKICKAT_SERVER) {
                System.out.println("Fel i meddelande " + i + ": " + resultat.getText());
            }
        }
    }

    public void testSkickaFelAdress() throws Exception {
        EpostMeddelandeSenderImpl impl = getImpl();
        Meddelande meddelande = new Meddelande("Testmeddelande", "Detta är från ett JUnit-test", "jonas.ohrnell@csn.se.nisse", "noreply@csn.se", "Noreply");
        SandResultat resultat = impl.skickaMeddelande(meddelande);
        assertFalse(resultat.getKod() == MeddelandeHandelse.SKICKAT_SERVER);

        meddelande = new Meddelande("Testmeddelande", "Detta är från ett JUnit-test", "faertwcae45.fgds", "Noreply", "noreply@csn.se", "Noreply");
        resultat = impl.skickaMeddelande(meddelande);
        assertFalse(resultat.getKod() == MeddelandeHandelse.SKICKAT_SERVER);
        log.debug(resultat.getText());
    }

    public void testSkickaSMS() throws Exception {
        EpostMeddelandeSenderImpl impl = getImpl();
        Meddelande meddelande = new Meddelande("Testmeddelande", "Detta är från ett JUnit-test", "jonas.ohrnell@csn.se.nisse", "noreply@csn.se", "Noreply");
        meddelande.getMottagare()[0].setTyp("SMS");
        SandResultat resultat = impl.skickaMeddelande(meddelande);
        assertNull(resultat);

        meddelande = new Meddelande("Testmeddelande", "Detta är från ett JUnit-test", "faertwcae45.fgds", "Noreply", "noreply@csn.se", "Noreply");
        resultat = impl.skickaMeddelande(meddelande);
        assertFalse(resultat.getKod() == MeddelandeHandelse.SKICKAT_SERVER);
        log.debug(resultat.getText());
    }

    public void testSkickaEpostOchSMS() throws Exception {
        EpostMeddelandeSenderImpl impl = getImpl();
        Meddelande meddelande = new Meddelande("Testmeddelande", "Detta är från ett JUnit-test", "jonas.ohrnell@csn.se.nisse", "noreply@csn.se", "Noreply");
        meddelande.getMottagare()[0].setTyp("SMS");

        Mottagare mott = new Mottagare("jonas.ohrnell@csn.se", "Jonas å");
        mott.setTyp("EPOST,EPOSTCC");
        meddelande.addMottagare(mott);

        SandResultat resultat = impl.skickaMeddelande(meddelande);
        assertEquals(resultat.getMottagare().length, 1);
        assertEquals(resultat.getSandare(), impl);
    }

    public void testKanSkickaMeddelande() {
        Meddelande m = new Meddelande("Rubrik", "Text");
        Mottagare mott = new Mottagare();
        //mott.setTyp("EPOST");
        m.addMottagare(mott);
        EpostMeddelandeSenderImpl impl = new EpostMeddelandeSenderImpl("user", "password", "relay.wm.net", 25, 1000);
        assertTrue(impl.kanSkickaMeddelande(m));

        mott.setTyp("EPOST");
        assertTrue(impl.kanSkickaMeddelande(m));

        mott.setTyp("EPOSTBCC,SMS");
        assertTrue(impl.kanSkickaMeddelande(m));

        mott.setTyp("SMS");
        assertFalse(impl.kanSkickaMeddelande(m));
    }

    public void testMatchandeTyp() {
        EpostMeddelandeSenderImpl impl = new EpostMeddelandeSenderImpl("user", "password", "relay.wm.net", 25, 1000);
        assertTrue(impl.matchandeTyp("EPOST"));
        assertTrue(impl.matchandeTyp(null));
        assertTrue(impl.matchandeTyp("EPOSTBCC,EPOST"));
        assertTrue(impl.matchandeTyp("SMS,EPOST"));
        assertFalse(impl.matchandeTyp("SMS"));
    }


}
