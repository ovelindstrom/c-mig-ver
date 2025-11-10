package se.csn.notmotor.ipl.validators;

import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.KodText;
/**
 * Kontrollerar: 
 * -mobilnummer
 * -att texten inte är längre än 160 tecken
 */
public class SMSValidator implements MeddelandeValidator {

    public static final String NUMMERFORMAT = "^(\\+?)[\\d]{10,18}$";

    public KodText getFelkodForMeddelande(Meddelande meddelande) {
        // Kontrollera: 
        // 1. telefonnumren
        Mottagare[] mott = meddelande.getMottagare();

        for (int i = 0;i < mott.length;i++) {
            if ((mott[i].getTyp() != null) && (mott[i].getTyp().equalsIgnoreCase("SMS"))) {
                String nummer = mott[i].getAdress();
                if ((nummer == null) || (nummer.length() == 0)) {
                    return new KodText(MeddelandeHandelse.FELAKTIG_MOTTAGARE, "SMS-nummer saknas");
                } else if (!nummer.matches(NUMMERFORMAT)) {
                    return new KodText(MeddelandeHandelse.FELAKTIG_MOTTAGARE,
                        "SMS-numret måste vara mellan 10 och 18 tecken och får bara innehålla inledande + och/eller siffror");
                }
            }
        }

        return null;
    }

    public boolean isValid(Meddelande m) {
        return getFelkodForMeddelande(m) == null;
    }

}
