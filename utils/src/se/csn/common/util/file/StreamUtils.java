/**
 * Skapad 2007-maj-30
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.common.util.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class StreamUtils {

    public static final int BUFFERSIZE = 1000;    
    
    public static String getStringFromInputStream(InputStream is) {
        if(is == null) {
            throw new IllegalArgumentException("InputStream måste vara satt");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = getStringFromReader(br);
        try {
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("Kunde inte stänga BufferedReader: " + e);
        }
        return s;
    }
    
    public static String getStringFromReader(Reader reader) {
        if(reader == null) {
            throw new IllegalArgumentException("Reader måste vara satt");
        }
        char[] buffer = new char[BUFFERSIZE];
        int available;
        StringBuffer sb = new StringBuffer();
        try {
            available = reader.read(buffer);
	        while(available > -1) {
	            sb.append(buffer, 0, available);	
	            available = reader.read(buffer);
	        }
	        reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Kunde inte läsa Reader: " + e);
        }
        return sb.toString();
    }
    
}
