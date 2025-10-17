/**
 * @since 2007-maj-03
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl;

import javax.mail.Message;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

import se.csn.ark.common.util.logging.Log;

/**
 * Klass som fangar upp events fran mail-transporttjansten. 
 * Lagrar status pa meddelandet. 
 */
public class TransportListenerImpl implements TransportListener {

    private Log log = Log.getInstance(TransportListenerImpl.class);

    public void messageDelivered(TransportEvent e) {
        log.debug("Meddelande skickat, csnid: " + getMessageId(e.getMessage()).longValue());
    }

    public void messageNotDelivered(TransportEvent e) {
        log.debug("Meddelande EJ skickat: " + e.getMessage());

    }

    public void messagePartiallyDelivered(TransportEvent e) {
        log.debug("Meddelande DELVIS skickat: " + e.getMessage());

    }

    private Long getMessageId(Message msg) {
        String[] id = new String[]{"NI"};
        try {
            id = msg.getHeader("csnid");
            return new Long(id[0]);
        } catch (Exception e) {
            log.error("Kunde inte hitta csnid för meddelande!");
            return new Long(-1);
        }
    }
}
