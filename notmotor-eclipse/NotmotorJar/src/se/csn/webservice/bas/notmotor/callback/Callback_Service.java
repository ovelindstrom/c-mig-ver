/**
 * Callback_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.callback;

public interface Callback_Service extends javax.xml.rpc.Service {
    public java.lang.String getCallbackSOAPAddress();

    public se.csn.webservice.bas.notmotor.callback.Callback_PortType getCallbackSOAP() throws javax.xml.rpc.ServiceException;

    public se.csn.webservice.bas.notmotor.callback.Callback_PortType getCallbackSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
