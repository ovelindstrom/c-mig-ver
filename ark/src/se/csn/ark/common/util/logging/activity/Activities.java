/*
 * Created on 2007-apr-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.csn.ark.common.util.logging.activity;

import org.apache.log4j.MDC;


/**
 *
 * Gränssnitt för det som skall kunna loggas av aktivitetsloggningen 
 *
 * @author csn7571
 * @since 2007-04-13
 * @version 1 Skapad - Marie-Helen Andersson
 */
public interface Activities {
	
	/**
	 * Ger kundens CSN nummer.
	 * 
	 * @return CSN nummer
	 */
	public Integer getCsnNummer();

	/**
	 * Ger kundens personnummer.
	 * 
	 * @return Personnummer
	 */
	public String getPersonNummer();

	/**
	 * Ger TransactionsId.
	 * 
	 * @return TransactionsId
	 */
	public String getTxId();

	/**
	 * 
	 * @return identifierings-typ
	 */
	public String getInloggnMetod();
		
	/**
	 * @return Returns the lastAccessedTime.
	 */
	public String getLastAccessedTime();
	
	/**
	 * @return Returns the server.
	 */
	public String getServer();
	
	/**
	 * @return Returns the session.
	 */
	public String getSessionId();
	
	/**
	 * @return Returns the system.
	 */
	public String getSystem();
				
	   		
	/**
	 * @return Returns the handlaggare.
	 */
	public String getHandlaggare();       
   		
	/**
	 * @return Returns the method.
	 */
	public String getMethod();       
   		
   		
	/**
	 * @return Returns the ekundstatus.
	 */
	public String getEkundStatus();       
   		
    /**
     * @return Returns the Handelse.
     */
    public String getHandelse();
    
    /**
     * @return Returns the Params.
     */
    public String getParams();    

    /**
     * @return Returns the StrutsAction.
     */
    public String getStrutsAction();

    /**
     * @return Returns the StrutsError.
     */
    public String getStrutsError();

    /**
     * @return Returns the StrutsKod.
     */
    public String getStrutsKod();

 }
