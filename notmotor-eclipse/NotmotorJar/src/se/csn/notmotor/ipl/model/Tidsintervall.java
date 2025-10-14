/**
 * Skapad 2007-apr-16
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.model;

import java.util.Date;

/***
 * Klass som representerar ett tidsintervall. 
 * Granserna kan sattas till null, de tolkas da som oppna. 
 * Ett intervall med bade start och slut satta till null omfattar
 * alltsa all tid.
 */
public class Tidsintervall {

    private Date starttid, sluttid;

    public Tidsintervall(Date start, Date slut) {
        this.starttid = start;
        this.sluttid = slut;
    }

    /**
     * Kontrollerar om inmatad tid befinner sig inom intervallet. 
     * @return 
     */
    public boolean isInIntervall(Date tidpunkt) {
        if (tidpunkt == null) {
        	return false;
        }
        if ((starttid == null) && (sluttid == null)) {
            return true;
        }
        if (starttid == null) {
            return tidpunkt.before(sluttid);
        }
        if (sluttid == null) {
            return tidpunkt.after(starttid);
        }
        return (tidpunkt.before(sluttid) && tidpunkt.after(starttid));
    }


    public Date getSluttid() {
        return sluttid;
    }
    public void setSluttid(Date sluttid) {
        this.sluttid = sluttid;
    }
    public Date getStarttid() {
        return starttid;
    }
    public void setStarttid(Date starttid) {
        this.starttid = starttid;
    }

    private boolean dateEquals(Date d1, Date d2) {
        if (d1 == null) {
        	return (d2 == null);
        }
        return d1.equals(d2);
    }

    public boolean equals(Object o) {
        if (o == null) {
        	return false;
        }
        if (!(o instanceof Tidsintervall)) {
        	return false;
        }
        Tidsintervall t = (Tidsintervall)o;
        return (dateEquals(starttid, t.starttid) && dateEquals(sluttid, t.sluttid));
    }

    public int hashCode() {
        int h1 = (starttid == null) ? 0 : starttid.hashCode();
        int h2 = (sluttid == null) ? 0 : sluttid.hashCode();
        return h1 ^ h2;
    }
}
