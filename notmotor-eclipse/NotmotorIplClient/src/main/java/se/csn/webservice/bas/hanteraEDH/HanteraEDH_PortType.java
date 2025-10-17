/**
// * HanteraEDH_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.hanteraEDH;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HanteraEDH_PortType extends Remote {
    public HamtaFranEDHSvar hamtaFranEDH(HamtaFranEDHFraga fraga) throws RemoteException;

    public SparaTillEDHSvar sparaTillEDH(SparaTillEDHFraga fraga) throws RemoteException;
}
