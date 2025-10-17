package se.csn.notmotor.admin.listeners;

import javax.faces.FactoryFinder;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sun.faces.lifecycle.LifecycleImpl;

/**
 * @since 2007-jun-18
 * @author Jonas Ahrnell (csn7821)
 */
public class TilesIntegrationContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * This code is necessary to set a new ViewHandler in the Faces system.
     * We want to do this to work around a limitation in the Tiles machinery;
     * Tiles checks that the response is not committed. The default implementation
     * of JSF's ViewHandler commits the response, assuming it is the sole renderer.
     * We replace it with a custom implementation that fixes this feature.
     */
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // Get the LifecycleFactory from the Factory Finder
        LifecycleFactory factory = (LifecycleFactory) FactoryFinder
                .getFactory("javax.faces.lifecycle.LifecycleFactory");

        /*
         * Create a new instance of Lifecycle implementation -
         * com.sun.faces.lifecycle.LifecycleImpl
         * According to the documentation, factory.getLifecycle("STFLifecycle")
         * should work, but JSF-RI has a defect.
         * Hence this workaround of creating a RI class explicitly.
         */
        LifecycleImpl stfLifecycleImpl = new LifecycleImpl();

        /*
         * Create a new instance of our STFViewHandler and set it on the Lifecycle
         * stfLifecycleImpl.setViewHandler(new STFViewHandlerImpl());
         * 
         * Register the new lifecycle with the factory with a unique
         * name "STFLifecycle"
         */
        factory.addLifecycle("STFLifecycle", stfLifecycleImpl);
    }
}
