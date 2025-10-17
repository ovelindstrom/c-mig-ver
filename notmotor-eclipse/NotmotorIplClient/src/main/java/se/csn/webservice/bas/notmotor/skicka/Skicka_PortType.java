/**
 * Skicka_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.skicka;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Skicka_PortType extends Remote {
    public DTONotifieringResultat skickaMeddelande(DTOMeddelande parameters) throws RemoteException;

    public DTOMeddelande hamtaMeddelande(long parameters) throws RemoteException;

    public DTONotifieringResultat taBortMeddelande(long parameters) throws RemoteException;
}
