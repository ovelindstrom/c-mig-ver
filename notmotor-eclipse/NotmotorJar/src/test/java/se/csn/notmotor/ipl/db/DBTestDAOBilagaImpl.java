/**
 * @since 2007-apr-12
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.util.List;

import junit.framework.TestCase;
import se.csn.common.db.TestUtils;
import se.csn.notmotor.ipl.model.Bilaga;


public class DBTestDAOBilagaImpl extends TestCase {

    private QueryProcessor getQueryProcessor() {
        return new SingleConnectionQueryProcessor(TestUtils.getTestDB2DataSource("csn24utv", 50000, "WDBUTV", "notmotor", "notmotor"));
    }

    public void testCRUD() {
        DAOBilagaImpl dao = new DAOBilagaImpl(getQueryProcessor());

        Bilaga b = new Bilaga();

        try {
            dao.createBilaga(b, 1);
            fail("Ska inte gå, data saknas");
        } catch (IllegalArgumentException iae) {
            //OK
        }

        b.setData(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
        long id = dao.createBilaga(b, 1);

        // Hämta:
        Bilaga b2 = dao.getBilaga(id);
        assertEquals(b2, b);
    }

    public void testHamtaPerMeddelande() {
        DAOBilagaImpl dao = new DAOBilagaImpl(getQueryProcessor());

        long meddelandebas = System.currentTimeMillis();

        Bilaga b = new Bilaga(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}, "Fil1");
        dao.createBilaga(b, meddelandebas);

        b = new Bilaga(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}, "Fil2");
        dao.createBilaga(b, meddelandebas + 1);

        b = new Bilaga(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}, "Fil3");
        dao.createBilaga(b, meddelandebas + 2);


        // Hämta:
        List list = dao.getBilagorForMeddelande(meddelandebas + 2);
        assertEquals(list.size(), 1);
        Bilaga b2 = (Bilaga) list.get(0);
        assertEquals(b2, b);
    }

}
