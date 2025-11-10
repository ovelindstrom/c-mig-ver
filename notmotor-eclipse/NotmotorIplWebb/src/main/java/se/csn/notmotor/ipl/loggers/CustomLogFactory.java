package se.csn.notmotor.ipl.loggers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;


public class CustomLogFactory extends LogFactory {

    @Override
    public Object getAttribute(String arg0) {
        return null;
    }

    @Override
    public String[] getAttributeNames() {
        return null;
    }

    @Override
    public Log getInstance(Class arg0) throws LogConfigurationException {
        System.out.println("LOGFACTORY: getting instance for " + arg0.getName());
        return new CustomLog(arg0.getName());
    }

    @Override
    public Log getInstance(String arg0) throws LogConfigurationException {
        return new CustomLog(arg0);
    }

    @Override
    public void release() {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAttribute(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAttribute(String arg0, Object arg1) {
        // TODO Auto-generated method stub

    }
}
