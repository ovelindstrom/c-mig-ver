package se.csn.ark.broker.plugin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import se.csn.ark.common.CsnArkBaseObjectImpl;


/**
 * CsnXmlCreator är en abstrakt hjälpklass som används för att
 * bygga upp ett XML-svar. Ärv ifrån denna klass och implementera
 * createXml(root).
 *
 *
 * @author Joakim Olsson
 * @since 20041007
 * @version 0.1 skapad
 */
public abstract class CsnXmlCreator extends CsnArkBaseObjectImpl {
	private Document xmlDocument = null;

	/**
	 * Ska implementeras av subklass. Här lägger man in XML-data
	 * under root-noden.
	 *
	 * @param root den nod som man bygger på under
	 */
	public abstract void createXml(Element root);




	/**
	 * Sätter ett XML-dokumen som man kan skapa noder från.
	 *
	 * @param doc XML-dokument
	 */
	public void setDocument(Document doc) {
		xmlDocument = doc;
	}




	/**
	 * Skapar en child-nod utan något värde.
	 *
	 * @param parent under denna skapas en ny nod
	 * @param name den nya nodens namn
	 * @return den nya child-noden
	 */
	protected Element createChild(Element parent, String name) {
		Element child = xmlDocument.createElement(name);
		parent.appendChild(child);

		return child;
	}




	/**
	 * Skapar en child-nod med ett värde. Om value==null så sätts
     * nodens värde till "".
	 *
	 * @param parent under denna skapas en ny nod
	 * @param name den nya nodens namn
	 * @param value den nya nodens värde
	 * @return den nya child-noden
	 */
	protected Element createChild(Element parent, String name, String value) {
		if (value == null) {
			value = "";
		}

		Text text = xmlDocument.createTextNode(value);
		Element child = xmlDocument.createElement(name);

		child.appendChild(text);
		parent.appendChild(child);

		return child;
	}
}