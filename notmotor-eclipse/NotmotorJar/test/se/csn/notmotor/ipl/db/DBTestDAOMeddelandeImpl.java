/**
 * Skapad 2007-apr-11
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import junit.framework.TestCase;
import se.csn.common.db.TestUtils;
import se.csn.notmotor.ipl.model.Meddelande;


public class DBTestDAOMeddelandeImpl extends TestCase {

    private QueryProcessor getQueryProcessor() {
        return new SingleConnectionQueryProcessor(TestUtils.getTestDB2DataSource("csn24utv",50000, "WDBUTV", "notmotor", "notmotor"));
    }
    
    public void testCRUD() {
        DAOHandelseImpl handelseImpl = new DAOHandelseImpl(getQueryProcessor());
        DAOAvsandareImpl avsandareImpl = new DAOAvsandareImpl(getQueryProcessor());
        DAOMottagareImpl mottagareImpl = new DAOMottagareImpl(getQueryProcessor());
        DAOBilagaImpl bilagaImpl = new DAOBilagaImpl(getQueryProcessor()); 
        DAOMeddelandeImpl dao = new DAOMeddelandeImpl(getQueryProcessor(), avsandareImpl, mottagareImpl, bilagaImpl, handelseImpl);
        
        // Skapa
        Meddelande m = new Meddelande("Rubrik","Meddelandetext!", "mottagare@anyhoo.com", "avsandare@csn.se", "Noreply");
        long id = dao.createMeddelande(m);
        
        // Hämta
        Meddelande m2 = dao.getMeddelande(id);
        assertEquals(m2, m);
        
        // Ändra
        // Hämta
        // Ta bort
        // Försök hämta
    }
    
}
