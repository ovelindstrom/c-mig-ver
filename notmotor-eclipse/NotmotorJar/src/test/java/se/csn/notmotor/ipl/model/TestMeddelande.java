/*
 * @since 2007-okt-04
 */
package se.csn.notmotor.ipl.model;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public class TestMeddelande extends TestCase {

    public void testGettersSetters() {
        Meddelande m = new Meddelande();
        m.setCallbackMask(new Integer(5));
        assertEquals(m.getCallbackMask(), new Integer(5));
        m.setCallbackURL("url");
        assertEquals(m.getCallbackURL(), "url");
        m.setCsnnummer(new Integer(12345678));
        assertEquals(m.getCsnnummer(), new Integer(12345678));
        m.setId(new Long(1234567890));
        assertEquals(m.getId(), new Long(1234567890));
        m.setMeddelandeEncoding("enc");
        assertEquals(m.getMeddelandeEncoding(), "enc");
        m.setMeddelandetext("text");
        assertEquals(m.getMeddelandetext(), "text");
        m.setRubrik("rub");
        assertEquals(m.getRubrik(), "rub");
        m.setRubrikEncoding("rubenc");
        assertEquals(m.getRubrikEncoding(), "rubenc");
        Calendar cal = Calendar.getInstance();
        Date d = cal.getTime();
        m.setSkapad(d);
        assertEquals(m.getSkapad(), d);
        cal.roll(Calendar.HOUR, true);
        d = cal.getTime();
        m.setSkickat(d);
        assertEquals(m.getSkickat(), d);
        cal.roll(Calendar.HOUR, true);
        d = cal.getTime();
        m.setSkickaTidigast(d);
        assertEquals(m.getSkickaTidigast(), d);

        Bilaga[] bilagor = new Bilaga[0];
        m.setBilagor(bilagor);
        assertEquals(m.getBilagor(), bilagor);
        assertFalse(m.hasBilagor());
        Bilaga b = new Bilaga();
        m.addBilaga(b);
        assertTrue(m.hasBilagor());
        assertEquals(m.getBilagor()[0], b);
        b = new Bilaga();
        m.addBilaga(b);
        assertEquals(m.getBilagor()[1], b);

        Mottagare[] mottagare = new Mottagare[0];
        m.setMottagare(mottagare);
        assertEquals(m.getMottagare(), mottagare);
        Mottagare mott = new Mottagare();
        m.addMottagare(mott);
        assertEquals(m.getMottagare()[0], mott);
        mott = new Mottagare();
        m.addMottagare(mott);
        assertEquals(m.getMottagare()[1], mott);

        MeddelandeHandelse[] handelser = new MeddelandeHandelse[0];
        m.setHandelser(handelser);
        assertEquals(m.getHandelser(), handelser);
        MeddelandeHandelse h = new MeddelandeHandelse();
        m.addHandelse(h);
        assertEquals(m.getHandelser()[0], h);
        h = new MeddelandeHandelse();
        m.addHandelse(h);
        assertEquals(m.getHandelser()[1], h);


    }

    public void testConstructors() {
        Meddelande m = new Meddelande("rub", "text");
        assertEquals(m.getRubrik(), "rub");
        assertEquals(m.getMeddelandetext(), "text");

        Mottagare[] mott = new Mottagare[0];
        m = new Meddelande("rub", "text", mott);
        assertEquals(m.getMottagare(), mott);

        m = new Meddelande("rub", "text", "mott", "avsadr");
        assertEquals(m.getMottagare()[0].getAdress(), "mott");
        assertEquals(m.getAvsandare().getEpostadress(), "avsadr");

        m = new Meddelande("rub", "text", "mott", "avsadr", "avsnamn");
        assertEquals(m.getAvsandare().getNamn(), "avsnamn");

        m = new Meddelande("rub", "text", "mott", "avsadr", "avsnamn", "app");
        assertEquals(m.getAvsandare().getApplikation(), "app");
    }


    public void testObjectFunctions() {
        Meddelande o1 = new Meddelande();
        Meddelande o2 = new Meddelande();

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
        assertNotNull(o1.toString());

        o1.setRubrik("rub");
        assertFalse(o1.equals(o2));
        assertFalse(o1.hashCode() == o2.hashCode());
    }

    public void testToString() {
        Meddelande meddelande = new Meddelande("SMS", "Detta är ett test-SMS");
        Mottagare mott = new Mottagare("0705976212", "Jonas å");
        mott.setTyp("SMS");
        meddelande.addMottagare(mott);

        mott = new Mottagare("jonas.ohrnell@csn.se", "Jonas å");
        mott.setTyp("EPOST,EPOSTCC");
        meddelande.addMottagare(mott);

        Avsandare avs = new Avsandare("JUnit-test", "jonas.ohrnell@csn.se", "Notmotor", "Apptest");
        meddelande.setAvsandare(avs);

        meddelande.setCallbackMask(new Integer(MeddelandeHandelse.ALLA_HANDELSER));
        meddelande.setCallbackURL("http://localhost:16080/NotmotorIPL/CallbackTestServlet");
        String s = meddelande.toString();
        System.out.println("Meddelande: " + s);
        assertNotNull(s);
    }

}
