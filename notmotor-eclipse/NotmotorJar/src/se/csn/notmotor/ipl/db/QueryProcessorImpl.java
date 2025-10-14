package se.csn.notmotor.ipl.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import se.csn.ark.common.dal.db.DatabaseException;
import se.csn.ark.common.util.logging.Log;

/**
 * QueryProcessor som använder en enda Connection. 
 * Denna connection lagras i en statisk medlemsvariabel.
 * OBS! Kan leda till udda beteende om flera webappar delar klassladdare.
 */
public class QueryProcessorImpl extends QueryProcessorBase implements QueryProcessor {
	
	private DataSource ds;
	private Connection conn;
	private Log log = Log.getInstance(QueryProcessorImpl.class);
	private int transactionIsolationLevel;
	
	public QueryProcessorImpl(DataSource ds) {
		this(ds, Connection.TRANSACTION_REPEATABLE_READ);
	}

	/**
	 * @param ds Datasource som ska användas för SQL-slagningarna.
	 * @param transactionIsolationLevel Anger den isolationsnivå som kommer att sättas
	 *        på nya Connection-objekt när de skapas.
	 */
	public QueryProcessorImpl(DataSource ds, int transactionIsolationLevel) {
	    this.ds = ds;
	    switch(transactionIsolationLevel) {
	    	case Connection.TRANSACTION_READ_COMMITTED:
	    	case Connection.TRANSACTION_READ_UNCOMMITTED:
	    	case Connection.TRANSACTION_REPEATABLE_READ:
	    	case Connection.TRANSACTION_SERIALIZABLE:
	    	    this.transactionIsolationLevel = transactionIsolationLevel;
	    		break;
	    	default: throw new IllegalArgumentException("Otillåten isolationsnivå: " + transactionIsolationLevel);
	    }
	}
	
    public Connection getConnection() {
       if(conn != null) {
           return conn;
       }
       try {
           Connection c = ds.getConnection();
           c.setTransactionIsolation(transactionIsolationLevel);
           return c;
       } catch (SQLException e) {
           log.fatal("Kunde inte skapa ny connection", e);
           throw new DatabaseException("Kunde inte skapa ny connection", e);
       }
    }
    
    public void setConnection(Connection conn, boolean handleConnection) {
        this.conn = conn;
        this.handleConnection = handleConnection;
    }
    
    protected void handleConnection(Connection c) throws SQLException {
        if((c != null) && !c.isClosed()) {
            c.commit();
        	c.close();
        }
    }
    

}
