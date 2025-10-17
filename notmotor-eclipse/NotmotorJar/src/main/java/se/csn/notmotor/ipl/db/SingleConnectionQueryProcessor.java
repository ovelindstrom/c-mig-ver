package se.csn.notmotor.ipl.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import se.csn.ark.common.dal.db.DatabaseException;
import se.csn.ark.common.util.logging.Log;

/**
 * QueryProcessor som anvander en enda Connection. 
 * Denna connection lagras i en statisk medlemsvariabel.
 * OBS! Kan leda till udda beteende om flera webappar delar klassladdare.
 */
public class SingleConnectionQueryProcessor extends QueryProcessorBase implements ControlledCommitQueryProcessor {

    private DataSource ds;
    private Log log = Log.getInstance(SingleConnectionQueryProcessor.class);
    private static Connection s_connection;
    private static Object lock = new Object();
    private static boolean s_commit = true;

    public SingleConnectionQueryProcessor(DataSource ds) {
        this.ds = ds;
        log.info("Created SingleConnectionQueryProcessor");
        //addQueryListener(new QueryListenerImpl(""));
    }

    private static int connCounter = 1;

    @Override
    public Connection getConnection() {
        try {
            synchronized (lock) {
                if ((s_connection == null) || (s_connection.isClosed())) {
                    log.debug("Creating new connection: " + connCounter);
                    connCounter++;
                    s_connection = ds.getConnection();
                    s_connection.setAutoCommit(false);
                    s_connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                }
                return s_connection;
            }
        } catch (SQLException e) {
            log.fatal("Kunde inte skapa ny connection", e);
            throw new DatabaseException("Kunde inte skapa ny connection", e);
        }
    }

    public void setConnection(Connection conn, boolean handleConnection) {
        s_connection = conn; // NOSONAR
        this.handleConnection = handleConnection;
    }


    protected void handleConnection(Connection conn) throws SQLException {
        if (s_commit && (!conn.isClosed())) {
            conn.commit();
        }
    }

    public void setCommitConnection(Connection conn, boolean commit) {
        s_commit = commit; // NOSONAR
    }
}
