package se.csn.webservice.bas.hanteraEDH;

import java.io.Serializable;

import javax.xml.namespace.QName;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class SparaTillEDHSvar  implements Serializable {
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

    private java.lang.Object __equalsCalc;

    @Override
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SparaTillEDHSvar)) {
            return false;
        }
        SparaTillEDHSvar other = (SparaTillEDHSvar) obj;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (__equalsCalc != null) {
            return __equalsCalc == obj;
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true
            && ((this.famID == null && other.getFamID() == null)
                || (this.famID != null
                    && this.famID.equals(other.getFamID())))
            && this.returkod == other.getReturkod();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc;

    @Override
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
    private static TypeDesc typeDesc =
        new TypeDesc(SparaTillEDHSvar.class, true);

    static {
        typeDesc.setXmlType(new QName("http://webservice.csn.se/bas/hanteraEDH", "sparaTillEDHSvar"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("famID");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/hanteraEDH", "famID"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("returkod");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/hanteraEDH", "returkod"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static Serializer getSerializer(
           java.lang.String mechType,
           java.lang.Class _javaType,
           QName _xmlType) {
        return
            new  BeanSerializer(
                _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static Deserializer getDeserializer(
           java.lang.String mechType,
           java.lang.Class _javaType,
           QName _xmlType) {
        return
            new  BeanDeserializer(
                _javaType, _xmlType, typeDesc);
    }

}
