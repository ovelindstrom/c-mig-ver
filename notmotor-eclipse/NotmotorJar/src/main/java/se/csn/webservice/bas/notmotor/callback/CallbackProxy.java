package se.csn.webservice.bas.notmotor.callback;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

public class CallbackProxy implements Callback_PortType {
    private String _endpoint;
    private Callback_PortType callback_PortType;

    public CallbackProxy() {
        _initCallbackProxy();
    }

    private void _initCallbackProxy() {
        try {
            callback_PortType = new Callback_ServiceLocator().getCallbackSOAP();
            if (callback_PortType != null) {
                if (_endpoint != null) {
                    ((Stub) callback_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
                } else {
                    _endpoint = (String) ((Stub) callback_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
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
        if (callback_PortType != null) {
            ((Stub) callback_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        }

    }

    public Callback_PortType getCallback_PortType() {
        if (callback_PortType == null) {
            _initCallbackProxy();
        }
        return callback_PortType;
    }

    @Override
    public void nyHandelse(DTOMeddelande parameters) throws RemoteException {
        if (callback_PortType == null) {
            _initCallbackProxy();
        }
        callback_PortType.nyHandelse(parameters);
    }


}
