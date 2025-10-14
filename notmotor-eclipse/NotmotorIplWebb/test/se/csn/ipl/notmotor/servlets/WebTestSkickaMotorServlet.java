/**
 * Skapad 2007-mar-08
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.ipl.notmotor.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import junit.framework.TestCase;


public class WebTestSkickaMotorServlet extends TestCase {

    public void testAsyncGetURL() throws IOException {
        URL url = new URL("http://localhost:9080/NotmotorIPL/SkickaMotorServlet");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        //conn.setFollowRedirects(false);
        conn.connect();
        System.out.println("Response: " + conn.getResponseMessage());
    }
}
