/**
 * Skapad 2007-apr-23
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.util.List;

import se.csn.notmotor.ipl.model.Server;

public interface DAOServer {
    public int skapa(Server server);
    public List getAktiva(boolean aktiv);
    public Server get(int serverid);
    public void uppdatera(Server server);
    public int getLevandeProcesser(int serverid);
    public void delete(int serverid);
}
