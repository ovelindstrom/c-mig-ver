/**
 * Skapad 2007-feb-20
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.config;

/**
 * Kasta detta exception när en klass saknas (inte hittas av klassladdaren) 
 */
public class MissingClassException extends ConfigException {
    
    /**
     * @param classname Namn på den saknade klassen; ska vara fully qualified.  
     * @param jarfile Namn på den jarfil som innehåller klassen; bara filnamnet, men MED
     *        filändelse (kan ju vara .zip också)
     */
    public MissingClassException(String classname, String jarfile) {
        super("Hittade inte " + classname + ", som hör till " + jarfile, "Kontrollera att " + jarfile + " finns på classpath");
    }
    
    /**
     * @param classname Namn på den saknade klassen; ska vara fully qualified.  
     * @param jarfile Namn på den jarfil som innehåller klassen; bara filnamnet, men MED
     *        filändelse (kan ju vara .zip också)
     * @param usedBy Används om jarfilen ifråga är en dependency av en annan jarfil. Bra att
     *        använda för att tydliggöra var en klass används om den inte används i 
     *        applikationskod. 
     */
    public MissingClassException(String classname, String jarfile, String usedBy) {
        super("Hittade inte " + classname + ", som hör till " + jarfile + ". Den används av " + usedBy, "Kontrollera att " + jarfile + " finns på classpath");
    }
}
