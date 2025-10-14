/**
 * Skapad 2007-apr-16
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.util.List;

import se.csn.notmotor.ipl.model.Tidsintervall;


public interface DAOSchema {

    /**
     * @return En lista av Tidsintervall
     */
    public List getIntervall();

    public void skapaIntervall(Tidsintervall intervall);

    public void delete(Tidsintervall intervall);
}
