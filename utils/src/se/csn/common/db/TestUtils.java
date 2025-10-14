/**
 * Skapad 2007-apr-11
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.db;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import se.csn.common.config.ClassDependencyTester;
import se.csn.common.config.CommunicationTester;


public class TestUtils {

	private static final String DB_BASE_URL = "jdbc:db2://";

	/**
	 * @return En BasicDataSource, dvs. en wrapper runt en JDBC-driver. ENDAST AVSEDD FÖR TESTBRUK! 
	 */
    public static DataSource getTestDB2DataSource(String host, int port, String database, String user, String password) {
        // Commons-dbcp kräver commons-pool:
        ClassDependencyTester.findClassesThrowException(new String[][]{ClassDependencyTester.COMMONS_POOL});
        
        if(!CommunicationTester.isPortOpen(host, port)) {
            throw new IllegalStateException("Kunde inte ansluta till " + host + ":" + port);
        }
        String url = DB_BASE_URL + host + ":" + port + "/" + database;
        
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setUrl(url);
        return ds;
    }
}
