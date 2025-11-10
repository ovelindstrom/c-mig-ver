/*
 * Created on 2007-apr-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.csn.ark.common.util.logging.activity;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import se.csn.ark.common.util.logging.Log;

/**
 * @author csn7571
 * @since 2007-04-13
 * @version 1 Skapad -Marie-Helen Andersson
 */
public class ActivityRecord implements Activities {

    private static final Log ALOGG = Log.getInstance(ActivityRecord.class);


    // Nya tabellen
    private String lastAccessedTime;
    // private Date dbtidpunkt; //Hämtas som current_timestamp vid INSERT
    private String system;

    private String server;

    private String sessionId;

    private String strutsKod; // se ovan

    private String strutsAction; // klass

    private String handelse;

    private Integer csnNummer; // int

    private String personNummer; // double

    private String method;

    private String txId; // Transactionid

    private String handlaggare;

    private String inloggnMetod;

    private String ekundStatus;

    private String params;

    private String strutsError;
    
    /**
     * 
     *
     */
    public ActivityRecord() {
    	
    }

    /**
     * Skapa record (Ny tabell).
     * 
     * @param request
     * @param tjanstId
     * @param form
     * @param nastaMetod
     * @param csnNummer
     * @param personNummer
     * @param method
     * @param txId
     * @param handlaggare
     * @param inloggnMetod
     * @param meddelandesatt
     * @param ekundStatus
     * @param meddelande
     */
    public ActivityRecord(HttpServletRequest request, String strutsKod,
            String strutsAction, String handelse, Integer csnNummer,
            String personNummer, String method, String txId,
            String handlaggare, String inloggnMetod, String ekundStatus,
            String params, String strutsError) {

        this(request.getSession().getId(), strutsKod, strutsAction, handelse,
                csnNummer, personNummer, method, txId, handlaggare,
                inloggnMetod, ekundStatus, params, strutsError);
    }


    /**
     * Skapa record (Ny tabell).
     * 
     * @param sessionId
     * @param tjanstId
     * @param form
     * @param nastaMetod
     * @param csnNummer
     * @param personNummer
     * @param method
     * @param txId
     * @param handlaggare
     * @param inloggnMetod
     * @param meddelandeSatt
     * @param ekundStatus
     * @param meddelande
     */
    public ActivityRecord(String sessionId, String strutsKod,
            String strutsAction, String handelse, Integer csnNummer,
            String personNummer, String method, String txId,
            String handlaggare, String inloggnMetod, String ekundStatus,
            String params, String strutsError) {
        super();

        this.system = ALOGG.getSystem();
        this.server = ALOGG.getServer();

        this.sessionId = sessionId;
        this.strutsKod = strutsKod;
        this.strutsAction = strutsAction;
        this.handelse = handelse;
        this.csnNummer = csnNummer;
        if (personNummer.length() == 12) {
        	//Inledande sekel bort
        	this.personNummer = personNummer.substring(2);
        } else {
            this.personNummer = personNummer;
        }
        this.method = method;
        this.txId = txId;
        this.handlaggare = handlaggare;
        this.inloggnMetod = inloggnMetod;
        this.ekundStatus = ekundStatus;
        this.params = params;
        this.strutsError = strutsError;

        //QC 609 @TIMESTAMP@ i ark_log4j som apptidpunkt blir
        // tidpunkten då loggern flushar buffern till DB
        // Ta egen timestamp
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        lastAccessedTime = f.format(new GregorianCalendar().getTime());


    }

    /**
     * @return csn-nummer
     */
    public Integer getCsnNummer() {

        return csnNummer;
    }

    /**
     * @return personnummer
     */
    public String getPersonNummer() {

        return personNummer;
    }

    /**
     * @param csnNr
     *            csn-nummer
     */
    public void setCsnNummer(Integer csnNr) {

        csnNummer = csnNr;
    }

    /**
     * @param pNr
     *            personnummer
     */
    public void setPersonNummer(String pNr) {

        personNummer = pNr;
    }

    /**
     * @return Returns the inloggnMetod.
     */
    public String getInloggnMetod() {
        return inloggnMetod;
    }

    /**
     * @param inloggnMetod
     *            The inloggnMetod to set.
     */
    public void setInloggnMetod(String inloggnMetod) {
        this.inloggnMetod = inloggnMetod;
    }

    /**
     * @return Returns the lastAccessedTime.
     */
    public String getLastAccessedTime() {
        return lastAccessedTime;
    }

    /**
     * @param lastAccessedTime
     *            The lastAccessedTime to set.
     */
    public void setLastAccessedTime(String lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    /**
     * @return Returns the server.
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server
     *            The server to set.
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return Returns the session.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId session
     *            The session to set.
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return Returns the system.
     */
    public String getSystem() {
        return system;
    }

    /**
     * @param system
     *            The system to set.
     */
    public void setSystem(String system) {
        this.system = system;
    }

    /**
     * @return Returns the txId.
     */
    public String getTxId() {
        return txId;
    }

    /**
     * @param txId
     *            The txId to set.
     */
    public void setTxId(String txId) { 
        this.txId = txId;
    }

    /**
     * @return Returns the ekundStatus.
     */
    public String getEkundStatus() {
        return ekundStatus;
    }

    /**
     * @param ekundStatus
     *            The ekundStatus to set.
     */
    public void setEkundStatus(String ekundStatus) {
        this.ekundStatus = ekundStatus;
    }


    /**
     * @return Returns the handlaggare.
     */
    public String getHandlaggare() {
        return handlaggare;
    }

    /**
     * @param handlaggare
     *            The handlaggare to set.
     */
    public void setHandlaggare(String handlaggare) {
        this.handlaggare = handlaggare;
    }

    /**
     * @return Returns the method.
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method
     *            The method to set.
     */
    public void setMethod(String method) {
        this.method = method;
    }


    /**
     * @return Returns the handelse.
     */
    public String getHandelse() {
        return handelse;
    }

    /**
     * @param handelse
     *            The handelse to set.
     */
    public void setHandelse(String handelse) {
        this.handelse = handelse;
    }

    /**
     * @return Returns the params.
     */
    public String getParams() {
        return params;
    }

    /**
     * @param params
     *            The params to set.
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * @return Returns the strutsAction.
     */
    public String getStrutsAction() {
        return strutsAction;
    }

    /**
     * @param strutsAction
     *            The strutsAction to set.
     */
    public void setStrutsAction(String strutsAction) {
        this.strutsAction = strutsAction;
    }

    /**
     * @return Returns the getStrutsError.
     */
    public String getStrutsError() {
        return strutsError;
    }

    /**
     * @param strutsError
     *            The strutsError to set.
     */
    public void setStrutsError(String strutsError) {
        this.strutsError = strutsError;
    }

    /**
     * @return strutsKod the strutsKod.
     */
    public String getStrutsKod() {
        return strutsKod;
    }

    /**
     * @param strutsKod
     *            The strutsKod to set.
     */
    public void setStrutsKod(String strutsKod) {
        this.strutsKod = strutsKod;
    }
}
