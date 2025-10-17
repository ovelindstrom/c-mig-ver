/**
 * @since 2007-apr-16
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import se.csn.notmotor.ipl.model.Tidsintervall;


public class DAOSchemaImpl extends DAOImplBase implements DAOSchema {


    public DAOSchemaImpl(QueryProcessor qp) {
        super(qp);
    }

    @Override
    public void delete(Tidsintervall intervall) {
        qp.executeThrowException("DELETE FROM KORSCHEMA WHERE STANGNINGSTID="
            + quoteValue(intervall.getStarttid()) + " AND OPPNINGSTID=" + quoteValue(intervall.getSluttid()));

    }


    @Override
    public Object newRow(ResultSet rs) throws SQLException {
        return new Tidsintervall(rs.getTimestamp("STANGNINGSTID"), rs.getTimestamp("OPPNINGSTID"));
    }

    @Override
    public List getIntervall() {
        return qp.processQuery("SELECT STANGNINGSTID, OPPNINGSTID FROM KORSCHEMA", this);
    }

    @Override
    public void skapaIntervall(Tidsintervall intervall) {
        qp.executeThrowException("INSERT INTO KORSCHEMA (STANGNINGSTID, OPPNINGSTID) VALUES "
            + "(" + quoteValue(intervall.getStarttid()) + ", " + quoteValue(intervall.getSluttid()) + ")");
    }
}
