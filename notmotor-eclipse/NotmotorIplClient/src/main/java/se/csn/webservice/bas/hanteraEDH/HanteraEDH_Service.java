package se.csn.webservice.bas.hanteraEDH;

import java.net.URL;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface HanteraEDH_Service extends Service {
    public java.lang.String gethanteraEDHSOAPAddress();

    public HanteraEDH_PortType gethanteraEDHSOAP() throws ServiceException;

    public HanteraEDH_PortType gethanteraEDHSOAP(URL portAddress) throws ServiceException;
}
