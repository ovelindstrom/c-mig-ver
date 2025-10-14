/**
 * DTOBilaga.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.callback;

public class DTOBilaga  implements java.io.Serializable {
    private java.lang.Long id;

    private java.lang.String mimetyp;

    private java.lang.String filnamn;

    private java.lang.String encoding;

    private byte[] data;

    public DTOBilaga() {
    }

    public DTOBilaga(
           java.lang.Long id,
           java.lang.String mimetyp,
           java.lang.String filnamn,
           java.lang.String encoding,
           byte[] data) {
           this.id = id;
           this.mimetyp = mimetyp;
           this.filnamn = filnamn;
           this.encoding = encoding;
           this.data = data;
    }


    /**
     * Gets the id value for this DTOBilaga.
     * 
     * @return id
     */
    public java.lang.Long getId() {
        return id;
    }


    /**
     * Sets the id value for this DTOBilaga.
     * 
     * @param id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }


    /**
     * Gets the mimetyp value for this DTOBilaga.
     * 
     * @return mimetyp
     */
    public java.lang.String getMimetyp() {
        return mimetyp;
    }


    /**
     * Sets the mimetyp value for this DTOBilaga.
     * 
     * @param mimetyp
     */
    public void setMimetyp(java.lang.String mimetyp) {
        this.mimetyp = mimetyp;
    }


    /**
     * Gets the filnamn value for this DTOBilaga.
     * 
     * @return filnamn
     */
    public java.lang.String getFilnamn() {
        return filnamn;
    }


    /**
     * Sets the filnamn value for this DTOBilaga.
     * 
     * @param filnamn
     */
    public void setFilnamn(java.lang.String filnamn) {
        this.filnamn = filnamn;
    }


    /**
     * Gets the encoding value for this DTOBilaga.
     * 
     * @return encoding
     */
    public java.lang.String getEncoding() {
        return encoding;
    }


    /**
     * Sets the encoding value for this DTOBilaga.
     * 
     * @param encoding
     */
    public void setEncoding(java.lang.String encoding) {
        this.encoding = encoding;
    }


    /**
     * Gets the data value for this DTOBilaga.
     * 
     * @return data
     */
    public byte[] getData() {
        return data;
    }


    /**
     * Sets the data value for this DTOBilaga.
     * 
     * @param data
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DTOBilaga)) return false;
        DTOBilaga other = (DTOBilaga) obj;
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
            ((this.mimetyp==null && other.getMimetyp()==null) ||
             (this.mimetyp!=null &&
              this.mimetyp.equals(other.getMimetyp()))) &&
            ((this.filnamn==null && other.getFilnamn()==null) ||
             (this.filnamn!=null &&
              this.filnamn.equals(other.getFilnamn()))) &&
            ((this.encoding==null && other.getEncoding()==null) ||
             (this.encoding!=null &&
              this.encoding.equals(other.getEncoding()))) &&
            ((this.data==null && other.getData()==null) ||
             (this.data!=null &&
              java.util.Arrays.equals(this.data, other.getData())));
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
        if (getMimetyp() != null) {
            _hashCode += getMimetyp().hashCode();
        }
        if (getFilnamn() != null) {
            _hashCode += getFilnamn().hashCode();
        }
        if (getEncoding() != null) {
            _hashCode += getEncoding().hashCode();
        }
        if (getData() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getData());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getData(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DTOBilaga.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "DTOBilaga"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mimetyp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "mimetyp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filnamn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "filnamn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encoding");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "encoding"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
