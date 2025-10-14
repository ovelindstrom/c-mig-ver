/**
 * DTOAvsandare.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.skicka;

public class DTOAvsandare  implements java.io.Serializable {
    private java.lang.Long id;

    private java.lang.String applikation;

    private java.lang.String kategori;

    private java.lang.String namn;

    private java.lang.String epostadress;

    private java.lang.String replyTo;

    public DTOAvsandare() {
    }

    public DTOAvsandare(
           java.lang.Long id,
           java.lang.String applikation,
           java.lang.String kategori,
           java.lang.String namn,
           java.lang.String epostadress,
           java.lang.String replyTo) {
           this.id = id;
           this.applikation = applikation;
           this.kategori = kategori;
           this.namn = namn;
           this.epostadress = epostadress;
           this.replyTo = replyTo;
    }


    /**
     * Gets the id value for this DTOAvsandare.
     * 
     * @return id
     */
    public java.lang.Long getId() {
        return id;
    }


    /**
     * Sets the id value for this DTOAvsandare.
     * 
     * @param id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }


    /**
     * Gets the applikation value for this DTOAvsandare.
     * 
     * @return applikation
     */
    public java.lang.String getApplikation() {
        return applikation;
    }


    /**
     * Sets the applikation value for this DTOAvsandare.
     * 
     * @param applikation
     */
    public void setApplikation(java.lang.String applikation) {
        this.applikation = applikation;
    }


    /**
     * Gets the kategori value for this DTOAvsandare.
     * 
     * @return kategori
     */
    public java.lang.String getKategori() {
        return kategori;
    }


    /**
     * Sets the kategori value for this DTOAvsandare.
     * 
     * @param kategori
     */
    public void setKategori(java.lang.String kategori) {
        this.kategori = kategori;
    }


    /**
     * Gets the namn value for this DTOAvsandare.
     * 
     * @return namn
     */
    public java.lang.String getNamn() {
        return namn;
    }


    /**
     * Sets the namn value for this DTOAvsandare.
     * 
     * @param namn
     */
    public void setNamn(java.lang.String namn) {
        this.namn = namn;
    }


    /**
     * Gets the epostadress value for this DTOAvsandare.
     * 
     * @return epostadress
     */
    public java.lang.String getEpostadress() {
        return epostadress;
    }


    /**
     * Sets the epostadress value for this DTOAvsandare.
     * 
     * @param epostadress
     */
    public void setEpostadress(java.lang.String epostadress) {
        this.epostadress = epostadress;
    }


    /**
     * Gets the replyTo value for this DTOAvsandare.
     * 
     * @return replyTo
     */
    public java.lang.String getReplyTo() {
        return replyTo;
    }


    /**
     * Sets the replyTo value for this DTOAvsandare.
     * 
     * @param replyTo
     */
    public void setReplyTo(java.lang.String replyTo) {
        this.replyTo = replyTo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DTOAvsandare)) return false;
        DTOAvsandare other = (DTOAvsandare) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.applikation==null && other.getApplikation()==null) || 
             (this.applikation!=null &&
              this.applikation.equals(other.getApplikation()))) &&
            ((this.kategori==null && other.getKategori()==null) || 
             (this.kategori!=null &&
              this.kategori.equals(other.getKategori()))) &&
            ((this.namn==null && other.getNamn()==null) || 
             (this.namn!=null &&
              this.namn.equals(other.getNamn()))) &&
            ((this.epostadress==null && other.getEpostadress()==null) || 
             (this.epostadress!=null &&
              this.epostadress.equals(other.getEpostadress()))) &&
            ((this.replyTo==null && other.getReplyTo()==null) || 
             (this.replyTo!=null &&
              this.replyTo.equals(other.getReplyTo())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getApplikation() != null) {
            _hashCode += getApplikation().hashCode();
        }
        if (getKategori() != null) {
            _hashCode += getKategori().hashCode();
        }
        if (getNamn() != null) {
            _hashCode += getNamn().hashCode();
        }
        if (getEpostadress() != null) {
            _hashCode += getEpostadress().hashCode();
        }
        if (getReplyTo() != null) {
            _hashCode += getReplyTo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DTOAvsandare.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOAvsandare"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("applikation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "applikation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kategori");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "kategori"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("namn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "namn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("epostadress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "epostadress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("replyTo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "replyTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
