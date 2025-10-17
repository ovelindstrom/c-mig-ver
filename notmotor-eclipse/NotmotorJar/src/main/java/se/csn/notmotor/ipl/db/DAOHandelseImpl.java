/**
 * Skapad 2007-mar-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import se.csn.notmotor.ipl.model.MeddelandeHandelse;


public class DAOHandelseImpl extends DAOImplBase implements DAOHandelse {

    public DAOHandelseImpl(QueryProcessor qp) {
        super(qp);
    }

    @Override
    public long createHandelse(MeddelandeHandelse h, long meddelandeid) {
        return createHandelse(h, meddelandeid, null);
    }

    /**
     * Skapar en rad i databasen och 
     */
    @Override
    public long createHandelse(MeddelandeHandelse h, long meddelandeid, Connection conn) {
        long id = qp.getCounter("SEKVENS", "HANDELSEID");
        int kod = getInt(h.getFelkod(), 0);
        int typ = getInt(h.getHandelsetyp(), 0);
        int instans = getInt(h.getInstans(), -1);
        String feltext = quoteValue(h.getFeltext());
        String datum = quoteValue(h.getTidpunkt());
        qp.executeThrowException("INSERT INTO HANDELSE (ID,MEDDELANDEID,TYP,KOD,TEXT,TIDPUNKT,INSTANS) " +
            "VALUES (" + id + ", " + meddelandeid + ", " + typ + ", " + kod +
            ", " + feltext + ", " + datum + ", " + instans + ")");
        h.setId(new Long(id));
        return id;
    }

    @Override
    public Object newRow(ResultSet rs) throws SQLException {
        MeddelandeHandelse h = new MeddelandeHandelse();
        h.setId(new Long(rs.getLong("ID")));
        h.setHandelsetyp(new Integer(rs.getInt("TYP")));
        h.setFelkod(new Integer(rs.getInt("KOD")));
        h.setFeltext(rs.getString("TEXT"));
        h.setTidpunkt(rs.getTimestamp("TIDPUNKT"));
        h.setInstans(new Integer(rs.getInt("INSTANS")));
        return h;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MeddelandeHandelse> getHandelserForMeddelande(long meddelandeid) {
        return qp.processQuery("SELECT * FROM HANDELSE WHERE MEDDELANDEID=" + meddelandeid + " ORDER BY ID", this);
    }

    @Override
    public MeddelandeHandelse getHandelse(long id) {
        List list = qp.processQuery("SELECT * FROM HANDELSE WHERE ID=" + id, this);
        if (list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new IllegalStateException("Hittade mer än ett meddelande med id " + id);
        }
        return (MeddelandeHandelse) list.get(0);
    }

}
