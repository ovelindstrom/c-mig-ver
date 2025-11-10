package se.csn.notmotor.ipl.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import se.csn.notmotor.ipl.model.Mottagare;

/**
 * CRUD for mottagare
 */
public class DAOMottagareImpl extends DAOImplBase implements DAOMottagare {

    public DAOMottagareImpl(QueryProcessor qp) {
        super(qp);
    }

    @Override
    public int createMottagare(Mottagare mott, long meddelandeId) {
        if (mott == null) {
            throw new IllegalArgumentException("Mottagare måste vara satt");
        }
        int id = (int) qp.getCounter("SEKVENS", "MOTTAGARE");
        qp.executeThrowException("INSERT INTO MOTTAGARE(ID,TYP,MEDDELANDEID,NAMN,ADRESS,CSNNUMMER,STATUS) "
            + "VALUES(" + id + ", " + quoteValue(mott.getTyp()) + ", " + meddelandeId + ", "
            + quoteValue(mott.getNamn()) + ", " + quoteValue(mott.getAdress()) + ", " + quoteValue(mott.getCsnnummer()) + ", "
            + quoteValue(mott.getStatus()) + ")");
        mott.setId(Long.valueOf(id));
        return id;
    }

    @Override
    public Object newRow(ResultSet rs) throws SQLException {
        Mottagare mott = new Mottagare();
        mott.setId(Long.valueOf(rs.getLong("ID")));
        mott.setAdress(rs.getString("ADRESS"));
        mott.setCsnnummer((Integer) rs.getObject("CSNNUMMER"));
        mott.setNamn(rs.getString("NAMN"));
        mott.setTyp(rs.getString("TYP"));
        //int cn = rs.getInt("STATUS");
        mott.setStatus((Integer) rs.getObject("STATUS"));
        return mott;
    }

    public List getMottagare(String namn, String adress, String typ, Integer csnnummer) {
        String sql = "SELECT ID,TYP,MEDDELANDEID,NAMN,ADRESS,CSNNUMMER,STATUS FROM MOTTAGARE";
        String where = "";
        where = addRestriction(where, "NAMN", "LIKE", namn);
        where = addRestriction(where, "ADRESS", "LIKE", adress);
        where = addRestriction(where, "TYP", "LIKE", typ);
        where = addRestriction(where, "CSNNUMMER", "=", csnnummer);
        if (where.length() > 0) {
            sql += " WHERE " + where;
        }
        return qp.processQuery(sql, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Mottagare> getMottagare(Mottagare mott) {
        return getMottagare(mott.getNamn(), mott.getAdress(), mott.getTyp(), mott.getCsnnummer());
    }

    @Override
    public Mottagare getMottagare(long id) {
        return (Mottagare) qp.getObject("SELECT ID,TYP,NAMN,ADRESS,CSNNUMMER,STATUS FROM MOTTAGARE "
            + "WHERE ID=" + id, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Mottagare> getMottagareForMeddelande(long meddelandeId) {
        return qp.processQuery("SELECT ID,TYP,NAMN,ADRESS,CSNNUMMER,STATUS FROM MOTTAGARE "
            + "WHERE MEDDELANDEID=" + meddelandeId, this);
    }

    @Override
    public void updateMottagare(Mottagare mott) {
        String query = makeUpdateQuery("MOTTAGARE", new Object[]{"TYP", mott.getTyp(), "NAMN", mott.getNamn(),
                "ADRESS", mott.getAdress(), "CSNNUMMER", mott.getCsnnummer(), "STATUS", mott.getStatus()},
            new Object[]{"ID", mott.getId()});
        qp.executeThrowException(query);
    }

    @Override
    public void deleteMottagare(Mottagare mott) {
        qp.executeThrowException("DELETE FROM MOTTAGARE WHERE ID=" + mott.getId());
    }
}
