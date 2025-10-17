package se.csn.webservice.bas.hanteraEDH;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HanteraEDH_PortType extends Remote {
    public HamtaFranEDHSvar hamtaFranEDH(HamtaFranEDHFraga fraga) throws RemoteException;

    public SparaTillEDHSvar sparaTillEDH(SparaTillEDHFraga fraga) throws RemoteException;
}
