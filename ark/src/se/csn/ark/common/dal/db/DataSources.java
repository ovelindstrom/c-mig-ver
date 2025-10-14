/*
 * Created on 2004-dec-27
 *
*/
package se.csn.ark.common.dal.db;

/**
 * Konstanter för databaskopplingar.
 * Det verkliga namnen finns i ark.properties
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041227
 * @version 1 skapad
 *
 */
public interface DataSources {

	static final String DATA_SOURCE_BASE = "java:comp/env/";
	
	static final String DATA_SOURCE_INTERNAL_ALIAS_KEY1 = "dao.db.ds.internal1"; 

	static final String DATA_SOURCE1_KEY = "dao.db.ds1"; 

	static final String DATA_SOURCE2_KEY = "dao.db.ds2"; 

	static final String DATA_SOURCE3_KEY = "dao.db.ds3"; 

	static final String DATA_SOURCE4_KEY = "dao.db.ds4"; 

	static final String DATA_SOURCE5_KEY = "dao.db.ds5"; 

	static final String EJB_DATA_SOURCE1_KEY = "dao.db.ejb.ds1"; 

	static final String EJB_DATA_SOURCE2_KEY = "dao.db.ejb.ds2"; 

	static final String EJB_DATA_SOURCE3_KEY = "dao.db.ejb.ds3"; 

	static final String EJB_DATA_SOURCE4_KEY = "dao.db.ejb.ds4"; 

	static final String EJB_DATA_SOURCE5_KEY = "dao.db.ejb.ds5"; 

	/**
	 * Datakälla 1 för databas.
	 * 
	 * Konfigureras med property <b>dao.db.ds1</b> för webb
	 * och med <b>dao.db.ejb.ds1</b> för ejb.
	 */
	public static final int DATA_SOURCE1 = 0;

	/**
	 * Datakälla 2 för databas.
	 * 
	 * Konfigureras med property <b>dao.db.ds2</b>. för webb
	 * och med <b>dao.db.ejb.ds2</b> för ejb.
	 */
	public static final int DATA_SOURCE2 = 1;

	/**
	 * Datakälla 3 för databas.
	 * 
	 * Konfigureras med property <b>dao.db.ds3</b> för webb
	 * och med <b>dao.db.ejb.ds3</b> för ejb.
	 */
	public static final int DATA_SOURCE3 = 2;

	/**
	 * Datakälla 4 för databas.
	 * 
	 * Konfigureras med property <b>dao.db.ds4</b> för webb
	 * och med <b>dao.db.ejb.ds4</b> för ejb.
	 */

	public static final int DATA_SOURCE4 = 3;

	/**
	 * Datakälla 5 för databas.
	 * 
	 * Konfigureras med property <b>dao.db.ds5</b> för webb
	 * och med <b>dao.db.ejb.ds5</b> för ejb.
	 */
	public static final int DATA_SOURCE5 = 4;

}
