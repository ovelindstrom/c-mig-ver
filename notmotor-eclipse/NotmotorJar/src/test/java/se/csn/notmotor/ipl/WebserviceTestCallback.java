/*
 * @since 2007-okt-08
 */
package se.csn.notmotor.ipl;

import java.io.IOException;
import java.net.URLEncoder;

import junit.framework.TestCase;
import se.csn.common.serializing.ObjectSerializer;
import se.csn.common.servlet.ServletUtils;
import se.csn.notmotor.ipl.model.Meddelande;

public class WebserviceTestCallback extends TestCase {

    public void testNyHandelse() {
        //CallbackClient cli = new CallbackClient("http://localhost:16080/NotmotorIPL/services/CallbackTestService");
        //CallbackTestServiceProxy cts = new CallbackTestServiceProxy("http://localhost:16080/NotmotorIPL/services/CallbackTestService");
        /*
        DTOMeddelande m = new DTOMeddelande("Rubrik", "Text", "Mottagare", "Avsadr", "Avsnamn");
        try {
          //  cts.nyHandelse(m);
        } catch (RemoteException e) {
            System.err.println("Error: " + e);
        }
        */
    }

    public void testServlet() throws IOException {
        Meddelande m = new Meddelande("Rubrik", "Text", "Mottagare", "Avsadr", "Avsnamn");
        String data = ObjectSerializer.toBase64String(m);
        String url = "http://localhost:16080/NotmotorIPL/CallbackTestServlet?MEDDELANDE=" + URLEncoder.encode(data, "utf-8");
        //url = "http://localhost:16080/NotmotorIPL/CallbackTestServlet";
        System.out.println("Resultat: " + ServletUtils.hamtaURL(url));
    }
}
