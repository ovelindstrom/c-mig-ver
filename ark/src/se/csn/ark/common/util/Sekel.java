/*
 * Created on 2007-aug-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.csn.ark.common.util;

/**
 * @author csk4135
 *
 * Returnera ett sekel utifrån ett personnummer
 */
public class Sekel {

    /**
     * @return Retrunerar sekel utifrån ett personnummer.
     */
    public static String getSekel(String personnummer) {
        if(personnummer != null && personnummer.length() > 1 && Character.isDigit(personnummer.charAt(0)) && Character.isDigit(personnummer.charAt(1))) {
            Integer year  = new Integer(personnummer.substring(0,2));
            if(year.intValue() < 40) {
                return "20";
            }
        }
        return "19";
    }

}
