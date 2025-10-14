/**
 * Skapad 2007-apr-10
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.util.List;

import se.csn.notmotor.ipl.model.Mottagare;

public interface DAOMottagare {
    public int createMottagare(Mottagare mott, long meddelandeId);

    public List<Mottagare> getMottagareForMeddelande(long meddelandeId);

    public List<Mottagare> getMottagare(Mottagare mott);

    public Mottagare getMottagare(long id);

    public void updateMottagare(Mottagare mott);

    public void deleteMottagare(Mottagare mott);
}