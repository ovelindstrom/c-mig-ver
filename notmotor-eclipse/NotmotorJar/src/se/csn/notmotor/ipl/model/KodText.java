/*
 * Skapad 2007-okt-31
 */
package se.csn.notmotor.ipl.model;

/**
 * Enkel klass som håller ett par av kod och text
 * @author Jonas åhrnell - csn7821
 */
public class KodText {

    private int kod; 
    private String text;
    
    public KodText(int kod, String text) {
        this.kod = kod;
        this.text = text;
    }
    
    public String toString() {
        return "" + kod + ": " + text;
    }
    
    
    public int getKod() {
        return kod;
    }
    public void setKod(int kod) {
        this.kod = kod;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
