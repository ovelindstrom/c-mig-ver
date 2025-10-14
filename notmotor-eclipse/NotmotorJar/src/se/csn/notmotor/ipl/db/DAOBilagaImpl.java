/**
 * Skapad 2007-mar-20
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.model.Bilaga;

/**
 * CRUD för bilagor
 */
public class DAOBilagaImpl implements RowToObjectMapper, DAOBilaga {

    private QueryProcessor qp;
    private Log log = Log.getInstance(RowToObjectMapper.class);
    
    public DAOBilagaImpl(QueryProcessor qp) {
        this.qp = qp;
    }
    
    /**
     * Skapar bilaga i databasen
     * @return nyckeln för bilagan
     */
    public long createBilaga(Bilaga b, long meddelandeid) {
        if(b.getData() == null) {
            throw new IllegalArgumentException("Bilagan måste ha data.");
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = qp.getConnection();
            
            String query = "INSERT INTO BILAGA (ID,MEDDELANDEID,DATA,MIMETYP,ENCODING,FILNAMN) " +
        		"VALUES(?,?,?,?,?,?)";
        
            long id = qp.getCounter("SEKVENS", "BILAGAID");
            ps = conn.prepareStatement(query);
            ps.setLong(1,id);
            ps.setLong(2,meddelandeid);
            ps.setBinaryStream(3, new ByteArrayInputStream(b.getData()), b.getData().length);
            ps.setString(4, b.getMimetyp());
            ps.setString(5, b.getEncoding());
            ps.setString(6, b.getFilnamn());
            
            int result = ps.executeUpdate();
            if(result != 1) {
                throw new SQLException("INSERT returnerade " + result);
            }
            b.setId(new Long(id));
            return id;
        } catch (SQLException e) {
            log.error("Kunde inte skapa meddelande i databasen: ", e);
            throw new IllegalStateException("Kunde inte skapa meddelande i basen " + e);
        } finally {
            try {
	            if(ps != null) { ps.close(); }
            } catch(SQLException sqle) {
                throw new IllegalStateException("Kunde inte stänga resurser", sqle);
            }
        }
    }
    
    public Object newRow(ResultSet rs) throws SQLException {
        Bilaga b = new Bilaga();
        b.setId(new Long(rs.getLong("ID")));
        b.setData(qp.getBlob(rs, "DATA"));
        b.setEncoding(rs.getString("ENCODING"));
        b.setFilnamn(rs.getString("FILNAMN"));
        b.setMimetyp(rs.getString("MIMETYP"));
        return b;
    }
    
    @SuppressWarnings("unchecked")
	public List<Bilaga> getBilagorForMeddelande(long meddelandeid) {
        return qp.processQuery("SELECT ID,DATA,ENCODING,FILNAMN,MIMETYP FROM BILAGA WHERE MEDDELANDEID=" + meddelandeid, this);
    }
    
    public Bilaga getBilaga(long id) {
        return (Bilaga)qp.getObject("SELECT ID,DATA,ENCODING,FILNAMN,MIMETYP FROM BILAGA WHERE ID=" + id, this);
    }
    
    
}
