/**
 * SparaTillEDHFraga.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.hanteraEDH;

public class SparaTillEDHFraga  implements java.io.Serializable {
    private byte[] data;

    private java.lang.String ocrstrang;

    private java.lang.String signatur;

    private java.lang.String myndighetsstampling;

    private java.lang.String csnnr;

    private java.lang.String arendelopnr;

    private java.lang.String inhandling;

    private java.lang.String timestamp;

    public SparaTillEDHFraga() {
    }

    public SparaTillEDHFraga(
           byte[] data,
           java.lang.String ocrstrang,
           java.lang.String signatur,
           java.lang.String myndighetsstampling,
           java.lang.String csnnr,
           java.lang.String arendelopnr,
           java.lang.String inhandling,
           java.lang.String timestamp) {
        this.data = data;
        this.ocrstrang = ocrstrang;
        this.signatur = signatur;
        this.myndighetsstampling = myndighetsstampling;
        this.csnnr = csnnr;
        this.arendelopnr = arendelopnr;
        this.inhandling = inhandling;
        this.timestamp = timestamp;
    }


    /**
     * Gets the data value for this SparaTillEDHFraga.
     * 
     * @return data
     */
    public byte[] getData() {
        return data;
    }


    /**
     * Sets the data value for this SparaTillEDHFraga.
     * 
     * @param data
     */
    public void setData(byte[] data) {
        this.data = data;
    }


    /**
     * Gets the ocrstrang value for this SparaTillEDHFraga.
     * 
     * @return ocrstrang
     */
    public java.lang.String getOcrstrang() {
        return ocrstrang;
    }


    /**
     * Sets the ocrstrang value for this SparaTillEDHFraga.
     * 
     * @param ocrstrang
     */
    public void setOcrstrang(java.lang.String ocrstrang) {
        this.ocrstrang = ocrstrang;
    }


    /**
     * Gets the signatur value for this SparaTillEDHFraga.
     * 
     * @return signatur
     */
    public java.lang.String getSignatur() {
        return signatur;
    }


    /**
     * Sets the signatur value for this SparaTillEDHFraga.
     * 
     * @param signatur
     */
    public void setSignatur(java.lang.String signatur) {
        this.signatur = signatur;
    }


    /**
     * Gets the myndighetsstampling value for this SparaTillEDHFraga.
     * 
     * @return myndighetsstampling
     */
    public java.lang.String getMyndighetsstampling() {
        return myndighetsstampling;
    }


    /**
     * Sets the myndighetsstampling value for this SparaTillEDHFraga.
     * 
     * @param myndighetsstampling
     */
    public void setMyndighetsstampling(java.lang.String myndighetsstampling) {
        this.myndighetsstampling = myndighetsstampling;
    }


    /**
     * Gets the csnnr value for this SparaTillEDHFraga.
     * 
     * @return csnnr
     */
    public java.lang.String getCsnnr() {
        return csnnr;
    }


    /**
     * Sets the csnnr value for this SparaTillEDHFraga.
     * 
     * @param csnnr
     */
    public void setCsnnr(java.lang.String csnnr) {
        this.csnnr = csnnr;
    }


    /**
     * Gets the arendelopnr value for this SparaTillEDHFraga.
     * 
     * @return arendelopnr
     */
    public java.lang.String getArendelopnr() {
        return arendelopnr;
    }


    /**
     * Sets the arendelopnr value for this SparaTillEDHFraga.
     * 
     * @param arendelopnr
     */
    public void setArendelopnr(java.lang.String arendelopnr) {
        this.arendelopnr = arendelopnr;
    }


    /**
     * Gets the inhandling value for this SparaTillEDHFraga.
     * 
     * @return inhandling
     */
    public java.lang.String getInhandling() {
        return inhandling;
    }


    /**
     * Sets the inhandling value for this SparaTillEDHFraga.
     * 
     * @param inhandling
     */
    public void setInhandling(java.lang.String inhandling) {
        this.inhandling = inhandling;
    }


    /**
     * Gets the timestamp value for this SparaTillEDHFraga.
     * 
     * @return timestamp
     */
    public java.lang.String getTimestamp() {
        return timestamp;
    }


    /**
     * Sets the timestamp value for this SparaTillEDHFraga.
     * 
     * @param timestamp
     */
    public void setTimestamp(java.lang.String timestamp) {
        this.timestamp = timestamp;
    }

    private java.lang.Object __equalsCalc = null;

    @Override
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SparaTillEDHFraga)) return false;
        SparaTillEDHFraga other = (SparaTillEDHFraga) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.data == null && other.getData() == null) ||
                (this.data != null &&
                    java.util.Arrays.equals(this.data, other.getData()))) &&
            ((this.ocrstrang == null && other.getOcrstrang() == null) ||
                (this.ocrstrang != null &&
                    this.ocrstrang.equals(other.getOcrstrang()))) &&
            ((this.signatur == null && other.getSignatur() == null) ||
                (this.signatur != null &&
                    this.signatur.equals(other.getSignatur()))) &&
            ((this.myndighetsstampling == null && other.getMyndighetsstampling() == null) ||
                (this.myndighetsstampling != null &&
                    this.myndighetsstampling.equals(other.getMyndighetsstampling()))) &&
            ((this.csnnr == null && other.getCsnnr() == null) ||
                (this.csnnr != null &&
                    this.csnnr.equals(other.getCsnnr()))) &&
            ((this.arendelopnr == null && other.getArendelopnr() == null) ||
                (this.arendelopnr != null &&
                    this.arendelopnr.equals(other.getArendelopnr()))) &&
            ((this.inhandling == null && other.getInhandling() == null) ||
                (this.inhandling != null &&
                    this.inhandling.equals(other.getInhandling()))) &&
            ((this.timestamp == null && other.getTimestamp() == null) ||
                (this.timestamp != null &&
                    this.timestamp.equals(other.getTimestamp())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    @Override
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getData() != null) {
            for (int i = 0;
                 i < java.lang.reflect.Array.getLength(getData());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getData(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOcrstrang() != null) {
            _hashCode += getOcrstrang().hashCode();
        }
        if (getSignatur() != null) {
            _hashCode += getSignatur().hashCode();
        }
        if (getMyndighetsstampling() != null) {
            _hashCode += getMyndighetsstampling().hashCode();
        }
        if (getCsnnr() != null) {
            _hashCode += getCsnnr().hashCode();
        }
        if (getArendelopnr() != null) {
            _hashCode += getArendelopnr().hashCode();
        }
        if (getInhandling() != null) {
            _hashCode += getInhandling().hashCode();
        }
        if (getTimestamp() != null) {
            _hashCode += getTimestamp().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SparaTillEDHFraga.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "sparaTillEDHFraga"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ocrstrang");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "ocrstrang"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signatur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "signatur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("myndighetsstampling");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "myndighetsstampling"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("csnnr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "csnnr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arendelopnr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "arendelopnr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inhandling");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "inhandling"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timestamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "timestamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
