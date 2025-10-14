/*
 * Created on 2004-dec-12
 *
 */
package se.csn.ark.common.dal.db;

import se.csn.ark.common.dal.CsnDataAccessObjectImpl;
import se.csn.ark.common.util.logging.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Basklass för databasåtkomst.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041212
 * @version 1 skapad
 *
 */
public class CsnDAODatabaseImpl extends CsnDataAccessObjectImpl
	implements CsnDAODatabase, DataSources {
	private static Log log = Log.getInstance(CsnDAODatabaseImpl.class);
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	/**
	 * Hämtar databasanslutning till standard datakällan via JNDI .
     * @param dsType Typ enligt DataSources.
     * @return koppling till datakällan
     * @throws DatabaseException gick ej att skapa koppling
	 */
	protected Connection getConnection(int dsType) throws DatabaseException {
		connection = DbConnectionFactory.getConnection(dsType);

		return connection;
	}




	/**
	 * Kör en SQL fråga och levererar ett resultset.
	 *
	 * @param dsType Typ enligt DataSources.
	 * @param sqlQuery SQL sträng.
	 * @return Ett resultset om allt gick bra.
	 * @throws DatabaseException Om det inte gick vägen.
	 * @see DataSources
	 */
	public ResultSet executeQuery(int dsType, String sqlQuery)
	                       throws DatabaseException {
		try {
			Connection con = getConnection(dsType);

			statement = con.createStatement();
			resultSet = statement.executeQuery(sqlQuery);

			return resultSet;
		} catch (SQLException se) {
			log.fatal(se);
			throw new DatabaseException("Fel vi exekvering av SQL", se);
		}
	}




	/**
	 * Stänger statement, resultset och connection.
	 */
	public void close() {
		try {
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException se) {
			// Kasta vidare exception?
			log.error(se);
		}
	}
}