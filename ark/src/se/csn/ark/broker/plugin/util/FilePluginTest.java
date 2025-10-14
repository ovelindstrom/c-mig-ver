//package se.csn.ark.broker.plugin.util;
//
//import iipax.generic.plugin.broker.DataPartInfo;
//import iipax.generic.plugin.broker.MessageInfo;
//import iipax.generic.plugin.broker.MimeHeaders;
//import iipax.generic.plugin.broker.ReadableDataPart;
//import iipax.generic.plugin.broker.ReadableMessage;
//import iipax.generic.xml.XmlCompare;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Iterator;
//import java.util.logging.Logger;
//
//
///**
// * Testklass för FilePlugin.
// *
// * @author Joakim Olsson
// * @since 20041007
// * @version 0.1 skapad
// */
//public class FilePluginTest extends CsnPluginTestBase {
//	private FilePlugin mPlugin;
//
//	/**
//	 * Constructor for TestDataOnlinePluginTest.
//     * 
//	 * @param arg0 argument för junit.framework.TestCase
//	 */
//	public FilePluginTest(String arg0) {
//		super(arg0);
//        setDir("./src/se/csn/ark/broker/plugin/util/testdata");
//	}
//
//	/** 
//	 * @see junit.framework.TestCase#setUp()
//	 */
//	protected void setUp() throws Exception {
//		super.setUp();
//
//		mPlugin = new FilePlugin();
//		mPlugin.init(Logger.global);
//		mPlugin.setTestFileRoot(getDir());
//	}
//
//
//
//
//	/**
//	 * @see TestCase#tearDown()
//	 */
//	protected void tearDown() throws Exception {
//		super.tearDown();
//	}
//
//
//
//
//	/**
//	 * Testar FilePlugin.push
//	 */
//	public void testExecutePush() {
//		MessageInfo messageInfo = createMessageInfo();
//		DataPartInfo dataInfo = createDataPartInfo();
//
//		try {
//			InputStream input = new FileInputStream(getDir() + "/Test2.xml");
//			ReadableMessage message = createReadableMessage(input);
//
//			mPlugin.push(message, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail(e.getMessage());
//		}
//
//		try {
//			InputStream request =
//				new FileInputStream(getDir() + "/user/user_"
//				                    + mPlugin.getStoreTimeStamp() + ".xml");
//			InputStream correct = new FileInputStream(getDir() + "/Test2.xml");
//			XmlCompare x = new XmlCompare(request);
//
//			assertTrue("SOAP is not correct request", x.contains(correct));
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail(e.getMessage());
//		}
//	}
//
//
//
//
//	/**
//	 * Testar request-biten av FilePlugin.execute
//	 */
////	public void testExecuteRequest() {
////		MessageInfo messageInfo = createMessageInfo();
////		DataPartInfo dataInfo = createDataPartInfo();
////
////		ByteArrayOutputStream output = new ByteArrayOutputStream();
////
////		try {
////			InputStream input = createInputRequest("Test1", "Test1Request.xml");
////
////			mPlugin.execute(messageInfo, dataInfo, input, output);
////		} catch (Exception e) {
////			e.printStackTrace();
////			fail(e.getMessage());
////		}
////
////		try {
////			InputStream request =
////				new FileInputStream(getDir() + "/Test1/Test1_"
////				                    + mPlugin.getStoreTimeStamp() + ".xml");
////			InputStream correct = createCorrectRequest("Test1Request.xml");
////			XmlCompare x = new XmlCompare(request);
////
////			assertTrue("SOAP is not correct request", x.contains(correct));
////		} catch (Exception e) {
////			e.printStackTrace();
////			fail(e.getMessage());
////		}
////	}
//
//
//
//
//	/**
//	 * Testar response-biten av FilePlugin.execute
//	 */
////	public void testExecuteResponse() {
////		MessageInfo messageInfo = createMessageInfo();
////		DataPartInfo dataInfo = createDataPartInfo();
////
////		ByteArrayOutputStream output = new ByteArrayOutputStream();
////
////		try {
////			InputStream input = createInputRequest("Test1", "Test1Request.xml");
////
////			mPlugin.execute(messageInfo, dataInfo, input, output);
////		} catch (Exception e) {
////			e.printStackTrace();
////			fail(e.getMessage());
////		}
////
////		try {
////			InputStream response =
////				new ByteArrayInputStream(output.toByteArray());
////			InputStream correct =
////				createCorrectResponse("Test1Response", "Test1Response.xml");
////			XmlCompare x = new XmlCompare(response);
////
////			assertTrue("SOAP is not a correct response", x.contains(correct));
////		} catch (Exception e) {
////			e.printStackTrace();
////			fail(e.getMessage());
////		}
////	}
//
//
//
//
//	/**
//	 * Hjälpmetod för att skapa dummy-data
//     * 
//     * @return en dummy instans
//	 */
//	public DataPartInfo createDataPartInfo() {
//		return new DataPartInfo() {
//
//				private MimeHeaders mimeHeaders;
//                public void setMimeHeaders(MimeHeaders m) {
//                    mimeHeaders = m;
//                }
//				public MimeHeaders getMimeHeaders() {
//					return mimeHeaders;
//				}
//
//				private String name;
//                public void setName(String n) {
//                    name = n;
//                }
//				public String getName() {
//					return name;
//				}
//
//				private String type;
//				public void setType(String t) {
//					type = t;
//				}
//                public String getType() {
//                    return type;
//                }
//			};
//	}
//
//
//
//
//	/**
//	 * Hjälpmetod för att skapa dummy-data
//     * 
//     * @return en dummy instans
//	 */
//	public MessageInfo createMessageInfo() {
//		return new MessageInfo() {
//				private String agreement;
//				public String getAgreement() {
//					return agreement;
//				}
//
//                private String contenentId;
//				public String getContentId() {
//					return contenentId;
//				}
//
//                private String corrId;
//				public String getCorrId() {
//					return corrId;
//				}
//
//                private java.util.Date dateTime;
//				public java.util.Date getDateTime() {
//					return dateTime;
//				}
//
//                private String documentType;
//				public String getDocumentType() {
//					return documentType;
//				}
//
//                private String endRecipient;
//				public String getEndRecipient() {
//					return endRecipient;
//				}
//
//                private String from;
//				public String getFrom() {
//					return from;
//				}
//
//                private String metaData;
//				public String getMetaData(String meta) {
//					return metaData;
//				}
//				public void setMetaData(String meta, String data) {
//					metaData = data;
//				}
//
//                private Iterator metaNames;
//				public Iterator getMetaNames() {
//					return metaNames;
//				}
//
//                private int nrOfDataParts;
//				public int getNrOfDataParts() {
//					return nrOfDataParts;
//				}
//
//                private String originator;
//				public String getOriginator() {
//					return originator;
//				}
//
//                private String product;
//				public String getProduct() {
//					return product;
//				}
//
//                private String sequenceType;
//				public String getSequenceType() {
//					return sequenceType;
//				}
//
//                private String status;
//				public String getStatus() {
//					return status;
//				}
//
//                private String subject;
//				public String getSubject() {
//					return subject;
//				}
//
//                private String to;
//				public String getTo() {
//					return to;
//				}
//
//                private String transferType;
//				public String getTransferType() {
//					return transferType;
//				}
//
//                private String txId;
//				public String getTxId() {
//					return txId;
//				}
//			};
//	}
//
//
//
//
//	/**
//	 * Hjälpmetod för att skapa dummy-data
//     *
//     * @param in input-stream för första datadelen i meddelandet
//     *  
//     * @return en dummy instans
//	 */
//	public ReadableMessage createReadableMessage(final InputStream in) {
//		return new ReadableMessage() {
//				public MessageInfo getInfo() {
//					return null;
//				}
//
//				public Iterator getReadableDataParts(String arg0)
//				                              throws IOException {
//					return null;
//				}
//
//				private ReadableDataPart dataPart = createReadableDataPart(in);
//				public ReadableDataPart getReadableDataPart(int arg0)
//				                                     throws IOException {
//					return dataPart;
//				}
//			};
//	}
//
//
//
//
//	/**
//	 * Hjälpmetod för att skapa dummy-data
//     * 
//     * @param in input-stream för datadelen
//     *  
//     * @return en dummy instans
//	 */
//	public ReadableDataPart createReadableDataPart(final InputStream in) {
//		return new ReadableDataPart() {
//				private InputStream input = in;
//				public InputStream getInputStream() {
//					return input;
//				}
//
//                private String type;
//				public String getType() {
//					return type;
//				}
//
//                private String name;
//				public String getName() {
//					return name;
//				}
//
//				public MimeHeaders getMimeHeaders() {
//					return null;
//				}
//			};
//	}
//}