package se.csn.webservice.bas.notmotor.skicka;

public class Skicka_ServiceLocator extends org.apache.axis.client.Service implements se.csn.webservice.bas.notmotor.skicka.Skicka_Service {

    public Skicka_ServiceLocator() {
    }


    public Skicka_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Skicka_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
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

    public se.csn.webservice.bas.notmotor.skicka.Skicka_PortType getSkickaSOAP() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SkickaSOAP_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSkickaSOAP(endpoint);
    }

    public se.csn.webservice.bas.notmotor.skicka.Skicka_PortType getSkickaSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            se.csn.webservice.bas.notmotor.skicka.SkickaSOAPStub _stub = new se.csn.webservice.bas.notmotor.skicka.SkickaSOAPStub(portAddress, this);
            _stub.setPortName(getSkickaSOAPWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSkickaSOAPEndpointAddress(java.lang.String address) {
        SkickaSOAP_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (se.csn.webservice.bas.notmotor.skicka.Skicka_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                se.csn.webservice.bas.notmotor.skicka.SkickaSOAPStub _stub = new se.csn.webservice.bas.notmotor.skicka.SkickaSOAPStub(new java.net.URL(SkickaSOAP_address), this);
                _stub.setPortName(getSkickaSOAPWSDDServiceName());
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
        if ("SkickaSOAP".equals(inputPortName)) {
            return getSkickaSOAP();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "Skicka");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "SkickaSOAP"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

        if ("SkickaSOAP".equals(portName)) {
            setSkickaSOAPEndpointAddress(address);
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
