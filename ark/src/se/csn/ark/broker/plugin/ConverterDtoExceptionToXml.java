package se.csn.ark.broker.plugin;

import org.w3c.dom.Element;

import se.csn.ark.common.CsnException;
import se.csn.ark.common.util.logging.Log;


/**
 * Konverterare fel.
 * Konverterar från DTO (DataTransferObjekt) till XML.
 *
 * @author Joakim Olsson
 * @since 20041122
 * @version 0.1 skapad
 *
 */
public class ConverterDtoExceptionToXml extends CsnXmlCreator {
	private static Log log = Log.getInstance(ConverterDtoExceptionToXml.class);
	private CsnException csnExc = null;

	/**
	 * Omvandlar innehållet i DTO objektet till XML.
	 *
	 * @param data Exception objekt som skall omvandlas.
	 */
	public ConverterDtoExceptionToXml(CsnException data) {
		csnExc = data;
	}

	/** 
     * Skapar xml med typ:
     * <fel>
     *      <id> x </id>
     *   <typ> x </typ>
     *   <meddelande> x </meddelande>
     * </fel>
     * 
	 * @see se.csn.ark.broker.plugin.CsnXmlCreator#createXml(org.w3c.dom.Element)
	 */
	public void createXml(Element root) {
		try {
			Element fel = createChild(root, "fel");

			if (csnExc.getErrorId() != null) {
				createChild(fel, "id", csnExc.getErrorId().toString());
			}

			if (csnExc.getType() != null) {
				createChild(fel, "typ", csnExc.getType().toString());
			}

			if (csnExc.getMessage() != null) {
				createChild(fel, "meddelande", csnExc.getMessage());
			}
		} catch (Exception e) {
			log.error(e);
		}
	}
}