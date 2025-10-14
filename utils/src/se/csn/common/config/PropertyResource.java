/**
 * Skapad 2007-apr-24
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class PropertyResource extends ResourceBase {
    
    private String propfil; 
    
    public PropertyResource(String propertyname, String propertyFilename) {
        super(propertyname, "Property");
        this.propfil = propertyFilename;
    }
    
    public ResourceStatus doCheck() {
        if(!isPropertySetInFile(name, propfil)) {
            return new ResourceStatus("Kunde inte hitta property " + name + " i fil " + propfil,
                    "Kontrollera att propertyn är satt, antingen via environment, " +
                    "kommandorad (-D-flaggan) eller via propertiesfil.", 
                    ResourceStatus.ERROR);
        }
        return new ResourceStatus();
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
}
