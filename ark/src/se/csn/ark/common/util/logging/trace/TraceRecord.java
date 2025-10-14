package se.csn.ark.common.util.logging.trace;

import se.csn.ark.common.dt.CsnDataTransferObject;

/**
 * @author K-G Sjöström - AcandoFrontec
 * @since 2005-04-27
 * @version 1 Skapad - K-G Sjöström, AcandoFrontec
 */
public class TraceRecord implements Traceable {

	private Integer csnNummer;
	private Double personNummer;
	private String authType;
	private String handelse;
	private String transactionId;
	private String felMeddelande;


	/**
     * Skapa record
     * 
     * @param authType identifieringsmetod som ska loggas
     * @param felMeddelande felmeddelande som ska loggas
     */
    public TraceRecord() {

        this(null, null, null, null, null, null);
    }

    /**
     * Skapa record
     * 
     * @param authType identifieringsmetod som ska loggas
     * @param felMeddelande felmeddelande som ska loggas
     */
    public TraceRecord(String authType, String felMeddelande) {

        this(null, null, null, authType, null, felMeddelande);
    }

    /**
     * Skapa record
     * 
     * @param csnNummer som ska loggas
     * @param dto innehåller transaktionsid och händelse som ska loggas
     */
    public TraceRecord(Integer csnNummer, 
            CsnDataTransferObject dto) {

        this(dto, csnNummer, null, null, null);
    }
                    
	/**
     * @param csnNummer som ska loggas
     * @param dto innehåller transaktionsid och händelse som ska loggas
     * @param felMeddelande felmeddelande som ska loggas
	 */
	public TraceRecord(Integer csnNummer, 
			CsnDataTransferObject dto, String felMeddelande) {

		this(dto, csnNummer, null, null, felMeddelande);
	}
					
	/**
     * @param csnNummer som ska loggas
	 * @param personNummer som ska loggas
     * @param dto innehåller transaktionsid och händelse som ska loggas
	 */
	public TraceRecord(Integer csnNummer, Double personNummer, 
			CsnDataTransferObject dto) {

		this(dto, csnNummer, personNummer, null, null);
	}
					
	/**
     * @param csnNummer som ska loggas
     * @param personNummer som ska loggas
     * @param dto innehåller transaktionsid och händelse som ska loggas
     * @param authType identifieringsmetod som ska loggas
	 */
	public TraceRecord(Integer csnNummer, Double personNummer, 
			CsnDataTransferObject dto, String authType) {
		
		this(dto, csnNummer, personNummer, authType, null);
	}
	
	/**
     * @param dto innehåller transaktionsid och händelse som ska loggas
     * @param csnNummer som ska loggas
     * @param personNummer som ska loggas
     * @param authType identifieringsmetod som ska loggas
     * @param felMeddelande felmeddelande som ska loggas
	 */
	public TraceRecord(CsnDataTransferObject dto, Integer csnNummer, 
			Double personNummer, String authType,
			 String felMeddelande) {
	
		this.csnNummer = csnNummer;
		this.personNummer = personNummer;
		this.authType = authType;
		this.felMeddelande = felMeddelande;
		if (dto != null) {
			this.handelse = dto.getEvent();
			this.transactionId = dto.getTransactionId();
		} else {	 	
			this.handelse = null;
			this.transactionId = null;
		}
	}

	/**
	 * @param transactionId som ska loggas
	 * @param csnNummer som ska loggas
	 * @param personNummer som ska loggas
	 * @param authType identifieringsmetod som ska loggas
	 * @param handelse som ska loggas
	 * @param felMeddelande som ska loggas
	 */
	public TraceRecord(String transactionId, Integer csnNummer, 
			Double personNummer, String authType, 
			String handelse, String felMeddelande) {
			
		this.csnNummer = csnNummer;
		this.personNummer = personNummer;
		this.authType = authType;
		this.handelse = handelse;
		this.transactionId = transactionId;
		this.felMeddelande = felMeddelande;
	}	
			

	/**
	 * @return identifieringsmetod
	 */
	public String getAuthType() {
		
		return authType;
	}

	/**
	 * @return csn-nummer
	 */
	public Integer getCsnNummer() {
		
		return csnNummer;
	}

	/**
	 * @return felmeddelande
	 */
	public String getFelMeddelande() {
		
		return felMeddelande;
	}

	/**
	 * @return händelse
	 */
	public String getHandelse() {
		
		return handelse;
	}

	/**
	 * @return personnummer
	 */
	public Double getPersonNummer() {
		
		return personNummer;
	}

	/**
	 * @return transaktionsid
	 */
	public String getTransactionId() {
		
		return transactionId;
	}

	/**
	 * @param type identifieringsmetod
	 */
	public void setAuthType(String type) {
		
		this.authType = type;
	}

	/**
	 * @param csnNr csn-nummer
	 */
	public void setCsnNummer(Integer csnNr) {
		
		csnNummer = csnNr;
	}

	/**
	 * @param meddelande meddelande
	 */
	public void setFelMeddelande(String meddelande) {
		
		felMeddelande = meddelande;
	}

	/**
	 * @param h händelse
	 */
	public void setHandelse(String h) {
		
		this.handelse = h;
	}

	/**
	 * @param pNr personnummer
	 */
	public void setPersonNummer(Double pNr) {
		
		personNummer = pNr;
	}

	/**
	 * @param id transaktionsid
	 */
	public void setTransactionId(String id) {
		
		transactionId = id;
	}

}
