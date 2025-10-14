/*
 * Skapad 2007-okt-31
 */
package se.csn.notmotor.ipl.validators;

import se.csn.common.mail.EmailAdressValidator;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.KodText;

/**
 * @author Jonas åhrnell - csn7821
 */
public class EpostValidator implements MeddelandeValidator {

    public KodText getFelkodForMeddelande(Meddelande meddelande) {
        if(!EmailAdressValidator.isValid(meddelande.getAvsandare().getEpostadress())) {
            return new KodText(MeddelandeHandelse.FELAKTIG_AVSANDARE, "Felaktig avsändaradress");
        }
        Mottagare[] mott = meddelande.getMottagare();
        for(int i = 0; i < mott.length; i++) {
            if(matchandeTyp(mott[i].getTyp())) {
                if(!EmailAdressValidator.isValid(mott[i].getAdress())) {
                    return new KodText(MeddelandeHandelse.FELAKTIG_MOTTAGARE, "Mottagare nr " + (i+1) + " har felaktig adress");
                }
            }
        }
        return null;
    }

    boolean matchandeTyp(String typ) {
        // EpostMeddelandeSendern är defaultsändare -> accepterar NULL
        if((typ == null) || (typ.length() == 0)) {
            return true;
        }
        final String kandaTyper = "EPOST EPOSTCC EPOSTBCC";
        String[] typer = typ.split(",");
        for (int typloop = 0; typloop < typer.length; typloop++) {
            if (kandaTyper.indexOf(typer[typloop]) >= 0) {
                return true;
            }
        }
        return false;
    }



    /** 
     * @see se.csn.notmotor.ipl.validators.MeddelandeValidator#isValid(se.csn.notmotor.ipl.model.Meddelande)
     */
    public boolean isValid(Meddelande meddelande) {
        return getFelkodForMeddelande(meddelande) == null;
    }
}
