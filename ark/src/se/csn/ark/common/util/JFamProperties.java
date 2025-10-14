package se.csn.ark.common.util;

import se.csn.ark.common.base.caching.CacheReloadException;
import se.csn.ark.common.base.caching.IReloadableCache;
import se.csn.ark.common.base.caching.ObjectKeeper;
import se.csn.ark.common.util.logging.Log;


/** Denna klass returnerar de inställningar som behövs för att köra mot JFam/Edh
 * Denna klass instansieras en gång därefter "cachas" värderna som static, detta
 * för att slippa läsa propertiesfilen varje gång ett anrop sker.
 * OBSERVERA att man vid förändringar i jfam.properties för säkerhets skull måste 
 * se till att ladda om informationen genom att starta om den virtuella maskinen 
 * eller anropa reload-metoden.
 */

/**
 * Properties för koppling mot jfam
 * 
 * @author Daniel Nordkvist
 * @since 20041028
 * @version 1 skapad
 *
 */
public final class JFamProperties implements IReloadableCache {
	//static Logger logger = Logger.getLogger(HamtaJFamInstallningar.class);
	private static Log	log = Log.getInstance(JFamProperties.class);
	private static JFamProperties myself;
	private String famHost;
	private String famPort;
	private String domainController;
	private String famArchive;
	private String famArchiveCache;
	private String user;
	private String pwd;
	private String configfil;

	/**
     * Laddar jfam-properties
	 * @see se.csn.ark.common.base.caching.IReloadableCache#reload()
	 */
	public void reload() throws CacheReloadException {
		// ladda in inställningarna från JFAMfil
		try {
			famHost = Properties.getProperty("jfam", "famHost");
			famPort = Properties.getProperty("jfam", "famPort");
			domainController = Properties.getProperty("jfam", "domainController");
			famArchive = Properties.getProperty("jfam", "famArchive");
			famArchiveCache = Properties.getProperty("jfam", "famArchiveCache");
			user = Properties.getProperty("jfam", "user");
			pwd = Properties.getProperty("jfam", "pwd");
			configfil = Properties.getProperty("jfam", "configfil");
			if (log.isInfoEnabled()) {
				log.info("JFaminställningarna inladdad");			
			}
			if (log.isDebugEnabled()) {
				log.debug(
					"Debugläge - följande Faminställningar laddades in : famhost = "
						+ famHost
						+ " famport = "
						+ famPort
						+ " domainController = "
						+ domainController
						+ " famArchive = "
						+ famArchive
						+ " famArchiveCache = "
						+ famArchiveCache
						+ " user = "
						+ user
						+ " pwd = "
						+ pwd
						+ " configfil = "
						+ configfil);
			}
		} catch (Exception e) {
			log.error(e);
			throw new CacheReloadException(e);

		}
	}

	/**
	 * @throws CacheReloadException gick ej att ladda properties
	 */
	private JFamProperties() throws CacheReloadException {
		reload();
		// För att förhindra att GC:n ska städa bort oss så "registrerar" vi oss 
        // hos singeltonkeepern. Då finns det alltid en referens till detta objekt.
		ObjectKeeper.getInstance().addObject(this);
	}

	/**
	 * Använd denna metod för att erhålla en instans av HamtaJFamInstallningar
     * @return jfam-properties
     * @throws CacheReloadException gick ej att ladda properties
	 */
	public static JFamProperties jfamInstallningar()
		throws CacheReloadException {
		if (myself == null) {
			myself = new JFamProperties();
		}
		return myself;
	}

	/**
	 * Returns the configfil.
	 * @return String
	 */
	public String getConfigfil() {
		return configfil;
	}

	/**
	 * Returns the domainController.
	 * @return String
	 */
	public String getDomainController() {
		return domainController;
	}

	/**
	 * Returns the famArchive.
	 * @return String
	 */
	public String getFamArchive() {
		return famArchive;
	}

	/**
	 * Returns the famHost.
	 * @return String
	 */
	public String getFamHost() {
		return famHost;
	}

	/**
	 * Returns the famPort.
	 * @return String
	 */
	public String getFamPort() {
		return famPort;
	}

	/**
	 * Returns the pwd.
	 * @return String
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * Returns the user.
	 * @return String
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the configfil.
	 * @param filename The configfil to set
	 */
	public void setConfigfil(String filename) {
		this.configfil = filename;
	}

	/**
	 * Sets the domainController.
	 * @param controller The domainController to set
	 */
	public void setDomainController(String controller) {
		this.domainController = controller;
	}

	/**
	 * Sets the famArchive.
	 * @param archive The famArchive to set
	 */
	public void setFamArchive(String archive) {
		this.famArchive = archive;
	}

	/**
	 * Sets the famHost.
	 * @param host The famHost to set
	 */
	public void setFamHost(String host) {
		this.famHost = host;
	}

	/**
	 * Sets the famPort.
	 * @param port The famPort to set
	 */
	public void setFamPort(String port) {
		this.famPort = port;
	}

	/**
	 * Sets the password.
	 * @param password The password to set
	 */
	public void setPwd(String password) {
		this.pwd = password;
	}

	/**
	 * Sets the user.
	 * @param usr The user to set
	 */
	public void setUser(String usr) {
		this.user = usr;
	}

	/**
	 * @return fam-arcive-cache
	 */
	public String getFamArchiveCache() {
		return famArchiveCache;
	}

	/**
	 * @param archiveCache fam-archive-cache
	 */
	public void setFamArchiveCache(String archiveCache) {
		this.famArchiveCache = archiveCache;
	}

}
