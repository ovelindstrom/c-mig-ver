/**
 * @since 2007-apr-10
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.util.Date;
import java.util.List;

import se.csn.notmotor.ipl.model.Avsandare;
import se.csn.notmotor.ipl.model.Bilaga;
import se.csn.notmotor.ipl.model.Kanal;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.Mottagare;

public interface DAOMeddelande extends RowToObjectMapper {
    long createMeddelande(Meddelande m);

    Meddelande getMeddelande(long meddelandeId);

    void markeraMeddelandenForInstans(int instans, int antalMeddelanden, List<Kanal> kanalerMedBegransning);

    List getMarkeradeMeddelanden(int instans);

    Meddelande[] sokMeddelanden(Date from, Date tom,
                                Avsandare[] avsandare, Mottagare[] mottagare,
                                String textinnehall, Integer minstorlek, Integer maxstorlek,
                                Integer handelseMask, Integer felmask, Bilaga[] bilagor);

    void deleteMeddelande(long meddelandeId);

    void updateMeddelande(Meddelande meddelande);

}
