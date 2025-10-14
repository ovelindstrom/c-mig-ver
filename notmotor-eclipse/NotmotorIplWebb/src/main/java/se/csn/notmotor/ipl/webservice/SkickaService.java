/**
 * Skapad 2007-apr-02
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.webservice;

import java.util.Date;

import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.ft.NotifieringProxy;
import se.csn.notmotor.ipl.ft.NotifieringProxyFactory;
import se.csn.notmotor.ipl.model.Avsandare;
import se.csn.notmotor.ipl.model.Bilaga;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.NotifieringResultat;


public class SkickaService implements NotifieringProxy {

    private static NotifieringProxyFactory factory;
    private static Log log = Log.getInstance(SkickaService.class);
    
    public Meddelande hamtaMeddelande(Long meddelandeId) {
        if (meddelandeId == null) { return null; }
        log.debug("hamtaMeddelande: " + meddelandeId.longValue());
        return getProxy().hamtaMeddelande(meddelandeId);
    }
    
    public NotifieringResultat skickaMeddelande(Meddelande meddelande) {
        try {
	        if (meddelande == null) {
	            throw new IllegalArgumentException("Meddelande måste vara satt"); 
	        }
	        log.debug("skickaMeddelande: " + meddelande.toString());
	        return getProxy().skickaMeddelande(meddelande);
        } catch (Exception e) {
            log.error("Fel: ", e);
            return new NotifieringResultat(-1, NotifieringResultat.FEL, e.toString());
        }
    }
    
    public NotifieringResultat taBortMeddelande(Long meddelandeId) {
        return getProxy().taBortMeddelande(meddelandeId);
    }
    public Avsandare[] sokAvsandare(String namndel, String applikationsdel,
            String kategoridel, String adressdel, String replytoDel) {
        return getProxy().sokAvsandare(namndel, applikationsdel, kategoridel, adressdel, replytoDel);
    }
    
    public Meddelande[] sokMeddelanden(Date from, Date tom,
            Avsandare[] avsandare, Mottagare[] mottagare,
            String textinnehall, Integer minstorlek, Integer maxstorlek,
            Integer handelseMask, Integer felmask, Bilaga[] bilagor) {
        return getProxy().sokMeddelanden(from, tom, avsandare, mottagare, textinnehall, minstorlek, maxstorlek, handelseMask, felmask, bilagor);
    }
    
    public Mottagare[] sokMottagare(String namndel, String adressdel,
            String typ, Integer csnnrFrom, Integer csnnrTom) {
        return getProxy().sokMottagare(namndel, adressdel, typ, csnnrFrom, csnnrTom);
    }
    
    private NotifieringProxy getProxy() {
        if (factory == null) {
            throw new IllegalStateException("Proxy måste vara satt");
        }
        return factory.getNotifieringProxy();
    }
    
    public static NotifieringProxyFactory getFactory() {
        return factory;
    }
    public static void setFactory(NotifieringProxyFactory factory) {
        SkickaService.factory = factory;
    }
}
