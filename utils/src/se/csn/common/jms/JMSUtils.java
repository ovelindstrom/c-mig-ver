/**
 * Skapad 2007-feb-02
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.common.jms;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Skapad 2007-feb-02
 * @author Jonas åhrnell (csn7821)
 */
public class JMSUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
    private static String NULLCORRID = "000000000000000000000000";
    
    /**
     * CorrID, Correlation ID, används för att koppla ihop olika JMS-meddelanden. 
     * Vi använder MQ och då är Correlation ID 24 tecken långt.
     * @return En sträng som är 24 tecken lång. De första 15 tecken är en tidsstämpel: 
     * YYMMDD hhmmss + millisekunder. De nästa 9 tecknen är ett slumptal.
     * OBS! Den här algoritmen kan inte garantera att två på varandra följande corr-id:n är 
     * unika. 
     * 1) Det finns en risk för kollision efter 100 år :=)
     * 2) Om flera corrID:n efterfrågas samma millisekund så kan det hända 
     *    att de kolliderar. Eftersom det bara är 10^9 slumptalsdelar så finns det
     *    en icke försumbar sannolikhet att det inträffar en kollision. 
     *    Om GARANTERAT unika corrID:n eftersträvas så kan man antingen gå till 
     *    en helt annan algoritm eller införa en fördröjning på en millisekund.  
     */
    public synchronized static String generateRandomCorrID() {
        // 15 siffror tidsangivelse (YYMMDDHHMMSSmmm) + 9 siffror slump:
        String corrID = sdf.format(new Date()) + (int)(Math.random() * 1000000000);
        //String corrId = Math.abs(((System.currentTimeMillis()>>>16)<<2)+Math.random().nextLong())+"";
		
		return corrID + NULLCORRID.substring(0, 24 - corrID.length());
    }
    
    public synchronized static String getSelectorForCorrid(String corrID, String encoding) {
        if((corrID == null) || (corrID.length() != 24) || (encoding == null)) throw new IllegalArgumentException("CorrID måste vara exakt 24 tecken");
        byte[] correlBytes = null;
        try {
            correlBytes = corrID.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Hittade inte codepage: " + encoding);
        }
        
        StringBuffer fBuf = new StringBuffer("JMSCorrelationID = 'ID:");
    	for(int i=0; i<24; i++){ 
   			if (i>=correlBytes.length) {
    			fBuf.append("00");
    		} else {
				byte b = correlBytes[i];
				String hexStr = Integer.toHexString(b);
				if (hexStr.length()>2) {
				    hexStr = hexStr.substring(hexStr.length()-2);
				}
				if (hexStr.length()<2) {
				    fBuf.append("0");
				}
				fBuf.append(hexStr);
   			}
   		}
   		fBuf.append("'");
   		return fBuf.toString();	
    }
    
    
}
