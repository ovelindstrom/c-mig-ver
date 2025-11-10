/*
 * Skapad 2007-nov-29
 */
package se.csn.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jonas åhrnell - csn7821
 * Hjälpklass för variabler i config.xml.
 * 
 */
public class ConfigVariable {
    
    private String name, description;
    private Map values;
    
    public ConfigVariable(String name, String description) {
        this.name = name;
        this.description = description;
        values = new HashMap();
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
   
    /**
     * 
     * @param environment Case insensitive: jobbar med UPPERCASE internt
     * @return
     */
    public String getVariableForEnvironment(String environment) {
        return (String)values.get(environment.toUpperCase());
    }
    
    public boolean isSetForEnvironment(String environment) {
        return values.containsKey(environment.toUpperCase());
    }
    
    public void addValue(String environment, String value) {
        values.put(environment.toUpperCase(), value);
    }
    
    public Set getEnvironments() {
        return values.keySet();
    }
}
