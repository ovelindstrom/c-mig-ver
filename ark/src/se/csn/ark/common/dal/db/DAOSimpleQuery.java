/*
 * Created on 2004-dec-12
 *
 */
package se.csn.ark.common.dal.db;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Testar att databasåtkomst fungerar.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041212
 * @version 1 skapad
 *
 */
public class DAOSimpleQuery extends CsnDAODatabaseImpl {
	private static final String TEST_QUERY = "select count(*) from e569dbt.tpostort";

	/**
	 * @return teststräng från db
	 * @throws DatabaseException misslyckat skapande av db-koppling
	 * @throws SQLException misslyckat db-anrop
	 */
	public String getTestResult() throws DatabaseException, SQLException {
		StringBuffer strBuf = new StringBuffer();

		ResultSet res = executeQuery(DATA_SOURCE1, TEST_QUERY);

		res.next();
		strBuf.append("JDBC Datasource Test : " + res.getString(1)
		              + " rader i tabell TPOSTORT");
		close();

		return strBuf.toString();
	}
}