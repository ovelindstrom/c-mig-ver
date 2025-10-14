/*
 * Skapad 2007-sep-21
 */
package se.csn.notmotor.admin.beans;

import junit.framework.TestCase;
import se.csn.notmotor.admin.beans.SokBean;

/**
 * @author Jonas åhrnell - csn7821
 */
public class TestSokBean extends TestCase {

    public void testSkapaSokSQL() {
        SokBean bean = new SokBean();
        bean.setApplikation("APP");
        bean.setAvsandarnamn("AVSNAMN");
        bean.setAvsandaradress("AVSADRESS");

        String sql = bean.skapaSokWherevillkor();
        assertEquals(sql, "(AVS.NAMN LIKE '%AVSNAMN%') AND (AVS.EPOST LIKE '%AVSADRESS%') AND (AVS.PROGRAMNAMN LIKE '%APP%')");

        bean.setCsnnummer(12345678);
        assertEquals(bean.skapaSokWherevillkor(), "(AVS.NAMN LIKE '%AVSNAMN%') AND (AVS.EPOST LIKE '%AVSADRESS%') AND (AVS.PROGRAMNAMN LIKE '%APP%') AND (MEDD.CSNNUMMER = 12345678)");

        bean = new SokBean();
        bean.setMottagarnamn("MOTTNAMN");
        bean.setMottagaradress("MOTTADRESS");
        assertEquals(bean.skapaSokWherevillkor(), "(MOTT.NAMN LIKE '%MOTTNAMN%') AND (MOTT.ADRESS LIKE '%MOTTADRESS%')");

        bean = new SokBean();
        bean.setMottagaradress("");
        assertEquals(bean.skapaSokWherevillkor(), "");

    }

}
