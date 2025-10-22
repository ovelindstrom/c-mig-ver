package se.csn.webservice.bas.notmotor.skicka;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;

import javax.xml.namespace.QName;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class DTOMeddelande  implements Serializable {
    private java.lang.Long id;

    private java.lang.Integer csnnummer;

    private java.lang.String rubrik;

    private java.lang.String meddelandetext;

    private java.lang.String rubrikEncoding;

    private java.lang.String meddelandeEncoding;

    private DTOBilaga[] bilagor;

    private Calendar skapad;

    private Calendar skickat;

    private Calendar skickaTidigast;

    private DTOAvsandare avsandare;

    private DTOMottagare[] mottagare;

    private DTOMeddelandeHandelse[] handelser;

    private java.lang.String callbackURL;

    private java.lang.Integer callbackMask;

    private java.lang.String mimetyp;

    private java.lang.String kanal;

    public DTOMeddelande() {
    }

    public DTOMeddelande(
           java.lang.Long id,
           java.lang.Integer csnnummer,
           java.lang.String rubrik,
           java.lang.String meddelandetext,
           java.lang.String rubrikEncoding,
           java.lang.String meddelandeEncoding,
           DTOBilaga[] bilagor,
           Calendar skapad,
           Calendar skickat,
           Calendar skickaTidigast,
           DTOAvsandare avsandare,
           DTOMottagare[] mottagare,
           DTOMeddelandeHandelse[] handelser,
           java.lang.String callbackURL,
           java.lang.Integer callbackMask,
           java.lang.String mimetyp,
           java.lang.String kanal) {
        this.id = id;
        this.csnnummer = csnnummer;
        this.rubrik = rubrik;
        this.meddelandetext = meddelandetext;
        this.rubrikEncoding = rubrikEncoding;
        this.meddelandeEncoding = meddelandeEncoding;
        this.bilagor = bilagor;
        this.skapad = skapad;
        this.skickat = skickat;
        this.skickaTidigast = skickaTidigast;
        this.avsandare = avsandare;
        this.mottagare = mottagare;
        this.handelser = handelser;
        this.callbackURL = callbackURL;
        this.callbackMask = callbackMask;
        this.mimetyp = mimetyp;
        this.kanal = kanal;
    }


    /**
     * Gets the id value for this DTOMeddelande.
     * 
     * @return id
     */
    public java.lang.Long getId() {
        return id;
    }


    /**
     * Sets the id value for this DTOMeddelande.
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }


    /**
     * Gets the csnnummer value for this DTOMeddelande.
     * 
     * @return csnnummer
     */
    public java.lang.Integer getCsnnummer() {
        return csnnummer;
    }


    /**
     * Sets the csnnummer value for this DTOMeddelande.
     */
    public void setCsnnummer(java.lang.Integer csnnummer) {
        this.csnnummer = csnnummer;
    }


    /**
     * Gets the rubrik value for this DTOMeddelande.
     * 
     * @return rubrik
     */
    public java.lang.String getRubrik() {
        return rubrik;
    }


    /**
     * Sets the rubrik value for this DTOMeddelande.
     */
    public void setRubrik(java.lang.String rubrik) {
        this.rubrik = rubrik;
    }


    /**
     * Gets the meddelandetext value for this DTOMeddelande.
     * 
     * @return meddelandetext
     */
    public java.lang.String getMeddelandetext() {
        return meddelandetext;
    }


    /**
     * Sets the meddelandetext value for this DTOMeddelande.
     */
    public void setMeddelandetext(java.lang.String meddelandetext) {
        this.meddelandetext = meddelandetext;
    }


    /**
     * Gets the rubrikEncoding value for this DTOMeddelande.
     * 
     * @return rubrikEncoding
     */
    public java.lang.String getRubrikEncoding() {
        return rubrikEncoding;
    }


    /**
     * Sets the rubrikEncoding value for this DTOMeddelande.
     */
    public void setRubrikEncoding(java.lang.String rubrikEncoding) {
        this.rubrikEncoding = rubrikEncoding;
    }


    /**
     * Gets the meddelandeEncoding value for this DTOMeddelande.
     * 
     * @return meddelandeEncoding
     */
    public java.lang.String getMeddelandeEncoding() {
        return meddelandeEncoding;
    }


    /**
     * Sets the meddelandeEncoding value for this DTOMeddelande.
     */
    public void setMeddelandeEncoding(java.lang.String meddelandeEncoding) {
        this.meddelandeEncoding = meddelandeEncoding;
    }


    /**
     * Gets the bilagor value for this DTOMeddelande.
     * 
     * @return bilagor
     */
    public DTOBilaga[] getBilagor() {
        return bilagor;
    }


    /**
     * Sets the bilagor value for this DTOMeddelande.
     */
    public void setBilagor(DTOBilaga[] bilagor) {
        this.bilagor = bilagor;
    }

    public DTOBilaga getBilagor(int i) {
        return this.bilagor[i];
    }

    public void setBilagor(int i, DTOBilaga _value) {
        this.bilagor[i] = _value;
    }


    /**
     * Gets the skapad value for this DTOMeddelande.
     * 
     * @return skapad
     */
    public Calendar getSkapad() {
        return skapad;
    }


    /**
     * Sets the skapad value for this DTOMeddelande.
     */
    public void setSkapad(Calendar skapad) {
        this.skapad = skapad;
    }


    /**
     * Gets the skickat value for this DTOMeddelande.
     * 
     * @return skickat
     */
    public Calendar getSkickat() {
        return skickat;
    }


    /**
     * Sets the skickat value for this DTOMeddelande.
     */
    public void setSkickat(Calendar skickat) {
        this.skickat = skickat;
    }


    /**
     * Gets the skickaTidigast value for this DTOMeddelande.
     * 
     * @return skickaTidigast
     */
    public Calendar getSkickaTidigast() {
        return skickaTidigast;
    }


    /**
     * Sets the skickaTidigast value for this DTOMeddelande.
     */
    public void setSkickaTidigast(Calendar skickaTidigast) {
        this.skickaTidigast = skickaTidigast;
    }


    /**
     * Gets the avsandare value for this DTOMeddelande.
     * 
     * @return avsandare
     */
    public DTOAvsandare getAvsandare() {
        return avsandare;
    }


    /**
     * Sets the avsandare value for this DTOMeddelande.
     */
    public void setAvsandare(DTOAvsandare avsandare) {
        this.avsandare = avsandare;
    }


    /**
     * Gets the mottagare value for this DTOMeddelande.
     * 
     * @return mottagare
     */
    public DTOMottagare[] getMottagare() {
        return mottagare;
    }


    /**
     * Sets the mottagare value for this DTOMeddelande.
     */
    public void setMottagare(DTOMottagare[] mottagare) {
        this.mottagare = mottagare;
    }

    public DTOMottagare getMottagare(int i) {
        return this.mottagare[i];
    }

    public void setMottagare(int i, DTOMottagare _value) {
        this.mottagare[i] = _value;
    }


    /**
     * Gets the handelser value for this DTOMeddelande.
     * 
     * @return handelser
     */
    public DTOMeddelandeHandelse[] getHandelser() {
        return handelser;
    }


    /**
     * Sets the handelser value for this DTOMeddelande.
     */
    public void setHandelser(DTOMeddelandeHandelse[] handelser) {
        this.handelser = handelser;
    }

    public DTOMeddelandeHandelse getHandelser(int i) {
        return this.handelser[i];
    }

    public void setHandelser(int i, DTOMeddelandeHandelse _value) {
        this.handelser[i] = _value;
    }


    /**
     * Gets the callbackURL value for this DTOMeddelande.
     * 
     * @return callbackURL
     */
    public java.lang.String getCallbackURL() {
        return callbackURL;
    }


    /**
     * Sets the callbackURL value for this DTOMeddelande.
     */
    public void setCallbackURL(java.lang.String callbackURL) {
        this.callbackURL = callbackURL;
    }


    /**
     * Gets the callbackMask value for this DTOMeddelande.
     * 
     * @return callbackMask
     */
    public java.lang.Integer getCallbackMask() {
        return callbackMask;
    }


    /**
     * Sets the callbackMask value for this DTOMeddelande.
     */
    public void setCallbackMask(java.lang.Integer callbackMask) {
        this.callbackMask = callbackMask;
    }


    /**
     * Gets the mimetyp value for this DTOMeddelande.
     * 
     * @return mimetyp
     */
    public java.lang.String getMimetyp() {
        return mimetyp;
    }


    /**
     * Sets the mimetyp value for this DTOMeddelande.
     */
    public void setMimetyp(java.lang.String mimetyp) {
        this.mimetyp = mimetyp;
    }


    /**
     * Gets the kanal value for this DTOMeddelande.
     * 
     * @return kanal
     */
    public java.lang.String getKanal() {
        return kanal;
    }


    /**
     * Sets the kanal value for this DTOMeddelande.
     */
    public void setKanal(java.lang.String kanal) {
        this.kanal = kanal;
    }

    private java.lang.Object __equalsCalc;

    @Override
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DTOMeddelande)) {
            return false;
        }
        DTOMeddelande other = (DTOMeddelande) obj;
        if (this == obj) {
            return true;
        }
        if (__equalsCalc != null) {
            return __equalsCalc == obj;
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true
            && ((this.id == null && other.getId() == null)
            || (this.id != null
            && this.id.equals(other.getId())))
            && ((this.csnnummer == null && other.getCsnnummer() == null)
            || (this.csnnummer != null
            && this.csnnummer.equals(other.getCsnnummer())))
            && ((this.rubrik == null && other.getRubrik() == null)
            || (this.rubrik != null
            && this.rubrik.equals(other.getRubrik())))
            && ((this.meddelandetext == null && other.getMeddelandetext() == null)
            || (this.meddelandetext != null
            && this.meddelandetext.equals(other.getMeddelandetext())))
            && ((this.rubrikEncoding == null && other.getRubrikEncoding() == null)
            || (this.rubrikEncoding != null
            && this.rubrikEncoding.equals(other.getRubrikEncoding())))
            && ((this.meddelandeEncoding == null && other.getMeddelandeEncoding() == null)
            || (this.meddelandeEncoding != null
            && this.meddelandeEncoding.equals(other.getMeddelandeEncoding())))
            && ((this.bilagor == null && other.getBilagor() == null)
            || (this.bilagor != null
            && Arrays.equals(this.bilagor, other.getBilagor())))
            && ((this.skapad == null && other.getSkapad() == null)
            || (this.skapad != null
            && this.skapad.equals(other.getSkapad())))
            && ((this.skickat == null && other.getSkickat() == null)
            || (this.skickat != null
            && this.skickat.equals(other.getSkickat())))
            && ((this.skickaTidigast == null && other.getSkickaTidigast() == null)
            || (this.skickaTidigast != null
            && this.skickaTidigast.equals(other.getSkickaTidigast())))
            && ((this.avsandare == null && other.getAvsandare() == null)
            || (this.avsandare != null
            && this.avsandare.equals(other.getAvsandare())))
            && ((this.mottagare == null && other.getMottagare() == null)
            || (this.mottagare != null
            && Arrays.equals(this.mottagare, other.getMottagare())))
            && ((this.handelser == null && other.getHandelser() == null)
            || (this.handelser != null
            && Arrays.equals(this.handelser, other.getHandelser())))
            && ((this.callbackURL == null && other.getCallbackURL() == null)
            || (this.callbackURL != null
            && this.callbackURL.equals(other.getCallbackURL())))
            && ((this.callbackMask == null && other.getCallbackMask() == null)
            || (this.callbackMask != null
            && this.callbackMask.equals(other.getCallbackMask())))
            && ((this.mimetyp == null && other.getMimetyp() == null)
            || (this.mimetyp != null
            && this.mimetyp.equals(other.getMimetyp())))
            && ((this.kanal == null && other.getKanal() == null)
            || (this.kanal != null
            && this.kanal.equals(other.getKanal())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getCsnnummer() != null) {
            _hashCode += getCsnnummer().hashCode();
        }
        if (getRubrik() != null) {
            _hashCode += getRubrik().hashCode();
        }
        if (getMeddelandetext() != null) {
            _hashCode += getMeddelandetext().hashCode();
        }
        if (getRubrikEncoding() != null) {
            _hashCode += getRubrikEncoding().hashCode();
        }
        if (getMeddelandeEncoding() != null) {
            _hashCode += getMeddelandeEncoding().hashCode();
        }
        if (getBilagor() != null) {
            for (int i = 0;
                 i < java.lang.reflect.Array.getLength(getBilagor());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBilagor(), i);
                if (obj != null
                    && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSkapad() != null) {
            _hashCode += getSkapad().hashCode();
        }
        if (getSkickat() != null) {
            _hashCode += getSkickat().hashCode();
        }
        if (getSkickaTidigast() != null) {
            _hashCode += getSkickaTidigast().hashCode();
        }
        if (getAvsandare() != null) {
            _hashCode += getAvsandare().hashCode();
        }
        if (getMottagare() != null) {
            for (int i = 0;
                 i < java.lang.reflect.Array.getLength(getMottagare());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMottagare(), i);
                if (obj != null
                    && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getHandelser() != null) {
            for (int i = 0;
                 i < java.lang.reflect.Array.getLength(getHandelser());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getHandelser(), i);
                if (obj != null
                    && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCallbackURL() != null) {
            _hashCode += getCallbackURL().hashCode();
        }
        if (getCallbackMask() != null) {
            _hashCode += getCallbackMask().hashCode();
        }
        if (getMimetyp() != null) {
            _hashCode += getMimetyp().hashCode();
        }
        if (getKanal() != null) {
            _hashCode += getKanal().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static TypeDesc typeDesc =
        new TypeDesc(DTOMeddelande.class, true);

    static {
        typeDesc.setXmlType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelande"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "id"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("csnnummer");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "csnnummer"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("rubrik");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "rubrik"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("meddelandetext");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "meddelandetext"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("rubrikEncoding");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "rubrikEncoding"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("meddelandeEncoding");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "meddelandeEncoding"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("bilagor");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "bilagor"));
        elemField.setXmlType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOBilaga"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("skapad");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "skapad"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("skickat");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "skickat"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("skickaTidigast");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "skickaTidigast"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("avsandare");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "avsandare"));
        elemField.setXmlType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOAvsandare"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("mottagare");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "mottagare"));
        elemField.setXmlType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMottagare"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("handelser");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "handelser"));
        elemField.setXmlType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelandeHandelse"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("callbackURL");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "callbackURL"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("callbackMask");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "callbackMask"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("mimetyp");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "mimetyp"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("kanal");
        elemField.setXmlName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "kanal"));
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
