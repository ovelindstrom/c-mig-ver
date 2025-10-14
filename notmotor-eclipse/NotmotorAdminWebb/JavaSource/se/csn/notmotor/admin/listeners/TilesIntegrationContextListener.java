/**
 * Skapad 2007-jun-18
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.admin.listeners;

import javax.faces.FactoryFinder;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sun.faces.lifecycle.LifecycleImpl;

//import org.apache.myfaces.lifecycle.LifecycleImpl;


public class TilesIntegrationContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

    }
    
    /**
     * Denna kod nödvändig för att kunna sätta en ny ViewHandler i 
     * Faces-systemet. 
     * Vi vill göra det för att komma runt en begränsning i Tiles-
     * maskineriet; Tiles kontrollerar att svaret inte är committat. 
     * Default-implementationen av JSF:s ViewHandler committar svaret, 
     * den utgår från att den är ensam renderare. 
     * Vi ersätter med en "custom-implementation" som fixar featuren:
     *  
     */
    public void contextInitialized(ServletContextEvent arg0) {
//      Get the LifecycleFactory from the Factory Finder
        LifecycleFactory factory = (LifecycleFactory) 
          FactoryFinder.getFactory("javax.faces.lifecycle.LifecycleFactory");

//        Create a new instance of Lifecycle implementation - 
//        com.sun.faces.lifecycle.LifecycleImpl
//        According to the documentation, factory.getLifecycle("STFLifecycle") 
//        should work, but JSF-RI has a defect.
//        Hence this workaround of creating a RI class explicitly.
        LifecycleImpl stfLifecycleImpl = new LifecycleImpl();

//        Create a new instance of our STFViewHandler and set it on the Lifecycle
        //stfLifecycleImpl.setViewHandler(new STFViewHandlerImpl());

//        Register the new lifecycle with the factory with a unique 
//        name "STFLifecycle"
        factory.addLifecycle("STFLifecycle", stfLifecycleImpl);

    }
}
