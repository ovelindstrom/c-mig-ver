/*
 * Skapad 2007-sep-21
 */
package se.csn.notmotor.ipl;

import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.KodText;
import se.csn.notmotor.ipl.model.SandResultat;
import se.csn.notmotor.ipl.sms.DTOSMSIn;
import se.csn.notmotor.ipl.sms.DTOSMSUt;
import se.csn.notmotor.ipl.sms.SMSTjaenst;

/**
 * @author Jonas åhrnell - csn7821
 * Klassen skickar meddelande via SMS.
 */
public class SMSMeddelandeSenderImpl implements MeddelandeSender {

    public static final String NUMMERFORMAT = "^(\\+?)[\\d]{10,18}$";
    private SMSTjaenst smstjaenst;
    private Log log = Log.getInstance(SMSMeddelandeSenderImpl.class);
    
    public SMSMeddelandeSenderImpl(SMSTjaenst smstjaenst) {
        this.smstjaenst = smstjaenst;
        smstjaenst.checkParams();
    }
    
    public SandResultat skickaMeddelande(Meddelande meddelande) {
        Mottagare[] mott = meddelande.getMottagare();
        SandResultat handelse = null;
        for (int i = 0; i < mott.length; i++) {
            if ((mott[i].getTyp() != null) && (mott[i].getTyp().equalsIgnoreCase("SMS"))) {
                // Kontrollera om meddelandet redan är skickat till denna mottagare. Om ja, fortsätt:
                if ((mott[i].getStatus() != null) && (mott[i].getStatus().intValue() == MeddelandeHandelse.SKICKAT_SERVER)) {
                    continue;
                }
                
                // Skicka meddelandet
                DTOSMSIn in = null;
                try {
                	in = new DTOSMSIn(mott[i].getAdress(), meddelande.getRubrik(), meddelande.getMeddelandetext(), 
                        meddelande.getAvsandare().getApplikation(), meddelande.getAvsandare().getKategori());
                } catch (IllegalArgumentException iae) {
                	return new SandResultat(MeddelandeHandelse.MEDDELANDEFEL, MeddelandeHandelse.OKANT_FEL, "Fel i inparametrar till SMS: " + iae.getMessage(), this, mott[i]);
                }
                try {
	                DTOSMSUt ut = smstjaenst.execute(in);
	                if (ut.sandningLyckad()) {
	                    handelse = new SandResultat(MeddelandeHandelse.SKICKAT_SERVER, MeddelandeHandelse.OK, ut.getReturStatusText(), this, mott[i]);
	                } else if (ut.isTemporaryError()) {
	                    return new SandResultat(MeddelandeHandelse.TEKNISKT_FEL, MeddelandeHandelse.ALLMANT_FEL, ut.getReturStatusText(), this, mott[i]);
	                } else {
	                	return new SandResultat(MeddelandeHandelse.TEKNISKT_FEL, MeddelandeHandelse.STOPPANDE_FEL, ut.getReturStatusText(), this, mott[i]);
	                }
                } catch (Exception e) {
                    log.error("Kunde inte skicka sms " + meddelande, e);
                    return skapaSandResultat(meddelande, MeddelandeHandelse.TEKNISKT_FEL, MeddelandeHandelse.OKANT_FEL, e.toString());
                }
		    }
        }
        return handelse;
    }
    
    SandResultat skapaSandResultat(Meddelande m, int handelsetyp, int kod, String text) {
        SandResultat sr = new SandResultat(handelsetyp, kod, text, this, null);
        // Sätt mottagare
        if (m.getMottagare() != null) {
            for (int i = 0; i < m.getMottagare().length; i++) {
                sr.addMottagare(m.getMottagare()[i]);
            }
        }
        return sr;
    }

    /**
     * @see se.csn.notmotor.ipl.MeddelandeSender#getFelkodForMeddelande(se.csn.notmotor.ipl.model.Meddelande)
     */
    public KodText getFelkodForMeddelande(Meddelande meddelande) {
        // Kontrollera: 
        // 1. telefonnumren
        Mottagare[] mott = meddelande.getMottagare(); 
        for (int i = 0; i < mott.length; i++) {
		    if ((mott[i].getTyp() != null) && (mott[i].getTyp().equalsIgnoreCase("SMS"))) {
		        String nummer = mott[i].getAdress();
		        if ((nummer == null) || (nummer.length() == 0)) {
		            return new KodText(MeddelandeHandelse.FELAKTIG_MOTTAGARE, "SMS-nummer saknas"); 
		        } else if (!nummer.matches(NUMMERFORMAT)) {
		            return new KodText(MeddelandeHandelse.FELAKTIG_MOTTAGARE, 
		            		"SMS-numret måste vara mellan 10 och 18 tecken"
		            		+ " och får bara innehålla inledande + och/eller siffror");
		        }
		    }
        }

        // 2. Meddelandets längd
        if ((meddelande.getMeddelandetext() != null) && (meddelande.getMeddelandetext().length() > 160)) {
            return new KodText(MeddelandeHandelse.FELAKTIG_SUBJECT, "Meddelandet hade mer än 160 tecken");
        }

        // Allt är OK, returnera null
        return null;
    }
    
    /**
     * @see se.csn.notmotor.ipl.MeddelandeSender#kanSkickaMeddelande(se.csn.notmotor.ipl.model.Meddelande)
     */
    public boolean kanSkickaMeddelande(Meddelande meddelande) {
        Mottagare[] mott = meddelande.getMottagare(); 
        for (int i = 0; i < mott.length; i++) {
		    if ((mott[i].getTyp() != null) && (mott[i].getTyp().equalsIgnoreCase("SMS"))) {
		        return true;
		    }
        }
        return false;
    }
}
