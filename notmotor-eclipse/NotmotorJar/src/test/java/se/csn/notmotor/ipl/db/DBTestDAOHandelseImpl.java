/**
 * Skapad 2007-apr-12
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;
import se.csn.common.db.TestUtils;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;


public class DBTestDAOHandelseImpl extends TestCase {
    private QueryProcessor getQueryProcessor() {
        return new SingleConnectionQueryProcessor(TestUtils.getTestDB2DataSource("csn24utv", 50000, "WDBUTV", "notmotor", "notmotor"));
    }

    public void testCRUD() {
        DAOHandelseImpl dao = new DAOHandelseImpl(getQueryProcessor());

        String[][] testdata = new String[][]{
            // typ, kod, text, tidpunkt, instans
            {"1", "2", "Text", "20070420123456", "1"},
            {"1", "2", "Textåäö ÅÄÖ ", null, "1"},
        };

        for (int i = 0;i < testdata.length;i++) {
            // Skapa
            MeddelandeHandelse h = new MeddelandeHandelse();
            h.setHandelsetyp(toInteger(testdata[i][0]));
            h.setFelkod(toInteger(testdata[i][1]));
            h.setFeltext(testdata[i][2]);
            h.setTidpunkt(toDate(testdata[i][3]));
            h.setInstans(toInteger(testdata[i][4]));
            long id = dao.createHandelse(h, 1);

            // Hämta
            MeddelandeHandelse h2 = dao.getHandelse(id);
            assertTrue(h.equals(h2));
        }

        // Ändra ej intressant
        // Ta bort ej intressant

    }

    private Integer toInteger(String s) {
        if (s == null) return null;
        return new Integer(s);
    }

    private Date toDate(String s) {
        if (s == null) return null;
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(s);
        } catch (ParseException e) {
            throw new IllegalArgumentException(s + " måste ha format yyyyMMddHHmmss");
        }
    }

}
