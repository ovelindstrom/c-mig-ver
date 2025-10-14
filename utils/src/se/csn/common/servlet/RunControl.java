/**
 * Skapad 2007-maj-23
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.servlet;

/**
 * Liten utilityklass som används för att styra termineringen av servlettrådar. 
 * Kontrollera metoden isRunning() med jämna mellanrum i servlettråden, 
 * terminera om isRunning() är false. 
 * 
 */
public class RunControl {
    
    private boolean running;

    public RunControl() {
        running = true;
    }
    
    public synchronized boolean isRunning() {
        return running;
    }
    public synchronized void setRunning(boolean running) {
        this.running = running;
    }
}
