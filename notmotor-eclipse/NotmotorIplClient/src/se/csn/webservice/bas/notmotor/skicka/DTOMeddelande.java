/**
 * DTOMeddelande.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.skicka;

public class DTOMeddelande  implements java.io.Serializable {
    private java.lang.Long id;

    private java.lang.Integer csnnummer;

    private java.lang.String rubrik;

    private java.lang.String meddelandetext;

    private java.lang.String rubrikEncoding;

    private java.lang.String meddelandeEncoding;

    private se.csn.webservice.bas.notmotor.skicka.DTOBilaga[] bilagor;

    private java.util.Calendar skapad;

    private java.util.Calendar skickat;

    private java.util.Calendar skickaTidigast;

    private se.csn.webservice.bas.notmotor.skicka.DTOAvsandare avsandare;

    private se.csn.webservice.bas.notmotor.skicka.DTOMottagare[] mottagare;

    private se.csn.webservice.bas.notmotor.skicka.DTOMeddelandeHandelse[] handelser;

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
           se.csn.webservice.bas.notmotor.skicka.DTOBilaga[] bilagor,
           java.util.Calendar skapad,
           java.util.Calendar skickat,
           java.util.Calendar skickaTidigast,
           se.csn.webservice.bas.notmotor.skicka.DTOAvsandare avsandare,
           se.csn.webservice.bas.notmotor.skicka.DTOMottagare[] mottagare,
           se.csn.webservice.bas.notmotor.skicka.DTOMeddelandeHandelse[] handelser,
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
     * 
     * @param id
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
     * 
     * @param csnnummer
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
     * 
     * @param rubrik
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
     * 
     * @param meddelandetext
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
     * 
     * @param rubrikEncoding
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
     * 
     * @param meddelandeEncoding
     */
    public void setMeddelandeEncoding(java.lang.String meddelandeEncoding) {
        this.meddelandeEncoding = meddelandeEncoding;
    }


    /**
     * Gets the bilagor value for this DTOMeddelande.
     * 
     * @return bilagor
     */
    public se.csn.webservice.bas.notmotor.skicka.DTOBilaga[] getBilagor() {
        return bilagor;
    }


    /**
     * Sets the bilagor value for this DTOMeddelande.
     * 
     * @param bilagor
     */
    public void setBilagor(se.csn.webservice.bas.notmotor.skicka.DTOBilaga[] bilagor) {
        this.bilagor = bilagor;
    }

    public se.csn.webservice.bas.notmotor.skicka.DTOBilaga getBilagor(int i) {
        return this.bilagor[i];
    }

    public void setBilagor(int i, se.csn.webservice.bas.notmotor.skicka.DTOBilaga _value) {
        this.bilagor[i] = _value;
    }


    /**
     * Gets the skapad value for this DTOMeddelande.
     * 
     * @return skapad
     */
    public java.util.Calendar getSkapad() {
        return skapad;
    }


    /**
     * Sets the skapad value for this DTOMeddelande.
     * 
     * @param skapad
     */
    public void setSkapad(java.util.Calendar skapad) {
        this.skapad = skapad;
    }


    /**
     * Gets the skickat value for this DTOMeddelande.
     * 
     * @return skickat
     */
    public java.util.Calendar getSkickat() {
        return skickat;
    }


    /**
     * Sets the skickat value for this DTOMeddelande.
     * 
     * @param skickat
     */
    public void setSkickat(java.util.Calendar skickat) {
        this.skickat = skickat;
    }


    /**
     * Gets the skickaTidigast value for this DTOMeddelande.
     * 
     * @return skickaTidigast
     */
    public java.util.Calendar getSkickaTidigast() {
        return skickaTidigast;
    }


    /**
     * Sets the skickaTidigast value for this DTOMeddelande.
     * 
     * @param skickaTidigast
     */
    public void setSkickaTidigast(java.util.Calendar skickaTidigast) {
        this.skickaTidigast = skickaTidigast;
    }


    /**
     * Gets the avsandare value for this DTOMeddelande.
     * 
     * @return avsandare
     */
    public se.csn.webservice.bas.notmotor.skicka.DTOAvsandare getAvsandare() {
        return avsandare;
    }


    /**
     * Sets the avsandare value for this DTOMeddelande.
     * 
     * @param avsandare
     */
    public void setAvsandare(se.csn.webservice.bas.notmotor.skicka.DTOAvsandare avsandare) {
        this.avsandare = avsandare;
    }


    /**
     * Gets the mottagare value for this DTOMeddelande.
     * 
     * @return mottagare
     */
    public se.csn.webservice.bas.notmotor.skicka.DTOMottagare[] getMottagare() {
        return mottagare;
    }


    /**
     * Sets the mottagare value for this DTOMeddelande.
     * 
     * @param mottagare
     */
    public void setMottagare(se.csn.webservice.bas.notmotor.skicka.DTOMottagare[] mottagare) {
        this.mottagare = mottagare;
    }

    public se.csn.webservice.bas.notmotor.skicka.DTOMottagare getMottagare(int i) {
        return this.mottagare[i];
    }

    public void setMottagare(int i, se.csn.webservice.bas.notmotor.skicka.DTOMottagare _value) {
        this.mottagare[i] = _value;
    }


    /**
     * Gets the handelser value for this DTOMeddelande.
     * 
     * @return handelser
     */
    public se.csn.webservice.bas.notmotor.skicka.DTOMeddelandeHandelse[] getHandelser() {
        return handelser;
    }


    /**
     * Sets the handelser value for this DTOMeddelande.
     * 
     * @param handelser
     */
    public void setHandelser(se.csn.webservice.bas.notmotor.skicka.DTOMeddelandeHandelse[] handelser) {
        this.handelser = handelser;
    }

    public se.csn.webservice.bas.notmotor.skicka.DTOMeddelandeHandelse getHandelser(int i) {
        return this.handelser[i];
    }

    public void setHandelser(int i, se.csn.webservice.bas.notmotor.skicka.DTOMeddelandeHandelse _value) {
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
     * 
     * @param callbackURL
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
     * 
     * @param callbackMask
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
     * 
     * @param mimetyp
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
     * 
     * @param kanal
     */
    public void setKanal(java.lang.String kanal) {
        this.kanal = kanal;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DTOMeddelande)) return false;
        DTOMeddelande other = (DTOMeddelande) obj;
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
            ((this.csnnummer==null && other.getCsnnummer()==null) || 
             (this.csnnummer!=null &&
              this.csnnummer.equals(other.getCsnnummer()))) &&
            ((this.rubrik==null && other.getRubrik()==null) || 
             (this.rubrik!=null &&
              this.rubrik.equals(other.getRubrik()))) &&
            ((this.meddelandetext==null && other.getMeddelandetext()==null) || 
             (this.meddelandetext!=null &&
              this.meddelandetext.equals(other.getMeddelandetext()))) &&
            ((this.rubrikEncoding==null && other.getRubrikEncoding()==null) || 
             (this.rubrikEncoding!=null &&
              this.rubrikEncoding.equals(other.getRubrikEncoding()))) &&
            ((this.meddelandeEncoding==null && other.getMeddelandeEncoding()==null) || 
             (this.meddelandeEncoding!=null &&
              this.meddelandeEncoding.equals(other.getMeddelandeEncoding()))) &&
            ((this.bilagor==null && other.getBilagor()==null) || 
             (this.bilagor!=null &&
              java.util.Arrays.equals(this.bilagor, other.getBilagor()))) &&
            ((this.skapad==null && other.getSkapad()==null) || 
             (this.skapad!=null &&
              this.skapad.equals(other.getSkapad()))) &&
            ((this.skickat==null && other.getSkickat()==null) || 
             (this.skickat!=null &&
              this.skickat.equals(other.getSkickat()))) &&
            ((this.skickaTidigast==null && other.getSkickaTidigast()==null) || 
             (this.skickaTidigast!=null &&
              this.skickaTidigast.equals(other.getSkickaTidigast()))) &&
            ((this.avsandare==null && other.getAvsandare()==null) || 
             (this.avsandare!=null &&
              this.avsandare.equals(other.getAvsandare()))) &&
            ((this.mottagare==null && other.getMottagare()==null) || 
             (this.mottagare!=null &&
              java.util.Arrays.equals(this.mottagare, other.getMottagare()))) &&
            ((this.handelser==null && other.getHandelser()==null) || 
             (this.handelser!=null &&
              java.util.Arrays.equals(this.handelser, other.getHandelser()))) &&
            ((this.callbackURL==null && other.getCallbackURL()==null) || 
             (this.callbackURL!=null &&
              this.callbackURL.equals(other.getCallbackURL()))) &&
            ((this.callbackMask==null && other.getCallbackMask()==null) || 
             (this.callbackMask!=null &&
              this.callbackMask.equals(other.getCallbackMask()))) &&
            ((this.mimetyp==null && other.getMimetyp()==null) || 
             (this.mimetyp!=null &&
              this.mimetyp.equals(other.getMimetyp()))) &&
            ((this.kanal==null && other.getKanal()==null) || 
             (this.kanal!=null &&
              this.kanal.equals(other.getKanal())));
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBilagor());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBilagor(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMottagare());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMottagare(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getHandelser() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getHandelser());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getHandelser(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
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
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DTOMeddelande.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelande"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("csnnummer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "csnnummer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rubrik");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "rubrik"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("meddelandetext");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "meddelandetext"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rubrikEncoding");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "rubrikEncoding"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("meddelandeEncoding");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "meddelandeEncoding"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bilagor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "bilagor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOBilaga"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skapad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "skapad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skickat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "skickat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skickaTidigast");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "skickaTidigast"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avsandare");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "avsandare"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOAvsandare"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mottagare");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "mottagare"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMottagare"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("handelser");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "handelser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelandeHandelse"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callbackURL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "callbackURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callbackMask");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "callbackMask"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mimetyp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "mimetyp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kanal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "kanal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
