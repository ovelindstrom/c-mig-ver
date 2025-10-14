/*
 * Created on 2005-feb-24
 *
 */
package se.csn.ark.common.jms;

import javax.jms.QueueConnection;

/**
 * Denna klass innehåller grundläggande funktionalitet för kö hantering.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @version 1.0
 *
 */
public class Queue {

	private javax.jms.QueueConnection queueConnection;
	private javax.jms.Queue queue;

	/**
	 * Konstruktor för kontainer objektet
	 * @param con Anslutning
	 * @param queue kö
	 */
	Queue(QueueConnection con, javax.jms.Queue queue) {
		
		this.queueConnection = con;
		this.queue = queue;
	}
	
	/**
	 * @return queueConnection - köanslutning
	 */
	public QueueConnection getConnection() {
		return queueConnection;
	}

	/**
	 * @return queue - kö
	 */
	public javax.jms.Queue getQueue() {
		return queue;
	}

	/**
	 * Sträng representation av klassen
    * @see java.lang.Object#toString()
    */
   public String toString() {
		String str = "se.csn.ark.common.jms.Queue\n";
		
		str += "\tQueueConnection=" + queueConnection + "\n";
		str += "\tQueue=" + queue;
		return str;
	}
}