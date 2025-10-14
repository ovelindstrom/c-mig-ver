/**
 * Skapad 2007-feb-26
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.config;


public class MissingPropertyException extends ConfigException {
    
    private static String INSTRUCTION = "Kontrollera att propertyn är satt, antingen via environment, kommandorad (-D-flaggan) eller via propertiesfil. "; 
    
    public MissingPropertyException(String property) {
        super("Saknad(e) properti(es): " + property, INSTRUCTION);
    }
    
}
