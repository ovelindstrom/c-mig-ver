/*
 * Created on 2005-feb-23
 *
 */
package se.csn.ark.common.jms;

import se.csn.ark.common.util.logging.Log;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;


/**
 *    Namn :    JmsQue
 *
 *    Beskrivning:
 *  klass för att skapa kö anslutning och tillhandahålla grund objekten
 *    QueueConnection, QueueSession och Queue. 
 *
 *
 * @author csnnlm
 * @version 1.0
 *
 */
public class JmsQueue {
    public static final int AUTO_ACKNOWLEDGE = Session.AUTO_ACKNOWLEDGE;
    public static final int CLIENT_ACKNOWLEDGE = Session.CLIENT_ACKNOWLEDGE;
    public static final int DUPS_OK_ACKNOWLEDGE = Session.DUPS_OK_ACKNOWLEDGE;
    private static Log log = Log.getInstance(JmsQueue.class);
    private QueueConnection qConnection = null;
    private QueueSession session = null;
    private Queue que = null;
    private se.csn.ark.common.jms.Queue queueConnection = null;

    /**
     * Hämtar angiven jndi kö via QueueFactory
     * @param jmsSource Queues.QUEUE1 eller Queues.QUEUE2
     * @throws QueueException Vid misslyckande att få connectiom eller kö
     */
    public void getQueueConnetion(int jmsSource) throws QueueException {
        try {
            queueConnection = QueueFactory.getQueue(jmsSource);
            qConnection = queueConnection.getConnection();
            que = queueConnection.getQueue();
        } catch (QueueException e) {
            log.fatal("Misslyckades med att skapa Kö Koppling", e);
            throw e;
        }
    }
    
    /**
     * Stänger anslutningen.
     * @throws JMSException fel vid stängning av anslutning
    */
   public void close() throws JMSException {
        qConnection.close();
    }
    /**
     * @return koppling till kön
     */
    protected QueueConnection getQConnection() {
        return qConnection;
    }

    /**
     * @return kö
     */
    protected Queue getQue() {
        return que;
    }

    /**
     * @return session
     */
    protected QueueSession getSession() {
        return session;
    }

    /**
     * @param connection sätt koppling
     */
    protected void setQConnection(QueueConnection connection) {
        qConnection = connection;
    }

    /**
     * @param q sätt kö
     */
    protected void setQue(Queue q) {
        que = q;
    }

    /**
     * @param qs sätt session
     */
    protected void setSession(QueueSession qs) {
        this.session = qs;
    }

}