/**
 * Skapad 2007-apr-03
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.ft;

import java.sql.Connection;
import java.util.Date;

import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.MeddelandeMottagare;
import se.csn.notmotor.ipl.db.DAOMeddelande;
import se.csn.notmotor.ipl.db.QueryProcessor;
import se.csn.notmotor.ipl.model.Avsandare;
import se.csn.notmotor.ipl.model.Bilaga;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.NotifieringResultat;


public class NotifieringProxyImpl implements NotifieringProxy {

    private DAOMeddelande meddelandeHandler;
    private MeddelandeMottagare meddelandeMottagare;
    private QueryProcessor qp;
    private Log log = Log.getInstance(NotifieringProxyImpl.class);
    
    public NotifieringProxyImpl(DAOMeddelande mh, QueryProcessor qp, MeddelandeMottagare mm) {
        meddelandeHandler = mh;
        meddelandeMottagare = mm;
        this.qp = qp;
    }
    
    public Meddelande hamtaMeddelande(Long meddelandeId) {
        return meddelandeHandler.getMeddelande(meddelandeId.longValue());
    }

    /**
     * Validerar, lägger till händelsen MOTTAGET och skriver till databas.
     */
    public NotifieringResultat skickaMeddelande(Meddelande meddelande) {
        try {
            return meddelandeMottagare.skickaMeddelande(meddelande, meddelandeHandler);
        } catch (Exception e) {
            log.error("Fel vid sändning av meddelande", e);
            return new NotifieringResultat(-1, NotifieringResultat.FEL, "Tekniskt fel.");
        }
    }
    
    public NotifieringResultat taBortMeddelande(Long meddelandeId) {
        try {
        	Connection conn = qp.getConnection();
            int antalRader = qp.executeThrowException("UPDATE MEDDELANDE SET STATUS = "
                    + MeddelandeHandelse.BORTTAGET + " WHERE ID = " + meddelandeId.longValue());
            if ((conn != null) && (!conn.isClosed())) {
            	conn.commit();
                conn.close();
            }
            if (antalRader == 1) {
                return new NotifieringResultat(meddelandeId.longValue(), NotifieringResultat.OK, "Meddelandet borttaget");
            } else {
                return new NotifieringResultat(meddelandeId.longValue(), NotifieringResultat.VARNING, "Hittade inte meddelande " + meddelandeId.longValue() + ", det blev inte borttaget");
            }
        } catch (Exception e) {
            log.error("Fel i taBortMeddelande", e);
            return new NotifieringResultat(meddelandeId.longValue(), NotifieringResultat.FEL, "Fel, meddelande " + meddelandeId.longValue() + ": " + e);
        }
    }
    
    public Avsandare[] sokAvsandare(String namndel, String applikationsdel,
            String kategoridel, String adressdel, String replytoDel) {
        return null;
    }
    public Meddelande[] sokMeddelanden(Date from, Date tom,
            Avsandare[] avsandare, Mottagare[] mottagare,
            String textinnehall, Integer minstorlek, Integer maxstorlek,
            Integer handelseMask, Integer felmask, Bilaga[] bilagor) {
        return null;
    }
    public Mottagare[] sokMottagare(String namndel, String adressdel,
            String typ, Integer csnnrFrom, Integer csnnrTom) {
        return null;
    }
}
