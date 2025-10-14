/**
 * Skapad 2007-apr-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import se.csn.common.db.TestUtils;
import se.csn.notmotor.ipl.model.Status;


public class DBTestDAOStatusImpl extends TestCase {

    private QueryProcessor getQueryProcessor() {
        return new SingleConnectionQueryProcessor(TestUtils.getTestDB2DataSource("csn24utv", 50000, "WDBUTV", "notmotor", "notmotor"));
    }
    
    public void testCRU() {
        DAOStatusImpl dao = new DAOStatusImpl(getQueryProcessor());
        
        // Skapa
        Status s = new Status();
        s.setServer(10);
        Date skapaddat = datumUtanMillis();
        s.setStartad(skapaddat);
        s.setStatus(2);
        int id = dao.skapa(s);
        assertEquals(id, s.getInstans());

        // Hämta
        Status s2 = dao.getStatus(id);
        assertEquals(s, s2);
        
        // Uppdatera
        Date stoppaddat = datumUtanMillis();
        s.setStoppad(stoppaddat);
        s.setWatchdog(stoppaddat);
        dao.uppdatera(s);
        // Hämta
        s2 = dao.getStatus(id);
        assertEquals(s, s2);
    }
    
    public void testGet() {
        DAOStatusImpl dao = new DAOStatusImpl(getQueryProcessor());
        // Skapa tre, två med samma server, en med annan
        Status s = new Status();
        int serverid = (int)(System.currentTimeMillis() % Integer.MAX_VALUE);
        int status = serverid - 10000000;
        s.setServer(serverid);
        s.setStatus(status);
        
        int id1 = dao.skapa(s);
        int id2 = dao.skapa(s);
        
        s.setServer(serverid-1);
        int id3 = dao.skapa(s);
        
        List list = dao.getStatus(null, new Integer(serverid));
        assertEquals(list.size(), 2);
        list = dao.getStatus(null, new Integer(serverid-1));
        assertEquals(list.size(), 1);
        list = dao.getStatus(new Integer(status), null);
        assertEquals(list.size(), 3);
    }
    
    private Date datumUtanMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
