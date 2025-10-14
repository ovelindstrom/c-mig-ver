/**
 * Skapad 2007-jun-07
 * @author Jonas åhrnell (csn7821)
 * 
 */
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
     * Lista med de parametrar som finns tillgängliga för nedprioriterade kanaler.
     * Varje element i listan består i sin tur av en lista som innehåller två element;
     * namn och beskrivning. Om parameterns namn innehåller "{{KANAL}}" så kommer detta
     * att bytas ut mot kanalens namn.
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
        for (int i = 0; i < params.length; i++) {
            Setting s = new Setting(params[i][0], paramKalla.getStringParam(params[i][0]), params[i][1]);
            plist.add(s);
        }
        parametrar = new ListDataModel(plist);
        
        List<Setting> pklist = new ArrayList<Setting>();
        for (String kanal : paramKalla.getStringParam("KANALER_MED_BEGRANSNINGAR", "").split("[, ]+")) {
        	if (kanal.trim().length() > 0) {
        		for (int i = 0; i < paramsKanaler.length; i++) {
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
        for (Iterator it = settings.iterator(); it.hasNext();) {
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
        for (Iterator it = settings.iterator(); it.hasNext();) {
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
