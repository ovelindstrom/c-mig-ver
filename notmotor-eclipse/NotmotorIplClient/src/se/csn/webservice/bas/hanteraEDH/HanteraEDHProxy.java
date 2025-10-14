////package se.csn.webservice.bas.hanteraEDH;
//
//public class HanteraEDHProxy implements se.csn.webservice.bas.hanteraEDH.HanteraEDH_PortType {
//  private String _endpoint = null;
//  private se.csn.webservice.bas.hanteraEDH.HanteraEDH_PortType hanteraEDH_PortType = null;
//  
//  public HanteraEDHProxy() {
//    _initHanteraEDHProxy();
//  }
//  
//  public HanteraEDHProxy(String endpoint) {
//    _endpoint = endpoint;
//    _initHanteraEDHProxy();
//  }
//  
//  private void _initHanteraEDHProxy() {
//    try {
//      hanteraEDH_PortType = (new se.csn.webservice.bas.hanteraEDH.HanteraEDH_ServiceLocator()).gethanteraEDHSOAP();
//      if (hanteraEDH_PortType != null) {
//        if (_endpoint != null)
//          ((javax.xml.rpc.Stub)hanteraEDH_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
//        else
//          _endpoint = (String)((javax.xml.rpc.Stub)hanteraEDH_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
//      }
//      
//    }
//    catch (javax.xml.rpc.ServiceException serviceException) {}
//  }
//  
//  public String getEndpoint() {
//    return _endpoint;
//  }
//  
//  public void setEndpoint(String endpoint) {
//    _endpoint = endpoint;
//    if (hanteraEDH_PortType != null)
//      ((javax.xml.rpc.Stub)hanteraEDH_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
//    
//  }
//  
//  public se.csn.webservice.bas.hanteraEDH.HanteraEDH_PortType getHanteraEDH_PortType() {
//    if (hanteraEDH_PortType == null)
//      _initHanteraEDHProxy();
//    return hanteraEDH_PortType;
//  }
//  
//  public se.csn.webservice.bas.hanteraEDH.HamtaFranEDHSvar hamtaFranEDH(se.csn.webservice.bas.hanteraEDH.HamtaFranEDHFraga fraga) throws java.rmi.RemoteException{
//    if (hanteraEDH_PortType == null)
//      _initHanteraEDHProxy();
//    return hanteraEDH_PortType.hamtaFranEDH(fraga);
//  }
//  
//  public se.csn.webservice.bas.hanteraEDH.SparaTillEDHSvar sparaTillEDH(se.csn.webservice.bas.hanteraEDH.SparaTillEDHFraga fraga) throws java.rmi.RemoteException{
//    if (hanteraEDH_PortType == null)
//      _initHanteraEDHProxy();
//    return hanteraEDH_PortType.sparaTillEDH(fraga);
//  }
//  
//  
//}