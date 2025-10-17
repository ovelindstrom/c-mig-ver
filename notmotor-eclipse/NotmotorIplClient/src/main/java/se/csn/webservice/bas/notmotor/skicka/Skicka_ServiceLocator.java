package se.csn.webservice.bas.notmotor.skicka;

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

public class Skicka_ServiceLocator extends Service implements Skicka_Service {

    public Skicka_ServiceLocator() {
    }


    public Skicka_ServiceLocator(EngineConfiguration config) {
        super(config);
    }

    public Skicka_ServiceLocator(java.lang.String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SkickaSOAP
    private java.lang.String SkickaSOAP_address = "http://webservice.csn.se/bas/notmotor/skicka";

    public java.lang.String getSkickaSOAPAddress() {
        return SkickaSOAP_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SkickaSOAPWSDDServiceName = "SkickaSOAP";

    public java.lang.String getSkickaSOAPWSDDServiceName() {
        return SkickaSOAPWSDDServiceName;
    }

    public void setSkickaSOAPWSDDServiceName(java.lang.String name) {
        SkickaSOAPWSDDServiceName = name;
    }

    public Skicka_PortType getSkickaSOAP() throws ServiceException {
        URL endpoint;
        try {
            endpoint = new URL(SkickaSOAP_address);
        }
        catch (MalformedURLException e) {
            throw new ServiceException(e);
        }
        return getSkickaSOAP(endpoint);
    }

    public Skicka_PortType getSkickaSOAP(URL portAddress) throws ServiceException {
        try {
            SkickaSOAPStub _stub = new SkickaSOAPStub(portAddress, this);
            _stub.setPortName(getSkickaSOAPWSDDServiceName());
            return _stub;
        }
        catch (AxisFault e) {
            return null;
        }
    }

    public void setSkickaSOAPEndpointAddress(java.lang.String address) {
        SkickaSOAP_address = address;
    }

    /*
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (Skicka_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                SkickaSOAPStub _stub = new SkickaSOAPStub(new URL(SkickaSOAP_address), this);
                _stub.setPortName(getSkickaSOAPWSDDServiceName());
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
        if ("SkickaSOAP".equals(inputPortName)) {
            return getSkickaSOAP();
        } else {
            Remote _stub = getPort(serviceEndpointInterface);
            ((Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public QName getServiceName() {
        return new QName("http://webservice.csn.se/bas/notmotor/skicka", "Skicka");
    }

    private Set ports;

    public Iterator getPorts() {
        if (ports == null) {
            ports = new HashSet();
            ports.add(new QName("http://webservice.csn.se/bas/notmotor/skicka", "SkickaSOAP"));
        }
        return ports.iterator();
    }

    /*
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws ServiceException {

        if ("SkickaSOAP".equals(portName)) {
            setSkickaSOAPEndpointAddress(address);
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
