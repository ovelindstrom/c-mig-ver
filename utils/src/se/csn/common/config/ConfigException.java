/**
 * Skapad 2007-feb-09
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.config;

/**
 * Kasta detta fel om det är något fel på den externa 
 * konfigurationen: saknad DB-koppling, kö etc.
 * Skapad 2007-feb-09
 * @author Jonas Öhrnell (csn7821)
 */
public class ConfigException extends RuntimeException {
    
    private String problem, fix;
    
    public ConfigException(String problem, String fix) {
        super("Problem: " + problem + " Åtgärd: " + fix);
        this.problem = problem;
        this.fix = fix;
    }
    
    public String toString() {
        return "Problem: " + problem + " Åtgärd: " + fix;
    }
    
    public String getFix() {
        return fix;
    }
    
    public String getProblem() {
        return problem;
    }
}

