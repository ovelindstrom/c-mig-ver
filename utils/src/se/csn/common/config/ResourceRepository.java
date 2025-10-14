/*
 * Skapad 2007-nov-29
 */
package se.csn.common.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonas åhrnell - csn7821
 * Klass från Domain Driven Design: skilj Factory från Repository. 
 * Repositoryt innehåller alla Resource-objekt. Kan användas för att kontrollera
 * alla dependencies, för att bara instantiera just precis de Resources som behövs 
 * (undvika dubbelkontroller) mm.
 * Det är även ResourceRepository som är ansvarigt för att göra trådade kontroller etc.
 */
public class ResourceRepository {
    
    private List resources;
    
    public ResourceRepository() {
        resources = new ArrayList();
    }
    
    /**
     * Används för att inte hålla flera referenser till identiska objekt.
     * @param r
     * @return Det objekt som fanns i repositoryt om det fanns ett sådant, 
     * 		   annars det objekt som lades till repositoryt 
     */
    public Resource addResourceIfMissing(Resource r) {
        int i = resources.indexOf(r);
        if(i >= 0) {
            return (Resource)resources.get(i);
        }
        resources.add(r);
        return r;
    }
    

}
