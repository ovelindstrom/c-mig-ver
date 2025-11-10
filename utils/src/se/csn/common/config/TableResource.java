/*
 * Skapad 2007-nov-30
 */
package se.csn.common.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * @author Jonas åhrnell - csn7821
 */
public class TableResource extends ResourceBase {

    private DataSource ds;
    private String schemaname;
    private List kolumner;
    
    public TableResource(String name, String schemaname, DataSource ds) {
        super(name, "Tabell");
        this.schemaname = schemaname;
        this.ds = ds;
        kolumner = new ArrayList();
    }
    
    public void addColumn(String kolumn) {
        kolumner.add(kolumn);
    }
    
    
    /**
     * Kontrollerar: 
     * -att schemat finns
     * -att tabellen finns
     * -att den har de angivna kolumnerna
     */
    public ResourceStatus doCheck() {
        Connection conn = null;
        DatabaseMetaData md = null;
        try {
            conn = ds.getConnection();
            md = conn.getMetaData();

            // Kolla att schemat finns:
            if(!getSchemas(md).contains(schemaname.toUpperCase())) {
                return new ResourceStatus("Hittade inte schemanamnet " + schemaname, 
                        "Kontrollera DataSource: pekar den på rätt databas?\n" +
                        "Kontrollera databasen, saknas schemat?", ResourceStatus.ERROR);
            }
            
            // Kolla att tabellen finns:
            if(!getTables(md).contains(schemaname.toUpperCase())) {
                return new ResourceStatus("Hittade inte tabellen " + name + " i schemat " + schemaname, 
                        "Kontrollera DataSource: pekar den på rätt databas?\n" +
                        "Kontrollera databasen, saknas tabellen?", ResourceStatus.ERROR);
            }
            
            // Kolla tt
            
        } catch (SQLException e) {
            return new ResourceStatus("Kunde inte hämta metadata för " + name, 
                    "Kontrollera DataSource-inställningar", ResourceStatus.ERROR);
        }
        return null;
        

    }
    
    /**
     * Hämtar alla scheman från den connection som md tagits från
     * @param md 
     * @return En lista över alla scheman i databasen, i UPPERCASE
     * @throws SQLException
     */
    public List getSchemas(DatabaseMetaData md) throws SQLException {
        List list = new ArrayList();
        ResultSet rs = md.getSchemas();
        while(rs.next()) {
            list.add(rs.getString(1).toUpperCase());
        }
        return list;
    }
    
    public List getTables(DatabaseMetaData md) throws SQLException {
        List list = new ArrayList();
        ResultSet rs = md.getTables(null, schemaname, name, null);
        while(rs.next()) {
            list.add(rs.getString(3).toUpperCase());
        }
        return list;
        
    }
    
    
}
