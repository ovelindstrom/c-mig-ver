/**
 * Skapad 2007-apr-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import se.csn.notmotor.ipl.model.Status;


public class DAOStatusImpl extends DAOImplBase implements DAOStatus {

    public DAOStatusImpl(QueryProcessor qp) {
        super(qp);
    }

    public Status getStatus(int instans) {
        return (Status)qp.getObject("SELECT INSTANS,STARTAD,STOPPAD,STATUS,WATCHDOGTSTAMP,SERVER,TYP FROM STATUS WHERE INSTANS=" + instans, this);
    }

    public List getStatus(Integer status, Integer server) {
        String where = addRestriction("", "STATUS", "=", status);
        where = addRestriction(where, "SERVER", "=", server);
        if(where.length() > 0) {
            where = (" WHERE " + where);
        }
        where += " ORDER BY INSTANS";
        return qp.processQuery("SELECT INSTANS,STARTAD,STOPPAD,STATUS,WATCHDOGTSTAMP,SERVER,TYP FROM STATUS " + where, this);
    }

    public int skapa(Status status) {
        if(status == null) {
            throw new IllegalArgumentException("Status måste vara satt");
        }
        int id = (int)qp.getCounter("SEKVENS", "STATUSID");
        String sql = "INSERT INTO STATUS (INSTANS,STARTAD,STOPPAD,STATUS,WATCHDOGTSTAMP,SERVER,TYP) VALUES " +
        		"(" + id + "," + quoteValue(status.getStartad()) + "," + quoteValue(status.getStoppad())
                + "," + status.getStatus()+ "," + quoteValue(status.getWatchdog()) + "," + status.getServer() + "," + quoteValue(status.getTyp()) + ")";
        qp.executeThrowException(sql);
        status.setInstans(id);
        return id;
    }

    /**
     * Uppdaterar endast STATUS, STOPPAD och WATCHDDOGTSTAMP
     */
    public void uppdatera(Status status) {
        if(status == null) {
            throw new IllegalArgumentException("Status måste vara satt");
        }
        qp.executeThrowException("UPDATE STATUS SET STATUS=" + status.getStatus() + ", STOPPAD=" + quoteValue(status.getStoppad()) + ", " +
        		"WATCHDOGTSTAMP=" + quoteValue(status.getWatchdog()) + " WHERE INSTANS=" + status.getInstans());
    }

    public Object newRow(ResultSet rs) throws SQLException {
        Status s = new Status();
        s.setInstans(rs.getInt("INSTANS"));
        s.setServer(rs.getInt("SERVER"));
        s.setStartad(rs.getTimestamp("STARTAD"));
        s.setStoppad(rs.getTimestamp("STOPPAD"));
        s.setStatus(rs.getInt("STATUS"));
        s.setWatchdog(rs.getTimestamp("WATCHDOGTSTAMP"));
        s.setTyp(rs.getString("TYP"));
        return s;
    }


    public void delete(int instans) {
        qp.executeThrowException("DELETE FROM STATUS WHERE INSTANS=" + instans);
    }
}
