/**
 * Skicka_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.skicka;

public interface Skicka_Service extends javax.xml.rpc.Service {
    public java.lang.String getSkickaSOAPAddress();

    public se.csn.webservice.bas.notmotor.skicka.Skicka_PortType getSkickaSOAP() throws javax.xml.rpc.ServiceException;

    public se.csn.webservice.bas.notmotor.skicka.Skicka_PortType getSkickaSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
