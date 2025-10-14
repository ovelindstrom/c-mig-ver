//package se.csn.ark.common.dal;
//
//import com.frontec.fam.FamConnection;
//import com.frontec.fam.FamConnectionPool;
//import com.frontec.fam.FamEnvironment;
//
//import com.tietoenator.pcs.coor.Config;
//
//import se.csn.ark.common.util.JFamProperties;
//import se.csn.ark.common.util.logging.Log;
//
//
///**
// * Generell bas för edh/fam-koppling
// * 
// * @author Magnus Storsjö
// * @since 20041028
// * @version 1 skapad
// */
//public class CsnDAOEdhImpl extends CsnDataAccessObjectImpl implements CsnDAOEdh {
//	// Hämta loggern för klassen
//	private static Log log = Log.getInstance(CsnDAOEdhImpl.class);
//
//	//static Logger logger = Logger.getLogger(EDHKoppling.class);
//	// Deklarerar EDH-Fam connection variabler. 
//	private String famHost;
//	private String famPort;
//	private String domainController;
//	private String famArchive;
//	private String famArchiveCache;
//	private String user;
//	private String pwd;
//	private String configfil;
//
//	/**
//     * Skapa instans
//	 * @throws EdhException gick ej att läsa inställningar
//	 */
//	public CsnDAOEdhImpl() throws EdhException {
//		try {
//			JFamProperties installningar = JFamProperties.jfamInstallningar();
//
//			this.famHost = installningar.getFamHost();
//			this.famPort = installningar.getFamPort();
//			this.domainController = installningar.getDomainController();
//			this.famArchive = installningar.getFamArchive();
//			this.famArchiveCache = installningar.getFamArchiveCache();
//			this.user = installningar.getUser();
//			this.pwd = installningar.getPwd();
//			this.configfil = installningar.getConfigfil();
//		} catch (Exception e) {
//			log.error("Det gick inte att hämta Faminställningarna. Följande fel mottogs : "
//			          + e);
//			throw new EdhException("Fel från EDHKoppling.skapaKoppling -- Faminställningarna "//                                   + "gick inte att skapa. Följande meddelande mottogs : "
//			                       + e);
//		}
//	}
//
//	/**
//	 * @param sCache != -> läs från cache-arkiv
//	 * @return en koppling mot Fam
//	 * @throws EdhException gick ej att skapa fam-koppling
//	 */
//	public FamConnection skapaKoppling(String sCache) throws EdhException {
//		//Här skapas en Famkoppling utifrån de inlästa värden som finns på JFam.properties
//		FamConnection famConn = null;
//
//		try {
//			if (log.isDebugEnabled()) {
//				log.debug("Debugläge - Nu instansieras Fammiljön. Configfil =  "
//				          + this.configfil);
//			}
//
//			//*
//			// Initialize configuration
//			FamEnvironment env = new FamEnvironment(this.configfil);
//			Config config = FamEnvironment.getConfig();
//			String debugconfig = config.toString();
//
//			if (log.isDebugEnabled()) {
//				log.debug("Debugläge - värden från config :" + debugconfig);
//			}
//
//			/**
//			 * Väljer om vi ska läsa från cache arkivet eller inte
//			 *
//			 */
//			String sFamArchive = null;
//
//			if (sCache != null) {
//				sFamArchive = this.famArchiveCache;
//			} else {
//				sFamArchive = this.famArchive;
//			}
//
//			String famURI =
//				"fam://" + this.user + ":" + this.pwd + "@" + this.famHost
//				+ ":" + this.famPort + "/" + sFamArchive;
//
//			if (log.isDebugEnabled()) {
//				log.debug("Debugläge - famuri = " + famURI);
//				log.debug("Debugläge - getConnection - pool");
//			}
//
//			famConn = FamConnectionPool.getConnection(famURI);
//
//			if (log.isDebugEnabled()) {
//				log.debug("Debugläge - getConnection genomförd");
//			}
//
//			return famConn;
//		} catch (Exception e) {
//			log.error("FamKoppling gick inte att skapa. Följande fel mottogs : "
//			          + e);
//			throw new EdhException("Fel från EDHKoppling.skapaKoppling -- FamKoppling gick "//                                   + "inte att skapa. Meddelande från Fam : "
//			                       + e);
//		}
//	}
//}