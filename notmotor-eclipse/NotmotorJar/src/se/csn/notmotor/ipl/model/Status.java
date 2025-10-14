/**
 * Skapad 2007-apr-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import se.csn.notmotor.ipl.SkickaMeddelandeStateMachine;


public class Status {

    private int instans, status, server;
    private String typ;
    private Date startad, stoppad, watchdog;
 
    public Status() {
    }
    
    public Status(int instansnr, int status, int server, Date startad, Date stoppad, Date watchdog, String typ) {
        this.instans = instansnr;
        this.status = status;
        this.server = server;
        this.startad = startad;
        this.stoppad = stoppad;
        this.watchdog = watchdog;
        this.typ = typ;
    }
    
    public int getInstans() {
        return instans;
    }
    public void setInstans(int instans) {
        this.instans = instans;
    }
    public int getServer() {
        return server;
    }
    public void setServer(int server) {
        this.server = server;
    }
    public Date getStartad() {
        return startad;
    }
    public void setStartad(Date startad) {
        this.startad = startad;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public Date getStoppad() {
        return stoppad;
    }
    public void setStoppad(Date stoppad) {
        this.stoppad = stoppad;
    }
    public Date getWatchdog() {
        return watchdog;
    }
    public void setWatchdog(Date watchdog) {
        this.watchdog = watchdog;
    }
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    public String getTyp() {
        return typ;
    }
    public void setTyp(String typ) {
        this.typ = typ;
    }
    /**
     * @return true om denna instans fortfarande är aktiv, false annars.
     */
    public boolean isAktiv() {
        if(stoppad != null) {
        	return false;
        }
        return status != SkickaMeddelandeStateMachine.STOPPED;
    }
}
