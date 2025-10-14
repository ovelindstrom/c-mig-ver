/**
 * SparaTillEDHSvar.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.hanteraEDH;

public class SparaTillEDHSvar  implements java.io.Serializable {
    private java.lang.String famID;

    private int returkod;

    public SparaTillEDHSvar() {
    }

    public SparaTillEDHSvar(
           java.lang.String famID,
           int returkod) {
           this.famID = famID;
           this.returkod = returkod;
    }


    /**
     * Gets the famID value for this SparaTillEDHSvar.
     * 
     * @return famID
     */
    public java.lang.String getFamID() {
        return famID;
    }


    /**
     * Sets the famID value for this SparaTillEDHSvar.
     * 
     * @param famID
     */
    public void setFamID(java.lang.String famID) {
        this.famID = famID;
    }


    /**
     * Gets the returkod value for this SparaTillEDHSvar.
     * 
     * @return returkod
     */
    public int getReturkod() {
        return returkod;
    }


    /**
     * Sets the returkod value for this SparaTillEDHSvar.
     * 
     * @param returkod
     */
    public void setReturkod(int returkod) {
        this.returkod = returkod;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SparaTillEDHSvar)) return false;
        SparaTillEDHSvar other = (SparaTillEDHSvar) obj;
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
              this.famID.equals(other.getFamID()))) &&
            this.returkod == other.getReturkod();
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
        _hashCode += getReturkod();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SparaTillEDHSvar.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "sparaTillEDHSvar"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("famID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "famID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returkod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/hanteraEDH", "returkod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
