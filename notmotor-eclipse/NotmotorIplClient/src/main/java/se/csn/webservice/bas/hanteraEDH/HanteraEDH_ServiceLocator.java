package se.csn.webservice.bas.hanteraEDH;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

/*
 * HanteraEDH_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
public class HanteraEDH_ServiceLocator extends Service implements HanteraEDH_Service {

    public HanteraEDH_ServiceLocator() {
    }


    public HanteraEDH_ServiceLocator(EngineConfiguration config) {
        super(config);
    }

    public HanteraEDH_ServiceLocator(java.lang.String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for hanteraEDHSOAP
    private java.lang.String hanteraEDHSOAP_address = "http://webservice.csn.se/bas/hanteraEDH";

    public java.lang.String gethanteraEDHSOAPAddress() {
        return hanteraEDHSOAP_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String hanteraEDHSOAPWSDDServiceName = "hanteraEDHSOAP";

    public java.lang.String gethanteraEDHSOAPWSDDServiceName() {
        return hanteraEDHSOAPWSDDServiceName;
    }

    public void sethanteraEDHSOAPWSDDServiceName(java.lang.String name) {
        hanteraEDHSOAPWSDDServiceName = name;
    }

    public HanteraEDH_PortType gethanteraEDHSOAP() throws ServiceException {
        URL endpoint;
        try {
            endpoint = new URL(hanteraEDHSOAP_address);
        }
        catch (MalformedURLException e) {
            throw new ServiceException(e);
        }
        return gethanteraEDHSOAP(endpoint);
    }

    public HanteraEDH_PortType gethanteraEDHSOAP(URL portAddress) throws ServiceException {
        try {
            HanteraEDHSOAPStub _stub = new HanteraEDHSOAPStub(portAddress, this);
            _stub.setPortName(gethanteraEDHSOAPWSDDServiceName());
            return _stub;
        }
        catch (AxisFault e) {
            return null;
        }
    }

    public void sethanteraEDHSOAPEndpointAddress(java.lang.String address) {
        hanteraEDHSOAP_address = address;
    }

    /*
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (HanteraEDH_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                HanteraEDHSOAPStub _stub = new HanteraEDHSOAPStub(new URL(hanteraEDHSOAP_address), this);
                _stub.setPortName(gethanteraEDHSOAPWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new ServiceException(t);
        }
        throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /*
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(QName portName, Class serviceEndpointInterface) throws ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("hanteraEDHSOAP".equals(inputPortName)) {
            return gethanteraEDHSOAP();
        } else {
            Remote _stub = getPort(serviceEndpointInterface);
            ((Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public QName getServiceName() {
        return new QName("http://webservice.csn.se/bas/hanteraEDH/", "hanteraEDH");
    }

    private Set ports;

    public Iterator getPorts() {
        if (ports == null) {
            ports = new HashSet();
            ports.add(new QName("http://webservice.csn.se/bas/hanteraEDH/", "hanteraEDHSOAP"));
        }
        return ports.iterator();
    }

    /*
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws ServiceException {

        if ("hanteraEDHSOAP".equals(portName)) {
            sethanteraEDHSOAPEndpointAddress(address);
        } else
        { // Unknown Port Name
            throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /*
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(QName portName, java.lang.String address) throws ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
