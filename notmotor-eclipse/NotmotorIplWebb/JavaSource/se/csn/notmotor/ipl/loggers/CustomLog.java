/**
 * Skapad 2007-jun-01
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.loggers;

import org.apache.commons.logging.Log;


public class CustomLog implements Log {

    private String classname;
    
    public CustomLog(String classname) {
        this.classname = classname;
    }
    
    public void debug(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }
    public void debug(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }
    public void error(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }
    public void error(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }
    public void fatal(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }
    public void fatal(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }
    public void info(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }
    public void info(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }
    public boolean isDebugEnabled() {
        return true;
    }
    public boolean isErrorEnabled() {
        return true;
    }
    public boolean isFatalEnabled() {
        return true;
    }
    public boolean isInfoEnabled() {
        return true;
    }
    public boolean isTraceEnabled() {
        return true;
    }
    public boolean isWarnEnabled() {
        return true;
    }
    public void trace(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }
    public void trace(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }
    public void warn(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }
    public void warn(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }
}
