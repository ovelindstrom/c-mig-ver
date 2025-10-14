/**
 * Skapad 2007-mar-30
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.ft;

import java.util.Date;

import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.MeddelandeSender;
import se.csn.notmotor.ipl.model.Avsandare;
import se.csn.notmotor.ipl.model.Bilaga;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.NotifieringResultat;
import se.csn.notmotor.ipl.model.SandResultat;

/**
 * Enkel meddelandesandningsstubbe. Tankt for utvecklingsfasen innan notmotorn ar helt pa 
 * plats. Kan anvandas i samma process som klientkoden; konfa da upp med properties.
 * Implementerar bara skicka-metoden.  
 */
public class SimpleNotifieringProxyImpl implements NotifieringProxy {

    private MeddelandeSender sender;
    private Log log = Log.getInstance(SimpleNotifieringProxyImpl.class);

    public SimpleNotifieringProxyImpl(MeddelandeSender sender) {
        if (sender == null) {
            throw new IllegalArgumentException("sender får inte vara null");
        }
        this.sender = sender;
    }

    /**
     * Enklast mojliga implementation. Skickar meddelandet men returnerar
     * inget meddelande-id
     */
    public NotifieringResultat skickaMeddelande(Meddelande meddelande) {
        log.debug("Mottaget meddelande: " + meddelande.toString());
        long id = System.currentTimeMillis();
        meddelande.setId(new Long(id));
        SandResultat handelse = sender.skickaMeddelande(meddelande);
        log.debug("Skickat meddelande, det tog " + (System.currentTimeMillis() - id) + " ms");
        if (handelse.getKod() == MeddelandeHandelse.SKICKAT_SERVER) {
            return new NotifieringResultat();
        } else {
            return new NotifieringResultat(-1L, NotifieringResultat.FEL, handelse.getText());
        }
    }

    /**
     * Ej implementerad
     */
    public NotifieringResultat taBortMeddelande(Long meddelandeId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Ej implementerad
     */
    public Avsandare[] sokAvsandare(String namndel, String applikationsdel,
                                    String kategoridel, String adressdel, String replytoDel) {
        throw new UnsupportedOperationException();
    }


    /**
     * Ej implementerad
     */
    public Meddelande[] sokMeddelanden(Date from, Date tom,
                                       Avsandare[] avsandare, Mottagare[] mottagare,
                                       String textinnehall, Integer minstorlek, Integer maxstorlek,
                                       Integer handelseMask, Integer felmask, Bilaga[] bilagor) {
        throw new UnsupportedOperationException();
    }

    /**
     * Ej implementerad
     */
    public Meddelande hamtaMeddelande(Long meddelandeId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Ej implementerad
     */
    public Mottagare[] sokMottagare(String namndel, String adressdel,
                                    String typ, Integer csnnrFrom, Integer csnnrTom) {
        throw new UnsupportedOperationException();
    }
}
