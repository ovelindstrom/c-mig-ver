/**
 * Skapad 2007-jun-01
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.loggers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;


public class CustomLogFactory extends LogFactory {

    public Object getAttribute(String arg0) {
        return null;
    }
    public String[] getAttributeNames() {
        return null;
    }
    public Log getInstance(Class arg0) throws LogConfigurationException {
        System.out.println("LOGFACTORY: getting instance for " + arg0.getName());
        return new CustomLog(arg0.getName());
    }
    public Log getInstance(String arg0) throws LogConfigurationException {
        return new CustomLog(arg0);
    }
    public void release() {
        // TODO Auto-generated method stub

    }
    public void removeAttribute(String arg0) {
        // TODO Auto-generated method stub

    }
    public void setAttribute(String arg0, Object arg1) {
        // TODO Auto-generated method stub

    }
}
