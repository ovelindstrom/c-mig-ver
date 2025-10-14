/**
 * Skapad 2007-feb-26
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.config;

import java.io.PrintStream;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.Log;

/**
 * Kontrollerar att properties är satta. 
 */
public class PropertyDependencyTester {
    
    /**
     * Kontrollerar bara mot systemproperties
     * @return true om 
     */
    public static boolean isSystemPropertySet(String property) {
        String prop = System.getProperty(property); 
        return prop != null;
    }
    
    /**
     * Söker propertyn i propertiesfilen.
     * @param namn på propertyn 
     * @param propertyfile Namn på prpertyfilen.Om namnet på propertiesfilen 
     * inte innehåller någon punkt så appendas .properties till namnet. 
     * Propertiesfilerna söks på classpath. 
     * 
     * @return true om propertyn hittades, false annars.  
     */
    public static boolean isPropertySetInFile(String property, String propertyfile) {
		try {
	        ResourceBundle bundle = ResourceBundle.getBundle(propertyfile);
			if(bundle.getString(property) != null) {
			    return true;
			} else {
			    return false;
			}
		} catch(MissingResourceException e) {
		    return false;
		}
    }
    
    public static boolean isPropertySetInFile(String property, String[] propertyfiles) {
        if((propertyfiles == null) || (propertyfiles.length == 0)) return false;
        for(int i = 0; i < propertyfiles.length; i++) {
            if(isPropertySetInFile(property, propertyfiles[i])) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Kollar mot de properties som laddats via ark-klassen Properties. 
     */
    public static boolean isPropertySet(String property) {
        return Properties.getProperty(property) != null;
    }
    
    public static boolean arePropertiesSetInFile(String[] properties, String propfil) {
        if(properties == null) return true;
        for(int i = 0; i < properties.length; i++) {
            if(!isPropertySetInFile(properties[i], propfil)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean arePropertiesSet(String[] properties) {
        if(properties == null) return true;
        for(int i = 0; i < properties.length; i++) {
            if(!isPropertySet(properties[i])) {
                return false;
            }
        }
        return true;
    }
    
    public static void checkPropertyThrowException(String property) {
        if(!isPropertySet(property)) {
            throw new MissingPropertyException(property);
        }
    }
    
    public static void checkPropertyInFileThrowException(String property, String file) {
        if(!isPropertySetInFile(property, file)) {
            throw new MissingPropertyException(property);
        }
    }
    
    
    public static void checkPropertiesThrowException(String[] properties) {
        if(properties == null) return;
        for(int i = 0; i < properties.length; i++) {
            checkPropertyThrowException(properties[i]);
        }
    }
    
    public static void checkPropertiesInFileThrowException(String[] properties, String file) {
        if(properties == null) return;
        for(int i = 0; i < properties.length; i++) {
            checkPropertyInFileThrowException(properties[i], file);
        }
    }

    
    public static void dumpPropertiesThrowIfMissing(Log log, String[] properties) {
        log.debug("Dumpar kontrollerade properties:");
        if(properties == null) return;
        String missing = "";
        for(int i = 0; i < properties.length; i++) {
            String value = Properties.getProperty(properties[i]);
            if(value == null) {
                missing += (properties[i] + " ");
            }
            log.debug(properties[i] + "=" + value + "<slut>");
        }
        log.debug("Dumpat " + properties.length + " properties.");
        if(missing.length() > 0) {
            log.debug("Minst en av dessa properties var null: " + missing);
            throw new MissingPropertyException(missing);
        }
    }
    
    public static void dumpPropertiesThrowIfMissing(PrintStream out, String[] properties) {
        out.println("Dumpar kontrollerade properties:");
        if(properties == null) return;
        String missing = "";
        for(int i = 0; i < properties.length; i++) {
            String value = Properties.getProperty(properties[i]);
            if(value == null) {
                missing += (properties[i] + " ");
            }
            out.println(properties[i] + "=" + value + "<slut>");
        }
        out.println("Dumpat " + properties.length + " properties.");
        if(missing.length() > 0) {
            out.println("Minst en av dessa properties var null: " + missing);
            throw new MissingPropertyException(missing);
        }
    }
    
    /**
     * Skriver till system.out
     */
    public static void dumpPropertiesThrowIfMissing(String[] properties) {
        dumpPropertiesThrowIfMissing(System.out, properties);
    }

}
