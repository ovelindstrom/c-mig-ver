/**
 * Callback_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.callback;

public class Callback_ServiceLocator extends org.apache.axis.client.Service implements se.csn.webservice.bas.notmotor.callback.Callback_Service {

    public Callback_ServiceLocator() {
    }


    public Callback_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Callback_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CallbackSOAP
    private java.lang.String CallbackSOAP_address = "http://webservice.csn.se/bas/notmotor/callback";

    public java.lang.String getCallbackSOAPAddress() {
        return CallbackSOAP_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CallbackSOAPWSDDServiceName = "CallbackSOAP";

    public java.lang.String getCallbackSOAPWSDDServiceName() {
        return CallbackSOAPWSDDServiceName;
    }

    public void setCallbackSOAPWSDDServiceName(java.lang.String name) {
        CallbackSOAPWSDDServiceName = name;
    }

    public se.csn.webservice.bas.notmotor.callback.Callback_PortType getCallbackSOAP() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CallbackSOAP_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCallbackSOAP(endpoint);
    }

    public se.csn.webservice.bas.notmotor.callback.Callback_PortType getCallbackSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            se.csn.webservice.bas.notmotor.callback.CallbackSOAPStub _stub = new se.csn.webservice.bas.notmotor.callback.CallbackSOAPStub(portAddress, this);
            _stub.setPortName(getCallbackSOAPWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCallbackSOAPEndpointAddress(java.lang.String address) {
        CallbackSOAP_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (se.csn.webservice.bas.notmotor.callback.Callback_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                se.csn.webservice.bas.notmotor.callback.CallbackSOAPStub _stub = new se.csn.webservice.bas.notmotor.callback.CallbackSOAPStub(new java.net.URL(CallbackSOAP_address), this);
                _stub.setPortName(getCallbackSOAPWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CallbackSOAP".equals(inputPortName)) {
            return getCallbackSOAP();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "Callback");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/callback", "CallbackSOAP"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

        if ("CallbackSOAP".equals(portName)) {
            setCallbackSOAPEndpointAddress(address);
        } else
        { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
