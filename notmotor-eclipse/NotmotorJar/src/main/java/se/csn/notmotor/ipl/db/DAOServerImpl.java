/**
 * @since 2007-apr-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import se.csn.notmotor.ipl.MeddelandeStateMachineBase;
import se.csn.notmotor.ipl.model.Server;


public class DAOServerImpl extends DAOImplBase implements DAOServer {

    public DAOServerImpl(QueryProcessor qp) {
        super(qp);
    }

    @Override
    public List getAktiva(boolean aktiv) {
        String sql = "SELECT ID,AKTIV,NOTMOTORSERVLETURL,PRESTANDA FROM SERVER";
        if (aktiv) {
            sql += " WHERE AKTIV='J' ORDER BY ID";
        } else {
            sql += " ORDER BY ID";
        }
        return qp.processQuery(sql, this);
    }

    @Override
    public int skapa(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("Server måste anges");
        }
        int id = (int) qp.getCounter("SEKVENS", "SERVERID");
        String aktiv = "J";
        if (!server.isAktiv()) {
            aktiv = "N";
        }
        String sql = "INSERT INTO SERVER (ID,AKTIV,NOTMOTORSERVLETURL,PRESTANDA) VALUES " +
            "(" + id + ",'" + aktiv + "', '" + server.getServleturl() + "'," + server.getPrestanda() + ")";
        qp.executeThrowException(sql);
        server.setId(id);
        return id;
    }

    @Override
    public void uppdatera(Server server) {
        String aktiv = "J";
        if (!server.isAktiv()) {
            aktiv = "N";
        }
        String sql = "UPDATE SERVER SET AKTIV='" + aktiv + "',NOTMOTORSERVLETURL='"
            + server.getServleturl() + "',PRESTANDA=" + server.getPrestanda()
            + " WHERE ID=" + server.getId();
        qp.executeThrowException(sql);
    }

    @Override
    public Object newRow(ResultSet rs) throws SQLException {
        String aktiv = rs.getString("AKTIV");
        return new Server(rs.getInt("ID"), aktiv.equals("J"), rs.getInt("PRESTANDA"), rs.getString("NOTMOTORSERVLETURL"));
    }


    @Override
    public int getLevandeProcesser(int serverid) {
        return qp.getInt("SELECT COUNT(*) FROM STATUS WHERE SERVER=" + serverid + " AND STATUS <> " + MeddelandeStateMachineBase.STOPPED, 0);
    }

    @Override
    public void delete(int serverid) {
        qp.executeThrowException("DELETE FROM SERVER WHERE ID=" + serverid);
    }

    @Override
    public Server get(int serverid) {
        return (Server) qp.getObject("SELECT ID,AKTIV,NOTMOTORSERVLETURL,PRESTANDA FROM SERVER WHERE ID=" + serverid, this);
    }
}
