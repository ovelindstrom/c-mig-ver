package se.csn.ark.common.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import se.csn.ark.common.util.logging.Log;


/**
 * 	Namn:    MeddelandeKo
 *
 * 	Beskrivning:
 *		Klass för att kolla/prenumerera på meddelande som skall skickas iväg av meddelande
 * 	och kö hantering.
 *
 * @author csnnlm
 * @version 1.0
 */
public class MeddelandeKo {
	private InitialContext context = null;
	private QueueConnectionFactory qcf = null;
	private QueueSender sender = null;
	private QueueReceiver receiver = null;
	private ObjectMessage sendMessage = null;
	private QueueConnection conn = null;
	private QueueSession session = null;
	private Queue q = null;
	private static Log log = Log.getInstance(MeddelandeKo.class);

	/**
	 *  Konstruktor for MeddelandeKo
	 */
	public MeddelandeKo() {
	}

	/**
	 * Konstruktor - se init
    * @param contextConectionFactory skapare av kopplings omgivning
    * @param que namn på sökt kö
    */
   public MeddelandeKo(String contextConectionFactory, String que) {
		init(contextConectionFactory, que);
	}

	/**
	 * Initiering, hämtar anslutning aoch startar connection.
	 * @param contextConectionFactory skapare av kopplings omgivning
	 * @param que namn på sökt kö
	 */
	public void init(String contextConectionFactory, String que) {
		/*
		 * Skapar ett JNDI API InitialContext objekt
		 */
		try {
			context = new InitialContext();

			//  queue connection factory from the initial context.
			qcf = (QueueConnectionFactory)context.lookup(contextConectionFactory);

			// Create a new queue connection from the queue connection factory. 
			conn = qcf.createQueueConnection();

			// Lookup the queue to be used to send and receive messages from the initial context. 
			q = (Queue)context.lookup(que);

			//starta anslutningen
			conn.start();

			// Create a new queue session from the queue         
			session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (NamingException e) {
			log.debug("Could not create JNDI API context: "
			                   + e.getMessage());
			System.exit(1);
		} catch (JMSException e) {
			log.debug("Kan inte skapa anslutning till avgiven kö : "
			                   + e.getMessage());
			System.exit(1);
		}
	}




	/**
	 * Hämtar nästa meddelande från en kö om sådant finns
	 * @return Objekt från kön.
	 */
	public Object nasta() {
		// Use the queue receiver to receive the message that 
		// was sent previously.
		Object o = null;

		try {
			o = ((ObjectMessage)receiver.receiveNoWait()).getObject();
		} catch (JMSException e) {
			log.debug("Fel vid mottagande av Objektet : "
			                   + e.getMessage());
		}

		return o;
	}




	/**
	 * Öppnar en mottagarkö som kan ta emot meddelanden från angiven kö
	 */
	public void openReciver() {
		// Create a new queue receiver using the queue session. 
		// The queue receiver should be created to receive 
		// messages from the queue q. 
		try {
			receiver = session.createReceiver(q);
		} catch (JMSException e) {
			log.debug("Kan inte skapa en reciver : " + e.getMessage());
			System.exit(1);
		}

		// Use the queue receiver to receive the message that 
		// was sent previously. 
		//		final Message receivedMessage = receiver.receiveNoWait();			
	}




	/**
    * Öppnar en anslutning för att sända ett meddelande på angiven kö 
    * måste göras efter init.
    */
   public void openSender() {
		// Create a new queue sender using the queue session. 
		// The sender should be created to send messages to the queue q. 
		try {
			sender = session.createSender(q);

			// Create a ObjectMessage using the queue session. 
			sendMessage = session.createObjectMessage();
		} catch (JMSException e) {
			log.debug("Kan inte skapa en anslutning : "
			                   + e.getMessage());
			System.exit(1);
		}
	}




	/**
	 * Postar meddelande på en kö.
	 * @param s serialeserbart objekt som skall skickas.
	 * @return 1 alltid
	 */
	public int skicka(Serializable s) {
		// Send the sendMessage object using the queue sender.
		try {
			sendMessage.setObject(s);
			sender.send(sendMessage);
		} catch (JMSException e) {
			log.debug("Misslyckades att initiera meddelandet : "
			                   + e.getMessage());
			System.exit(1);
		}

		return 1;
	}
}