package se.csn.notmotor.ipl.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import se.csn.ark.common.dal.db.DatabaseException;
import se.csn.ark.common.util.logging.Log;

/**
 * Anvander en enda Connection. 
 * Denna connection lagras i en statisk medlemsvariabel.
 * OBS! Kan leda till udda beteende om flera webappar delar klassladdare.
 */
public class WebServiceQueryProcessor extends QueryProcessorBase implements ControlledCommitQueryProcessor {
	
	private DataSource ds;
	private Connection connection;
	private boolean commit = false;
	private Log log = Log.getInstance(WebServiceQueryProcessor.class);
	
	public WebServiceQueryProcessor(DataSource ds) {
		this.ds = ds;
	}
	
	/**
	 * 
	 */
	public Connection getConnection() {
		try {
			if ((connection == null) || (connection.isClosed())) {
				connection = ds.getConnection();
				connection.setAutoCommit(false);
				connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			}
			return connection;
		} catch (SQLException e) {
			log.fatal("Kunde inte skapa ny connection", e);
			throw new DatabaseException("Kunde inte skapa ny connection", e);
		}
	}

    public void setConnection(Connection conn, boolean handleConnection) {
    	connection = conn;
    	this.handleConnection = handleConnection;
	}

    protected void handleConnection(Connection conn) throws SQLException {
        if (commit && (!conn.isClosed())) {
    		conn.commit();
    	}
    }

    public void setCommitConnection(Connection conn, boolean commit) {
    	this.commit = commit;
    }
}
