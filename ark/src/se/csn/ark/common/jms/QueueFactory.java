/*
 * Created on 2005-feb-22
 *
 */
package se.csn.ark.common.jms;

import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.Log;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 *  Namn : QueueFactory <br>
 * 
 *  Beskrivning:
 *      Hämtar en Köanslutning och kö enligt det som angivets i 
 *  properties filen "ark.properties"
 * 
 * @author csnnlm
 * @version 1.0
 *
 */

public final class QueueFactory implements Queues {
    private static final String JMS_PROP_QCF = "jms.factory.standalone";
    private static final String JMS_PROP_Q = "jms.queue1.standalone";
    private static Queue standAloneCon;
    private static Context initialContext;
    private static Log log = Log.getInstance(QueueFactory.class);

    /**
     * Privat konstruktor, endast statisk åtkomst
     */
    private QueueFactory() {
    }




    /**
     * Ger en kökoppling enligt angiven typ.
     *
     * @param queueType Typ enligt Queues.
     * @return En köförbindelse enligt angiven typ.
     * @throws QueueException Om förbindelse ej kan skapas.
     * @see Queues
     */
    public static synchronized Queue getQueue(int queueType)
                                       throws QueueException {
        QueueConnection queueConnection = null;
        javax.jms.Queue jmsQueue = null;
        Queue queue = null;

        if (log.isDebugEnabled()) {
            log.debug("getQueue, queueType=" + queueType);
        }

        if (standAloneCon != null) {
            return standAloneCon;
        }

        try {
            if (initialContext == null) {
                initialContext = new javax.naming.InitialContext();
            }

            // Hämta koppling till JMS från fabriken.
            queueConnection = getConnection();

            // Hämta kön.
            switch (queueType) {
            case QUEUE1:
                jmsQueue = getQueue(QUEUE1_KEY);

                break;

            case QUEUE2:
                jmsQueue = getQueue(QUEUE2_KEY);

                break;

            default:
                throw new QueueException("Okänd typ för kö, queueType="
                                         + queueType);
            }

            // Plocka ihop eget köobjekt.
            queue = new Queue(queueConnection, jmsQueue);

            if (log.isDebugEnabled()) {
                log.debug("getQueue, queue=" + queue);
            }
        } catch (QueueException je) {
            // Om det är ett JmsException så kommer det från getConnection(String queueKey)
            // och är redan loggat så släng bara vidare.
            throw je;
        } catch (Exception e) {
            // Här fångar vi bara Exception eftersom om det går snett här så är
            // det kört, dvs ingen idé att särskilja vad som inte funkar.
            String errMsg = "Kan ej skapa köförbindelse";

            log.fatal(errMsg, e);
            throw new QueueException(errMsg, e);
        }

        return queue;
    }




    /**
    * @param queueKey Namn på önskad kö
    * @return jmsQueue - sökt kö
    * @throws QueueException Misslyckas att hämta önskad köanslutning
    */
   private static javax.jms.Queue getQueue(String queueKey)
                                     throws QueueException {
        javax.jms.Queue jmsQueue = null;
        String queueValue = null;

        try {
            queueValue = Properties.getProperty(queueKey);
            jmsQueue = (javax.jms.Queue)initialContext.lookup(queueValue);

            if (log.isDebugEnabled()) {
                log.debug("jmsQueue=" + jmsQueue);
            }
        } catch (NamingException ne) {
            String errMsg = "Kan ej hämta kö.";

            if (queueValue != null) {
                errMsg += (" Key=" + queueKey + ", property value="
                + queueValue);
            }

            log.fatal(errMsg, ne);
            throw new QueueException(errMsg, ne);
        }

        return jmsQueue;
    }




    /**
     * Kämtar kö angiven i properiesfilen. 
    * @return QueConnetion - kö anslutning
    * @throws QueueException Något går fel, och kö anslutning kan ej erhållas
    */
   private static QueueConnection getConnection() throws QueueException {
        QueueConnection queueConnection = null;
        QueueConnectionFactory qFac;
        String factoryValue = null;

        try {
            factoryValue = Properties.getProperty(QUEUE_FACTORY_KEY);
            qFac = (QueueConnectionFactory)initialContext.lookup(factoryValue);
            queueConnection = qFac.createQueueConnection();

            if (log.isDebugEnabled()) {
                log.debug("QueueConnectionFactory=" + qFac);
            }
        } catch (NamingException ne) {
            String errMsg = "Kan ej skapa köfabrik.";

            if (factoryValue != null) {
                errMsg += (" Key=" + QUEUE_FACTORY_KEY + ", property value="
                + factoryValue);
            }

            log.fatal(errMsg, ne);
            throw new QueueException(errMsg, ne);
        } catch (JMSException je) {
            String errMsg = "Kan ej skapa köförbindelse";

            if (factoryValue != null) {
                errMsg += (" för " + factoryValue + ". Key="
                + QUEUE_FACTORY_KEY + ", property value=" + factoryValue);
            }

            log.fatal(errMsg, je);
            throw new QueueException(errMsg, je);
        }

        return queueConnection;
    }




    /**
     * Skapar en fristående koppling utan inblandning av WAS för att kunna
     * köra JUnit testfall.
     *
     * @throws Exception Om kö ansltning inte kan erhållas
     */
    public static void createStandAloneConnection() throws Exception {
        try {

            InitialContext context = new InitialContext();

            if (log.isDebugEnabled()) {
                log.debug("QueueConnectionFactory qcf = "
                               + Properties.getProperty(JMS_PROP_QCF));
                log.debug("QueueConnectionFactory qcf =  (QueueConnectionFactory)"
                    + " context.lookup(Properties.getProperty(JMS_PROP_QCF));");
            }
            //  queue connection factory from the initial context.
            QueueConnectionFactory qcf =
                (QueueConnectionFactory)context.lookup(Properties.getProperty(JMS_PROP_QCF));

            // Create a new queue connection from the queue connection factory. 
            QueueConnection queueConnection = qcf.createQueueConnection();

            // Lookup the queue to be used to send and receive messages from the initial context.
            String strQ = Properties.getProperty(JMS_PROP_QCF);
            javax.jms.Queue q = (javax.jms.Queue)context.lookup(strQ);

            if (log.isDebugEnabled()) {
                log.debug("createStandAloneConnection for Q = " + strQ);
            }

            standAloneCon = new Queue(queueConnection, q);
        } catch (Exception e) {
            throw new Exception("Kan ej skapa fristående kö", e);
        }
    }
}