//package se.csn.ark.broker.plugin.util;
//
//import iipax.generic.plugin.PluginException;
//import iipax.generic.plugin.StringMap;
//import iipax.generic.plugin.broker.DataPartInfo;
//import iipax.generic.plugin.broker.MessageInfo;
//import iipax.generic.plugin.broker.OnlinePlugin;
//import iipax.generic.plugin.broker.PushPlugin;
//import iipax.generic.plugin.broker.PushPluginException;
//import iipax.generic.plugin.broker.ReadableDataPart;
//import iipax.generic.plugin.broker.ReadableMessage;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.logging.Logger;
//
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerConfigurationException;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMResult;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//import javax.xml.transform.stream.StreamSource;
//
//import org.w3c.dom.DOMException;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.xml.sax.SAXException;
//
//import se.csn.ark.broker.plugin.CsnPluginBase;
//import se.csn.ark.common.util.Properties;
//
//
///**
// * TestDataOnlinePlugin is an Online Plugin to the broker. It us used to test
// * jsp-pagen that sends and requests data from the Broker using <eforms:import>
// * and <online:send>. The plugin writes the request to a file named
// * 'name of the method node' + 'currnet time' + '.xml'.
// * The plugin reads the response from a file named
// * 'name of the method node' + '.xml'.
// *
// * @author Joakim Olsson
// * @since 20041007
// * @version 0.1 skapad
// */
//public class FilePlugin extends CsnPluginBase implements OnlinePlugin,
//                                                         PushPlugin {
//	private static final String CLASS_NAME = FilePlugin.class.getName();
//	private String storeTimeStamp = null;
//	private String mDir = ".";
//    private Logger mLog;
//
//	/**
//	 * Används av junit-testklassen.
//	 *
//	 * @param  root a Logger object to be used by the plugin
//	 *
//	 */
//	public void setTestFileRoot(String root) {
//		mDir = root;
//	}
//
//
//
//
//	/**
//	 * Anropas när plug-inen laddas i brokern. Ger den Logger-klass som
//	 * man loggar till. Konstruerar en DOM-hanterare och en SAX-parser.
//	 *
//	 * @param pluginLogger a Logger object to be used by the plugin
//	 * @throws PluginException when fails to create a DocumentBuilder
//	 */
//	public void init(Logger pluginLogger) throws PluginException {
//		String functionName = "init";
//        mLog = pluginLogger;
//        
//		super.init(pluginLogger);
//
//		mDir = Properties.getProperty("dao.file.pluginTestFilePath");
//	}
//
//
//
//
//	/**
//	 * Handles synchronous messages by writing and reading from disk.
//	 *
//	 * @param  messageInfo information about the message, taken from the SHS label
//	 * @param  dataInfo information abouth the soap request data part
//     * @param  in input data
//     * @param  out output data       
//	 * @throws PluginException when fails to handle the message
//	 */
//	public void execute(
//	                    MessageInfo messageInfo,
//	                    DataPartInfo dataInfo,
//	                    InputStream in,
//	                    OutputStream out) throws PluginException {
//		final String functionName = "execute";
//
//		mLog.entering(CLASS_NAME, functionName);
//
//		Node methodNode;
//		String methodName;
//
//		try {
//			methodNode = parseSoapRequest(in);
//		} catch (SAXException se) {
//			throw new PluginException(logException(
//			                                       functionName,
//			                                       se,
//			                                       "Could not parse request. "
//			                                       + "SAX parser exception"));
//		} catch (IOException ioe) {
//			throw new PluginException(logException(
//			                                       functionName,
//			                                       ioe,
//			                                       "Could not parse request. "
//			                                       + "IOException while reading input"));
//		}
//
//		if (methodNode == null) {
//			throw new PluginException("Could not parse request. Not XML");
//		}
//
//		methodName = methodNode.getNodeName();
//
//		Document outputDocument = createOutpuDocument();
//		Element returnElement =
//			createResponseInDocument(methodName, outputDocument);
//
//		forwardRequest(methodNode);
//		readResponse(methodName, returnElement);
//
//		try {
//			writeSoapResponse(outputDocument, out);
//		} catch (DOMException de) {
//			throw new PluginException(logException(
//			                                       functionName,
//			                                       de,
//			                                       "Could not handle request. "
//			                                       + "Exception while traversing DOM-tree"));
//		} catch (TransformerConfigurationException tce) {
//			throw new PluginException(logException(
//			                                       functionName,
//			                                       tce,
//			                                       "Could not build response. "
//			                                       + "Invalid DOM to stream "
//                                                   + "transformer configuration"));
//		} catch (TransformerException te) {
//			throw new PluginException(logException(
//			                                       functionName,
//			                                       te,
//			                                       "Could not build response. "
//			                                       + "Transformer exception"));
//		}
//
//		mLog.exiting(CLASS_NAME, functionName);
//	}
//
//
//
//
//	/**
//	 * Handles asynchronous messages by writing it to disk.
//	 *
//	 * @param  message the complete message, the xml-tree resides in
//	 * the first data part
//	 * @param  msgArgs might contain name-value pairt of data from other
//	 * plugins
//	 * @return      a string
//	 * @throws PushPluginException when fails to handle the message
//	 * @throws IOException not used
//	 */
//	public String push(ReadableMessage message, StringMap msgArgs)
//	            throws PushPluginException, IOException {
//		final String functionName = "execute";
//
//		mLog.entering(CLASS_NAME, functionName);
//
//		String id = null;
//		Node methodNode;
//		String methodName;
//
//		try {
//			ReadableDataPart dataPart = message.getReadableDataPart(0);
//			InputStream in = dataPart.getInputStream();
//
//			methodNode = parseXmlPush(in);
//			methodName = methodNode.getNodeName();
//		} catch (SAXException se) {
//			throw new PushPluginException(logException(
//			                                           functionName,
//			                                           se,
//			                                           "Could not parse request. "
//			                                           + "SAX parser exception"));
//		} catch (IOException ioe) {
//			throw new PushPluginException(logException(
//			                                           functionName,
//			                                           ioe,
//			                                           "Could not parse request. "
//			                                           + "IOException while reading input"));
//		} catch (DOMException de) {
//			throw new PushPluginException(logException(
//			                                           functionName,
//			                                           de,
//			                                           "Could not handle request. "
//			                                           + "Exception while traversing DOM-tree"));
//		}
//
//		forwardRequest(methodNode);
//
//		mLog.exiting(CLASS_NAME, functionName);
//
//		return id;
//	}
//
//
//
//
//	/**
//	 * Writes the node and its children to a file named
//	 * 'name of the root node' + 'currnet time' + '.xml'.
//	 *
//	 * @param  methodNode the node to write to disk
//	 * @throws PushPluginException when fails to write the node
//	 */
//	private void forwardRequest(Node methodNode) throws PushPluginException {
//		String functionName = "dumpRequestToFile";
//
//		try {
//			String currentTime =
//				(new SimpleDateFormat("yyMMdd-HHmmss")).format(new Date());
//
//			storeTimeStamp = currentTime;
//
//			String name = methodNode.getNodeName();
//
//			File dir = new File(mDir + "/" + name);
//
//			dir.mkdirs();
//
//			File file = new File(dir, name + "_" + currentTime + ".xml");
//			FileOutputStream fileOut = new FileOutputStream(file);
//
//			DOMSource source = new DOMSource(methodNode);
//			StreamResult result = new StreamResult(fileOut);
//			Transformer trans =
//				TransformerFactory.newInstance().newTransformer();
//
//			trans.transform(source, result);
//		} catch (IOException ioe) {
//			throw new PushPluginException(logException(
//			                                           functionName,
//			                                           ioe,
//			                                           "Could not parse request. "
//			                                           + "IOException while reading input"));
//		} catch (TransformerConfigurationException tce) {
//			throw new PushPluginException(logException(
//			                                           functionName,
//			                                           tce,
//			                                           "Could not store request file. "
//			                                           + "Invalid DOM to stream transformer "
//                                                       + "configuration"));
//		} catch (TransformerException te) {
//			throw new PushPluginException(logException(
//			                                           functionName,
//			                                           te,
//			                                           "Could not store request file. "
//			                                           + "Transformer exception"));
//		}
//	}
//
//
//
//
//	/**
//	 * Reads a node and its children from a file named
//	 * fileName + '.xml'.
//	 *
//	 * @param  fileName the name of the file to read from
//	 * @param  resultNode the node where to place the read data
//	 * @return  the resultNode
//	 * @throws PluginException when fails to write to the node
//	 */
//	private Element readResponse(String fileName, Element resultNode)
//	                      throws PluginException {
//		String functionName = "readResponseFromFile";
//
//		try {
//			FileInputStream fileIn =
//				new FileInputStream(mDir + "/" + fileName + "Response.xml");
//
//			StreamSource source = new StreamSource(fileIn);
//			DOMResult result = new DOMResult();
//
//			result.setNode(resultNode);
//
//			Transformer trans =
//				TransformerFactory.newInstance().newTransformer();
//
//			trans.transform(source, result);
//		} catch (IOException ioe) {
//			throw new PluginException(logException(
//			                                       functionName,
//			                                       ioe,
//			                                       "Could not parse response file. "
//			                                       + "IOException while reading input"));
//		} catch (TransformerConfigurationException tce) {
//			throw new PluginException(logException(
//			                                       functionName,
//			                                       tce,
//			                                       "Could not build response. "
//			                                       + "Invalid stream to DOM transformer " 
//                                                   + "configuration"));
//		} catch (TransformerException te) {
//			throw new PluginException(logException(
//			                                       functionName,
//			                                       te,
//			                                       "Could not build response. "
//			                                       + "Transformer exception"));
//		}
//
//		return resultNode;
//	}
//
//
//
//
//	/**
//	 * Parsar en SOAP-fråga från en InputStream till ett DOM Document.
//	 *
//	 * @param  in strömmen som innehåller SOAP-frågan
//	 * @return den nod som pekar på metoden i SOAP-frågan
//	 * @throws SAXException vid misslyckad parsnins
//	 * @throws IOException vid misslyckad läsning av strömmen
//	 */
//	protected Node parseSoapRequest(InputStream in)
//	                         throws SAXException, IOException {
//		Node node;
//		Document docIn = getBuilder().parse(in);
//
//		// Hitta  <SOAP:Envelope>
//		node = docIn.getDocumentElement();
//
//		while ((node != null) && (node.getNodeType() != Node.ELEMENT_NODE)) {
//			node = node.getNextSibling();
//		}
//
//		// Hitta <SOAP:Body>
//		node = node.getFirstChild();
//
//		while ((node != null) && (node.getNodeType() != Node.ELEMENT_NODE)) {
//			node = node.getNextSibling();
//		}
//
//		// Hitta metod-noden
//		node = node.getFirstChild();
//
//		while ((node != null) && (node.getNodeType() != Node.ELEMENT_NODE)) {
//			node = node.getNextSibling();
//		}
//
//		return node;
//	}
//
//
//
//
//	/**
//	 * Skapar ett Document som används för att bygga xml(SOAP) svar
//	 *
//	 * @return  nytt Document
//	 */
//	protected Document createOutpuDocument() {
//		Document outputDocument = getBuilder().newDocument();
//
//		return outputDocument;
//	}
//
//
//
//
//	/**
//	 * Skapar och initierar ett SOAP-svar i ett DOM-dokument.
//	 *
//	 * @param methodName namnet på frågan som det ska skapas ett svar till
//	 * @param docOut dokumentet där svaret ska skapas
//	 * @return den nod där svars-parametrarna ska skrivas
//	 */
//	protected Element createResponseInDocument(
//	                                           String methodName,
//	                                           Document docOut) {
//		Element childElem;
//		Element parentElem;
//
//		// Skapa <SOAP-Envelope> noden
//		parentElem = docOut.createElement("SOAP-ENV:Envelope");
//		parentElem.setAttribute(
//		                        "SOAP-ENV:encodingStyle",
//		                        "http://schemas.xmlsoap.org/soap/encoding/");
//		parentElem.setAttribute(
//		                        "xmlns:SOAP-ENV",
//		                        "http://schemas.xmlsoap.org/soap/envelope/");
//		parentElem.setAttribute(
//		                        "xmlns:xsi",
//		                        "http://www.w3.org/2001/XMLSchema-instance");
//		docOut.appendChild(parentElem);
//
//		// Skapa <SOAP-Body> noden
//		childElem = docOut.createElement("SOAP-ENV:Body");
//		parentElem.appendChild(childElem);
//		parentElem = childElem;
//
//		// Skapa <SOAP-response> noden
//		childElem = docOut.createElement(methodName + "Response");
//		parentElem.appendChild(childElem);
//		parentElem = childElem;
//
//		// Skapa <SOAP-return> noden
//		childElem = docOut.createElement("return");
//		parentElem.appendChild(childElem);
//
//		return childElem;
//	}
//
//
//
//
//	/**
//	 * Skriver ett SOAP-svar från ett DOM-dokument till en OutpuStream.
//	 *
//	 * @param soapResponse dokumentet som innehåller ett SOAP-svar
//	 * @param out strömmen dit svaret ska skrivas
//	 * @throws TransformerException misslyckads skriva svar
//	 */
//	protected void writeSoapResponse(Document soapResponse, OutputStream out)
//	                          throws TransformerException {
//		String functionName = "writeSoapResponse";
//
//		DOMSource source = new DOMSource(soapResponse);
//		StreamResult result = new StreamResult(out);
//		Transformer trans = TransformerFactory.newInstance().newTransformer();
//
//		trans.transform(source, result);
//	}
//
//
//
//
//	/**
//	 * Parsar XML från en 'push' från en InputStream till ett DOM Document.
//	 *
//	 * @param  in strömmen som innehåller SOAP-frågan
//	 * @return den nod som pekar på metoden i SOAP-frågan
//	 * @throws SAXException vid misslyckad parsnins
//	 * @throws IOException vid misslyckad läsning av strömmen
//	 */
//	protected Node parseXmlPush(InputStream in)
//	                     throws SAXException, IOException {
//		Document docIn = getBuilder().parse(in);
//
//		// top node
//		Node node = docIn.getDocumentElement();
//
//		while ((node != null) && (node.getNodeType() != Node.ELEMENT_NODE)) {
//			node = node.getNextSibling();
//		}
//
//		return node;
//	}
//    
//    
//    
//    
//	/**
//	 * @return tidpunkt för lagring
//	 */
//	public String getStoreTimeStamp() {
//		return storeTimeStamp;
//	}
//
//}