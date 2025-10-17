package se.csn.webservice.bas.hanteraEDH;

import java.io.Serializable;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class HamtaFranEDHSvar  implements Serializable {
    private byte[] data;

    private int returkod;

    public HamtaFranEDHSvar() {
    }

    public HamtaFranEDHSvar(
           byte[] data,
           int returkod) {
        this.data = data;
        this.returkod = returkod;
    }


    /**
     * Gets the data value for this HamtaFranEDHSvar.
     * 
     * @return data
     */
    public byte[] getData() {
        return data;
    }


    /**
     * Sets the data value for this HamtaFranEDHSvar.
     * 
     * @param data
     */
    public void setData(byte[] data) {
        this.data = data;
    }


    /**
     * Gets the returkod value for this HamtaFranEDHSvar.
     * 
     * @return returkod
     */
    public int getReturkod() {
        return returkod;
    }


    /**
     * Sets the returkod value for this HamtaFranEDHSvar.
     * 
     * @param returkod
     */
    public void setReturkod(int returkod) {
        this.returkod = returkod;
    }

    private java.lang.Object __equalsCalc;

    @Override
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HamtaFranEDHSvar)) {
            return false;
        }
        HamtaFranEDHSvar other = (HamtaFranEDHSvar) obj;
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
            && ((this.data == null && other.getData() == null)
                || (this.data != null
                    && Arrays.equals(this.data, other.getData())))
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
        if (getData() != null) {
            for (int i = 0;
                 i < java.lang.reflect.Array.getLength(getData());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getData(), i);
                if (obj != null
                    && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getReturkod();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static TypeDesc typeDesc =
        new TypeDesc(HamtaFranEDHSvar.class, true);

    static {
        typeDesc.setXmlType(new QName("http://webservice.csn.se/bas/hanteraEDH", "hamtaFranEDHSvar"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/hanteraEDH", "data"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
