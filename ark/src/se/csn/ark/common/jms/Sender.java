/*
 * Created on 2005-feb-24
 *
 */
package se.csn.ark.common.jms;

import se.csn.ark.common.util.logging.Log;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.QueueSender;

/**
 *     Namn :    Sender
 *
 *     Beskrivning:Klass för att kunna skicka meddelande via jms kö
 *
 * @author csnnlm
 * @version 1.01
 *
 */
public class Sender extends JmsQueue {
    private static Log log = Log.getInstance(Sender.class);
    private QueueSender sender = null;
    private ObjectMessage sendMessage = null;

    /**
     * Öppnar en sändar kö (QueueSender)som inte transaktionshanteras och med AUTO_ACKNOWLEDGE
     * @throws JMSException Kastas om ett meddelandeobjekt inte kunde skapas. 
     */
    public void openSender() throws JMSException {
        // Create a new queue sender using the queue session. 
        // The sender should be created to send messages to the queue q. 
        try {
            getQConnection().start();
            setSession(getQConnection().createQueueSession(false, AUTO_ACKNOWLEDGE));
            sender = getSession().createSender(getQue());

            // Create a ObjectMessage using the queue session. 
            sendMessage = getSession().createObjectMessage();
        } catch (JMSException e) {
            log.error(
                      "Misslyckades med att skapa en QueueSender i Sender::openSender(): ",
                      e);
            throw e;
        }
    }




    /**
     * Skickar ett meddelande via den öppnade kön, objektet måste ärva av Serializable
     * @param  s - Serializable, Det serialiserbara objektet som skall skickas.
     * @throws JMSException Om meddelandet inte kunde sändas.
     */
    public void sendMessage(Serializable s) throws JMSException {
        // Send the sendMessage object using the queue sender.
        try {
            sendMessage.setObject(s);
            sender.send(sendMessage);

        } catch (JMSException e) {
            log.error(
                "Misslyckades med att skicka meddelandet i Sender::sendMessage(Serializable): ",
                 e);
            throw e;
        }
    }
    
    /**
     * Stänger sender, queueconnection samt sessionen
     * @throws JMSException vid fel på sender.close eller session.close
     */
    public void close() throws JMSException {
		try {
			sender.close();
			super.close();
			getSession().close();
		} catch (JMSException e) {
			log.error("Misslycakdes att stänga sender eller session", e);
			throw e;
		}
    }
}