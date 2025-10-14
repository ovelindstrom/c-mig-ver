/**
 * HamtaFranEDHFraga.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.hanteraEDH;

public class HamtaFranEDHFraga  implements java.io.Serializable {
    private java.lang.String famID;

    public HamtaFranEDHFraga() {
    }

    public HamtaFranEDHFraga(
           java.lang.String famID) {
           this.famID = famID;
    }


    /**
     * Gets the famID value for this HamtaFranEDHFraga.
     * 
     * @return famID
     */
    public java.lang.String getFamID() {
        return famID;
    }


    /**
     * Sets the famID value for this HamtaFranEDHFraga.
     * 
     * @param famID
     */
    public void setFamID(java.lang.String famID) {
        this.famID = famID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HamtaFranEDHFraga)) return false;
        HamtaFranEDHFraga other = (HamtaFranEDHFraga) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.famID==null && other.getFamID()==null) || 
             (this.famID!=null &&
              this.famID.equals(other.getFamID())));
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
        if (getFamID() != null) {
            _hashCode += getFamID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HamtaFranEDHFraga.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "hamtaFranEDHFraga"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("famID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "famID"));
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
