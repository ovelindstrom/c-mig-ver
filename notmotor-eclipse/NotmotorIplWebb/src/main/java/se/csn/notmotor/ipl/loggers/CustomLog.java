/**
 * Skapad 2007-jun-01
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.loggers;

import org.apache.commons.logging.Log;


public class CustomLog implements Log {

    private String classname;

    public CustomLog(String classname) {
        this.classname = classname;
    }

    @Override
    public void debug(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }

    @Override
    public void debug(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }

    @Override
    public void error(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }

    @Override
    public void error(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }

    @Override
    public void fatal(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }

    @Override
    public void fatal(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }

    @Override
    public void info(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }

    @Override
    public void info(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isFatalEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void trace(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }

    @Override
    public void trace(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }

    @Override
    public void warn(Object arg0, Throwable arg1) {
        System.out.println(classname + ": " + arg0);
    }

    @Override
    public void warn(Object arg0) {
        System.out.println(classname + ": " + arg0);
    }
}
