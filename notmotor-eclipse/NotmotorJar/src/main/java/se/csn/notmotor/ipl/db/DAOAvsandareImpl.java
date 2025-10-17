package se.csn.notmotor.ipl.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import se.csn.notmotor.ipl.model.Avsandare;

/**
 * CRUD-metoder for avsandare
 */
public class DAOAvsandareImpl extends DAOImplBase implements RowToObjectMapper, DAOAvsandare {

    public DAOAvsandareImpl(QueryProcessor qp) {
        super(qp);
    }

    public int createAvsandare(Avsandare avs) {
        if (avs == null) {
            throw new IllegalArgumentException("Avsändare får inte vara null");
        }
        // Hämta ny nyckel:
        int id = (int) qp.getCounter("SEKVENS", "AVSANDAREID");
        String namn = (avs.getNamn() == null) ? null : "'" + avs.getNamn() + "'";
        String epost = (avs.getEpostadress() == null) ? null : "'" + avs.getEpostadress() + "'";
        String replyTo = (avs.getReplyTo() == null) ? null : "'" + avs.getReplyTo() + "'";
        String app = (avs.getApplikation() == null) ? null : "'" + avs.getApplikation() + "'";
        String kategori = (avs.getKategori() == null) ? null : "'" + avs.getKategori() + "'";

        qp.executeThrowException("INSERT INTO AVSANDARE (ID,NAMN,EPOST,REPLYTO,PROGRAMNAMN,KATEGORI)"
            + " VALUES(" + id + ", " + namn + ", " + epost + ", "
            + replyTo + ", " + app + ", " + kategori + ")");
        avs.setId(new Long(id));
        return id;
    }

    public Object newRow(ResultSet rs) throws SQLException {
        Avsandare avs = new Avsandare();
        avs.setId(new Long(rs.getInt("ID")));
        avs.setNamn(rs.getString("NAMN"));
        avs.setEpostadress(rs.getString("EPOST"));
        avs.setReplyTo(rs.getString("REPLYTO"));
        avs.setApplikation(rs.getString("PROGRAMNAMN"));
        avs.setKategori(rs.getString("KATEGORI"));
        return avs;
    }

    public Avsandare getAvsandare(int id) {
        return (Avsandare) qp.getObject("SELECT ID,NAMN,EPOST,REPLYTO,PROGRAMNAMN,KATEGORI FROM AVSANDARE WHERE ID=" + id, this);
    }

    public List getAvsandare(String namn) {
        return qp.processQuery("SELECT ID,NAMN,EPOST,REPLYTO,PROGRAMNAMN,KATEGORI FROM AVSANDARE WHERE NAMN='" + namn + "'", this);
    }

    /**
    * Om nagon inparameter ar null sa anvands den inte i fragan.
    */
    public List getAvsandare(String namn, String programnamn, String kategori, String epost, String replyto) {
        // Bygg SQL-satsen. 
        String sql = "SELECT ID,NAMN,EPOST,REPLYTO,PROGRAMNAMN,KATEGORI FROM AVSANDARE";
        String where = "";
        where = addRestriction(where, "NAMN", "LIKE", namn);
        where = addRestriction(where, "PROGRAMNAMN", "LIKE", programnamn);
        where = addRestriction(where, "KATEGORI", "LIKE", kategori);
        where = addRestriction(where, "EPOST", "LIKE", epost);
        where = addRestriction(where, "REPLYTO", "LIKE", replyto);
        if (where.length() > 0) {
            sql += " WHERE " + where;
        }
        return qp.processQuery(sql, this);
    }

    public List getAvsandare(Avsandare avs) {
        return getAvsandare(avs.getNamn(), avs.getApplikation(), avs.getKategori(), avs.getEpostadress(), avs.getReplyTo());
    }

    public Avsandare sokEnAvsandare(Avsandare avs) {
        // Bygg SQL-satsen. 
        String sql = "SELECT ID,NAMN,EPOST,REPLYTO,PROGRAMNAMN,KATEGORI FROM AVSANDARE";
        String where = "";
        where = addRestriction(where, "NAMN", "LIKE", avs.getNamn(), false);
        where = addRestriction(where, "PROGRAMNAMN", "LIKE", avs.getApplikation(), false);
        where = addRestriction(where, "KATEGORI", "LIKE", avs.getKategori(), false);
        where = addRestriction(where, "EPOST", "LIKE", avs.getEpostadress(), false);
        where = addRestriction(where, "REPLYTO", "LIKE", avs.getReplyTo(), false);
        if (where.length() > 0) {
            sql += " WHERE " + where;
        }
        return (Avsandare) qp.getObject(sql, this);
    }


    /**
     * Soker i databasen efter anvandare som matchar kriterierna
     * @return id för den matchande avsändaren; -1 
     * om det inte fanns någon matchande avsändare, -2 om det fanns mer än en 
     */
    public int getId(Avsandare avsandare) {
        Avsandare avs = sokEnAvsandare(avsandare);
        if (avs == null) {
            return -1;
        } else {
            return avs.getId().intValue();
        }
    }


    public void updateAvsandare(Avsandare avs) {
        if (avs == null) {
            throw new IllegalArgumentException("Avsändare får inte vara null");
        }
        qp.executeThrowException("UPDATE AVSANDARE SET NAMN='" + avs.getNamn() + "', EPOST='" + avs.getEpostadress()
            + "', REPLYTO='" + avs.getReplyTo() + "', PROGRAMNAMN='" + avs.getApplikation()
            + "', KATEGORI='" + avs.getKategori() + "' WHERE ID=" + avs.getId().intValue());

    }

    /**
     * @throws IllegalStateException om det fanns mer än en användare som matchade
     *         kriteriet.
     */
    public void deleteAvsandare(Avsandare avs) {
        if (avs == null) {
            throw new IllegalArgumentException("Avsändare får inte vara null");
        }
        if (avs.getId() == null) {
            throw new IllegalArgumentException("Avsändare måste ha id satt");
        }
        int id = avs.getId().intValue();
        deleteAvsandare(id);
    }


    public void deleteAvsandare(int id) {
        qp.executeThrowException("DELETE FROM AVSANDARE WHERE ID=" + id);
    }

}
