/**
 * SkickaSOAPImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.skicka;

import se.csn.notmotor.ipl.model.ConvertDTO;
import se.csn.notmotor.ipl.webservice.SkickaService;

public class SkickaSOAPImpl implements se.csn.webservice.bas.notmotor.skicka.Skicka_PortType{
    public se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat skickaMeddelande(se.csn.webservice.bas.notmotor.skicka.DTOMeddelande parameters) throws java.rmi.RemoteException {
    	SkickaService skicka = new SkickaService();
    	return ConvertDTO.getNotifieringresultat(
    			skicka.skickaMeddelande(ConvertDTO.getMeddelande(parameters)));
    }

    public se.csn.webservice.bas.notmotor.skicka.DTOMeddelande hamtaMeddelande(long parameters) throws java.rmi.RemoteException {
    	SkickaService skicka = new SkickaService();
    	return ConvertDTO.getMeddelande2(
    			skicka.hamtaMeddelande(parameters));
    }

    public se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat taBortMeddelande(long parameters) throws java.rmi.RemoteException {
    	SkickaService skicka = new SkickaService();
    	return ConvertDTO.getNotifieringresultat(
    			skicka.taBortMeddelande(parameters));
    }

}
