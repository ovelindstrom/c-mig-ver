/**
 * Skapad 2007-apr-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.common.config;

import java.util.List;


public interface Resource {
    
    						
    
    /**
     * @return En (kortare) text som beskriver resursen.
     */
    String getName();
    
    /**
     * @return En beskrivning av resursens typ, ex.vis "MQ-kö", "Fil" eller "Databaskoppling".
     */
    String getType();
    
    /**
     * @return Det fullt kvalificerade klassnamnet på Resource-implementationen
     */
    String getClassName();
    
    /**
     * Testar om denna resurs finns och är rätt konfigurerad. Testar alla 
     * dependencies rekursivt. Ska även lagra resultatet för framtida bruk.
     * @return status för resursen. 
     */
    ResourceStatus check();
    
    /**
     * @return Det kända tillståndet för resursen. Kör INTE en ny test.  
     */
    ResourceStatus getStatus();
    
    /**
     * Dependencies är resurser som måste fungera för att denna resurs ska fungera. 
     * @return En lista av alla resurser som måste fungera för att denna resurs ska fungera.
     */
    List getDependencies();
    boolean hasDependencies();
    /**
     * Skall även lägga upp this som dependant i dependencyn, dvs. skapa tvåvägskopplingen  
     * @param dependency
     */
    void addDependency(Resource dependency);
    /**
     * Skall även ta bort upp this som dependant i dependencyn, dvs. ta bort tvåvägskopplingen  
     * @param dependency
     */
    void removeDependency(Resource dependency);

    /** 
     * Dependants är resurser som är beroende av denna resurs för att fungera.
     * @return En lista av alla dependants
     */
    List getDependants();
    boolean hasDependants();
    void addDependant(Resource dependant);
    void removeDependant(Resource dependant);
    
    /**
     * XML-format:
     * <resource name="" type="" classname="" exists="">
     * 		<problem text"" />
     * 		<fix text="" />
     * 		<resource ...>
     * 		...
     * 		</resource>
     * </resource>
     * Returnerar en xml-beskrivning av detta element och dess underelement.
     * @param indentation Indenteringssträng. För varje nivå läggs en extra indentering till. 
     */
    String getXML(String indentation);

}
