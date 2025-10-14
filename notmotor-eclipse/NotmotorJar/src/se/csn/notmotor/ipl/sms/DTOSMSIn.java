/*
 * Created on 2005-feb-10
 *
 */
package se.csn.notmotor.ipl.sms;

/**
 * @author csn7511
 *
 */
public class DTOSMSIn {

	private String telnummer;
	private String rubrik;
	private String meddelande;
	private String applikationsnamn;
	private String funktionsnamn;
	private static final int MAXRUBRIKLANGD = 11;
	private static final int MAXMEDDELANDELANGD = 160;
	private static final int MINTELELANGD = 10;
	
	/**
	 * Parameterlös konstruktor för att uppfylla beankontraktet.
	 *
	 */
	public DTOSMSIn() {
	}
	
	public DTOSMSIn(String telnr, String rubrik, String meddelande, 
			String applikationsnamn, String funktionsnamn) {
	    setTelnummer(telnr);
	    setRubrik(rubrik);
	    setMeddelande(meddelande);
	    this.applikationsnamn = applikationsnamn;
	    this.funktionsnamn = funktionsnamn;
	}

	public String getMeddelande() {
		return meddelande;
	}

	public String getTelnummer() {
		return telnummer;
	}

	public final void setMeddelande(String meddelande) {
	    if((meddelande != null) && (meddelande.length() > MAXMEDDELANDELANGD)) {
	        throw new IllegalArgumentException("Meddelandetexten får inte vara längre än 160 tecken, var " + meddelande.length() + " tecken lång");    
	    }
	    this.meddelande = meddelande;
	}

	public final void setTelnummer(String telnr) {
	    if(telnr == null || telnr.length() < MINTELELANGD) {
	        throw new IllegalArgumentException("Ett SMS-mottagarnummer måste vara minst 10 tecken långt");
	    }
		telnummer = telnr;
	}

	public String getRubrik() {
		return rubrik;
	}

	public final void setRubrik(String rubrik) {
		if (rubrik == null || rubrik.length() < 1) {
			throw new IllegalArgumentException("SMS-rubriken måste vara minst 1 tecken");
		}
		if (rubrik.length() > MAXRUBRIKLANGD) {
			throw new IllegalArgumentException("SMS-rubriken får inte vara längre än 11 tecken");
		}
		this.rubrik = rubrik;
	}

	public String getApplikationsnamn() {
        return applikationsnamn;
    }
    public void setApplikationsnamn(String applikationsnamn) {
        this.applikationsnamn = applikationsnamn;
    }
    public String getFunktionsnamn() {
        return funktionsnamn;
    }
    public void setFunktionsnamn(String funktionsnamn) {
        this.funktionsnamn = funktionsnamn;
    }
    public String toString() {
        return "Tel: " + telnummer + " App: " + applikationsnamn 
        	+ " Funk: " + funktionsnamn + " Rubrik: " + rubrik + " Text: " + meddelande;
    }
    
}
