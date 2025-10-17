package se.csn.webservice.bas.notmotor.skicka;

import java.net.URL;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface Skicka_Service extends Service {
    public java.lang.String getSkickaSOAPAddress();

    public Skicka_PortType getSkickaSOAP() throws ServiceException;

    public Skicka_PortType getSkickaSOAP(URL portAddress) throws ServiceException;
}
