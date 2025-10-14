/*
 * Skapad 2007-nov-05
 */
package se.csn.notmotor.ipl.validators;

import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.KodText;

/**
 * Marklig namngivning pga Harvest:
 * @author Jonas åhrnell - csn7821
 */
public interface MeddelandeValidator {
    boolean isValid(Meddelande meddelande);
    KodText getFelkodForMeddelande(Meddelande meddelande);
}
