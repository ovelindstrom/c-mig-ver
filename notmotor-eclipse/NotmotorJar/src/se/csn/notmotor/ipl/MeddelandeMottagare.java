/*
 * Skapad 2007-sep-24
 */
package se.csn.notmotor.ipl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.db.DAOMeddelande;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.NotifieringResultat;
import se.csn.notmotor.ipl.model.KodText;
import se.csn.notmotor.ipl.validators.BasicMeddelandeValidator;
import se.csn.notmotor.ipl.validators.EpostValidator;
import se.csn.notmotor.ipl.validators.MeddelandeValidator;
import se.csn.notmotor.ipl.validators.SMSValidator;

/**
 * @author Jonas Öhrnell - csn7821
 * Klass som validerar meddelandet, lägger till händelsen MOTTAGET
 * och lagrar meddelandet till databas
 */
public class MeddelandeMottagare {
    
    private List<MeddelandeValidator> validators;
    private static final Log LOG = Log.getInstance(MeddelandeMottagare.class);
    private static final String LANDSNR = "46";
    
    public MeddelandeMottagare() {
        validators = new ArrayList<MeddelandeValidator>();
        validators.add(new BasicMeddelandeValidator());
        validators.add(new EpostValidator());
        validators.add(new SMSValidator());
    }
    
    public void addValidator(MeddelandeValidator validator) {
        validators.add(validator);
    }
    public void removeValidator(MeddelandeValidator validator) {
        validators.remove(validator);
    }
    public void clearValidators() {
        validators.clear();
    }
    
    public NotifieringResultat skickaMeddelande(Meddelande meddelande, DAOMeddelande meddelandeHandler) {
        // Validera meddelandet:
        for (Iterator it = validators.iterator(); it.hasNext();) {
            MeddelandeValidator validator = (MeddelandeValidator) it.next();
            KodText res = validator.getFelkodForMeddelande(meddelande); 
            if (res != null) {
                return new NotifieringResultat(-1, NotifieringResultat.FEL, res.getText());
            }
        }
        
        MeddelandeHandelse mh = new MeddelandeHandelse(MeddelandeHandelse.MOTTAGET);
        meddelande.addHandelse(mh);
        
        // 2007-10-25: inför separat status för varje mottagare
        for (int i = 0; i < meddelande.getMottagare().length; i++) {
            Mottagare mott = meddelande.getMottagare()[i];
            mott.setStatus(new Integer(MeddelandeHandelse.MOTTAGET));
            
            // 2008-04-15 Formatera smsnr så att det är på formatet 46701234567
            if ("SMS".equalsIgnoreCase(mott.getTyp())) {
            	// Formatera smsnr
            	String mobilnr = mott.getAdress();
            	
            	// Telia vill ha mobilnr på formatet 46701234567
            	try {
            		// Om mobilnr börjar med 00 (ex 004670...), ta bort 00
	            	if (mobilnr.startsWith("00")) {
	            		String nyttMobilnr = mobilnr.substring(2, mobilnr.length());
	            		mott.setAdress(nyttMobilnr);
	            	// Om mobilnr börjar med 0 (ex 070...), byt ut 0 mot 46
	            	} else if (mobilnr.startsWith("0")) {
	            		String nyttMobilnr = mobilnr.substring(1, mobilnr.length());
	            		mott.setAdress(LANDSNR + nyttMobilnr);
	            	// Om mobilnr börjar med + (ex. +4670...), ta bort +
	            	} else if (mobilnr.startsWith("+")) {
	            		String nyttMobilnr = mobilnr.substring(1, mobilnr.length());
	            		mott.setAdress(nyttMobilnr);
	            	}
            	} catch (IndexOutOfBoundsException be) {
        			LOG.error("Kunde inte formatera mobilnr på korrekt format.");
        			return new NotifieringResultat(-1, NotifieringResultat.FEL, 
        					"Mobilnr är på fel format.");
        		}
            }
            
            //trimma mottagaradress
            String mottadress = mott.getAdress();
            if (mottadress != null) {
            	mott.setAdress(mottadress.trim());
            }
        }
        
        meddelandeHandler.createMeddelande(meddelande);
        long id = meddelande.getId().longValue();
        
        return new NotifieringResultat(id);
    }

}
