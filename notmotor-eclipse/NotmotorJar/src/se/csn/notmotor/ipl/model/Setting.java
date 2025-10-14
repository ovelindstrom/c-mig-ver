/**
 * Skapad 2007-apr-13
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.model;


public class Setting {

    private String namn, beskrivning, varde;
    
    public Setting(String namn, String varde, String beskrivning) {
        this.namn = namn; 
        this.varde = varde;
        this.beskrivning = beskrivning;
    }
    
    public String getBeskrivning() {
        return beskrivning;
    }
    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }
    public String getNamn() {
        return namn;
    }
    public void setNamn(String namn) {
        this.namn = namn;
    }
    public String getVarde() {
        return varde;
    }
    public void setVarde(String varde) {
        this.varde = varde;
    }
}
