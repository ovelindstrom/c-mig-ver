/*
 * Created on 2004-dec-27
 *
 */
package se.csn.ark.common.dal.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.sql.DataSource;

import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.Log;


/**
 * Levererar databasförbindelser.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041227
 * @version 1 skapad
 *
 */
public final class DbConnectionFactory implements DataSources {
	private static final String DB_BASE_URL = "jdbc:db2://";
	private static final String DB_PROP_DRIVER = "dao.db.driver";
	private static final String DB_PROP_SERVER = "dao.db.server";
	private static final String DB_PROP_PORT = "dao.db.port";
	private static final String DB_PROP_DATABASE = "dao.db.database";
	private static final String DB_PROP_SCHEMA = "dao.db.schema";
	private static final String DB_PROP_FUNCTIONPATH = "dao.db.functionPath";
	private static final String DB_PROP_USER = "dao.db.user";
	private static final String DB_PROP_PASSWORD = "dao.db.password";
	private static Connection standAloneCon = null;
	private static Context initialContext;
	private static Log log = Log.getInstance(DbConnectionFactory.class);

	/**
	 * Privat konstruktor, endast statisk åtkomst
	 */
	private DbConnectionFactory() {
	}




	/**
	 * Ger en databaskoppling för åtkomst från Webb enligt angiven typ.
	 *
	 * @param dsType Typ enligt DataSources.
	 * @return En databasförbindelse enligt angiven typ.
	 * @throws DatabaseException Om förbindelse ej kan skapas.
	 * @see DataSources
	 */
	public static synchronized Connection getConnection(int dsType)
	                                             throws DatabaseException {
		Connection con = null;

		// Körs det en testklass dvs har det skapats en standalone connection
		// så ska den användas och då behöver inte fråga WAS om en datakälla.
		if (standAloneCon != null) {
			try {
				if (standAloneCon.isClosed()) {
					createStandAloneConnection();
				}
			} catch (SQLException se) {
				throw new DatabaseException("Kan ej använda standAloneCon", se);
			}

			return standAloneCon;
		}

		try {
			if (initialContext == null) {
				initialContext = new javax.naming.InitialContext();
			}

			switch (dsType) {
			case DATA_SOURCE1:
				con = getConnection(DATA_SOURCE1_KEY);

				break;

			case DATA_SOURCE2:
				con = getConnection(DATA_SOURCE2_KEY);

				break;

			case DATA_SOURCE3:
				con = getConnection(DATA_SOURCE3_KEY);

				break;

			case DATA_SOURCE4:
				con = getConnection(DATA_SOURCE4_KEY);

				break;

			case DATA_SOURCE5:
				con = getConnection(DATA_SOURCE5_KEY);

				break;

			default:
				throw new DatabaseException("Okänd typ för datakälla, dsType="
				                            + dsType);
			}

        // Om det är ett DatabaseException så kommer det från getConnection(String dsKey)
        // och är redan loggat så släng bara vidare.
		} catch (DatabaseException de) {
			throw de;

        // Här fångar vi bara Exception eftersom om det går snett här så är
        // det kört, dvs ingen idé att särskilja vad som inte funkar.
		} catch (Exception e) {
			String errMsg = "Kan ej skapa databasförbindelse";

			log.fatal(errMsg, e);
			throw new DatabaseException(errMsg, e);
		}

		return con;
	}




	/**
	 * @param dsKey nyckel för att välja datakälla
	 * @return koppling till databas
	 * @throws DatabaseException gick ej att skapa koppling
	 */
	private static Connection getConnection(String dsKey)
	                                 throws DatabaseException {
		Connection con = null;
		DataSource dataSource;
		String dsAlias = null;
		String dsValue = null;

		try {
			dsValue = Properties.getProperty(dsKey);
			dsAlias = DATA_SOURCE_BASE + dsValue;
			dataSource = (DataSource)initialContext.lookup(dsAlias);
			con = dataSource.getConnection();
			/* Test Kod
			System.err.println(con.getMetaData().getUserName());
			ResultSet resultSet = con.getMetaData().getProcedures(null, null, "%");
			if(con.getMetaData().getUserName().equalsIgnoreCase("vmtesrv01\\alfa")) {
			    //Kör proceduren
			    //OA040SP
			    //System.err
			    
			    ResultSet rs = con.createStatement().executeQuery("SELECT ORT FROM TORGANISATION");
			    while(rs.next()) {
			        System.err.println(rs.getObject(1));
			    }
			        
			    
			    
			    
			   	CallableStatement cs = con.prepareCall("{call  SUNDLING (?,?,?)}", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			   	cs.setString(1, "VU");     //ccn
	            cs.setDate(2, new java.sql.Date(java.util.Calendar.getInstance().getTimeInMillis()));  //sid
	            cs.registerOutParameter(3, java.sql.Types.INTEGER);
	            rs = cs.executeQuery();
			    
			    
			    
			   	cs = con.prepareCall("{call  CSNDBA.OA040SP (?, ? , ?)}", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			   	cs.setString(1, "VU");     //ccn
	            cs.setDate(2, new java.sql.Date(java.util.Calendar.getInstance().getTimeInMillis()));  //sid
	            cs.registerOutParameter(3, java.sql.Types.INTEGER);
	            rs = cs.executeQuery();
			}
			*/
		    

			
			

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

		return con;
	}




	/**
	 * Ger en databaskoppling för åtkomst från EJB enligt angiven typ.
	 *
	 * @param dsType Typ enligt DataSources.
	 * @return En databasförbindelse enligt angiven typ.
	 * @throws DatabaseException Om förbindelse ej kan skapas.
	 * @see DataSources
	 */
	public static synchronized Connection getConnectionForEjb(int dsType)
		throws DatabaseException {
		Connection con = null;

		// Körs det en testklass dvs har det skapats en standalone connection
		// så ska den användas och då behöver inte fråga WAS om en datakälla.
		if (standAloneCon != null) {
			return standAloneCon;
		}

		try {
			if (initialContext == null) {
				initialContext = new javax.naming.InitialContext();
			}

			switch (dsType) {
			case DATA_SOURCE1:
				con = getConnectionForEjb(EJB_DATA_SOURCE1_KEY);

				break;

			case DATA_SOURCE2:
				con = getConnectionForEjb(EJB_DATA_SOURCE2_KEY);

				break;

			case DATA_SOURCE3:
				con = getConnectionForEjb(EJB_DATA_SOURCE3_KEY);

				break;

			case DATA_SOURCE4:
				con = getConnectionForEjb(EJB_DATA_SOURCE4_KEY);

				break;

			case DATA_SOURCE5:
				con = getConnectionForEjb(EJB_DATA_SOURCE5_KEY);

				break;

			default:
				throw new DatabaseException("Okänd typ för datakälla, dsType="
				                            + dsType);
			}

        // Om det är ett DatabaseException så kommer det från getConnection(String dsKey)
        // och är redan loggat så släng bara vidare.
		} catch (DatabaseException de) {
			throw de;

        // Här fångar vi bara Exception eftersom om det går snett här så är
        // det kört, dvs ingen idé att särskilja vad som inte funkar.
		} catch (Exception e) {
			String errMsg = "Kan ej skapa databasförbindelse";

			log.fatal(errMsg, e);
			throw new DatabaseException(errMsg, e);
		}

		return con;
	}




	/**
	 * @param dsKey nyckel som identifierar datakällan
	 * @return koppling till datakällan
	 * @throws DatabaseException om koppling ej gick att skapa
	 */
	private static Connection getConnectionForEjb(String dsKey)
	                                       throws DatabaseException {
		Connection con = null;
		DataSource dataSource;
		String dsValue = null;

		try {
			dsValue = Properties.getProperty(dsKey);
			dataSource = (DataSource)initialContext.lookup(dsValue);
			con = dataSource.getConnection();

        // Här fångar vi bara Exception eftersom om det går snett här så är
        // det kört, dvs ingen idé att särskilja vad som inte funkar.
		} catch (Exception e) {
			String errMsg = "Kan ej skapa databasförbindelse";

			if (dsValue != null) {
				errMsg += (" för datakälla. Key=" + dsKey + ", property value="
				+ dsValue);
			}

			log.fatal(errMsg, e);
			throw new DatabaseException(errMsg, e);
		}

		return con;
	}




	/**
	 * Skapar en fristående koppling utan inblandning av WAS för att kunna
	 * köra JUnit testfall.
	 *
	 * @throws DatabaseException koppling gick ej att skapa
	 */
	public static void createStandAloneConnection() throws DatabaseException {
		try {
			Class.forName(Properties.getProperty(DB_PROP_DRIVER));

			String url =
				DB_BASE_URL + Properties.getProperty(DB_PROP_SERVER) + ":"
				+ Properties.getProperty(DB_PROP_PORT) + "/"
				+ Properties.getProperty(DB_PROP_DATABASE);

			if (log.isDebugEnabled()) {
				log.debug("createStandAloneConnection for url=" + url);
			}
			
			java.util.Properties connProperties = new java.util.Properties();
			connProperties.put("user", Properties.getProperty(DB_PROP_USER));
			connProperties.put("password", Properties.getProperty(DB_PROP_PASSWORD));
			String schema = Properties.getProperty(DB_PROP_SCHEMA);
			if (schema != null && schema.length() > 0) {
				connProperties.put("currentSchema", schema);
			}
			String functionPath = Properties.getProperty(DB_PROP_FUNCTIONPATH);
			if (functionPath != null && functionPath.length() > 0) {
				connProperties.put("currentFunctionPath", functionPath);
			}
			
			standAloneCon =
				DriverManager.getConnection(
				                            url,
				                            connProperties);
			
		} catch (SQLException se) {
			throw new DatabaseException(
			                            "Kan ej skapa fristående databaskoppling",
			                            se);
		} catch (ClassNotFoundException cnfe) {
			throw new DatabaseException(
			                            "Kan ej skapa fristående databaskoppling",
			                            cnfe);
		}
	}
}