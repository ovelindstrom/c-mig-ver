/**
 * HanteraEDH_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.hanteraEDH;

import java.net.URL;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface HanteraEDH_Service extends Service {
    public java.lang.String gethanteraEDHSOAPAddress();

    public HanteraEDH_PortType gethanteraEDHSOAP() throws ServiceException;

    public HanteraEDH_PortType gethanteraEDHSOAP(URL portAddress) throws ServiceException;
}
