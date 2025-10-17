/**
 * Skapad 2007-mar-02
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import se.csn.ark.common.dt.CsnDataTransferObjectImpl;


public class Avsandare extends CsnDataTransferObjectImpl {
    private static final long serialVersionUID = 1L;
    // ID för avsändare, används för spårbarhet mm
    private Long id;
    // Sändande applikation för spårbarhet, kan vara null
    private String applikation;
    // Kategori för spårbarhet, kan vara null
    private String kategori;
    // Namn på avsändare, kan vara null
    private String namn;
    // Mailadress för avsändare, måste vara satt och måste vara riktig adress
    private String epostadress;
    // Mailsvarsadress. Får vara null, måste vara riktig om satt
    private String replyTo;

    public Avsandare() {

    }

    public Avsandare(String namn, String adress) {
        this.namn = namn;
        this.epostadress = adress;
    }

    public Avsandare(String namn, String adress, String applikation, String kategori) {
        this.namn = namn;
        this.epostadress = adress;
        this.applikation = applikation;
        this.kategori = kategori;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplikation() {
        return applikation;
    }

    public void setApplikation(String applikation) {
        this.applikation = applikation;
    }

    public String getEpostadress() {
        return epostadress;
    }

    public void setEpostadress(String epostadress) {
        this.epostadress = epostadress;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

}
