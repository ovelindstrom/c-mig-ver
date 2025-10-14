//package se.csn.ark.broker.plugin;
//
//import iipax.generic.plugin.PluginException;
//import iipax.generic.plugin.broker.MimeHeaders;
//
//import org.w3c.dom.DOMException;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//
//import se.csn.ark.common.CsnArkBaseObject;
//import se.csn.ark.common.CsnArkBaseObjectImpl;
//import se.csn.ark.common.CsnException;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.FactoryConfigurationError;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerConfigurationException;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//
// 
///**
// * CsnPluginBase är en hjälpklass för att bygga Online Plugins
// * och Push Plugins. Den har funktioner för att parsa frågor och
// * bygga upp svar.
// *
// * @author Joakim Olsson
// * @since 20041007
// * @version 0.1 skapad
// */
//public class CsnPluginBase extends CsnArkBaseObjectImpl 
//		implements CsnArkBaseObject {
//			
//	private static final String CLASS_NAME = CsnPluginBase.class.getName();
//	private static MimeHeaders MIME_HEADERS = null;
//
//
//	private DocumentBuilder mBuilder;
//	private SAXParser mParser;
//	private Logger mLog;
//
//	/**
//	 * Anropas när plug-inen laddas i brokern. Ger den Logger-klass som
//	 * man loggar till. Konstruerar en DOM-hanterare och en SAX-parser.
//	 *
//	 * @param pluginLogger a Logger object to be used by the plugin
//	 * @throws PluginException when fails to create a DocumentBuilder
//	 */
//
//	public void init(Logger pluginLogger) throws PluginException {
//		String functionName = "init";
//		mLog = pluginLogger;
//
//		try {
//			mBuilder =
//				DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			mParser = SAXParserFactory.newInstance().newSAXParser();
//		} catch (ParserConfigurationException pce) {
//			throw new PluginException(logException(
//			                                       functionName,
//			                                       pce,
//			                                       "Could not parse request. "
//			                                       + "Invalid DOM parser configuration"));
//		} catch (SAXException se) {
//			throw new PluginException(logException(
//			                                       functionName,
//			                                       se,
//			                                       "Could not parse request. "
//			                                       + "Invalid SAX parser configuration"));
//		} catch (FactoryConfigurationError fce) {
//			throw new PluginException("Could not parse request. "
//			                          + "Invalid SAX parser configuration");
//		}
//	}
//
//
//
//
//	/**
//	 * Returnerar 'mime headers' som används i SOAP-svar
//	 *
//	 * @return 'mime headers' som används i SOAP-svar
//	 */
//	public MimeHeaders getMimeHeaders() {
//		
//		if( MIME_HEADERS == null) {
//			MIME_HEADERS = new MimeHeaders();
//			MIME_HEADERS.addHeader("content-type", "text/xml");
//		}
//
//		return MIME_HEADERS;
//	}
//
//
//
//
//	/**
//	 * Returnerar DocumentBuilder
//	 *
//	 * @return DocumentBuilder som används i SOAP-svar
//	 */
//	protected DocumentBuilder getBuilder() {
//		return mBuilder;
//	}
//
//
//
//
//	/**
//	 * Parsar en SOAP-fråga och genererar händelser till en XML-hanterare.
//	 *
//	 * @param  in strömmen som innehåller SOAP-frågan
//	 * @param  handler hanterar händelser för varje XML-tagg
//	 * @return handler som haterat parsningen
//	 * @throws SAXException vid misslyckad parsnins
//	 * @throws IOException vid misslyckad läsning av strömmen
//	 */
//	protected CsnXmlHandler parseSoapRequest(
//	                                         InputStream in,
//	                                         CsnXmlHandler handler)
//	                                  throws SAXException, IOException {
//		if (mLog.isLoggable(Level.FINEST)) {
//			in = logTheStream(readTheStream(in), Level.FINEST);
//		} else {
//			in = new ByteArrayInputStream(readTheStream(in).toByteArray());
//		}
//
//		handler.setIgnoreLevels(2);
//
//		InputSource inS = new InputSource(in);
//		inS.setEncoding("ISO-8859-1");
//		mParser.parse(inS, handler);
//
//		return handler;
//	}
//
//
//
//
//	/**
//     * Läser in strömmen gör viss teckenkonvertering
//	 * @param in inströmmen
//	 * @return samma ström
//	 * @throws IOException vid fel vid läsning av in
//	 */
//	protected ByteArrayOutputStream readTheStream(InputStream in)
//	                                       throws IOException {
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		int i = 0;
//		i = in.read();
//
//		int l1 = 0;
//		int l2 = 0;
//
//		while (i > 0) {
//			if ((i == '*') && (l1 == '<')) {
//                ;
//			} else if ((i == '*') && (l1 == '/') && (l2 == '<')) {
//				;
//			} else {
//				out.write((byte)i);
//			}
//
//			l2 = l1;
//			l1 = i;
//			i = in.read();
//		}
//
//		return out;
//	}
//
//
//
//
//	/**
//     * Loggar retur-strömmen
//	 * @param out retur-ström som ska loggas
//	 * @param logLevel nivå för loggning
//	 * @return out-strömmen som in-ström
//	 * @throws IOException vid läsfel
//	 */
//	protected InputStream logTheStream(
//	                                   ByteArrayOutputStream out,
//	                                   Level logLevel)
//	                            throws IOException {
//		mLog.log(logLevel, "Parsing: \n" + out.toString());
//
//		InputStream in = new java.io.ByteArrayInputStream(out.toByteArray());
//
//		return in;
//	}
//
//
//
//
//	/**
//	 * Parsar XML från en 'push' och genererar händelser till en XML-hanterare
//	 *
//	 * @param  in strömmen som innehåller SOAP-frågan
//	 * @param  handler hanterar händelser för varje XML-tagg
//	 * @return handler som haterat parsningen
//	 * @throws SAXException vid misslyckad parsnins
//	 * @throws IOException vid misslyckad läsning av strömmen
//	 */
//	protected CsnXmlHandler parseXmlPush(InputStream in, CsnXmlHandler handler)
//	                              throws SAXException, IOException {
//		if (mLog.isLoggable(Level.FINEST)) {
//			in = logTheStream(readTheStream(in), Level.FINEST);
//		} else {
//			in = new ByteArrayInputStream(readTheStream(in).toByteArray());
//		}
//
//		InputSource inS = new InputSource(in);
//		inS.setEncoding("ISO-8859-1");
//		mParser.parse(inS, handler);
//
//		return handler;
//	}
//
//
//
//
//	/**
//	 * Skriver ett SOAP-svar från ett DOM-dokument till en OutpuStream.
//	 *
//	 * @param creator skapare av XML-träd
//	 * @param methodName namnet på den metod som svaret skapas till
//	 * @param out strömmen dit svaret ska skrivas
//	 * @throws TransformerException transformer misslyckades
//	 */
//	protected void writeSoapResponse(
//	                                 CsnXmlCreator creator,
//	                                 String methodName,
//	                                 OutputStream out)
//	                          throws TransformerException {
//		String functionName = "writeSoapResponse";
//
//		Document outputDocument = mBuilder.newDocument();
//		Element returnElement =
//			createResponseInDocument(methodName, outputDocument);
//
//		creator.setDocument(outputDocument);
//		creator.createXml(returnElement);
//
//        
//		DOMSource source = new DOMSource(outputDocument);
//		StreamResult result = new StreamResult(out);
//		Transformer trans = TransformerFactory.newInstance().newTransformer();
//        trans.setOutputProperty("encoding", "ISO-8859-1");
//		trans.transform(source, result);
//	}
//
//
//
//
//	/**
//	 * Skriver ett Exception till logg.
//	 *
//	 * @param functionName det funktionsnamn som loggar felet
//	 * @param e det Exception som ska loggas
//	 * @param errMsg felmeddelande
//	 * @return felmeddelandet
//	 */
//	protected String logException(
//	                              final String functionName,
//	                              Exception e,
//	                              String errMsg) {
//		if (mLog.isLoggable(Level.WARNING)) {
//			mLog.logp(Level.WARNING, CLASS_NAME, functionName, errMsg, e);
//		}
//
//		return errMsg;
//	}
//
//
//
//
//	/**
//	 * Skriver ett Exception till svar.
//	 *
//	 * @param out output ström
//	 * @param csnExc det Exception som ska skickas tillbaks
//	 * @param methodName soap-metod som detta är svar till
//	 * @throws PluginException om något går fel
//	 */
//	protected void creatExceptionResponse(
//	                                      OutputStream out,
//	                                      CsnException csnExc,
//	                                      String methodName)
//	                               throws PluginException {
//		try {
//			writeSoapResponse(
//			                  new ConverterDtoExceptionToXml(csnExc),
//			                  methodName,
//			                  out);
//		} catch (DOMException de) {
//			String msg =
//				"Could not handle request. "
//				+ "Exception while traversing DOM-tree";
//			mLog.log(Level.WARNING, msg, de);
//			throw new PluginException(msg, de);
//		} catch (TransformerConfigurationException tce) {
//			String msg =
//				"Could not build response. "
//				+ "Invalid DOM to stream transformer configuration";
//			mLog.log(Level.WARNING, msg, tce);
//			throw new PluginException(msg, tce);
//		} catch (TransformerException te) {
//			String msg = "Could not build response. Transformer exception";
//			mLog.log(Level.WARNING, msg, te);
//			throw new PluginException(msg, te);
//		}
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
//	private Element createResponseInDocument(
//	                                         String methodName,
//	                                         Document docOut) {
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
//}