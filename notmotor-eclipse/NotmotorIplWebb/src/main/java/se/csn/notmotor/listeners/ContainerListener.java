/**
 * Skapad 2007-jun-01
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import se.csn.ark.common.util.logging.Log;


public class ContainerListener implements ServletContextListener {

    private Log log = Log.getInstance(ContainerListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        log.info("CONTEXT DESTROY");
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        log.info("CONTEXT INIT");
    }
}
