package se.csn.webservice.bas.notmotor.skicka;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Skicka_PortType extends Remote {
    public DTONotifieringResultat skickaMeddelande(DTOMeddelande parameters) throws RemoteException;

    public DTOMeddelande hamtaMeddelande(long parameters) throws RemoteException;

    public DTONotifieringResultat taBortMeddelande(long parameters) throws RemoteException;
}
