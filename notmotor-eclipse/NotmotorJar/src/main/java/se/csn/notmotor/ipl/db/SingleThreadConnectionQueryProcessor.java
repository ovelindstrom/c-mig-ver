package se.csn.notmotor.ipl.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import se.csn.ark.common.dal.db.DatabaseException;
import se.csn.ark.common.util.logging.Log;

/**
 * QueryProcessor som anvander en enda Connection. 
 * Denna connection lagras i en statisk medlemsvariabel.
 * OBS! Kan leda till udda beteende om flera webappar delar klassladdare.
 */
public class SingleThreadConnectionQueryProcessor extends QueryProcessorBase implements ControlledCommitQueryProcessor {

    private DataSource ds;
    private Log log = Log.getInstance(SingleThreadConnectionQueryProcessor.class);
    private static Map s_connections; // Innehåller ett antal Maps, en för varje DataSource
    private static Map<Connection, Boolean> s_commitFlags;
    private static int connectionCount = 0;
    private String tradNamn = Thread.currentThread().getName();

    public SingleThreadConnectionQueryProcessor(DataSource ds) {
        this.ds = ds;
    }


    /**
     * Hamtar connection, om det inte finns nagon skapas en ny.
     * @return Samma connection för alla anrop i denna tråd med denna datasource
     */
    public Connection getConnection() {
        try {
            Connection conn = getConnectionForThisThread();
            if ((conn != null) && (conn.isClosed())) {
                if (log.isDebugEnabled()) {
                    log.debug("Connection var stängd!");
                }
                ((Map) s_connections.get(ds)).remove(tradNamn);
                conn = null;
            }
            if ((conn == null) || (conn.isClosed())) {
                connectionCount++;
                log.info("Skapar ny connection, nr " + connectionCount);
                conn = ds.getConnection();
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                setConnectionForThisThread(conn);
            }
            return conn;
        } catch (SQLException e) {
            log.fatal("Kunde inte skapa ny connection", e);
            throw new DatabaseException("Kunde inte skapa ny connection", e);
        }
    }


    public void setConnection(Connection conn, boolean handleConnection) {
        setConnectionForThisThread(conn);
        this.handleConnection = handleConnection;
    }


    protected void handleConnection(Connection conn) throws SQLException {
        if (s_commitFlags == null) {
            s_commitFlags = new HashMap();
        }

        Boolean commit = (Boolean) s_commitFlags.get(conn);
        if ((commit == null) || commit.booleanValue()) {
            if (!conn.isClosed()) {
                conn.commit();
            }
        }
    }

    public void setCommitConnection(Connection conn, boolean commit) {
        if (s_commitFlags == null) {
            s_commitFlags = new HashMap();
        }
        s_commitFlags.put(conn, new Boolean(commit));
    }

    /**
     * Gor commit() och close() pa en connection, samt lyfter bort den ur tradmappen.
     */
    public void removeConnectionForThisThread() {
        Connection conn = getConnectionForThisThread();
        if (conn == null) {
            throw new IllegalStateException("Anropat remove innan Connection skapats");
        }
        try {
            conn.commit();
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Kunde inte göra commit() och/eller close():" + e);
        } finally {
            ((Map) s_connections.get(ds)).remove(tradNamn);
        }
    }


    /**
     * @return Connection för denna tråd och datasource
     */
    private Connection getConnectionForThisThread() {
        if (s_connections == null) {
            s_connections = new HashMap();
        }

        Map connections = (Map) s_connections.get(ds);
        if (connections == null) {
            connections = new HashMap();
            s_connections.put(ds, connections);
        }

        return (Connection) connections.get(tradNamn);
    }


    /**
     * Satter connection for denna trad och datasource.
     * @param conn Connection som ska sättas
     */
    private void setConnectionForThisThread(Connection conn) {
        if (s_connections == null) {
            s_connections = new HashMap();
        }

        Map connections = (Map) s_connections.get(ds);
        if (connections == null) {
            connections = new HashMap();
        }
        connections.put(tradNamn, conn);

        s_connections.put(ds, connections);
    }

}
