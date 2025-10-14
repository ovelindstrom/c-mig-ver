/**
 * Skapad 2007-apr-10
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.util.List;

import se.csn.notmotor.ipl.model.Avsandare;

public interface DAOAvsandare {
    int createAvsandare(Avsandare avs);

    Avsandare getAvsandare(int id);

    List getAvsandare(String namn);
    
    Avsandare sokEnAvsandare(Avsandare avs);
    /**
     * Om någon inparameter är null så används den inte i frågan
     */
    List getAvsandare(String namn, String programnamn,
            String kategori, String epost, String replyto);

    List getAvsandare(Avsandare avs);
    
    void updateAvsandare(Avsandare avs);

    void deleteAvsandare(Avsandare avs);
    
    int getId(Avsandare avsandare);
}