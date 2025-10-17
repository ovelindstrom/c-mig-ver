/**
 * Callback_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.callback;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Callback_PortType extends Remote {
    public void nyHandelse(DTOMeddelande parameters) throws RemoteException;
}
