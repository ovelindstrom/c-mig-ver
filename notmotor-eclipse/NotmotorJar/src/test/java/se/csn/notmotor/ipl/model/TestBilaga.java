/*
 * @since 2007-okt-04
 */
package se.csn.notmotor.ipl.model;

import junit.framework.TestCase;

public class TestBilaga extends TestCase {
    public void testGetterSetters() {
        Bilaga b = new Bilaga();
        byte[] data = new byte[]{1, 2, 3};
        b.setData(data);
        assertEquals(b.getData(), data);
        b.setEncoding("enc");
        assertEquals(b.getEncoding(), "enc");
        b.setFilnamn("fil");
        assertEquals(b.getFilnamn(), "fil");
        b.setId(new Long(12345));
        assertEquals(b.getId(), new Long(12345));
        b.setMimetyp("mime");
        assertEquals(b.getMimetyp(), "mime");
    }

    public void testConstructors() {
        byte[] data = new byte[]{1, 2, 3};
        Bilaga b = new Bilaga(data);
        assertEquals(b.getData(), data);

        b = new Bilaga(data, "fil");
        assertEquals(b.getFilnamn(), "fil");
    }

    public void testObjectFunctions() {
        Bilaga o1 = new Bilaga();
        Bilaga o2 = new Bilaga();

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
        assertNotNull(o1.toString());

        o1.setFilnamn("fil");
        assertFalse(o1.equals(o2));
        assertFalse(o1.hashCode() == o2.hashCode());
    }

}
