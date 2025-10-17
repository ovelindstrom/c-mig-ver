package se.csn.notmotor.ipl.db;

import java.util.List;

import se.csn.notmotor.ipl.model.Bilaga;

public interface DAOBilaga {
    /**
     * Skapar bilaga i databasen
     * @return nyckeln för bilagan
     */
    public long createBilaga(Bilaga b, long meddelandeid);

    public List<Bilaga> getBilagorForMeddelande(long meddelandeid);

    public Bilaga getBilaga(long id);
}
