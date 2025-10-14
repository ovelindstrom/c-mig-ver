/**
 * Skapad 2007-mar-26
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Diverse tester av kommunikation: finns server, är port öppen etc
 */
public class CommunicationTester {
	
	private static final int DEFAULT_SOCKET_CONNECT_TIMEOUT = 30000;
    public static final int SMTP_PORT = 25;
    public static final int HTTP_PORT = 80;
    public static final int EDH_PORT = 15000;
    public static final int SSL_PORT = 443;
    

    /**
     * Testar förbindelsen till en server:port genom att skapa en socket och 
     * öppna en tcp-ip-förbindelse.
     * @return true om förbindelsen kunde öppnas, false annars. 
     */
    public static boolean isPortOpen(String host, int port, int connectTimeout) {
        if((host == null) || (host.length() == 0)) {
            throw new IllegalArgumentException("Host måste anges");
        }
        if((port < 1) || (port > 65535)) {
            throw new IllegalArgumentException("port måsta ha ett värde mellan 1 och 65535");
        }
        if(connectTimeout <= 0) {
            throw new IllegalArgumentException("Timeout måste vara större än 0");
        }
        if(connectTimeout > 60000) {
            throw new IllegalArgumentException("Det kan inte ska ta mer än en minut att koppla mot servern. ");
        }
        Socket t;
        try {
            t = new Socket();
            t.connect(new InetSocketAddress(host, port), connectTimeout);
            t.close();
            return true;
        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
    
    public static boolean isPortOpen(String host, int port) {
        return isPortOpen(host, port, DEFAULT_SOCKET_CONNECT_TIMEOUT);
    }
    
    public static boolean isPortOpen(String url) {
        try {
            URL u = new URL(url);
            return isPortOpen(u.getHost(), u.getPort(), DEFAULT_SOCKET_CONNECT_TIMEOUT);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Ogiltig URL: " + url);
        }
    }
    
    public static boolean isPortOpen(URL url) {
        if(url == null) {
            throw new IllegalArgumentException("Url får inte vara null");
        }
        return isPortOpen(url.getHost(), url.getPort(), DEFAULT_SOCKET_CONNECT_TIMEOUT);
    }

}
