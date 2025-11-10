package se.csn.notmotor.ipl.db;

import java.sql.Connection;


public interface ControlledCommitQueryProcessor extends QueryProcessor {
    public void setCommitConnection(Connection conn, boolean commit);
}
