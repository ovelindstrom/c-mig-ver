/*
 * Created on 2005-feb-24
 *
 */
package se.csn.ark.common.jms;

import se.csn.ark.common.util.logging.Log;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.QueueReceiver;
import javax.jms.Session;


/**
 *     Namn :    Reciver
 *
 *     Beskrivning: Klass för att kunna ta emot meddelande som skickas via jms kö
 *
 *
 * @author csnnlm
 * @version 1.01
 *
 */
public class Reciver extends JmsQueue {
    private static Log log = Log.getInstance(Reciver.class);
    private QueueReceiver receiver = null;

    /**
     * åppnar en mottagarekö med som inte transaktionshanteras och med angivet
     * AcknowledgeMode
     *
     * @param acknowledgeMode AUTO_ACKNOWLEDGE,CLIENT_ACKNOWLEDGE,DUPS_OK_ACKNOWLEDGE
     * @throws JMSException åppnandet av mottagarkö misslyckdes.
     */
    public void openReciver(int acknowledgeMode) throws JMSException {
        // Create a new queue receiver using the queue session. 
        // The queue receiver should be created to receive 
        // messages from the queue q. 
        try {
            getQConnection().start();
            setSession(getQConnection().createQueueSession(false, acknowledgeMode));
            receiver = getSession().createReceiver(getQue());
        } catch (JMSException e) {
            log.error(
                      "Misslyckades med att skapa en reciver i Reciver::openReciver(): ",
                      e);
            throw e;
        }
    }




    /**
     *  åppnar en mottagarekö med som inte transaktionshanteras och med AUTO_ACKNOWLEDGE
     * @throws JMSException åppnandet av mottagarkö misslyckdes.
     */
    public void openReciver() throws JMSException {
        try {
            getQConnection().start();
            setSession(getQConnection().createQueueSession(false, Session.AUTO_ACKNOWLEDGE));
            receiver = getSession().createReceiver(getQue());
        } catch (JMSException e) {
            log.error(
                      "Misslyckades med att skapa en reciver i Reciver::openReciver(): ",
                      e);
            throw e;
        }
    }




    /**
     * Kollar om det finns ett meddelande att hämta från angiven kö, i så fall
     * returneras objektet annars null. Väntar inte på att ett meddelande skall komma.
     * @return object - Object.
     * @throws JMSException Hämtande av meddelande från kö misslyckades.
     */
    public Object receive() throws JMSException {
        Object object = null;

        try {
            ObjectMessage message = (ObjectMessage)receiver.receiveNoWait();

            if (message != null) {
                object = message.getObject();
            }
        } catch (JMSException e) {
            log.error(
                      "Fel vid hämtande av meddelande i Reciver::receive(): ",
                      e);
            throw e;
        }

        return object;
    }




    /**
     * Stänger kopplingen mot recivern, queueconnection och sessionen
     * @throws JMSException Fel Vid stänging av mottagren eller sessionen.
     *
     */
    
    public void close() throws JMSException {
        try {
			receiver.close();
            super.close();
            getSession().close();
        } catch (JMSException e) {
            log.error("Misslycakdes att stänga reciver eller session", e);
            throw e;
        }
    }
}