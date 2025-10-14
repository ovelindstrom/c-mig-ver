package se.csn.ark.common.util;
import java.util.Enumeration;
import java.util.ResourceBundle;


/**
 * @author Andreas CArlsson
 * @since 20041018
 * @version 1 skapad
 *
 * Klass för att läsa in properties från iipax_products.properties och sedan mappa
 * mot product idn
 * 
 */
public final class ProductProperties {
	private static final String IIPAX_PRODUCTS_CONFIG = "iipax_products";

	private static java.util.Properties iipaxProductsProperties = new java.util.Properties();

	static {

		init();
	}


    /**
     * Privat konstruktor, endast statisk åtkomst
     */
    private ProductProperties() {
    }




	/**
	 * @param key namn på property
	 * @return värde
	 */
	public static String getProperty(String key) {

		return iipaxProductsProperties.getProperty(key);
	}


	/**
	 * Läs in properties
	 */
	private static void init() {

		try {
			ResourceBundle bundle = ResourceBundle.getBundle(IIPAX_PRODUCTS_CONFIG);

			// Läs alla nycklar
			Enumeration keys = bundle.getKeys();

			// Lagra alla nycklar och värden.
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = bundle.getString(key);
				iipaxProductsProperties.put(key, value);
			}
		} catch (Exception e) {
			System.err.println(
				"se.csn.ark.common.util.ProductProperties.init()"
					+ ", kan ej hitta filen iipax_products.properties");
			e.printStackTrace();
		}

	}
	
}
