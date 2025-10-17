package se.csn.webservice.bas.notmotor.callback;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Callback_PortType extends Remote {
    public void nyHandelse(DTOMeddelande parameters) throws RemoteException;
}
