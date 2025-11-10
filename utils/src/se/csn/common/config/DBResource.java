/*
 * Skapad 2007-nov-29
 */
package se.csn.common.config;

import se.csn.common.jndi.ServiceLocator;

/**
 * @author Jonas åhrnell - csn7821
 * Klassen testar databaskoppling genom att slå på metadata. 
 * Kommer att lägga till kontroll av tabeller. 
 */
public class DBResource extends ResourceBase {

    private String jndiname;
    private ServiceLocator serviceLocator;
    
    public DBResource(String name, String jndiname, ServiceLocator sl) {
        super(name, "Databaskoppling");
        this.jndiname = jndiname;
        serviceLocator = sl;
    }
    
    /* (non-Javadoc)
     * @see se.csn.common.config.ResourceBase#doCheck()
     */
    public ResourceStatus doCheck() {
        String problem = null;
        String fix = null;
        try {
            serviceLocator.getDatasource(jndiname);
        } catch(IllegalArgumentException iae) {
            problem = "Kunde inte hitta " + jndiname;
            fix = "Kontrollera datasourcens JNDI-namn och hur det är uppsatt i WAS:en";
        } catch(IllegalStateException ise) {
            problem = "Kunde inte slå upp " + jndiname;
            fix = "Kontrollera datasourcens JNDI-namn och hur det är uppsatt i WAS:en";
        }
        if(problem != null) {
            return new ResourceStatus(problem, fix, ResourceStatus.ERROR);
        } else {
            return new ResourceStatus();
        }
    }
}
