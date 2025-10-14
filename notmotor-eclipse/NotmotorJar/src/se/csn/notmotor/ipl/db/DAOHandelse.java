/**
 * Skapad 2007-apr-11
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.sql.Connection;
import java.util.List;

import se.csn.notmotor.ipl.model.MeddelandeHandelse;

public interface DAOHandelse {
    public long createHandelse(MeddelandeHandelse h,
            long meddelandeid);

    public long createHandelse(MeddelandeHandelse h,
            long meddelandeid, Connection conn);

    public List<MeddelandeHandelse> getHandelserForMeddelande(long meddelandeid);
    
    public MeddelandeHandelse getHandelse(long id);
}