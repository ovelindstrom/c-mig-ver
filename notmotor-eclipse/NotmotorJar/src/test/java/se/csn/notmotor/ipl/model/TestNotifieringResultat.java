/*
 * @since 2007-okt-04
 */
package se.csn.notmotor.ipl.model;

import junit.framework.TestCase;

public class TestNotifieringResultat extends TestCase {

    public void testGettersSetters() {
        NotifieringResultat r = new NotifieringResultat();
        r.setInfo("info");
        assertEquals(r.getInfo(), "info");
        r.setMeddelandeId(new Long(1234567890));
        assertEquals(r.getMeddelandeId(), new Long(1234567890));
        r.setResultat(new Integer(123));
        assertEquals(r.getResultat(), new Integer(123));
    }

    public void testConstructors() {
        NotifieringResultat r = new NotifieringResultat(1234567890);
        assertEquals(r.getMeddelandeId(), new Long(1234567890));

        r = new NotifieringResultat(1234567891, 1, "res");
        assertEquals(r.getMeddelandeId(), new Long(1234567891));
        assertEquals(r.getResultat(), new Integer(1));
        assertEquals(r.getInfo(), "res");

        try {
            r = new NotifieringResultat(1234567891, 13451324, "res");
            fail("Felaktig resultatkod");
        } catch (IllegalArgumentException iae) {
            //OK
        }

    }

    public void testObjectFunctions() {
        NotifieringResultat o1 = new NotifieringResultat();
        NotifieringResultat o2 = new NotifieringResultat();

        assertNotNull(o1.toString());
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
        o1.setInfo("info");
        assertFalse(o1.equals(o2));
        assertFalse(o1.hashCode() == o2.hashCode());
    }

}
