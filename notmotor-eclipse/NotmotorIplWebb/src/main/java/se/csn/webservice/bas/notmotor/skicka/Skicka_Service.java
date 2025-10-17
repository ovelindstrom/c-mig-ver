/**
 * Skicka_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.skicka;

import java.net.URL;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface Skicka_Service extends Service {
    public java.lang.String getSkickaSOAPAddress();

    public Skicka_PortType getSkickaSOAP() throws ServiceException;

    public Skicka_PortType getSkickaSOAP(URL portAddress) throws ServiceException;
}
