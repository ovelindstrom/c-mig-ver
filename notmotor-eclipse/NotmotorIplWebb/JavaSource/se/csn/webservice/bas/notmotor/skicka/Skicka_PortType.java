/**
 * Skicka_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.skicka;

public interface Skicka_PortType extends java.rmi.Remote {
    public se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat skickaMeddelande(se.csn.webservice.bas.notmotor.skicka.DTOMeddelande parameters) throws java.rmi.RemoteException;
    public se.csn.webservice.bas.notmotor.skicka.DTOMeddelande hamtaMeddelande(long parameters) throws java.rmi.RemoteException;
    public se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat taBortMeddelande(long parameters) throws java.rmi.RemoteException;
}
