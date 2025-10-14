/**
 * Skapad 2007-jun-01
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.sql.Connection;


public interface ControlledCommitQueryProcessor extends QueryProcessor {
    public void setCommitConnection(Connection conn, boolean commit);
}
