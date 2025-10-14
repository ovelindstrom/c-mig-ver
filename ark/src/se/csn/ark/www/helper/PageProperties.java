package se.csn.ark.www.helper;

import se.csn.ark.common.util.Properties;

import java.util.Enumeration;
import java.util.Map;


/**
 * Properties för jsp-sidorna.
 *
 * @author Joakim Olsson
 * @since 20041202
 * @version 0.1 skapad
 */
public class PageProperties {
	private static final String CSNAPPLICATION_KONFIG = "sida";
	private static java.util.Properties properties;
	private static java.util.Properties products;

	static {
        Properties.init(CSNAPPLICATION_KONFIG);
		properties = Properties.getProperties("property.");
		products = Properties.getProperties("producttype.");
	}

	private String propertyPagePart;
	private java.util.Properties navigate;


	/**
	 * @param pageId id för aktuell sida
	 * @param pageContext context för aktuell sida
	 */
	public PageProperties(String pageId, String pageContext) {
		this.propertyPagePart = pageId + ".";

		java.util.Properties temp =
            Properties.getProperties("navigate." + propertyPagePart);

		navigate = new java.util.Properties();

		Enumeration keys = temp.keys();

		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();

			navigate.setProperty(key, pageContext + temp.getProperty(key));
		}
	}




	/**
	 * @return länkar för navigering
	 */
	public Map getNavigate() {
		return navigate;
	}




	/**
	 * @param direction navigeringsriktning
	 * @return url för navigeringsriktningen
	 */
	public String getNavigate(String direction) {
		return Properties.getProperty("navigate." + propertyPagePart + direction);
	}




	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		final String nl = "\n";
		String str = "";

		str += (nl + this.getClass().toString() + "=[");
		str += (nl + "  pagePart=" + propertyPagePart);
		str += (nl + "  properties=" + properties);
		str += "]";

		return str;
	}




	/**
	 * @return produkttyps-id'n
	 */
	public Map getProducts() {
		return products;
	}




	/**
	 * @return alla properties för den här sidan
	 */
	public Map getProperties() {
		return properties;
	}
}