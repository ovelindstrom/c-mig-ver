package se.csn.notmotor.ipl.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import junit.framework.TestCase;

public class WebserviceTestSkickaSMS extends TestCase {

    public void testSkicka() {
        //System.setProperty("javax.net.ssl.trustStore", "C:/Program Files/Java/j2re1.4.2_11/lib/security/cacerts");
        System.setProperty("javax.net.ssl.trustStore", "test/cacerts");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        SMSTjaenst tjaenst = new SMSTjaenst("https://ssc.telia.se/smsplus/sendsms", "CSN", "Stud13r");
        DTOSMSIn in = new DTOSMSIn("0705976212", "CSN", "testing testing", "NOTMOTOR", "TEST");
        DTOSMSUt ut = tjaenst.execute(in);
        System.out.println("Ut: " + ut);
    }

    public void testSkickaJDK() throws IOException {
        System.setProperty("javax.net.ssl.trustStore", "test/cacerts");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        //System.setProperty("javax.net.ssl.trustStorePassword", "secret");
        String httpsURL = "https://ssc.telia.se/smsplus/sendsms";
        URL myurl = new URL(httpsURL);
        HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
        InputStream ins = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();
    }

}
