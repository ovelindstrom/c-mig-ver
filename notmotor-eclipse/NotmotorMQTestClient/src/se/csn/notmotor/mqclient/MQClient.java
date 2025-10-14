package se.csn.notmotor.mqclient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.JMSException;
import javax.jms.Session;

import com.ibm.jms.*;
import com.ibm.mq.jms.*;

public class MQClient {

	private static final int CCSID = 278; // Stordatorn
	//private static final int CCSID = 850; // Windows TE
	// Port 1415 = MQT7 (Integration test)
	// Port 1414 = MQT9 (GRÖN)
	private static final String QUEUE_MANAGER = "MQINT00"; //"MQT7";
	private static final int MQ_SERVER_PORT = 1415;
	private static final String QUEUE_NAME = "UT.MAAAUD";//"UT.LADOKFRAGA.REGISTRERING";

	private static final String MQ_SERVER = "localhost"; //"mft.csnnet.int";
	private static final String SVRCONN = "DEV.ADMIN.SVRCONN"; //"SYSTEM.DEF.SVRCONN";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		processFile("TESTFIL.txt");
	}

	private static void processFile(String filename) {
		BufferedReader biff = null;
		String row = null;

		try {
			biff = new BufferedReader(new FileReader(filename));
			while ( (row = biff.readLine()) != null) {
				System.out.println(row);
				sendMQMessage(row);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				biff.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void sendMQMessage(String messagetext) {
		try {
			MQQueueConnectionFactory cf = new MQQueueConnectionFactory();

			// Config
			cf.setHostName(MQ_SERVER);
			cf.setPort(MQ_SERVER_PORT);
			cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
			cf.setQueueManager(QUEUE_MANAGER);
			cf.setChannel(SVRCONN);

			MQQueueConnection connection = (MQQueueConnection) cf.createQueueConnection("admin", "passw0rd");
			MQQueueSession session = (MQQueueSession) connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			MQQueue queue = (MQQueue) session.createQueue(QUEUE_NAME);
			MQQueueSender sender =  (MQQueueSender) session.createSender(queue);
			//			MQQueueReceiver receiver = (MQQueueReceiver) session.createReceiver(queue);      

			//long uniqueNumber = System.currentTimeMillis() % 1000;
			//byte[] b = mess.getBytes(Charset.forName("UTF-8"));
			//String messagetext = new String (b, Charset.forName("IBM278"));
			JMSTextMessage message = (JMSTextMessage) session.createTextMessage(messagetext);
			message.setIntProperty(WMQConstants.JMS_IBM_CHARACTER_SET, CCSID);

			// Start the connection
			connection.start();

			sender.send(message);
			System.out.println("Sent message: " + messagetext);
			/*
			JMSMessage receivedMessage = (JMSMessage) receiver.receive(10000);
			System.out.println("\\nReceived message:\\n" + receivedMessage);
			 */
			sender.close();
			//			receiver.close();
			session.close();
			connection.close();

			System.out.println("\nSUCCESS\n");
		}
		catch (JMSException jmsex) {
			System.out.println(jmsex);
			System.out.println("\nFAILURE\n");
		}
		catch (Exception ex) {
			System.out.println(ex);
			System.out.println("\nFAILURE\n");
		}
	}
}
