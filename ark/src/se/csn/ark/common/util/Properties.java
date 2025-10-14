package se.csn.ark.common.util;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import se.csn.ark.common.util.logging.Log;


/**
 * Läser in inställningar/egenskaper från filer med nyckel=värde format.
 * Filerna skall ha extension <i>.properties</i>
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20040916
 * @version 1 skapad
 *
 */
public final class Properties {
	/**
	 * Grundinställningar för CSN:s arkitektur ramverk.
	 */
	public static final String ARK = "ark";
	private static java.util.Properties properties =
		new java.util.Properties();
    private static Vector<String> propertyFiles = new Vector<String>();
	private static Log log = Log.getInstance(Properties.class);

	// Vi initierar alltid grundinställningarna.
	static {
		init(ARK);
	}



    /**
     * Privat konstruktor, endast statisk åtkomst.
     */
    private Properties() {
    }




	/**
	 * Hämtar en egenskap från arkitektur grundinställningarna.
	 *
	 * @param key Nyckel för det värde som efterfrågas.
	 * @return En sträng med värdet om det fanns annars null.
	 */
	public static String getProperty(String key) {
	    String var = getProperty(ARK, key);
	    log.debug(key + ":" + var);
		return var;
	}


	/**
	 * Hämtar en egenskap från arkitektur grundinställningarna utan att skriva till errorlog
	 * om propertyn inte finns.
	 *
	 * @param key Nyckel för det värde som efterfrågas.
	 * @return En sträng med värdet om det fanns annars null.
	 */
	public static String getPropertyNoError(String key) {
	    String var = getPropertyNoError(ARK, key);
	    log.debug(key + ":" + var);
		return var;
	}


	/**
	 * Hämtar en egenskap från arkitektur grundinställningarna.
	 *
	 * @param key Nyckel för det värde som efterfrågas.
	 * @param defaultValue Standardvärde som returneras om nyckel inte finns eller inte kan tolkas.
	 * @return En int med värdet om det fanns i filen annrs redturneras <b>default</b>.
	 */
	public static int getIntProperty(String key, int defaultValue) {
		return getIntProperty(ARK, key, defaultValue);
	}




	/**
	 * Hämtar en egenskap från arkitektur grundinställningarna.
	 *
	 * @param key Nyckel för det värde som efterfrågas.
	 * @return En int med värdet om det fanns i filen annrs redturneras <b>default</b>.
	 * @throws PropertyException Om värdet ej finns eller om det inte kan tolkas till <b>int</b>
	 */
	public static int getIntProperty(String key) throws PropertyException {
		return getIntProperty(ARK, key);
	}




	/**
	 * Hämtar en egenskap från arkitektur grundinställningarna.
	 *
	 * @param key Nyckel för det värde som efterfrågas.
	 * @param defaultValue Standardvärde som returneras om nyckel inte finns eller inte kan tolkas.
	 * @return En boolean med värdet om det fanns i filen annrs returneras <b>default</b>.
	 */
	public static boolean getBooleanProperty(String key, boolean defaultValue) {
		return getBooleanProperty(ARK, key, defaultValue);
	}




	/**
	 * Hämtar en egenskap från arkitektur grundinställningarna.
	 *
	 * @param key Nyckel för det värde som efterfrågas.
	 * @return En int med värdet om det fanns i filen annars returneras <b>default</b>.
	 * @throws PropertyException Om värdet ej finns eller om det inte kan tolkas till <b>int</b>
	 */
	public static boolean getBooleanProperty(String key)
	                                  throws PropertyException {
		return getBooleanProperty(ARK, key);
	}




	/**
	 * Hämtar en egenskap från den aktuella egenskapsfilen.
	 *
	 * @param propertyFile Fil med egenskaper man namn enligt <i>propertyFile</i>.properties
	 * @param key Nyckel för det värde som efterfrågas.
	 * @param defaultValue Standardvärde som returneras om nyckel inte finns eller inte kan tolkas.
	 * @return En int med värdet om det fanns i filen annars returneras <b>default</b>.
	 */
	public static int getIntProperty(
	                                 String propertyFile,
	                                 String key,
	                                 int defaultValue) {
		int intValue = defaultValue;

		try {
			intValue = getIntProperty(propertyFile, key);
		} catch (PropertyException pe) {
			// Vi har ju ett standardvärde att returnera så vi sväljer bara det här undantaget.
		}

		return intValue;
	}

	/**
	 * Hämtar en egenskap från den aktuella egenskapsfilen.
	 *
	 * @param propertyFile Fil med egenskaper man namn enligt <i>propertyFile</i>.properties
	 * @param key Nyckel för det värde som efterfrågas.
	 * @return En int med värdet om det fanns i filen.
	 * @throws PropertyException Om värdet ej finns eller om det inte kan tolkas till <b>int</b>
	 */
	public static int getIntProperty(String propertyFile, String key)
	                          throws PropertyException {
		String strValue;
		int intValue;

		try {
			strValue = getProperty(propertyFile, key);
			intValue = Integer.parseInt(strValue);
		} catch (Exception e) {
			throw new PropertyException("getIntProperty", e);
		}

		return intValue;
	}

	/**
	 * Hämtar en egenskap från arkitektur grundinställningarna.
	 *
	 * @param key Nyckel för det värde som efterfrågas.
	 * @return En long med värdet om det fanns i filen annars returneras <b>default</b>.
	 * @throws PropertyException Om värdet ej finns eller om det inte kan tolkas till <b>long</b>
	 */
	public static long getLongProperty(String key) throws PropertyException {
		return getLongProperty(ARK, key);
	}

	/**
	 * Hämtar en egenskap från den aktuella egenskapsfilen.
	 *
	 * @param propertyFile Fil med egenskaper man namn enligt <i>propertyFile</i>.properties
	 * @param key Nyckel för det värde som efterfrågas.
	 * @return En long med värdet om det fanns i filen.
	 * @throws PropertyException Om värdet ej finns eller om det inte kan tolkas till <b>long</b>
	 */
	public static long getLongProperty(String propertyFile, String key)
							  throws PropertyException {
		String strValue;
		long longValue;

		try {
			strValue = getProperty(propertyFile, key);
			longValue = Long.parseLong(strValue);
		} catch (Exception e) {
			throw new PropertyException("getLongProperty", e);
		}
		return longValue;
	}

	/**
	 * Hämtar en egenskap från den aktuella egenskapsfilen.
	 *
	 * @param propertyFile Fil med egenskaper man namn enligt <i>propertyFile</i>.properties
	 * @param key Nyckel för det värde som efterfrågas.
	 * @param defaultValue Standardvärde som returneras om nyckel inte finns eller inte kan tolkas.
	 * @return En long med värdet om det fanns i filen annars returneras <b>default</b>.
	 */
	public static long getLongProperty(
									 String propertyFile,
									 String key,
									 long defaultValue) {
		long longValue = defaultValue;

		try {
			longValue = getIntProperty(propertyFile, key);
		} catch (PropertyException pe) {
			// Vi har ju ett standardvärde att returnera så vi sväljer bara det här undantaget.
		}
		return longValue;
	}

	/**
	 * Hämtar en egenskap från den aktuella egenskapsfilen.
	 *
	 * @param propertyFile Fil med egenskaper man namn enligt <i>propertyFile</i>.properties
	 * @param key Nyckel för det värde som efterfrågas.
	 * @param defaultValue Standardvärde som returneras om nyckel inte finns eller inte kan tolkas.
	 * @return En boolean med värdet om det fanns i filen annars returneras <b>default</b>.
	 */
	public static boolean getBooleanProperty(
	                                         String propertyFile,
	                                         String key,
	                                         boolean defaultValue) {
		boolean boolValue = defaultValue;

		try {
			boolValue = getBooleanProperty(propertyFile, key);
		} catch (PropertyException pe) {
			// Vi har ju ett standardvärde att returnera så vi sväljer bara det här undantaget.
		}

		return boolValue;
	}




	/**
	 * Hämtar en egenskap från den aktuella egenskapsfilen.
	 *
	 * @param propertyFile Fil med egenskaper man namn enligt <i>propertyFile</i>.properties
	 * @param key Nyckel för det värde som efterfrågas.
	 * @return En boolean med värdet om det fanns i filen.
	 * @throws PropertyException Om värdet ej finns eller om det inte kan tolkas till <b>int</b>
	 */
	public static boolean getBooleanProperty(String propertyFile, String key)
	                                  throws PropertyException {
		String strValue;
		boolean booleanValue;

		try {
			strValue = getProperty(propertyFile, key);

			// Om det inte är null och det innehåller true eller false så är det OK ...
			if (
			    (strValue != null)
			    && ((strValue.toLowerCase().equals("true"))
			    || (strValue.toLowerCase().equals("false")))) {
				booleanValue = (Boolean.valueOf(strValue)).booleanValue();

            // ... annars är det inte OK.
			} else {
				throw new PropertyException("getBooleanProperty Invalid boolean property="
				                            + strValue);
			}
		} catch (Exception e) {
			throw new PropertyException("getBooleanProperty", e);
		}

		return booleanValue;
	}




	/**
	 * Hämtar en egenskap från den aktuella egenskapsfilen.
	 *
	 * @param propertyFile Fil med egenskaper man namn enligt <i>propertyFile</i>.properties
	 * @param key Nyckel för det värde som efterfrågas.
	 * @return En sträng med värdet om det fanns i filen annars null.
	 */
	public static String getProperty(String propertyFile, String key) {
		// Har vi läst in den här property filen?
		if (!propertyFiles.contains(propertyFile)) {
			// Om inte så läser vi in den först innan vi försöker hämta property.
			init(propertyFile);
		}
		String value = properties.getProperty(key);
		if (value == null) {
		    log.error("Missing properties value for properties: " + key);
		}

		return value;
	}


	/**
	 * Hämtar en egenskap från den aktuella egenskapsfilen utan att skiva till errorlog
	 * om propertyn inte finns.
	 *
	 * @param propertyFile Fil med egenskaper man namn enligt <i>propertyFile</i>.properties
	 * @param key Nyckel för det värde som efterfrågas.
	 * @return En sträng med värdet om det fanns i filen annars null.
	 */
	public static String getPropertyNoError(String propertyFile, String key) {
		// Har vi läst in den här property filen?
		if (!propertyFiles.contains(propertyFile)) {
			// Om inte så läser vi in den först innan vi försöker hämta property.
			init(propertyFile);
		}
		String value = properties.getProperty(key);

		return value;
	}


	/**
     * Hämtar alla properties som har ett givet prefix
	 * @param prefix ska matcha de properties som hämtas
	 * @return properties
	 */
	public static java.util.Properties getProperties(String prefix) {
		java.util.Properties propsWithPrefix = new java.util.Properties();
		Enumeration keys = properties.keys();

		while (keys.hasMoreElements()) {
			String name = (String) keys.nextElement();

			if (name.startsWith(prefix)) {
				propsWithPrefix.put(
				                    name.substring(prefix.length()),
				                    properties.get(name));
			}
		}

		return propsWithPrefix;
	}




	/**
	 * Laddar in konfigurationen från den angivna propertyfilen.
	 *
	 * @param propertyFile Namn på filen med inställningar enligt <i>propertyFile</i>.properties
	 */
	public static void init(String propertyFile) {
		try {
			log.debug("\n init propertiefil : " + propertyFile);
			
			java.net.URL u1 = Properties.class.getClassLoader().getResource(propertyFile + ".properties");
			log.debug("Laddar ResourceBundle: " + u1);
			ResourceBundle bundle = ResourceBundle.getBundle(propertyFile);

			

			// Läs alla nycklar
			Enumeration keys = bundle.getKeys();
			
			// Lagra alla nycklar och värden.
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = bundle.getString(key);
				properties.put(key, value);
			}

			if (log.isDebugEnabled()) {
				java.net.URL u = Properties.class.getClassLoader().getResource(propertyFile + ".properties");

				log.debug("Laddar ResourceBundle '" + u + "'  [ " + properties + " ]");
			}
			log.debug("Laddar ResourceBundle '" + Properties.class.getClassLoader().getResource(propertyFile 
					+ ".properties") + "'  [ " + properties + " ]");
			java.net.URL u =
				Properties.class.getClassLoader().getResource(propertyFile
															  + ".properties");
			log.debug("Laddar ResourceBundle '" + u + "'  [ " + properties + " ]");

			// Lagra propertyfilens namn så vi vet om vi laddat den eller ej.
			propertyFiles.add(propertyFile);
		} catch (Exception e) {
			System.err.println("se.csn.ark.common.util.Properties.init()"
			                   + ", kan ej hitta filen " + propertyFile
			                   + ".properties");
			e.printStackTrace();
		}
	}




	/**
	 * Laddar om konfigurationen för den angivna propertyfilen.
	 *
	 *	2005-10-05 Reload var tidigare ej möjligt att göra eftersom filen cachades.
	 *	Har lagt in kod som låser upp och nollar filcachen så det blir möjligt att läsa in
	 *	nya värden. 
	 *
	 * @param propertyFile Namn på filen med inställningar enligt <i>propertyFile</i>.properties
	 */
	
	public static void reload(String propertyFile) {
		
		log.debug("\n reload propertiefil : " + propertyFile);
		
		/*
		 * Hämtar bundle klassen
		 */  
		 
		Class klass = null;
		boolean missingResource = false;
		
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(propertyFile);
			if (bundle != null) {
				klass = bundle.getClass().getSuperclass();
			} else {
				missingResource = true;
			}
		} catch (Exception e) {
			System.err.println("se.csn.ark.common.util.Properties.reload()"
					+ ", fel vid reload av filen " + propertyFile
					+ ".properties" + "I första steget där bundle klassen hämtas.");
			e.printStackTrace();
		}
		
		/*
		 * Hämtar cache fältet
		 */
		 
		 Field field = null;
		 
		 try {
		 	field = klass.getDeclaredField("cacheList");
		 } catch (Exception e) {
			System.err.println("se.csn.ark.common.util.Properties.reload()"
				+ ", fel vid reload av filen " + propertyFile
				+ ".properties" + "I andra steget där cache fältet hämtas.");
				e.printStackTrace();
		 }
		 
		 /*
		  * Rensa cache
		  */
		 
		 field.setAccessible(true);
		 
		try {
			((Map) field.get(ResourceBundle.class)).clear();
		} catch (Exception e) {
			System.err.println("se.csn.ark.common.util.Properties.reload()"
				+ ", fel vid reload av filen " + propertyFile
				+ ".properties" + "I tredje steget där cachen rensas.");
				e.printStackTrace();
		}
		
		field.setAccessible(false);
		
		/*
		 * Initierar filen på nytt och läser in aktuella värden.
		 */
		init(propertyFile);
	
	}
	

	/**
	 * Skapar en sträng med alla properties.
	 *
	 * @return En sträng formaterar enligt nyckel=värde och radbrytning efter varje värde.
	 */
	public static String propertiesToString() {
		StringBuffer strBuf = new StringBuffer();

		// Läs alla nycklar
		Enumeration keys = properties.keys();

		// Bygger en sträng alla nycklar och värden.
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String value = properties.getProperty(key);

			strBuf.append("\n  " + key + " -> '" + value + "'");
		}

		return strBuf.toString();
	}
	
	
	/**
	 * Skapar en sträng med alla properties som är avsedd att användas för html-presentation.
	 * @author CSN7504
	 * @return En sträng formaterad som html med alla nycklar och värden.
	 * 
	 */
	public static String propertiesToHtmlString() {
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("<h2>Följande värden finns inlästa från propertiesfilerna :</h2><br><br>");

			// Läs alla nycklar
			Enumeration keys = properties.keys();

			// Bygger en sträng alla nycklar och värden.
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = properties.getProperty(key);

				strBuf.append(key + "=" + value + "<br>");
			}

			return strBuf.toString();
	}
	
}
