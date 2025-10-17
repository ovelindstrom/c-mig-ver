/**
 * @since 2007-mar-02
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import se.csn.ark.common.dt.CsnDataTransferObjectImpl;


public class Bilaga  extends CsnDataTransferObjectImpl {

    private static final long serialVersionUID = 1L;
    // Nyckel
    private Long id;
    // Mimetyp. Kan vara null, i sådana fall används application/octetstream
    private String mimetyp;
    // Beskrivning av bilagan, får vara null
    private String filnamn;
    // Encoding för bilagan; måste inte anges
    private String encoding;
    // Data för bilagan, måste sättas
    private byte[] data;

    public Bilaga() {
    }

    public Bilaga(byte[] data) {
        this.data = data;
    }

    public Bilaga(byte[] data, String filnamn) {
        this.data = data;
        this.filnamn = filnamn;
    }

    public String getFilnamn() {
        return filnamn;
    }

    public void setFilnamn(String filnamn) {
        this.filnamn = filnamn;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getMimetyp() {
        return mimetyp;
    }

    public void setMimetyp(String mimetyp) {
        this.mimetyp = mimetyp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
