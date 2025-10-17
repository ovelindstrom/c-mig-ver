package se.csn.notmotor.ipl.model;

import junit.framework.TestCase;

public class TestMottagare extends TestCase {
    public void testGettersSetters() {
        Mottagare m = new Mottagare();
        m.setAdress("adr");
        assertEquals(m.getAdress(), "adr");
        m.setCsnnummer(Integer.valueOf(1));
        assertEquals(m.getCsnnummer(), Integer.valueOf(1));
        m.setId(Long.valueOf(2));
        assertEquals(m.getId(), Long.valueOf(2));
        m.setNamn("namn");
        assertEquals(m.getNamn(), "namn");
        m.setTyp("typ");
        assertEquals(m.getTyp(), "typ");
        m.setStatus(Integer.valueOf(3));
        assertEquals(m.getStatus(), Integer.valueOf(3));
    }

    public void testConstructors() {
        Mottagare m = new Mottagare("adr1");
        assertEquals(m.getAdress(), "adr1");

        m = new Mottagare("adr2", "namn2");
        assertEquals(m.getAdress(), "adr2");
        assertEquals(m.getNamn(), "namn2");

        m = new Mottagare("adr3", "namn3", 123);
        assertEquals(m.getAdress(), "adr3");
        assertEquals(m.getNamn(), "namn3");
        assertEquals(m.getCsnnummer(), Integer.valueOf(123));

    }

    public void testObjectFunctions() {
        Mottagare o1 = new Mottagare();
        Mottagare o2 = new Mottagare();

        assertNotNull(o1.toString());
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
        o1.setAdress("adr");
        assertFalse(o1.equals(o2));
        assertFalse(o1.hashCode() == o2.hashCode());
    }

}
