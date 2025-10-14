/**
 * Skapad 2007-mar-02
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import se.csn.ark.common.dt.CsnDataTransferObjectImpl;


public class MeddelandeHandelse  extends CsnDataTransferObjectImpl {

    private static final long serialVersionUID = 1L;
	// Händelsetyp
    public static final int
    	// Notmotorn har tagit emot meddelandet och lagt det i databasen
		MOTTAGET = 1,

		// Meddelandet skickat till mailserver el dyl för vidare befordran
		SKICKAT_SERVER = 2,

		// Meddelandet kom i retur; detta ska normalt inte hända. Kan bero på att
		// mottagarens mailbox är full, att meddelandet fastnat i spamfilter, 
		// att användaren inte finns, autoreply etc etc
		BESVARAT = 4,

		// Meddelandet togs bort av användande applikation
		BORTTAGET = 8,

		// Kunde inte sända meddelandet till mailservern pga fel i meddelandet
		MEDDELANDEFEL = 16,

		// Kunde inte sända meddelandet till mailservern pga bruten koppling
		TEKNISKT_FEL = 32,

    	// Värde som kan användas om man är intresserad av alla händelser
    	ALLA_HANDELSER = Integer.MAX_VALUE;


    public static String getTyptext(int typ) {
        switch(typ) {
        	case MeddelandeHandelse.BESVARAT : return "Besvarat";
        	case MeddelandeHandelse.MOTTAGET : return "Mottaget";
        	case MeddelandeHandelse.SKICKAT_SERVER : return "Sänt";
        	case MeddelandeHandelse.BORTTAGET : return "Borttaget";
        	case MeddelandeHandelse.MEDDELANDEFEL : return "Fel i meddelande";
        	case MeddelandeHandelse.TEKNISKT_FEL : return "Tekniskt fel";
        	default : if(typ < 0) {
        	    return "Under sändning (instans " + (-typ) + ")";
        	} else {
        	    return "Okänd (" + typ + ")";
        	}
        }
    }


    // Felkoder:
    public static final int OK = 0, // dvs inget fel
    						FELAKTIG_MOTTAGARE = 1,
    						FELAKTIG_DOMAN = 2,
    						FELAKTIG_SUBJECT = 4,
    						FELAKTIG_TEXTENCODING = 8,
    						FELAKTIG_RUBRIKENCODING = 16,
    						FELAKTIG_BILAGEMIMETYP = 32,
    						FELAKTIG_AVSANDARE = 64,
    						ALLMANT_FEL = 128,
    						STOPPANDE_FEL = 256,

    						OKANT_FEL = 1073741824; // 2^30
	
    public static String getKodtext(int kod) {
        switch(kod) {
        	case MeddelandeHandelse.OK : return "";
        	case MeddelandeHandelse.FELAKTIG_MOTTAGARE : return "Felaktig mottagare";
        	case MeddelandeHandelse.FELAKTIG_DOMAN : return "Felaktig domän";
        	case MeddelandeHandelse.FELAKTIG_SUBJECT : return "Felaktigt ämne";
        	case MeddelandeHandelse.FELAKTIG_TEXTENCODING : return "Felaktig textencoding";
        	case MeddelandeHandelse.FELAKTIG_RUBRIKENCODING : return "Felaktig rubrikencoding";
        	case MeddelandeHandelse.FELAKTIG_BILAGEMIMETYP : return "Felaktig bilagemimetyp";
        	case MeddelandeHandelse.FELAKTIG_AVSANDARE : return "Felaktig avsändare";
        	case MeddelandeHandelse.ALLMANT_FEL : return "Allmänt fel";
        	case MeddelandeHandelse.STOPPANDE_FEL : return "Stoppande fel";
        	case MeddelandeHandelse.OKANT_FEL : return "Okänt fel";
        	default : return "Okänd (" + kod + ")";
        }
    }

    // Databasnyckel för spårbarhet
    private Long id;
    // Flagga som beskriver vad som skedde, exempelvis SÄND, ADRESSAT OKÄND, OMSÄND, EJ KONTAKT
    private Integer handelsetyp;
    // När händelsen ägde rum
    private Date tidpunkt;
	// En av ovanstående felkoder. 
	private Integer felkod;
	// Detaljerad feltext som pekar ut vilket data som var felaktigt (om något).
	private String feltext;
	// Nyckel till den programinstans som genererade händelsen
	private Integer instans;

	public MeddelandeHandelse() {
	}


	public MeddelandeHandelse(int handelsetyp) {
	    this.handelsetyp = new Integer(handelsetyp);
	    tidpunkt = new Date();
	    felkod = new Integer(OK);
	}

	public MeddelandeHandelse(int handelsetyp, int felkod, String felmeddelande) {
	    this.handelsetyp = new Integer(handelsetyp);
	    tidpunkt = new Date();
	    this.felkod = new Integer(felkod);
	    this.feltext = felmeddelande;
	}




    public Integer getFelkod() {
        return felkod;
    }
    public void setFelkod(Integer felkod) {
        this.felkod = felkod;
    }
    public String getFeltext() {
        return feltext;
    }
    public void setFeltext(String feltext) {
        this.feltext = feltext;
    }
    public Integer getHandelsetyp() {
        return handelsetyp;
    }
    public void setHandelsetyp(Integer handelsetyp) {
        this.handelsetyp = handelsetyp;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getTidpunkt() {
        return tidpunkt;
    }
    public void setTidpunkt(Date tidpunkt) {
        this.tidpunkt = tidpunkt;
    }

    public Integer getInstans() {
        return instans;
    }
    public void setInstans(Integer instans) {
        this.instans = instans;
    }
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
}
