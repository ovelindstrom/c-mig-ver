package se.csn.ark.broker.plugin;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import se.csn.ark.common.dt.CsnDataTransferObject;


/**
 * CsnXmlHandler är en abstrakt hjälpklass som används av SAX-parsern
 * när den parsar en fråga. Ärv ifrån denna klass och implementera
 * startElement(name) och endElement(name, value).
 *
 *
 * @author Joakim Olsson
 * @since 20041007
 * @version 0.1 skapad
 */

public abstract class CsnXmlHandler extends DefaultHandler {
	private String rootNodeName = null;
	private StringBuffer currentName = new StringBuffer();
	private StringBuffer currentValue = null;
	private String iipaxTransactionId = null;
	private CsnDataTransferObject dto = null;
	private int ignoreLevels = 1;

	/**
	 * @param iipaxTxId id som används från DTO-object vid
     * transaktions-loggning
	 */
	public CsnXmlHandler(String iipaxTxId) {
		iipaxTransactionId = iipaxTxId;
	}

	/**
	 * Ska implementeras av subklass. Convertern kan används för konvertering
	 * från xml till flera DTO-er. Beroende på rootName ska den valda
	 * DTO'en retruneras här.
	 *
	 * @param rootName namnet på root-taggen == den funktion som anropas
     * @return transformerat DTO
	 */
	public abstract CsnDataTransferObject getDto(String rootName);




	/**
	 * Ska implementeras av subklass. Hanterar händelsen startElement.
	 *
	 * @param name namnet på taggen enligt 'tag name 1'.'tag name 2'. etc
	 */
	public abstract void startElement(String name);




	/**
	 * Ska implementeras av subklass. Hanterar händelsen endElement.
	 *
	 * @param name namnet på taggen enligt 'tag name 1'.'tag name 2'. etc
	 * @param value det data som följer med namnet, null om inget data finns
	 */
	public abstract void endElement(String name, String value);




	/**
	 * Sätter hur många XML-nivåer som ska ignoreras innan händelser börjar
	 * genereras.
	 *
	 * @param ignore antal nivåer som ska ignoreras
	 */
	public void setIgnoreLevels(int ignore) {
		if (ignore < 0) {
			ignoreLevels = 0;
		} else {
			ignoreLevels = ignore + 1;
		}
	}




	/**
	 * Ger namnet på root-noden.
	 *
	 * @return namnet på root-noden
	 */
	public String getRootName() {
		return rootNodeName;
	}




	/**
	 * Ger parsade dto-object.
	 *
	 * @return namnet på root-noden
	 */
	public CsnDataTransferObject getDto() {
		return dto;
	}




	/**
	 * Hanterar händelsen startElement.
	 *
	 * @param uri namespace-uri'n
	 * @param localName 'local name' utan prefix, tomma strängen om inga namespace används
	 * @param qName 'qualified name' med prefix, tomma strängen om inga 'qualified name' finns
	 * @param attributes tillhörande attribut, specificerade eller default
	 */
	public void startElement(
	                         String uri,
	                         String localName,
	                         String qName,
	                         Attributes attributes) {
		if (ignoreLevels == 0) {
			if (currentName.length() == 0) {
				currentName.append(qName);
			} else {
				currentName.append("." + qName);
			}

			// mLog.finest("Start - "+mCurrentName);
			if (currentName.toString().startsWith("trace")) {
				; // Gör ingenting
			} else {
				startElement(currentName.toString());
			}
		} else if (ignoreLevels == 1) {
			rootNodeName = qName;
			ignoreLevels = 0;
			dto = getDto(rootNodeName);

			if (dto != null) {
				dto.setEvent(rootNodeName);
				dto.setTransactionId("/" + iipaxTransactionId);
			}
		} else {
			--ignoreLevels;
		}

		currentValue = null;
	}




	/**
	 * Hanterar händelsen endElement.
	 *
	 * @param uri namespace-uri'n
	 * @param localName 'local name' utan prefix, tomma strängen om inga namespace används
	 * @param qName 'qualified name' med prefix, tomma strängen om inga 'qualified name' finns
	 */
	public void endElement(String uri, String localName, String qName) {
		if (currentName.length() > 0) {
			String value = null;
			String name = currentName.toString();

			if (null != currentValue) {
				value = currentValue.toString();
			}

			if (null == value) {
				value = "";
			}

			// mLog.finest("Stop - " + mCurrentName + "[" + value + "]");
			if (name.startsWith("trace")) {
				if (name.equals("trace.event") && (dto != null)) {
					dto.setEvent(value + "/" + rootNodeName);
				} else if (name.equals("trace.transactionid") && (dto != null)) {
					dto.setTransactionId(value + "/" + iipaxTransactionId);
				}
			} else {
				endElement(name, value);
			}

			if (currentName.length() > qName.length()) {
				currentName.setLength(currentName.length()
				                       - (qName.length() + 1));
			} else {
				currentName.setLength(0);
			}
		}

		currentValue = null;
	}




	/**
	 * Hanterar händelsen att fler char finns tillgängliga.
	 *
	 * @param ch buffer med char's
	 * @param start första char som ska hanteras
	 * @param length antal chars som ska hanteras
	 */
	public void characters(char[] ch, int start, int length) {
		if (currentValue == null) {
			currentValue = new StringBuffer();
		}

		currentValue.append(ch, start, length);
	}
}