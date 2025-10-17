package se.csn.webservice.bas.hanteraEDH;

import java.io.Serializable;

import javax.xml.namespace.QName;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class HamtaFranEDHFraga  implements Serializable {
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

    private java.lang.Object __equalsCalc;

    @Override
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HamtaFranEDHFraga)) {
            return false;
        }
        HamtaFranEDHFraga other = (HamtaFranEDHFraga) obj;
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
                    && this.famID.equals(other.getFamID())));
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static TypeDesc typeDesc =
        new TypeDesc(HamtaFranEDHFraga.class, true);

    static {
        typeDesc.setXmlType(new QName("http://webservice.csn.se/bas/hanteraEDH", "hamtaFranEDHFraga"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("famID");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/hanteraEDH", "famID"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
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
