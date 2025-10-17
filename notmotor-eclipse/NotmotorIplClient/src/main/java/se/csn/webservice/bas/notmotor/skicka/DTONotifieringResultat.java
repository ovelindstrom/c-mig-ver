/**
 * DTONotifieringResultat.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.skicka;

import java.io.Serializable;

import javax.xml.namespace.QName;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class DTONotifieringResultat  implements Serializable {
    private long meddelandeId;

    private java.lang.Integer resultat;

    private java.lang.String info;

    public DTONotifieringResultat() {
    }

    public DTONotifieringResultat(
           long meddelandeId,
           java.lang.Integer resultat,
           java.lang.String info) {
        this.meddelandeId = meddelandeId;
        this.resultat = resultat;
        this.info = info;
    }


    /**
     * Gets the meddelandeId value for this DTONotifieringResultat.
     * 
     * @return meddelandeId
     */
    public long getMeddelandeId() {
        return meddelandeId;
    }


    /**
     * Sets the meddelandeId value for this DTONotifieringResultat.
     * 
     * @param meddelandeId
     */
    public void setMeddelandeId(long meddelandeId) {
        this.meddelandeId = meddelandeId;
    }


    /**
     * Gets the resultat value for this DTONotifieringResultat.
     * 
     * @return resultat
     */
    public java.lang.Integer getResultat() {
        return resultat;
    }


    /**
     * Sets the resultat value for this DTONotifieringResultat.
     * 
     * @param resultat
     */
    public void setResultat(java.lang.Integer resultat) {
        this.resultat = resultat;
    }


    /**
     * Gets the info value for this DTONotifieringResultat.
     * 
     * @return info
     */
    public java.lang.String getInfo() {
        return info;
    }


    /**
     * Sets the info value for this DTONotifieringResultat.
     * 
     * @param info
     */
    public void setInfo(java.lang.String info) {
        this.info = info;
    }

    private java.lang.Object __equalsCalc;

    @Override
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DTONotifieringResultat)) {
            return false;
        }
        DTONotifieringResultat other = (DTONotifieringResultat) obj;
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
            && this.meddelandeId == other.getMeddelandeId()
            && ((this.resultat == null && other.getResultat() == null)
                || (this.resultat != null
                    && this.resultat.equals(other.getResultat())))
            && ((this.info == null && other.getInfo() == null)
                || (this.info != null
                    && this.info.equals(other.getInfo())));
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
        _hashCode += new Long(getMeddelandeId()).hashCode();
        if (getResultat() != null) {
            _hashCode += getResultat().hashCode();
        }
        if (getInfo() != null) {
            _hashCode += getInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static TypeDesc typeDesc =
        new TypeDesc(DTONotifieringResultat.class, true);

    static {
        typeDesc.setXmlType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTONotifieringResultat"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("meddelandeId");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "meddelandeId"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("resultat");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "resultat"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("info");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "info"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
