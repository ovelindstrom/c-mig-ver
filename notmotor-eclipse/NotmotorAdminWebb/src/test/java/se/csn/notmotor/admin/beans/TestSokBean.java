package se.csn.notmotor.admin.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import junit.framework.TestCase;
import se.csn.notmotor.admin.beans.SokBean;

public class TestSokBean extends TestCase {

    public void testSkapaSokSQL() {
        SokBean bean = new SokBean();
        bean.setApplikation("APP");
        bean.setAvsandarnamn("AVSNAMN");
        bean.setAvsandaradress("AVSADRESS");

        String sql = bean.skapaSokWherevillkor();
        assertTrue(sql.endsWith("AND (AVS.NAMN LIKE '%AVSNAMN%') AND (AVS.EPOST LIKE '%AVSADRESS%') AND (AVS.PROGRAMNAMN LIKE '%APP%')"));

        bean.setCsnnummer(12345678);
        sql = bean.skapaSokWherevillkor();
        assertTrue(sql.endsWith("(AVS.NAMN LIKE '%AVSNAMN%') AND (AVS.EPOST LIKE '%AVSADRESS%') AND (AVS.PROGRAMNAMN LIKE '%APP%') AND (MEDD.CSNNUMMER = 12345678)"));

        bean = new SokBean();
        bean.setMottagarnamn("MOTTNAMN");
        bean.setMottagaradress("MOTTADRESS");
        assertTrue(bean.skapaSokWherevillkor().endsWith("(MOTT.NAMN LIKE '%MOTTNAMN%') AND (MOTT.ADRESS LIKE '%MOTTADRESS%')"));

        bean = new SokBean();
        bean.setMottagaradress("");
        assertFalse(bean.skapaSokWherevillkor().contains("MOTT."));

    }

    public void testSkapaSokWherevillkorMedDatumIntervall() {
        SokBean bean = new SokBean();
        
        // Beräkna gårdagens och dagens datum
        Calendar cal = Calendar.getInstance();
        
        // Dagens datum (tom-datum med tid 23:59:59)
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();
        
        // Gårdagens datum (from-datum med tid 00:00:00)  
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date yesterday = cal.getTime();
        
        // Formatera datum enligt förväntat format (yyyy-mm-dd)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayStr = dateFormat.format(yesterday);
        String todayStr = dateFormat.format(today);
        
        // Sätt datum i bean
        bean.setFromSkapat(yesterdayStr);
        bean.setTomSkapat(todayStr);
        
        String sql = bean.skapaSokWherevillkor();
        
        // Kontrollera att SQL innehåller korrekta TIMESTAMP-villkor
        // Format: TIMESTAMP('yyyy-MM-dd-HH.mm.ss.000000')
        assertTrue("SQL ska innehålla SKAPADTIDPUNKT >= villkor", 
                   sql.contains("(MEDD.SKAPADTIDPUNKT >= TIMESTAMP('" + yesterdayStr + "-00.00.00.000000'))"));
        assertTrue("SQL ska innehålla SKAPADTIDPUNKT <= villkor",
                   sql.contains("(MEDD.SKAPADTIDPUNKT <= TIMESTAMP('" + todayStr + "-23.59.59.000000'))"));
        
        // Kontrollera att båda villkoren finns med AND
        assertTrue("SQL ska innehålla både from- och tom-villkor med AND", 
                   sql.contains("AND"));

        // Mer detaljerad validering
        String expectedFromClause = "(MEDD.SKAPADTIDPUNKT >= TIMESTAMP('" + yesterdayStr + "-00.00.00.000000'))";
        String expectedToClause = "(MEDD.SKAPADTIDPUNKT <= TIMESTAMP('" + todayStr + "-23.59.59.000000'))";
        
        // Kontrollera att SQL matchar det förväntade mönstret med rätt datum
        assertTrue("SQL ska innehålla korrekt datumintervall för gårdag till idag", 
                   sql.matches(".*\\(MEDD\\.SKAPADTIDPUNKT >= TIMESTAMP\\('" + yesterdayStr + "-00\\.00\\.00\\.000000'\\)\\) AND \\(MEDD\\.SKAPADTIDPUNKT <= TIMESTAMP\\('" + todayStr + "-23\\.59\\.59\\.000000'\\)\\).*"));
        
        // Debug-utskrift för manuell verifiering (synligt i Maven Surefire rapporter)
        System.out.println("=== TEST DEBUG INFO ===");
        System.out.println("Förväntat from-villkor: " + expectedFromClause);
        System.out.println("Förväntat to-villkor: " + expectedToClause);
        System.out.println("Genererat SQL: " + sql);
        System.out.println("======================");
    }

}
