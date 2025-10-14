package se.csn.webservice.bas.notmotor.skicka;

public class SkickaProxy implements se.csn.webservice.bas.notmotor.skicka.Skicka_PortType {
  private String _endpoint = null;
  private se.csn.webservice.bas.notmotor.skicka.Skicka_PortType skicka_PortType = null;

  public SkickaProxy() {
    _initSkickaProxy();
  }

  private void _initSkickaProxy() {
    try {
      skicka_PortType = (new se.csn.webservice.bas.notmotor.skicka.Skicka_ServiceLocator()).getSkickaSOAP();
      if (skicka_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)skicka_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)skicka_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }

    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }

  public String getEndpoint() {
    return _endpoint;
  }

  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (skicka_PortType != null)
      ((javax.xml.rpc.Stub)skicka_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

  }

  public se.csn.webservice.bas.notmotor.skicka.Skicka_PortType getSkicka_PortType() {
    if (skicka_PortType == null)
      _initSkickaProxy();
    return skicka_PortType;
  }

  public se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat skickaMeddelande(se.csn.webservice.bas.notmotor.skicka.DTOMeddelande parameters) throws java.rmi.RemoteException{
    if (skicka_PortType == null)
      _initSkickaProxy();
    return skicka_PortType.skickaMeddelande(parameters);
  }

  public se.csn.webservice.bas.notmotor.skicka.DTOMeddelande hamtaMeddelande(long parameters) throws java.rmi.RemoteException{
    if (skicka_PortType == null)
      _initSkickaProxy();
    return skicka_PortType.hamtaMeddelande(parameters);
  }

  public se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat taBortMeddelande(long parameters) throws java.rmi.RemoteException{
    if (skicka_PortType == null)
      _initSkickaProxy();
    return skicka_PortType.taBortMeddelande(parameters);
  }


}