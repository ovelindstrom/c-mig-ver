/**
 * Skapad 2007-mar-02
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import se.csn.ark.common.dt.CsnDataTransferObjectImpl;

/**
 * Modellklass för Meddelande.
 *
 */
public class Meddelande extends CsnDataTransferObjectImpl {
	private static final long serialVersionUID = 1L;
	
    // Databasnyckel för spårbarhet och omsändning
    private Long id;
    // Självförklarande
    private Integer csnnummer;
    // Det som anges i Subject-raden, måste sättas
    private String rubrik;
    // Meddelandetext, måste sättas
    private String meddelandetext;
    // Encoding för rubrik; om inget anges används samma som i meddelandet
    private String rubrikEncoding;
    // Encoding för meddelande; om inget anges används iso-8859-1
    private String meddelandeEncoding;
    
    // X antal bilagor
    private Bilaga[] bilagor;
    // Datum. Om null så sätts det vid sändningstillfället, annars är det ok att sätta ett eget
    private Date skapad;
    // Datum då meddelandet skickades
    private Date skickat;
    // Tidpunkt då meddelandet SOM TIDIGAST får sändas. För att batcha mailutskick.
    private Date skickaTidigast;
    
    // Avsändare, måste vara satt
    private Avsandare avsandare;
    
    // Måste sätta minst en mottagare
    private Mottagare[] mottagare;
    
    // Händelser för meddelandet
    private MeddelandeHandelse[] handelser;
    
    // Callbackparametrar:
    private String callbackURL;
    private Integer callbackMask;
    
    //  Mimetyp.
    private String mimetyp;
    
    // Kanal. Olika meddelandekanaler kan ha olika prioritering.
    private String kanal;
    
    public Meddelande() {
        
    }
    
    public Meddelande(String rubrik, String text) {
        this.rubrik = rubrik;
        this.meddelandetext = text;
    }
    
    public Meddelande(String rubrik, String text, String mottagare, String avsandaradress) {
        this(rubrik, text, mottagare, avsandaradress, null, null, null);
    }
    
    public Meddelande(String rubrik, String text, String mottagare, String avsandaradress, String avsandarnamn) {
        this(rubrik, text, mottagare, avsandaradress, avsandarnamn, null, null);
    }

    public Meddelande(String rubrik, String text, String mottagare, String avsandaradress, String avsandarnamn, String applikation) {
    	this(rubrik, text, mottagare, avsandaradress, avsandarnamn, applikation, null);
    }
    
    public Meddelande(String rubrik, String text, String mottagare, String avsandaradress, String avsandarnamn, String applikation, String kanal) {
        this.rubrik = rubrik;
        this.meddelandetext = text;
        Mottagare mott = new Mottagare(mottagare);
        setMottagare(new Mottagare[]{mott});
        Avsandare avs = new Avsandare(avsandarnamn, avsandaradress, applikation, null);
        setAvsandare(avs);
        setKanal(kanal);
    }
    
    
    public Meddelande(String rubrik, String text, Mottagare[] mottagare) {
        this.rubrik = rubrik;
        this.meddelandetext = text;
        setMottagare(mottagare);
    }

    
    public boolean hasBilagor() {
        return (bilagor != null) && (bilagor.length > 0);
    }
    
    public void addBilaga(Bilaga bilaga) {
        if (bilaga == null) { return; }
        
        List<Bilaga> list = new ArrayList<Bilaga>();
        if (bilagor != null) {
            for (int i = 0; i < bilagor.length; i++) {
                list.add(bilagor[i]);
            }
        }
        list.add(bilaga);
        bilagor = (Bilaga[]) list.toArray(new Bilaga[0]);
    }
    
    
    public Integer getCsnnummer() {
        return csnnummer;
    }
    public void setCsnnummer(Integer csnnummer) {
        this.csnnummer = csnnummer;
    }
    public Integer getCallbackMask() {
        return callbackMask;
    }
    public void setCallbackMask(Integer callbackMask) {
        this.callbackMask = callbackMask;
    }
    public String getCallbackURL() {
        return callbackURL;
    }
    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }
    public Avsandare getAvsandare() {
        return avsandare;
    }
    public final void setAvsandare(Avsandare avsandare) {
        this.avsandare = avsandare;
    }
    public Bilaga[] getBilagor() {
        return bilagor;
    }
    public void setBilagor(Bilaga[] bilagor) {
        this.bilagor = bilagor;
    }
    public Bilaga getBilaga(int i) {
        return this.bilagor[i];
    }
    public MeddelandeHandelse[] getHandelser() {
        return handelser;
    }
    public void setHandelser(MeddelandeHandelse[] handelser) {
        this.handelser = handelser;
    }
    public void addHandelse(MeddelandeHandelse h) {
        if (h == null) { return; }
        
        List<MeddelandeHandelse> list = new ArrayList<MeddelandeHandelse>();
        if (handelser != null) {
            for (int i = 0; i < handelser.length; i++) {
                list.add(handelser[i]);
            }
        }
        list.add(h);
        handelser = (MeddelandeHandelse[]) list.toArray(new MeddelandeHandelse[0]);
    }
    public MeddelandeHandelse getHandelse(int i) {
        return this.handelser[i];
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMeddelandeEncoding() {
        return meddelandeEncoding;
    }
    public void setMeddelandeEncoding(String meddelandeEncoding) {
        this.meddelandeEncoding = meddelandeEncoding;
    }
    public String getMeddelandetext() {
        return meddelandetext;
    }
    public void setMeddelandetext(String meddelandetext) {
        this.meddelandetext = meddelandetext;
    }
    
    public Mottagare[] getMottagare() {
        return mottagare;
    }
    public final void setMottagare(Mottagare[] mottagare) {
        this.mottagare = mottagare;
    }
    public Mottagare getMottagare(int i) {
        return this.mottagare[i];
    }
    public void addMottagare(Mottagare mott) {
        if (mott == null) { return; }
        
        List<Mottagare> list = new ArrayList<Mottagare>();
        if (mottagare != null) {
            for (int i = 0; i < mottagare.length; i++) {
                list.add(mottagare[i]);
            }
        }
        list.add(mott);
        mottagare = (Mottagare[]) list.toArray(new Mottagare[0]);
    }
    
    public String getRubrik() {
        return rubrik;
    }
    public void setRubrik(String rubrik) {
        this.rubrik = rubrik;
    }
    public String getRubrikEncoding() {
        return rubrikEncoding;
    }
    public void setRubrikEncoding(String rubrikEncoding) {
        this.rubrikEncoding = rubrikEncoding;
    }
    public Date getSkapad() {
        return skapad;
    }
    public void setSkapad(Date skapad) {
        this.skapad = skapad;
    }
    
    public Date getSkickat() {
        return skickat;
    }
    public void setSkickat(Date skickat) {
        this.skickat = skickat;
    }
    public Date getSkickaTidigast() {
        return skickaTidigast;
    }
    public void setSkickaTidigast(Date skickaTidigast) {
        this.skickaTidigast = skickaTidigast;
    }
    public String getMimetyp() {
		return mimetyp;
	}
	public void setMimetyp(String mimetyp) {
		this.mimetyp = mimetyp;
	}
	public String getKanal() {
		return kanal;
	}
	public final void setKanal(String kanal) {
		this.kanal = kanal;
	}

	public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Id: "); sb.append(id); sb.append("\n");
        sb.append("Rubrik: "); sb.append(rubrik); sb.append('\n');
        sb.append("Text: "); sb.append(meddelandetext); sb.append('\n');
        sb.append("Skapad: "); sb.append(skapad); sb.append('\n');
        if (avsandare != null) {
            sb.append(avsandare.toString()); sb.append('\n');
        }
        if (mottagare != null) {
            for (int i = 0; i < mottagare.length; i++) {
                sb.append("Mottagare nr "); sb.append(i); sb.append(':'); 
                sb.append(mottagare[i].toString()); sb.append('\n');
            }
        }
        return sb.toString();
    }
    
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }
}
