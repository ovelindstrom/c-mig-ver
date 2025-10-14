/*
 * Skapad 2007-okt-04
 */
package se.csn.notmotor.ipl.model;

import junit.framework.TestCase;

/**
 * @author Jonas åhrnell - csn7821
 */
public class TestAvsandare extends TestCase {
    public void testGettersSetters() {
        Avsandare avs = new Avsandare();
        avs.setApplikation("app");
        assertEquals(avs.getApplikation(), "app");
        avs.setEpostadress("epost");
        assertEquals(avs.getEpostadress(), "epost");
        avs.setId(new Long(1));
        assertEquals(avs.getId(), new Long(1));
        avs.setKategori("kat");
        assertEquals(avs.getKategori(), "kat");
        avs.setNamn("namn");
        assertEquals(avs.getNamn(), "namn");
        avs.setReplyTo("rep");
        assertEquals(avs.getReplyTo(), "rep");
    }

    public void testConstructors() {
        Avsandare a = new Avsandare("namn", "adr");
        assertEquals(a.getNamn(), "namn");
        assertEquals(a.getEpostadress(), "adr");
        
        a = new Avsandare("namn", "adr", "app", "kat");
        assertEquals(a.getApplikation(), "app");
        assertEquals(a.getKategori(), "kat");
    }
    
    public void testObjectFunctions() {
        Avsandare o1 = new Avsandare();
        Avsandare o2 = new Avsandare();
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
        assertNotNull(o1.toString());

        o1.setNamn("a1");
        assertFalse(o1.equals(o2));
        assertFalse(o1.hashCode() ==  o2.hashCode());
    }
}
