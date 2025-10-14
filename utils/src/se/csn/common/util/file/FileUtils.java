/**
 * Skapad 2007-mar-15
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.common.util.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.FileOutputStream;


public class FileUtils {

    // Vik 24 bytes för klassoverhead för att komma under den magiska 1k-gränsen
    
    
    public static String readStringFromFile(File file) {
        if(file == null) {
            throw new IllegalArgumentException("file måste vara satt");
        }
        try {
            FileReader reader = new FileReader(file);
            String s = StreamUtils.getStringFromReader(reader);
            reader.close();
            return s;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Kunde inte hitta fil " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException("Fick(kunde inte läsa fil " + file.getAbsolutePath() + ": " + e);
        } 
    }
    
    public static String readStringFromFile(String filename) {
        if(filename == null) {
            throw new IllegalArgumentException("filename måste vara satt");
        }
        return readStringFromFile(new File(filename));
    }
    
    public static void writeBytesToFile(byte[] data, File file) {
        if(data == null) {
            throw new IllegalArgumentException("data måste vara satt");
        }
        if(file == null) {
            throw new IllegalArgumentException("file måste vara satt");
        }
        try {
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Kunde inte hitta fil " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException("Fick(kunde) inte skriva till fil " + file.getAbsolutePath() + ": " + e);
        }
    }
    
    public static void writeBytesToFile(byte[] data, String filename) {
        if(filename == null) {
            throw new IllegalArgumentException("filename måste vara satt");
        }
        writeBytesToFile(data, new File(filename));        
    }
    
    
}
