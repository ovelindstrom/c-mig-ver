/*
 * Created on 2005-feb-10
 *
 */
package se.csn.notmotor.ipl.sms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author csn7511
 *
 */
public class DTOSMSUt implements Serializable {
    private static final long serialVersionUID = 1L;
    private int returStatus;
    private String responseMessage;
    private boolean temporaryError;

    static final String[][] RETURKODTABELL = new String[][]{
        {"902", "Kod 2 från Telia: Wrong username or password is used or the content provider is barred"},
        {"997", "Kunde inte tolka svaret från sms-tjänsten"},
        {"998", "Webservice-fel"},
	};

    private static Map<String, String> returkoder;
    private static Object lock = new Object();

    public DTOSMSUt() {
        synchronized (lock) {
            if (returkoder != null) {
                return;
            }
            returkoder = new HashMap<String, String>();
            for (int i = 0;i < RETURKODTABELL.length;i++) {
                returkoder.put(RETURKODTABELL[i][0], RETURKODTABELL[i][1]);
            }
            returStatus = 0;
            responseMessage = "";
            temporaryError = false;
        }
    }

    public boolean sandningLyckad() {
        switch(returStatus) {
            case 2:
                return true;
            default:
                return false;
        }
    }

    public int getReturStatus() {
        return returStatus;
    }

    public String getReturStatusText() {
        String msg = (String) returkoder.get("" + returStatus);
        if (msg == null) {
            msg = responseMessage;
        }
        if (msg == null) {
            msg = "Okänd status";
        }
        return msg;
    }

    public void setReturStatus(int i) {
        returStatus = i;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public boolean isTemporaryError() {
        return temporaryError;
    }

    public void setTemporaryError(boolean temporaryError) {
        this.temporaryError = temporaryError;
    }

    public String toString() {
        return "Status: " + returStatus + " (" + getReturStatusText() + ")";
    }

}
