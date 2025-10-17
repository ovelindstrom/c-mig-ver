/**
 * @since 2007-apr-24
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import se.csn.common.db.TestUtils;
import junit.framework.TestCase;


public class DBTestDAOServerImpl extends TestCase {

    private QueryProcessor getQueryProcessor() {
        return new SingleConnectionQueryProcessor(TestUtils.getTestDB2DataSource("csn24utv", 50000, "WDBUTV", "notmotor", "notmotor"));
    }

    public void testCRU() {
        DAOServerImpl dao = new DAOServerImpl(getQueryProcessor());

        // Skapa
        // Hämta
        // Uppdatera
        // Hämta
    }

    public void testGet() {

    }
}
