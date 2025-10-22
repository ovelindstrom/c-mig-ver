package se.csn.notmotor.admin.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.ListDataModel;

import se.csn.notmotor.admin.actions.ActionHelper;
import se.csn.notmotor.ipl.db.ParameterKalla;
import se.csn.notmotor.ipl.model.Kanal;
import se.csn.notmotor.ipl.model.Setting;


public class ParamBean {

    private static final String[][] params = {
        {"WATCHDOGTID", "Sekunder mellan uppdatering av processernas watchdogflagga"},
        {"TICKTID", "Väntetid i sekunder efter varje arbetsmoment"},
        {"SOVTID", "Den tid i sekunder som processerna ska vänta på arbete"},
        {"BATCHSTORLEK", "Max antal meddelanden att sända åt gången"},
        {"CALLBACKS", "Styr om notmotorn ska göra callbacks för att meddela statusändringar på meddelanden"},
        {"MINTIDTILLSENASTEFEL", "Tid i sekunder mellan omsändningsförsök av misslyckade meddelanden"},
        {"MAXSANDFORSOK", "Max antal omsändningsförsök per meddelande"},
        {"KANALER_MED_BEGRANSNINGAR", "Nedprioriterade inkanaler med begränsningar. Kommaseparerad lista."},
    };

    /**
     * Definitions for channel-specific configuration parameters.
     *
     * <p>Each entry in this two-dimensional array is a String[2] where:
     * <ol>
     *   <li>Index 0: a parameter key pattern containing the placeholder "{{KANAL}}".
     *       Replace "{{KANAL}}" with a concrete channel identifier to obtain the actual config key
     *       (for example, "KANAL_{{KANAL}}_PERTIMME" -> "KANAL_1_PERTIMME").</li>
     *   <li>Index 1: a human-readable description (Swedish) intended for UI labels or documentation.</li>
     * </ol>
     *
     * <p>Expected keys and semantics (examples):
     * <ul>
     *   <li>..._PERTIMME  Max antal meddelanden att sanda per timme.</li>
     *   <li>..._BATCHSTORLEK  Max antal meddelanden att sanda i foljd.</li>
     *   <li>..._SOVTID  Sovtid i sekunder mellan varje batch.</li>
     *   <li>..._OPPNINGSTID  Oppningstid for aktuell kanal, format "HH:mm:ss".</li>
     *   <li>..._STANGNINGSTID  Stangningstid for aktuell kanal, format "HH:mm:ss".</li>
     * </ul>
     *
     * <p>Usage example:
     * <pre>
     * String keyPattern = paramsKanaler[i][0];
     * String key = keyPattern.replace("{{KANAL}}", channelId);
     * String description = paramsKanaler[i][1];
     * </pre>
     *
     * <p>Notes:
     * <ul>
     *   <li>The array is intended as a constant lookup table; do not modify at runtime.</li>
     *   <li>Consumers should validate that each row has exactly two elements before use.</li>
     *   <li>Descriptions are currently in Swedish; consider externalizing for localization if needed.</li>
     * </ul>
     */
    private static final String[][] paramsKanaler = {
        {"KANAL_{{KANAL}}_PERTIMME", "Max antal meddelanden att sända per timme."},
        {"KANAL_{{KANAL}}_BATCHSTORLEK", "Max antal meddelanden att sända i följd."},
        {"KANAL_{{KANAL}}_SOVTID", "Sovtid i sekunder mellan varje batch."},
        {"KANAL_{{KANAL}}_OPPNINGSTID", "åppningstid för aktuell kanal. (HH:mm:ss)"},
        {"KANAL_{{KANAL}}_STANGNINGSTID", "Stängningstid för aktuell kanal. (HH:mm:ss)"},
    };

    private ListDataModel parametrar;
    private ListDataModel parametrarKanaler;

    public ParamBean() {
        init();
    }

    private void init() {
        ParameterKalla paramKalla = ActionHelper.getResourceFactory().getParameterKalla();
        List<Setting> plist = new ArrayList<Setting>();
        for (int i = 0;i < params.length;i++) {
            Setting s = new Setting(params[i][0], paramKalla.getStringParam(params[i][0]), params[i][1]);
            plist.add(s);
        }
        parametrar = new ListDataModel(plist);

        List<Setting> pklist = new ArrayList<Setting>();
        for (String kanal : paramKalla.getStringParam("KANALER_MED_BEGRANSNINGAR", "").split("[, ]+")) {
            if (kanal.trim().length() > 0) {
                for (int i = 0;i < paramsKanaler.length;i++) {
                    String key = paramsKanaler[i][0].replaceAll("\\{\\{KANAL\\}\\}", kanal);
                    Setting s = new Setting(key, paramKalla.getStringParam(key), paramsKanaler[i][1]);
                    pklist.add(s);
                }
            }
        }
        parametrarKanaler = new ListDataModel(pklist);
    }

    public void uppdatera(ActionEvent e) {
        ParameterKalla paramKalla = ActionHelper.getResourceFactory().getParameterKalla();
        List settings = (List) parametrar.getWrappedData();
        for (Iterator it = settings.iterator();it.hasNext();) {
            Setting s = (Setting) it.next();
            paramKalla.setStringParam(s.getNamn(), s.getVarde());
        }
        init();
    }

    public void uppdateraKanaler(ActionEvent e) {
        ParameterKalla paramKalla = ActionHelper.getResourceFactory().getParameterKalla();
        // Nollställ temporära parametrar
        for (String kanal : paramKalla.getStringParam("KANALER_MED_BEGRANSNINGAR", "").split("[, ]+")) {
            if (kanal.trim().length() > 0) {
                paramKalla.setStringParam(new Kanal(kanal).getBatchKvarKey(), null);
                paramKalla.setStringParam(new Kanal(kanal).getSoverTimestampKey(), null);
            }
        }
        // Uppdatera parametrar
        List settings = (List) parametrarKanaler.getWrappedData();
        for (Iterator it = settings.iterator();it.hasNext();) {
            Setting s = (Setting) it.next();
            paramKalla.setStringParam(s.getNamn(), s.getVarde());
        }
        init();
    }

    public ListDataModel getParametrar() {
        return parametrar;
    }

    public void setParametrar(ListDataModel parametrar) {
        this.parametrar = parametrar;
    }

    public ListDataModel getParametrarKanaler() {
        return parametrarKanaler;
    }

    public void setParametrarKanaler(ListDataModel parametrar) {
        this.parametrarKanaler = parametrar;
    }
}
