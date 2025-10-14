/*
 * CHECKSTYLE/OFF: System.out.print
 * CHECKSTYLE/OFF: MagicNumber
 */
package se.csn.ipl.notmotor.mdb;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
//import javax.rmi.PortableRemoteObject;

import junit.framework.TestCase;

public class IntegrationTestSkapaMQMeddelande extends TestCase {

	public static final String JNDI_INITIAL_CONTEXT_FACTORY = "com.ibm.websphere.naming.WsnInitialContextFactory";
	public static final String JNDI_PROVIDER_URL = "iiop://localhost:2809";
	//public static final String JNDI_PROVIDER_URL = "iiop://csnwebi003.csnnet.int:22204";
	public static final String JNDI_JMS_FACTORY = "jms/NotmotorMqConnFactory";
	public static final String JNDI_JMS_QUEUE = "queue/MAAAUD";
	
	private MQProducer mqproducer;
	private int msgcounter;
	private int antal;
	
	@Override
	protected void setUp() throws Exception {
		System.out.println("Skapar MQProducer");
		super.setUp();

		mqproducer = new MQProducer();
		msgcounter = 0;
	}

	@Override
	protected void tearDown() throws Exception {
		System.out.println("Stänger MQProducer");
		super.tearDown();
		mqproducer.close();
	}
	
	
	public void testSkapaEttMQMeddelande() {
		skapaMQMeddelande(1);
	}
	
	public void testSkapa10MQMeddelanden() {
		skapaMQMeddelande(10);
	}
	
	public void testSkapa100MQMeddelanden() {
		skapaMQMeddelande(100);
	}
	
	public void testSkapa1000MQMeddelanden() {
		skapaMQMeddelande(1000);
	}
	
	public void testSkapaEttMQMeddelandeELUR() {
		skapaMQMeddelande(1, "EPOST UPPMANING ORG");
	}
	
	public void testSkapa10MQMeddelandenELUR() {
		skapaMQMeddelande(10, "EPOST UPPMANING ORG");
	}
	
	
	private void skapaMQMeddelande(int antal) {
		skapaMQMeddelande(antal, "                        ");
	}
	
	private void skapaMQMeddelande(int antal, String correlationId) {
		this.antal = antal;
		Message msg;
		for (int i = 0; i < antal; i++) {
			msg = createMessage(correlationId);
			assertNotNull(msg);
			assertTrue(mqproducer.send(msg));
			System.out.println("Skapat testmeddelande " + (i + 1) + " av " + antal + " [" + correlationId + "]");
		}
		System.out.println("Commit");
		mqproducer.commit();
	}
		
	private Message createMessage(String correlationId) {
		StringBuffer msg = new StringBuffer();
		msg.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		msg.append("<NOTIFIERING>");
		msg.append("<REFNR>502090300020000200000000</REFNR>");
		msg.append("<MEDDSAETT>Epost</MEDDSAETT>");
		msg.append("<EPOSTADR>joel.norberg@gmail.com</EPOSTADR>");
		msg.append("<MOBILNR></MOBILNR>");
		msg.append("<RUBRIK>Testmeddelande # " + (++msgcounter) + "/" + antal + "</RUBRIK>");
		msg.append("<MOTIV>Hej!\n"
				+ "Detta är testmeddelande nummer " + msgcounter + " av " + antal + ".\n"
				+ "å ä ö Å Ä Ö snabel-@"
				+ "\n"
				+ "Genererat av " + this.getClass().getCanonicalName() + "\n"
				+ "Correlation ID: " + correlationId + "\n"
				+ "</MOTIV>");
		msg.append("</NOTIFIERING>");
		TextMessage tmsg = mqproducer.createTextMessage();
		try {
			tmsg.setText(msg.toString());
			tmsg.setJMSCorrelationID(correlationId);
		} catch (JMSException e) {
			e.printStackTrace();
			return null;
		}
		return tmsg;
	}
		
	
	class MQProducer {
		
		private QueueConnection qcon;
		private QueueSession qsession;
		private QueueSender qsender;
		
		public MQProducer() {
			init();
		}
				
		private void init() {
			Context ctx;
			QueueConnectionFactory qcfactory;
			Queue queue;
			java.util.Properties env = new java.util.Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_INITIAL_CONTEXT_FACTORY);
			env.put(Context.PROVIDER_URL, JNDI_PROVIDER_URL);
			try {
				ctx = new InitialContext(env);
				// Om du får: "java.net.ConnectException: connect: Address is invalid on local machine, or port is not valid on remote machine"
				// på raden: "ctx.lookup(JNDI_JMS_FACTORY);" och WAS security är påslaget så måste en property ändras på WAS'en,
				// Global Security->RMI/IIOP security->CSIv2 inbound communications->Transport = SSL-supported
				qcfactory = (QueueConnectionFactory) ctx.lookup(JNDI_JMS_FACTORY);
//				qcfactory = (QueueConnectionFactory) PortableRemoteObject.narrow(obj, QueueConnectionFactory.class);
				queue = (Queue) ctx.lookup(JNDI_JMS_QUEUE);
			} catch (NamingException e) {
				e.printStackTrace();
				throw new IllegalStateException("Kunde inte hitta angiven QueueConnectionFactory eller Queue", e);
			}
			try {
				qcon = qcfactory.createQueueConnection();
				qcon.start();
				qsession = qcon.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
				qsender = qsession.createSender(queue);
			} catch (JMSException e) {
				throw new IllegalStateException("Kunde inte skapa QueueSender", e);
			}
		}
		
		public TextMessage createTextMessage() {
			try {
				return qsession.createTextMessage();
			} catch (JMSException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public boolean send(Message msg) {
			try {
				qsender.send(msg);
				return true;
			} catch (JMSException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public boolean commit() {
			try {
				qsession.commit();
				return true;
			} catch (JMSException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public boolean rollback() {
			try {
				qsession.rollback();
				return true;
			} catch (JMSException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public void close() {
			try {
				if (qsender != null) {
					qsender.close();
				}
				if (qsession != null) {
					qsession.close();
				}
				if (qcon != null) {
					qcon.close();
				}
			} catch (JMSException e) {
				throw new IllegalStateException("Kunde inte stänga QueueSender, QueueSession och QueueConnection", e);
			}
		}
		
	}
	
}
