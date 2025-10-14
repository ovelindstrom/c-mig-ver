package se.csn.notmotor.ipl.ft;
import junit.framework.TestCase;
import se.csn.notmotor.ipl.EpostMeddelandeSenderImpl;
import se.csn.notmotor.ipl.MeddelandeSender;
import se.csn.notmotor.ipl.model.Meddelande;

/**
 * Skapad 2007-mar-30
 * @author Jonas åhrnell (csn7821)
 * 
 */

public class MailTestSimpleNotifieringProxyImpl extends TestCase {

    public void testSkicka() {
        Meddelande meddelande = new Meddelande("Testmeddelande", "Detta är från ett JUnit-test", "jonas.ohrnell@csn.se", "noreply@csn.se", "Noreply");
        MeddelandeSender sender = new EpostMeddelandeSenderImpl("emaal-skv", "emaal-skv", "CSNMAIL1.csnnet.int", 25, 30000);
        NotifieringProxy proxy = new SimpleNotifieringProxyImpl(sender);
        proxy.skickaMeddelande(meddelande);
    }
    
}
