/**
 * Skapad 2007-mar-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import se.csn.ark.common.dt.CsnDataTransferObjectImpl;


public class Mottagare extends CsnDataTransferObjectImpl {

	private static final long serialVersionUID = 1L;
	private Long id;
    // Namn på mottagare, kan var null
    private String namn;
    // Mailadress, måste vara angiven och enligt format
    private String adress;

    // CSN-nummer, måste inte anges
    private Integer csnnummer;

    // Kan vara EPOST,EPOSTCC,EPOSTBCC och/eller SMS (kommaseparerat)
    // Måste inte anges. Tomt fält ger EPOST
    private String typ;
    // Status för denna meddelandedel.
    private Integer status;

    public Mottagare() {
    }


    public Mottagare(String adress) {
        this.adress = adress;
    }

    public Mottagare(String adress, String namn) {
        this.adress = adress;
        this.namn = namn;
    }

    public Mottagare(String adress, String namn, int csnnummer) {
        this.adress = adress;
        this.namn = namn;
        this.csnnummer = new Integer(csnnummer);
    }



    public Integer getCsnnummer() {
        return csnnummer;
    }
    public void setCsnnummer(Integer csnnummer) {
        this.csnnummer = csnnummer;
    }
    public String getAdress() {
        return adress;
    }
    public void setAdress(String adress) {
        this.adress = adress;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNamn() {
        return namn;
    }
    public void setNamn(String namn) {
        this.namn = namn;
    }
    public String getTyp() {
        return typ;
    }
    public void setTyp(String typ) {
        this.typ = typ;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }
}
