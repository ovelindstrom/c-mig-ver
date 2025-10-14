/**
 * DTOMottagare.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.skicka;

public class DTOMottagare  implements java.io.Serializable {
    private java.lang.Long id;

    private java.lang.String namn;

    private java.lang.String adress;

    private java.lang.Integer csnnummer;

    private java.lang.String typ;

    private java.lang.Integer status;

    public DTOMottagare() {
    }

    public DTOMottagare(
           java.lang.Long id,
           java.lang.String namn,
           java.lang.String adress,
           java.lang.Integer csnnummer,
           java.lang.String typ,
           java.lang.Integer status) {
        this.id = id;
        this.namn = namn;
        this.adress = adress;
        this.csnnummer = csnnummer;
        this.typ = typ;
        this.status = status;
    }


    /**
     * Gets the id value for this DTOMottagare.
     * 
     * @return id
     */
    public java.lang.Long getId() {
        return id;
    }


    /**
     * Sets the id value for this DTOMottagare.
     * 
     * @param id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }


    /**
     * Gets the namn value for this DTOMottagare.
     * 
     * @return namn
     */
    public java.lang.String getNamn() {
        return namn;
    }


    /**
     * Sets the namn value for this DTOMottagare.
     * 
     * @param namn
     */
    public void setNamn(java.lang.String namn) {
        this.namn = namn;
    }


    /**
     * Gets the adress value for this DTOMottagare.
     * 
     * @return adress
     */
    public java.lang.String getAdress() {
        return adress;
    }


    /**
     * Sets the adress value for this DTOMottagare.
     * 
     * @param adress
     */
    public void setAdress(java.lang.String adress) {
        this.adress = adress;
    }


    /**
     * Gets the csnnummer value for this DTOMottagare.
     * 
     * @return csnnummer
     */
    public java.lang.Integer getCsnnummer() {
        return csnnummer;
    }


    /**
     * Sets the csnnummer value for this DTOMottagare.
     * 
     * @param csnnummer
     */
    public void setCsnnummer(java.lang.Integer csnnummer) {
        this.csnnummer = csnnummer;
    }


    /**
     * Gets the typ value for this DTOMottagare.
     * 
     * @return typ
     */
    public java.lang.String getTyp() {
        return typ;
    }


    /**
     * Sets the typ value for this DTOMottagare.
     * 
     * @param typ
     */
    public void setTyp(java.lang.String typ) {
        this.typ = typ;
    }


    /**
     * Gets the status value for this DTOMottagare.
     * 
     * @return status
     */
    public java.lang.Integer getStatus() {
        return status;
    }


    /**
     * Sets the status value for this DTOMottagare.
     * 
     * @param status
     */
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DTOMottagare)) return false;
        DTOMottagare other = (DTOMottagare) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.id == null && other.getId() == null) ||
                (this.id != null &&
                    this.id.equals(other.getId()))) &&
            ((this.namn == null && other.getNamn() == null) ||
                (this.namn != null &&
                    this.namn.equals(other.getNamn()))) &&
            ((this.adress == null && other.getAdress() == null) ||
                (this.adress != null &&
                    this.adress.equals(other.getAdress()))) &&
            ((this.csnnummer == null && other.getCsnnummer() == null) ||
                (this.csnnummer != null &&
                    this.csnnummer.equals(other.getCsnnummer()))) &&
            ((this.typ == null && other.getTyp() == null) ||
                (this.typ != null &&
                    this.typ.equals(other.getTyp()))) &&
            ((this.status == null && other.getStatus() == null) ||
                (this.status != null &&
                    this.status.equals(other.getStatus())));
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
        if (getNamn() != null) {
            _hashCode += getNamn().hashCode();
        }
        if (getAdress() != null) {
            _hashCode += getAdress().hashCode();
        }
        if (getCsnnummer() != null) {
            _hashCode += getCsnnummer().hashCode();
        }
        if (getTyp() != null) {
            _hashCode += getTyp().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DTOMottagare.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMottagare"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
        elemField.setFieldName("adress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "adress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("csnnummer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "csnnummer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typ");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "typ"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
