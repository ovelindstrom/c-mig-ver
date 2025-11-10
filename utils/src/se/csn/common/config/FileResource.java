/**
 * Skapad 2007-apr-23
 * @author Jonas åhrnell (csn7821)
 * @author Petrus Bergman (csn7820)
 */
package se.csn.common.config;

import java.io.File;


public class FileResource extends ResourceBase {
    
    private boolean writeable;
    
    public FileResource(String filnamn, String typ, Resource parent, boolean writeable) {
        super(filnamn, typ, parent);
        this.writeable = writeable;
    }
    
    public FileResource(String filnamn, Resource parent, boolean writeable) {
        this(filnamn, "Fil", parent, writeable);
    }
    
    public FileResource(String filnamn, boolean writeable) {
        this(filnamn, "Fil", null, writeable);
    }
    
    public ResourceStatus doCheck() {
        // Om skrivbar: kolla att det går att skriva till filen
        if(writeable) {
            if(!fileWriteable(name)) {
                return new ResourceStatus("Kunde inte skriva till fil " + getAbsolutSokvag(name), 
                        "Kontrollera att katalogen finns och att den är skrivbar för JVM:en", 
                        ResourceStatus.ERROR);
            }
        } else {
            if(!fileReadable(name)) {
                return new ResourceStatus("Kunde inte läsa fil " + getAbsolutSokvag(name), 
                        "Kontrollera att filen finns och att den är läsbar för JVM:en", 
                        ResourceStatus.ERROR);
            }
        }
        return new ResourceStatus();
    }
    

    /**
     * Kontrollerar om en fil är läsbar.
     * 
     * @param filename den fil som ska kontrolleras
     * @throws IllegalArgumentException om filnamnet är <code>null</code>
     * @return <code>true</code> om filen är läsbar, annars <code>false</code> 
     */
    public static boolean fileReadable(String filename) {
    	if (filename == null) {
    		throw new IllegalArgumentException("Filnamnet får inte vara null");
    	}
        File f = new File(filename);
        return f.exists() && f.canRead(); 
    }
    
    
    public static String getAbsolutSokvag(String filename) {
    	if (filename == null) {
    		throw new IllegalArgumentException("Filnamnet får inte vara null");
    	}
        File f = new File(filename);
        return f.getAbsolutePath();
    }
    
    /**
     * Kontrollerar om en fil är skrivbar.
     * 
     * @param filename den fil som ska kontrolleras
     * @throws IllegalArgumentException om filnamnet är <code>null</code>
     * @return <code>true</code> om filen är skrivbar, annars <code>false</code> 
     */
    public static boolean fileWriteable(String filename) {
    	if (filename == null) {
    		throw new IllegalArgumentException("Filnamnet får inte vara null");
    	}
        File f = new File(filename);
        return f.exists() && f.canWrite(); 
    }
    
    /**
     * Kontrollerar om en fil är läsbar och kastar ett fel om
     * den inte är det.
     * 
     * @param filename den fil som ska kontrolleras
     * @throws IllegalArgumentException om filnamnet är <code>null</code>
     * @throws MissingFileException om filen inte kan läsas
     */
    public static void findReadableFileThrowException(String filename) {
        if (!fileReadable(filename)) {
            throw new MissingFileException(filename);
        }
    }

    /**
     * Kontrollerar om en fil är skrivbar och kastar ett fel om
     * den inte är det.
     * 
     * @param filename den fil som ska kontrolleras
     * @throws IllegalArgumentException om filnamnet är <code>null</code>
     * @throws MissingFileException om filen inte är skrivbar
     */
    public static void findWriteableFileThrowException(String filename) {
        if (!fileWriteable(filename)) {
        	throw new MissingFileException(filename);
        }
    }

    
}
