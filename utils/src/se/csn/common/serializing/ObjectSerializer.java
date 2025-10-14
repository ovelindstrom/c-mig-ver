/*
 * Skapad 2007-okt-12
 */
package se.csn.common.serializing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Jonas Öhrnell - csn7821
 */
public class ObjectSerializer {

    /**
     * Kopierar alla attribut, rekursivt, mha ObjectOutputStream 
     * och ObjectInputStream.
     * @param orig Det objekt som ska klonas.
     * @return Ett objekt som är djup-kopierat, dvs. alla ingående
     * 	attribut har också kopierats. Det nya objektet delar inga referenser
     * 	med originalet.
     */
    public static Object deepClone(Serializable orig) {
        if(orig == null) return null;
        
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }
    
    /**
     * Konverterar ett objekt till bytedata.
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Serializable obj) {
        if(obj == null) return null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            out.close();
            return bos.toByteArray();
        }
        catch(IOException e) {
            throw new IllegalArgumentException("Kunde inte konvertera objektet till bytes: " + e);
        }
    }
    
    /**
     * Konverterar bytedata till objekt mha ObjectInputStream
     * @param data
     * @return Ett objekt.
     */
    public static Object toObject(byte[] data) {
        if((data == null) || (data.length == 0)) return null;
        
        try {
            // Write the object out to a byte array
            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
            return in.readObject();
        }
        catch(IOException e) {
            throw new IllegalArgumentException("Kunde inte konvertera bytes till objekt: " + e);
        }
        catch(ClassNotFoundException cnfe) {
            throw new IllegalArgumentException("Kunde inte konvertera bytes till objekt: " + cnfe);
        }
    }
    
    /**
     * Konverterar ett objekt till en <b>URL-säker, icke radbruten</b> Base64-sträng. 
     * @param obj
     * @return
     */
    public static String toBase64String(Serializable obj) {
        byte[] data = toByteArray(obj);
        if(data == null) return null;
        return Base64.encodeBytes(data, Base64.URL_SAFE + Base64.DONT_BREAK_LINES);
    }
    
    public static Object toObjectFromBase64String(String base64data) {
        if(base64data == null) return null;
        return toObject(Base64.decode(base64data, Base64.DONT_BREAK_LINES + Base64.URL_SAFE));
    }
    

}
