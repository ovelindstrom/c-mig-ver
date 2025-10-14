/**
 * Skapad 2007-apr-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.util.List;

import se.csn.notmotor.ipl.model.Status;


public interface DAOStatus {

    /**
     * Skapar en ny rad i tabellen Status
     */
    public int skapa(Status status);

    /**
     * Uppdaterar statusraden.
     */
    public void uppdatera(Status status);

    /**
     * Soker ut de statusar som matchar sokkriterierna
     * @param status Begränsa sökningen till de rader som har denna status. 
     *        Om null så begränsas inte sökningen på status.
     * @param server Begränsa sökningen till de rader som har denna server. 
     *        Om null så begränsas inte sökningen på server.
     */
    public List getStatus(Integer status, Integer server);

    /**
     * Laser upp ett statusonbjekt fran databas.
     */
    public Status getStatus(int instans);

    /**
     * Tar bort motsvarande rad ur databasen. 
     * OBS! Kan ge markliga problem om raden anvands av en aktiv instans. 
     */
    public void delete(int instans);
}
