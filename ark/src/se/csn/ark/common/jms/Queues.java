/*
 * Created on 2005-feb-23
 *
 */
package se.csn.ark.common.jms;

/**
 * Klass för att skapa konstanter.
 * @author K-G Sjöström - AcandoFrontec
 * @version 1.01
 *
 */

public interface Queues {

	static final String QUEUE_JNDI_BASE = "java:comp/env/";

	static final String QUEUE_FACTORY_KEY = "jms.factory"; 

	static final String QUEUE1_KEY = "jms.queue1"; 

	static final String QUEUE2_KEY = "jms.queue2"; 

	/**
	 * JMS kö nr 1.
	 * 
	 * Konfigureras med property <b>jms.queue1</b>.
	 */
	public static final int QUEUE1 = 0;

	/**
	 * JMS kö nr 2.
	 * 
	 * Konfigureras med property <b>jms.queue2</b>.
	 */
	public static final int QUEUE2 = 1;


}
