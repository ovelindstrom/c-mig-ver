package se.csn.notmotor.ipl.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import se.csn.ark.common.util.logging.Log;

public class SMSTjaenst {
    private String userid;
    private String password;
    private String endpoint;
    private Log log = Log.getInstance(SMSTjaenst.class);

    public SMSTjaenst() {
    }

    /**
     * Skapar en ny instans av SMSTjaenst med angiven endpoint.
     * 
     * @throws IllegalArgumentException om det inte g\u00e5r att skapa en URL av endpoint
     */
    public SMSTjaenst(String endpoint) throws IllegalArgumentException {
        this();
        try {
            URL url = new URL(endpoint);
            // String host = url.getHost();
            int port = url.getPort();
            if (port == -1) {
                port = 80;
                if (endpoint.startsWith("https")) {
                    port = 443;
                }
            }
            
            this.endpoint = endpoint;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Felaktig endpoint-URL " + endpoint, e);
        }
    }

    public SMSTjaenst(String endpoint, String userid, String password) throws IllegalArgumentException {
        this(endpoint);
        this.userid = userid;
        this.password = password;
    }

    /**
     * Kontrollerar att alla nodvandiga data ar satta.
     * 
     * @throws IllegalStateException om n\u00e5gon parameter saknas f\u00f6r
     *                               att tj\u00e4nsten ska kunna anv\u00e4ndas
     */
    public void checkParams() throws IllegalStateException {
        if (userid == null) {
            throw new IllegalStateException("userid m\u00e5ste vara satt");
        }
        if (password == null) {
            throw new IllegalStateException("password m\u00e5ste vara satt");
        }
        if (endpoint == null) {
            throw new IllegalStateException("endpoint m\u00e5ste vara satt");
        }
    }

    public boolean testConnection() {
        BufferedReader in = null;
        try {
            URL url = new URL(endpoint);
            URLConnection con = (URLConnection) url.openConnection();
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            in = new BufferedReader(isr);
            boolean connectionOK = false;
            if (in.readLine() != null) { // NOSONAR
                connectionOK = true;
            }
            in.close();
            return connectionOK;
        } catch (IOException io) {
            log.warn("Har testat anslutning till " + endpoint + " men det misslyckades: ", io);
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("Kunde inte st\u00e4nga BufferedReader...");
                }
            }
        }
    }

    /**
     * Skickar SMS till Telia.
     * 
     * @param in DTOSMSIn
     * @return DTOSMSUt
     * @throws IllegalArgumentException om indata \u00e4r null
     */
    public DTOSMSUt execute(DTOSMSIn in) throws IllegalArgumentException {
        // Dumpa SSL-properties:
        String[] props = { "javax.net.ssl.trustStore", "javax.net.ssl.trustStorePassword",
                "javax.net.ssl.keyStore", "javax.net.ssl.keyStorePassword" };
        for (int i = 0; i < props.length; i++) {
            log.info("Prop: " + props[i] + ": " + System.getProperty(props[i]));
        }
        if (in == null) {
            throw new IllegalArgumentException("Indata m\u00e5ste vara satt");
        }
        checkParams();

        if (log.isDebugEnabled()) {
            log.debug("Anropar sms-tj\u00e4nsten med f\u00f6ljande indata.\n  Telnr:" + in.getTelnummer()
                    + "\n  Meddelande:" + in.getMeddelande()
                    + "\n  Userid:" + userid
                    + "\n  Password:" + password
                    + "\n  Appname:" + in.getApplikationsnamn()
                    + "\n  Func:" + in.getFunktionsnamn()
                    + "\n  Endpoint:" + endpoint);
        }

        DTOSMSUt response = new DTOSMSUt();
        PostMethod post;
        String xml = null;
        String sResponseCode = null;
        String sResponseMessage = "";
        String sTemporaryError = "false";
        int responseCode = -1;
        try {
            HttpClient client = new HttpClient();
            post = new PostMethod(endpoint);
            NameValuePair[] data = {
                    new NameValuePair("originatingAddress", in.getRubrik()),
                    new NameValuePair("destinationAddress", in.getTelnummer()),
                    new NameValuePair("userData", in.getMeddelande()),
            };
            post.setRequestBody(data);
            post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            Credentials defaultcreds = new UsernamePasswordCredentials(userid, password);
            client.getState().setCredentials(AuthScope.ANY, defaultcreds);
            client.executeMethod(post);
        } catch (HttpException he) {
            String error = "F\u00e5ngade HttpException";
            log.error(error, he);
            response.setReturStatus(998);
            return response;
        } catch (IOException ioe) {
            String error = "F\u00e5ngade IOException";
            log.error(error, ioe);
            response.setReturStatus(998);
            return response;
        }

        try {
            xml = post.getResponseBodyAsString();
        } catch (IOException ioe) {
            String error = "F\u00e5ngade IOException";
            log.error(error, ioe);
            response.setReturStatus(997);
            return response;
        }
        if (xml == null) {
            response.setReturStatus(997);
            return response;
        }
        DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(new StringReader(xml)));
        } catch (SAXException saxe) {
            String error = "F\u00e5ngade SAXException";
            log.error(error, saxe);
            response.setReturStatus(997);
            return response;
        } catch (IOException ioe) {
            String error = "F\u00e5ngade IOException";
            log.error(error, ioe);
            response.setReturStatus(997);
            return response;
        }
        Document doc = parser.getDocument();
        sResponseCode = doc.getElementsByTagName("responseCode").item(0).getChildNodes().item(0).getNodeValue();
        sResponseMessage = doc.getElementsByTagName("responseMessage").item(0).getChildNodes().item(0).getNodeValue();
        sTemporaryError = doc.getElementsByTagName("temporaryError").item(0).getChildNodes().item(0).getNodeValue();
        try {
            responseCode = Integer.parseInt(sResponseCode);
        } catch (NumberFormatException nfe) {
            response.setReturStatus(997);
            return response;
        }

        // Om vi f\u00e5r returkod 2 fr\u00e5n telia anv\u00e4nder vi 902 internt ist\u00e4llet
        // d\u00e5 kod 2 betyder "skickat server" i notmotorn
        if (responseCode == 2) {
            responseCode = 902;
        }
        if (responseCode == 0) {
            responseCode = 2; // Betyder att meddelandet \u00e4r skickat
        }
        response.setReturStatus(responseCode);
        response.setResponseMessage(sResponseMessage);
        if (sTemporaryError != null && "true".equals(sTemporaryError)) {
            response.setTemporaryError(true);
        }

        if (response.sandningLyckad()) {
            log.debug("Skickat meddelande: " + in + " -> " + response);
        } else {
            log.debug("Sändning misslyckades: " + in + " -> " + response);
        }

        return response;
    }

}
