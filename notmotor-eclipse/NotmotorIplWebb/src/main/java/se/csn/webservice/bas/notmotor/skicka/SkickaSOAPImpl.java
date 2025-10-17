package se.csn.webservice.bas.notmotor.skicka;

import java.rmi.RemoteException;


import se.csn.notmotor.ipl.model.ConvertDTO;
import se.csn.notmotor.ipl.webservice.SkickaService;

public class SkickaSOAPImpl implements Skicka_PortType {
    @Override
    public DTONotifieringResultat skickaMeddelande(DTOMeddelande parameters) throws RemoteException {
        SkickaService skicka = new SkickaService();
        return ConvertDTO.getNotifieringresultat(
            skicka.skickaMeddelande(ConvertDTO.getMeddelande(parameters)));
    }

    @Override
    public DTOMeddelande hamtaMeddelande(long parameters) throws RemoteException {
        SkickaService skicka = new SkickaService();
        return ConvertDTO.getMeddelande2(
            skicka.hamtaMeddelande(parameters));
    }

    @Override
    public DTONotifieringResultat taBortMeddelande(long parameters) throws RemoteException {
        SkickaService skicka = new SkickaService();
        return ConvertDTO.getNotifieringresultat(
            skicka.taBortMeddelande(parameters));
    }

}
