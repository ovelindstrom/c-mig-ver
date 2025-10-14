/**
 * Skapad 2007-mar-08
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ServletUtils {
    
    /**
     * Utilitymetod för att anropa en servlet inifrån en annan servlet, 
     * "asynkront", dvs. utan att vänta in svaret. 
     * Metoden kan användas för att starta worker-servlets som inte terminerar. 
     * OBS! Dessa servlets måste då returnera OK och anropa response.flushBuffer()
     * så att response-koden går iväg till denna connection.
     */
    public static boolean anropaServletAsynkront(String url) {
        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(url + " är en felaktig url: " + e);
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)u.openConnection();
            conn.connect();
            return (conn.getResponseCode() == 200);
        } catch (IOException e1) {
            throw new IllegalStateException("Kunde inte öppna url " + url + ", " + e1);
        }
    }
    
    public static String hamtaURL(String url) {
        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(url + " är en felaktig url: " + e);
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)u.openConnection();
            conn.connect();
            //System.out.println("ResponseCode: " + conn.getResponseCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            rd.close();
            return sb.toString();
        } catch (IOException e1) {
            throw new IllegalStateException("Kunde inte öppna url " + url + ", " + e1);
        }
   	}
}
