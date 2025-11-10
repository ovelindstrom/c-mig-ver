/**
 * Skapad 2007-jun-11
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
    
    private static final String[][] TIDSNAMN = new String[][] {
            {"sekunder", "sekund"},
            {"minuter", "minut"},
            {"timmar", "timme"},
            {"dagar", "dag"},
    };
    
    private static final String[] DATUMFORMAT = new String[] {
        "yyyy-MM-dd","yy-MM-dd","yyyyMMdd","yyMMdd"
    };
    private static final String[] TIDFORMAT = new String[] {
        "HH:mm:ss","HH:mm","HH.mm.ss","HH.mm",
    };

    
    public static String sekunderTillTidStrang(int sekunder) {
        if(sekunder < 0) {
            throw new IllegalArgumentException("Ange 0 eller fler sekunder, angav " + sekunder);
        }
        
        int[] tider = new int[4];
        tider[0] = sekunder % 60;
	    sekunder /= 60;
	    tider[1] = sekunder % 60;
	    sekunder /= 60;
	    tider[2] = sekunder % 24;
	    sekunder /= 24;
	    tider[3] = sekunder;
	    
	    String result = "";
	    for(int i = 0; i < tider.length; i++) {
	        String s = "";
	        if(tider[i] > 1) {
	            s = ("" + tider[i] + " " + TIDSNAMN[i][0]);
	        } else if(tider[i] == 1) {
	            s = ("" + tider[i] + " " + TIDSNAMN[i][1]);
	        } else {
	            // Skriv inget
	        }
	        // Hantera kommaseparation:
	        if(s.length() > 0) {
		        if(result.length() > 0) {
		            result = s + ", " + result;
		        } else {
		            result = s;
		        }
	        }
	    }
	    
	    // Hantera om det inte fanns någon tid:
	    if(result.equals("")) {
	        result = "0 sekunder";
	    }
	    return result;
  	}
    
    public static Date strToDate(String s) {
        return strToDate(s, true);
    }
    
    /**
     * @param dygnetsBorjan Har bara betydelse om tidsdelen utelämnas. 
     *        Om true så sätts tiden till 00.00.00.000, dvs. den första millisekunden 
     *        på det angivna dygnet. Om false sätts tiden till 23.59.59.999, alltså 
     * 		  dygnets sista millisekund.
     */
    public static Date strToDate(String s, boolean dygnetsBorjan) {
        if(s == null) {
            return null;
        }
        s = s.trim();
        if(s.length() == 0) {
            return null;
        }
        // Loopa igenom tidsformaten och testa. 
        // först tillsammans, sedan var för sig
        Date d = null;
        for(int df = 0; df < DATUMFORMAT.length; df++) {
            for(int tf = 0; tf < TIDFORMAT.length; tf++) {
                SimpleDateFormat fmt = new SimpleDateFormat(DATUMFORMAT[df] + " " + TIDFORMAT[tf]);
                try {
                    d = fmt.parse(s);
                    return d;
                } catch (ParseException e) {
                }
            }
        }
        for(int df = 0; df < DATUMFORMAT.length; df++) {
            SimpleDateFormat fmt = new SimpleDateFormat(DATUMFORMAT[df]);
            try {
                d = fmt.parse(s);
                // Här sätts tiden beroende på om det är dygnets början:
                if(dygnetsBorjan) {
                    return setTidsdel(d, 0, 0, 0, 0);
                } else {
                    return setTidsdel(d, 23,59,59,999);
                }
            } catch (ParseException e) {
                //System.out.println("Matchade inte " + DATUMFORMAT[df]);
            }
        }
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        for(int tf = 0; tf < TIDFORMAT.length; tf++) {
            SimpleDateFormat fmt = new SimpleDateFormat(TIDFORMAT[tf]);
            try {
                d = fmt.parse(s);
                cal.setTime(d);
                cal.set(year, month, day);
                cal.set(Calendar.MILLISECOND, 0);
                return cal.getTime();
            } catch (ParseException e) {
            }
        }
        return d;
    }
    
    public static Date setTidsdel(Date d, int timme, int minut, int sekund, int millisekund) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, timme);
        cal.set(Calendar.MINUTE, minut);
        cal.set(Calendar.SECOND, sekund);
        cal.set(Calendar.MILLISECOND, millisekund);
        return cal.getTime();
    }
    
}
