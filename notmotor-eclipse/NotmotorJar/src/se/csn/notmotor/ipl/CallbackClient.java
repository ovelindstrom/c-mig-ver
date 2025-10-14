/*
 * Skapad 2007-okt-12
 */
package se.csn.notmotor.ipl;

import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.model.ConvertCallbackDTO;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.webservice.bas.notmotor.callback.CallbackProxy;

/**
 * Klient som används för att skicka ett callbackanrop till webbservice.
 * @author Jonas åhrnell - csn7821
 */
public class CallbackClient {
    
    private static Log log = Log.getInstance(CallbackClient.class);
    
    public CallbackClient() {
        
    }
    
    public void rapporteraHandelseWS(Meddelande meddelande) {
        String url = meddelande.getCallbackURL();
        if (url == null) {
        	// Ska inte inträffa då detta kontrolleras tidigare
        	log.error("Ingen callback-url i meddelandet.");
        	return;
        }
        CallbackProxy callback = new CallbackProxy();
        callback.setEndpoint(url);
        try {
        	callback.nyHandelse(ConvertCallbackDTO.getMeddelande2(meddelande));
	    } catch (Exception e) {
			log.error("Kunde inte anropa url: " + url + " för att meddela ny händelse.");
		}
    }
    
}
