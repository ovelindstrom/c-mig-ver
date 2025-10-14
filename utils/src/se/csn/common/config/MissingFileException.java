/**
 * Skapad 2007-feb-26
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.config;


public class MissingFileException extends ConfigException {

    private static String INSTRUCTION = " 1. Kontrollera att filen finns på angiven sökväg. 2. Kontrollera att den får läsas av JVM:en. 3. (Om den ska kunna skrivas): kolla att angiven katalog är skrivbar."; 
    
    public MissingFileException(String filename) {
        super(filename, INSTRUCTION);
    }
    
    public String getInstructionToCorrect() {
        return INSTRUCTION;
    }
}
