/**
 * Skapad 2007-apr-12
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import se.csn.common.db.TestUtils;
import se.csn.notmotor.ipl.model.Mottagare;
import junit.framework.TestCase;


public class DBTestDAOMottagareImpl extends TestCase {

    private QueryProcessor getQueryProcessor() {
        return new SingleConnectionQueryProcessor(TestUtils.getTestDB2DataSource("csn24utv", 50000, "WDBUTV", "notmotor", "notmotor"));
    }

    public void testCRUD() {
        DAOMottagareImpl dao = new DAOMottagareImpl(getQueryProcessor());
        Mottagare mott = new Mottagare("jonas.ohrnell@csn.se", "Jonas å", 12345678);

        long id = dao.createMottagare(mott, 1);

        Mottagare mott2 = dao.getMottagare(id);
        assertEquals(mott2, mott);
    }


}
