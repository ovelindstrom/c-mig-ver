package se.csn.webservice.bas.notmotor.skicka;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

public class SkickaProxy implements Skicka_PortType {
    private String _endpoint;
    private Skicka_PortType skicka_PortType;

    public SkickaProxy() {
        _initSkickaProxy();
    }

    private void _initSkickaProxy() {
        try {
            skicka_PortType = new Skicka_ServiceLocator().getSkickaSOAP();
            if (skicka_PortType != null) {
                if (_endpoint != null) {
                    ((Stub) skicka_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
                } else {
                    _endpoint = (String) ((Stub) skicka_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
                }
            }

        }
        catch (ServiceException serviceException) {
        }
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (skicka_PortType != null) {
            ((Stub) skicka_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        }

    }

    public Skicka_PortType getSkicka_PortType() {
        if (skicka_PortType == null) {
            _initSkickaProxy();
        }
        return skicka_PortType;
    }

    @Override
    public DTONotifieringResultat skickaMeddelande(DTOMeddelande parameters) throws RemoteException {
        if (skicka_PortType == null) {
            _initSkickaProxy();
        }
        return skicka_PortType.skickaMeddelande(parameters);
    }

    @Override
    public DTOMeddelande hamtaMeddelande(long parameters) throws RemoteException {
        if (skicka_PortType == null) {
            _initSkickaProxy();
        }
        return skicka_PortType.hamtaMeddelande(parameters);
    }

    @Override
    public DTONotifieringResultat taBortMeddelande(long parameters) throws RemoteException {
        if (skicka_PortType == null) {
            _initSkickaProxy();
        }
        return skicka_PortType.taBortMeddelande(parameters);
    }


}
