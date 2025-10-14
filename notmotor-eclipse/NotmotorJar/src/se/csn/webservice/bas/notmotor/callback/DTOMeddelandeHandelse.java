/**
 * DTOMeddelandeHandelse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.callback;

public class DTOMeddelandeHandelse  implements java.io.Serializable {
    private java.lang.Long id;

    private java.lang.Integer handelsetyp;

    private java.util.Calendar tidpunkt;

    private java.lang.Integer felkod;

    private java.lang.String feltext;

    private java.lang.Integer instans;

    public DTOMeddelandeHandelse() {
    }

    public DTOMeddelandeHandelse(
           java.lang.Long id,
           java.lang.Integer handelsetyp,
           java.util.Calendar tidpunkt,
           java.lang.Integer felkod,
           java.lang.String feltext,
           java.lang.Integer instans) {
           this.id = id;
           this.handelsetyp = handelsetyp;
           this.tidpunkt = tidpunkt;
           this.felkod = felkod;
           this.feltext = feltext;
           this.instans = instans;
    }


    /**
     * Gets the id value for this DTOMeddelandeHandelse.
     * 
     * @return id
     */
    public java.lang.Long getId() {
        return id;
    }


    /**
     * Sets the id value for this DTOMeddelandeHandelse.
     * 
     * @param id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }


    /**
     * Gets the handelsetyp value for this DTOMeddelandeHandelse.
     * 
     * @return handelsetyp
     */
    public java.lang.Integer getHandelsetyp() {
        return handelsetyp;
    }


    /**
     * Sets the handelsetyp value for this DTOMeddelandeHandelse.
     * 
     * @param handelsetyp
     */
    public void setHandelsetyp(java.lang.Integer handelsetyp) {
        this.handelsetyp = handelsetyp;
    }


    /**
     * Gets the tidpunkt value for this DTOMeddelandeHandelse.
     * 
     * @return tidpunkt
     */
    public java.util.Calendar getTidpunkt() {
        return tidpunkt;
    }


    /**
     * Sets the tidpunkt value for this DTOMeddelandeHandelse.
     * 
     * @param tidpunkt
     */
    public void setTidpunkt(java.util.Calendar tidpunkt) {
        this.tidpunkt = tidpunkt;
    }


    /**
     * Gets the felkod value for this DTOMeddelandeHandelse.
     * 
     * @return felkod
     */
    public java.lang.Integer getFelkod() {
        return felkod;
    }


    /**
     * Sets the felkod value for this DTOMeddelandeHandelse.
     * 
     * @param felkod
     */
    public void setFelkod(java.lang.Integer felkod) {
        this.felkod = felkod;
    }


    /**
     * Gets the feltext value for this DTOMeddelandeHandelse.
     * 
     * @return feltext
     */
    public java.lang.String getFeltext() {
        return feltext;
    }


    /**
     * Sets the feltext value for this DTOMeddelandeHandelse.
     * 
     * @param feltext
     */
    public void setFeltext(java.lang.String feltext) {
        this.feltext = feltext;
    }


    /**
     * Gets the instans value for this DTOMeddelandeHandelse.
     * 
     * @return instans
     */
    public java.lang.Integer getInstans() {
        return instans;
    }


    /**
     * Sets the instans value for this DTOMeddelandeHandelse.
     * 
     * @param instans
     */
    public void setInstans(java.lang.Integer instans) {
        this.instans = instans;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DTOMeddelandeHandelse)) return false;
        DTOMeddelandeHandelse other = (DTOMeddelandeHandelse) obj;
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
            ((this.handelsetyp==null && other.getHandelsetyp()==null) ||
             (this.handelsetyp!=null &&
              this.handelsetyp.equals(other.getHandelsetyp()))) &&
            ((this.tidpunkt==null && other.getTidpunkt()==null) ||
             (this.tidpunkt!=null &&
              this.tidpunkt.equals(other.getTidpunkt()))) &&
            ((this.felkod==null && other.getFelkod()==null) ||
             (this.felkod!=null &&
              this.felkod.equals(other.getFelkod()))) &&
            ((this.feltext==null && other.getFeltext()==null) ||
             (this.feltext!=null &&
              this.feltext.equals(other.getFeltext()))) &&
            ((this.instans==null && other.getInstans()==null) ||
             (this.instans!=null &&
              this.instans.equals(other.getInstans())));
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
        if (getHandelsetyp() != null) {
            _hashCode += getHandelsetyp().hashCode();
        }
        if (getTidpunkt() != null) {
            _hashCode += getTidpunkt().hashCode();
        }
        if (getFelkod() != null) {
            _hashCode += getFelkod().hashCode();
        }
        if (getFeltext() != null) {
            _hashCode += getFeltext().hashCode();
        }
        if (getInstans() != null) {
            _hashCode += getInstans().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DTOMeddelandeHandelse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "DTOMeddelandeHandelse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("handelsetyp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "handelsetyp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tidpunkt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "tidpunkt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("felkod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "felkod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("feltext");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "feltext"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instans");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "instans"));
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
