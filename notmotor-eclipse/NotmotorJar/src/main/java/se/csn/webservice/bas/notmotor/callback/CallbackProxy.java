package se.csn.webservice.bas.notmotor.callback;

public class CallbackProxy implements se.csn.webservice.bas.notmotor.callback.Callback_PortType {
    private String _endpoint = null;
    private se.csn.webservice.bas.notmotor.callback.Callback_PortType callback_PortType = null;

    public CallbackProxy() {
        _initCallbackProxy();
    }

    private void _initCallbackProxy() {
        try {
            callback_PortType = (new se.csn.webservice.bas.notmotor.callback.Callback_ServiceLocator()).getCallbackSOAP();
            if (callback_PortType != null) {
                if (_endpoint != null)
                    ((javax.xml.rpc.Stub) callback_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
                else
                    _endpoint = (String) ((javax.xml.rpc.Stub) callback_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
            }

        }
        catch (javax.xml.rpc.ServiceException serviceException) {
        }
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (callback_PortType != null)
            ((javax.xml.rpc.Stub) callback_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

    }

    public se.csn.webservice.bas.notmotor.callback.Callback_PortType getCallback_PortType() {
        if (callback_PortType == null)
            _initCallbackProxy();
        return callback_PortType;
    }

    @Override
    public void nyHandelse(se.csn.webservice.bas.notmotor.callback.DTOMeddelande parameters) throws java.rmi.RemoteException {
        if (callback_PortType == null)
            _initCallbackProxy();
        callback_PortType.nyHandelse(parameters);
    }


}