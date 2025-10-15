/*
 * Skapad 2007-okt-04
 */
package se.csn.notmotor.ipl.model;

import java.util.Date;

import junit.framework.TestCase;

/**
 * @author Jonas åhrnell - csn7821
 */
public class TestMeddelandeHandelse extends TestCase {
    public void testGetterSetters() {
        MeddelandeHandelse h = new MeddelandeHandelse();
        h.setFelkod(new Integer(1));
        assertEquals(h.getFelkod(), new Integer(1));
        h.setFeltext("fel");
        assertEquals(h.getFeltext(), "fel");
        h.setHandelsetyp(new Integer(2));
        assertEquals(h.getHandelsetyp(), new Integer(2));
        h.setId(new Long(1234567890));
        assertEquals(h.getId(), new Long(1234567890));
        h.setInstans(new Integer(3));
        assertEquals(h.getInstans(), new Integer(3));
        Date d = new Date();
        h.setTidpunkt(d);
        assertEquals(h.getTidpunkt(), d);
    }

    public void testConstructors() {
        MeddelandeHandelse h = new MeddelandeHandelse(1);
        assertEquals(h.getHandelsetyp(), new Integer(1));

        h = new MeddelandeHandelse(2, 3, "fel");
        assertEquals(h.getHandelsetyp(), new Integer(2));
        assertEquals(h.getFelkod(), new Integer(3));
        assertEquals(h.getFeltext(), "fel");
    }


    public void testObjectFunctions() {
        MeddelandeHandelse o1 = new MeddelandeHandelse();
        MeddelandeHandelse o2 = new MeddelandeHandelse();

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
        assertNotNull(o1.toString());
        o1.setFeltext("fel");
        assertFalse(o1.equals(o2));
        assertFalse(o1.hashCode() == o2.hashCode());
    }

    public void testHandelsetexter() {
        assertEquals(MeddelandeHandelse.getTyptext(MeddelandeHandelse.BESVARAT), "Besvarat");
        assertEquals(MeddelandeHandelse.getTyptext(MeddelandeHandelse.MOTTAGET), "Mottaget");
        assertEquals(MeddelandeHandelse.getTyptext(MeddelandeHandelse.SKICKAT_SERVER), "Sänt");
        assertEquals(MeddelandeHandelse.getTyptext(MeddelandeHandelse.MEDDELANDEFEL), "Fel i meddelande");
        assertEquals(MeddelandeHandelse.getTyptext(MeddelandeHandelse.TEKNISKT_FEL), "Tekniskt fel");
        assertEquals(MeddelandeHandelse.getTyptext(-1), "Under sändning (instans 1)");
        assertEquals(MeddelandeHandelse.getTyptext(1000000), "Okänd (1000000)");
    }

}
