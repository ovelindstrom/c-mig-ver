package se.csn.ipl.notmotor.xml;

import junit.framework.TestCase;


public class DigesterTest extends TestCase {
    public void testDummy() {
        assertNotNull("a");
    }
    /*public void testDigest() throws IOException, SAXException, ParseException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("test1.xml");
        MeddelandeDigester md = new MeddelandeDigester();
        DTOMeddelande m = null;
        try {
            m = md.xmlToDTOMeddelande(is);
        } catch(Exception e) {
            System.out.println("Fel: " + e);
            throw new RuntimeException(e);
        }
        
        assertNotNull(m);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd HHmmss");
        Date d = fmt.parse("20070930 100000");
        assertEquals(m.getSkickaTidigast(), d);
        assertEquals(m.getCsnnummer().intValue(), 12345678);
        assertEquals(m.getRubrik(), "Testmail");
        assertEquals(m.getRubrikEncoding(), "123");
        assertEquals(m.getMeddelandetext(), "Detta \nÄr \nEtt \nTestmail!!! åäö ÅÄÖ");
        assertEquals(m.getMeddelandeEncoding(), "456");
        
        DTOAvsandare avs = m.getAvsandare(); 
        assertNotNull(avs);
        assertEquals(avs.getNamn(), "Jonas å");
        assertEquals(avs.getEpostadress(), "noreply@csn.se");
        assertEquals(avs.getReplyTo(), "jonas.ohrnell@csn.se");
        assertEquals(avs.getApplikation(), "App1");
        assertEquals(avs.getKategori(), "Test");
        
        DTOMottagare[] mott = m.getMottagare();
        assertNotNull(mott);
        assertEquals(mott.length, 2);
        assertEquals(mott[0].getNamn(), "Jonas å");
        assertEquals(mott[0].getAdress(), "jonas.ohrnell@csn.se");
        assertEquals(mott[0].getCsnnummer().intValue(), 12345678);
        
    }*/
}
