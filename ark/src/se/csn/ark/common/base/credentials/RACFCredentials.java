package se.csn.ark.common.base.credentials;

import se.csn.ark.common.base.caching.CacheReloadException;
import se.csn.ark.common.base.caching.IReloadableCache;
import se.csn.ark.common.base.caching.ObjectKeeper;
import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.Log;


/**
 * Denna klass returnerar det användarid och lösenord som skall användas vid autentisering 
 * mot stordator/RACF, exempelvis mha Advantage:Gen's java-proxies.
 * 
 * I praktiken hämtar denna klass upp själva användarid och lösenord från propertiesfilen 
 * racf.properties som skall ligga i classpath.
 * 
 * Denna klass följer Singleton-mönster för att den om möjligt endast instansieras en enda gång.
 * Detta för att i praktiken cacha användarid och lösenord för att slippa läsa propertiesfil 
 * varje gång ett anrop sker.
 * 
 * Observera att man vid förändringar i racf.properties för säkerhets skull måste tillse att 
 * ladda om informationen genom att starta om den virtuella maskinen eller anropa reload-metoden.
 * 
 * @author Vincent Wong, Iocore
 * @since 2002-08-27; v1.0
 * @version 1.0
 */
public final class RACFCredentials implements ICredentials, IReloadableCache {

	private static Log	log = Log.getInstance(RACFCredentials.class);
	private static RACFCredentials myself;
//	private static Properties properties;
	
	private String password;
	private String userid;

	/**
	 * @see IReloadableCache#reload()
	 */
	public void reload() throws CacheReloadException {
		
		// Ladda in properties från properties-fil:
		try {
//			properties = new Properties();
//			File propsFile= new File(this.getClass().getClassLoader().
//              getResource("racf.properties").getPath());
//			if (propsFile == null) {
//				throw new CacheReloadException(new FileNotFoundException(), 
//                  "Could not find the file racf.properties.");
//			}
//			FileInputStream fis = new FileInputStream(propsFile);
//			properties.load(fis);
//			log.info("RACFCredentials loaded its cache.");
//			
//			password = properties.getProperty("password");
//			userid = properties.getProperty("userid");

			password = Properties.getProperty("racf", "password");			
			userid = Properties.getProperty("racf", "userid");	
			if ((userid != null) && (password != null))	{
				if (log.isInfoEnabled())  {	
					log.info("RACFCredentials cachen laddad.");
				}
			} else {
				throw new CacheReloadException("RACFCredentials cachen kunde ej laddas!");
			}
		} catch (Exception e) {
			throw new CacheReloadException(e);
		}
	}
	
    /**
     * Skapa RACFCredentials. Privat, använd getInstance istället 
     * @throws CacheReloadException gick ej att ladda properties
     */
	private RACFCredentials() throws CacheReloadException {
		
		reload();
		// "Registrera" dig hos SingeltonKeepern så att det alltid finns en referens 
        // till detta object, för att förhindra GC:n att städa bort dig.
		ObjectKeeper.getInstance().addObject(this);
	}

	/**
	 * Använd denna metod för att erhålla en instans av RACFCredentials.
     * @return singelton-instansen
     * @throws CacheReloadException gick ej att ladda properties
	 */
	protected static RACFCredentials getInstance() throws CacheReloadException {
		if (myself == null) {
			myself = new RACFCredentials();
		}
		return myself;
	}
	
	/**
	 * @see ICredentials#getUserid()
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @see ICredentials#getPassword()
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Overrides toString() i java.lang.Object för att göra en mer lättläst utskrift,
	 * dock med lösenordet "gömt".
     * @return info-sträng
	 */
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(super.toString());
		stringBuffer.append(": ");
		stringBuffer.append(this.getClass().getName());
		stringBuffer.append(": Userid=");
		stringBuffer.append(this.getUserid());
		stringBuffer.append("/Password=********");
		return  stringBuffer.toString();
	}

}