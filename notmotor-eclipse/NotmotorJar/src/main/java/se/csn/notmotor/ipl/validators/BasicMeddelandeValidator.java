package se.csn.notmotor.ipl.validators;

import se.csn.common.mail.EmailAdressValidator;
import se.csn.notmotor.ipl.model.Avsandare;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.KodText;

/**
 * Klass som kontrollerar att ett meddelande ar riktigt formaterat.
 */
public class BasicMeddelandeValidator implements MeddelandeValidator {

    @Override
    public boolean isValid(Meddelande meddelande) {
        return getFelkodForMeddelande(meddelande) == null;
    }

    @Override
    public KodText getFelkodForMeddelande(Meddelande meddelande) {
        if (meddelande == null) {
            return new KodText(MeddelandeHandelse.FELAKTIG_SUBJECT, "Inget meddelande!");
        }

        Avsandare avs = meddelande.getAvsandare();
        if (avs == null) {
            return new KodText(MeddelandeHandelse.FELAKTIG_AVSANDARE, "Avsändare saknas");
        } else {
            if (!EmailAdressValidator.isValid(avs.getEpostadress())) {
                return new KodText(MeddelandeHandelse.FELAKTIG_AVSANDARE, "Felaktig avsändaradress");
            }
            //if(avs.getReplyTo() == null) return skapaHandelse(DTOMeddelandeHandelse.FELAKTIG_AVSANDARE, "Returadress saknas");
        }

        Mottagare[] mott = meddelande.getMottagare();
        if ((mott == null) || (mott.length == 0)) {
            return new KodText(MeddelandeHandelse.FELAKTIG_MOTTAGARE, "Mottagare saknas");
        }
        return null;
    }

}
