/**
 * Skapad 2007-mar-21
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;


public class TestDAOImplBase extends TestCase {

    class HandlerBaseTestSub extends DAOImplBase {
        public HandlerBaseTestSub() {
            super(null);
        }
        public Object newRow(ResultSet rs) throws SQLException {
            return null;
        }
    }

    public void testQuoteValue() {
        HandlerBaseTestSub hb = new HandlerBaseTestSub();
        assertEquals(DAOImplBase.quoteValue(null), "NULL");
        assertEquals(DAOImplBase.quoteValue("String"), "'String'");
        assertEquals(DAOImplBase.quoteValue(new Integer(123)), "123");
        assertEquals(DAOImplBase.quoteValue(new Long(123)), "123");
    }

    public void testMakeUpdateQuery() {
        HandlerBaseTestSub hb = new HandlerBaseTestSub();
        String uq = hb.makeUpdateQuery("TABNAMN", new Object[]{"COL1", "VAL1", "COL2", new Integer(2)},null);
        assertEquals(uq, "UPDATE TABNAMN SET COL1='VAL1',COL2=2");
        uq = hb.makeUpdateQuery("TABNAMN", new Object[]{"COL1", null, "COL2", new Integer(2)},null);
        assertEquals(uq, "UPDATE TABNAMN SET COL1=null,COL2=2");

        uq = hb.makeUpdateQuery("TABNAMN", new Object[]{"COL1", "VAL1"},new Object[]{"COL2", null});
        assertEquals(uq, "UPDATE TABNAMN SET COL1='VAL1' WHERE COL2 IS NULL");
        uq = hb.makeUpdateQuery("TABNAMN", new Object[]{"COL1", "VAL1"},new Object[]{"COL2", "VAL2"});
        assertEquals(uq, "UPDATE TABNAMN SET COL1='VAL1' WHERE COL2='VAL2'");

    }

    public void testAddLike() {
        // Testa med eller utan AND
        String res = DAOImplBase.addLike("ORIG", "COL", "VALUE", false);
        assertEquals(res, "ORIG AND (COL LIKE 'VALUE')");
        res = DAOImplBase.addLike("", "COL", "VALUE", false);
        assertEquals(res, "(COL LIKE 'VALUE')");
        // Testa med NULL
        res = DAOImplBase.addLike("ORIG", "COL", null, false);
        assertEquals(res, "ORIG");
        // Testa med tom sträng
        res = DAOImplBase.addLike("ORIG", "COL", "", false);
        assertEquals(res, "ORIG");
        res = DAOImplBase.addLike("ORIG", "COL", "", true);
        assertEquals(res, "ORIG");
        // Testa med eller utan wildcards
        res = DAOImplBase.addLike(null, "COL", "%FORE", true);
        assertEquals(res, "(COL LIKE '%FORE%')");
        res = DAOImplBase.addLike(null, "COL", "EFTER%", true);
        assertEquals(res, "(COL LIKE '%EFTER%')");
        res = DAOImplBase.addLike(null, "COL", "UTAN", true);
        assertEquals(res, "(COL LIKE '%UTAN%')");
        // Testa med bara '%'
        res = DAOImplBase.addLike(null, "COL", "%", true);
        assertEquals(res, "(COL LIKE '%')");
        // Testa med % mitt i strängen
        res = DAOImplBase.addLike(null, "COL", "I %MITTEN", true);
        assertEquals(res, "(COL LIKE '%I %MITTEN%')");
    }
}
