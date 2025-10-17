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


public class NotifieringResultat  extends CsnDataTransferObjectImpl {

    private static final long serialVersionUID = 1L;

    public static final int OK = 0,
        NOT = 1,
        VARNING = 2,
        FEL = 3;

    // Databas-id för det meddelande som skapades vid anropet
    private Long meddelandeId;
    // Resultatkod som matchar en av de ovanstående
    private Integer resultat;
    // Information om resultatet. Null om resultatet var OK, annars en beskrivande text
    private String info;

    public NotifieringResultat() {
        resultat = new Integer(0);
    }

    public NotifieringResultat(long meddelandeid) {
        resultat = new Integer(0);
        this.meddelandeId = new Long(meddelandeid);
    }

    public NotifieringResultat(long meddelandeid, int resultat, String text) {
        if (!((resultat == OK) || (resultat == NOT) || (resultat == VARNING) || (resultat == FEL))) {
            throw new IllegalArgumentException("Okänd resultatkod: " + resultat);
        }
        this.resultat = new Integer(resultat);
        this.meddelandeId = new Long(meddelandeid);
        this.info = text;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getMeddelandeId() {
        return meddelandeId;
    }

    public void setMeddelandeId(Long meddelandeId) {
        this.meddelandeId = meddelandeId;
    }

    public Integer getResultat() {
        return resultat;
    }

    public void setResultat(Integer resultat) {
        this.resultat = resultat;
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
