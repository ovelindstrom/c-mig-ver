/**
 * Skapad 2007-mar-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.common.db;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import se.csn.ark.common.dal.db.DataSources;
import se.csn.ark.common.dal.db.DatabaseException;
import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.Log;

/**
 * Levererar databasfactories.
 */
public class DataSourceFactory implements DataSources {

	private static final String DB_BASE_URL = "jdbc:db2://";
	private static final String DB_PROP_DRIVER = "dao.db.driver";
	private static final String DB_PROP_SERVER = "dao.db.server";
	private static final String DB_PROP_PORT = "dao.db.port";
	private static final String DB_PROP_DATABASE = "dao.db.database";
	private static final String DB_PROP_USER = "dao.db.user";
	private static final String DB_PROP_PASSWORD = "dao.db.password";
	private static Log log = Log.getInstance(DataSourceFactory.class);
	private static Context initialContext = null;

	/**
	 * Privat konstruktor, endast statisk åtkomst
	 */
	private DataSourceFactory() {
	}




	/**
	 * Ger en databaskoppling för åtkomst från Webb enligt angiven typ.
	 *
	 * @param dsType Typ enligt DataSources.
	 * @return En databasförbindelse enligt angiven typ.
	 * @throws DatabaseException Om förbindelse ej kan skapas.
	 * @see DataSources
	 */
	public static synchronized DataSource getDataSource(int dsType) throws DatabaseException {
		switch (dsType) {
		case DATA_SOURCE1:
			return getDataSource(DATA_SOURCE1_KEY);
		case DATA_SOURCE2:
			return getDataSource(DATA_SOURCE2_KEY);
		case DATA_SOURCE3:
		    return getDataSource(DATA_SOURCE3_KEY);
		case DATA_SOURCE4:
		    return getDataSource(DATA_SOURCE4_KEY);
		case DATA_SOURCE5:
		    return getDataSource(DATA_SOURCE5_KEY);
		default:
			throw new DatabaseException("Okänd typ för datakälla, dsType=" + dsType);
		}
	}




	/**
	 * @param dsKey nyckel för att välja datakälla
	 * @return koppling till databas
	 * @throws DatabaseException gick ej att skapa koppling
	 */
	private static synchronized DataSource getDataSource(String dsKey)
	                                 throws DatabaseException {
		DataSource dataSource = null;
		String dsAlias = null;
		String dsValue = null;

		if (initialContext == null) {
			try {
                initialContext = new javax.naming.InitialContext();
            } catch (NamingException e) {
                log.fatal("Kunde inte skapa InitialContext, körs koden i appservern?", e);
                throw new IllegalStateException("Kunde inte skapa InitialContext, körs koden i appservern?" + e);
            }
		}

		try {
			dsValue = Properties.getProperty(dsKey);
			dsAlias = DATA_SOURCE_BASE + dsValue;
			return (DataSource)initialContext.lookup(dsAlias);

        // Här fångar vi bara Exception eftersom om det går snett här så är
        // det kört, dvs ingen idé att särskilja vad som inte funkar.
		} catch (Exception e) {
			String errMsg = "Kan ej skapa databasförbindelse";

			if (dsAlias != null) {
				errMsg += (" för datakälla " + dsAlias + ". Key=" + dsKey
				+ ", property value=" + dsValue);
			}

			log.fatal(errMsg, e);
			throw new DatabaseException(errMsg, e);
		}
    }
}
