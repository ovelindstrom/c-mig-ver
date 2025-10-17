/*
 * @since 2010-09-01
 */
package se.csn.notmotor.ipl.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Representerar en inkanal dar det finns begransningar i
 * hur meddelanden ska bearbetas.
 * 
 * @author Petrus Bergman, csn7820
 */
public class Kanal {

    /** Kanalens namn. */
    private String namn;

    /** Kanalens oppningstid (endast klockslag). */
    private Calendar oppningstid;

    /** Kanalens stangningstid (endast klockslag). */
    private Calendar stangningstid;

    /** Det maximala antalet meddelanden som far bearbetas per timme. */
    private int maxAntalPerTimme;

    /** Antal meddelanden som ska bearbetas i en foljd. */
    private int batchStorlek;

    /** Antal meddelanden som aterstar i aktuell batch. */
    private int batchKvar;

    /** Tid i sekunder mellan varje batch. */
    private int sovtid;

    /** Tidsstampel da kanalen senast somnat, eller null om vaken. */
    private Date soverTimestamp;

    /** Antal meddelanden som senast markerats. */
    private int markerade;


    /**
     * Skapar en ny instans av en inkanal med angivet namn.
     * 
     * @param namn kanalens namn
     */
    public Kanal(String namn) {
        if (namn == null) {
            throw new IllegalArgumentException("Kanalens namn får inte vara null");
        }
        this.namn = namn;
        // Initiera öppnings- och stängningstid
        setOppningstid(0, 0, 0);
        setStangningstid(23, 59, 59);
    }

    public String getNamn() {
        return namn;
    }

    public String getStatus() {
        String status = "";
        if (isOppen()) {
            if (isSovande()) {
                status += "Sover";
            } else {
                status += "åppen";
            }
        } else {
            status += "Stängd";
        }
        return status;
    }

    /**
     * Returnerar huruvida kanalen ar oppen eller stangd.
     * Kanalen anses vara oppen om nuvarande tidpunkt ar inom aktuell oppet-
     * och stangningstid.
     * 
     * @return <code>true</code> om kanalen är öppen, annars <code>false</code>
     */
    public boolean isOppen() {
        Calendar nu = new GregorianCalendar();
        nu.set(0, 0, 0);

        if (oppningstid.before(stangningstid)) {
            return nu.after(oppningstid) && nu.before(stangningstid);
        } else {
            return (nu.before(stangningstid)) || (nu.after(oppningstid));
        }
    }

    public String getOppningstidString() {
        return new SimpleDateFormat("HH:mm:ss").format(oppningstid.getTime());
    }

    public String getOppningstidKey() {
        return "KANAL_" + namn + "_OPPNINGSTID";
    }

    /**
     * Satt klockslag da kanalen ska oppnas.
     * 
     * @param klockslag anges på formen HH:mm:ss
     */
    public void setOppningstid(String klockslag) {
        if (klockslag == null || klockslag.trim().length() == 0) {
            return;
        }
        Pattern p = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})");
        Matcher m = p.matcher(klockslag);
        if (m.matches()) {
            // CHECKSTYLE/OFF:MagicNumber
            int hh = Integer.parseInt(m.group(1));
            int mm = Integer.parseInt(m.group(2));
            int ss = Integer.parseInt(m.group(3));
            // CHECKSTYLE/ON:MagicNumber
            setOppningstid(hh, mm, ss);
        } else {
            throw new IllegalArgumentException("Ogiltigt format på klockslag: " + klockslag);
        }
    }

    /**
     * Satt klockslag da kanalen ska oppnas.
     * 
     * @param hh timme (0-23)
     * @param mm minut (0-59)
     * @param ss sekund (0-59)
     */
    public final void setOppningstid(int hh, int mm, int ss) {
        this.oppningstid = new GregorianCalendar(0, 0, 0, hh, mm, ss);
    }

    public String getStangningstidString() {
        return new SimpleDateFormat("HH:mm:ss").format(stangningstid.getTime());
    }

    public String getStangningstidKey() {
        return "KANAL_" + namn + "_STANGNINGSTID";
    }

    /**
     * Satt klockslag da kanalen ska stangas.
     * 
     * @param klockslag anges på formen HH:mm:ss
     */
    public void setStangningstid(String klockslag) {
        if (klockslag == null || klockslag.trim().length() == 0) {
            return;
        }
        Pattern p = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})");
        Matcher m = p.matcher(klockslag);
        if (m.matches()) {
            // CHECKSTYLE/OFF:MagicNumber
            int hh = Integer.parseInt(m.group(1));
            int mm = Integer.parseInt(m.group(2));
            int ss = Integer.parseInt(m.group(3));
            // CHECKSTYLE/ON:MagicNumber
            setStangningstid(hh, mm, ss);
        } else {
            throw new IllegalArgumentException("Ogiltigt format på klockslag: " + klockslag);
        }
    }

    /**
     * Satt klockslag da kanalen ska stangas.
     * 
     * @param hh timme (0-23)
     * @param mm minut (0-59)
     * @param ss sekund (0-59)
     */
    public final void setStangningstid(int hh, int mm, int ss) {
        this.stangningstid = new GregorianCalendar(0, 0, 0, hh, mm, ss);
    }

    public String getMaxAntalPerTimmeKey() {
        return "KANAL_" + namn + "_PERTIMME";
    }

    public int getMaxAntalPerTimme() {
        return maxAntalPerTimme;
    }

    /**
     * Satter maximala antalet meddelanden som far bearbetas per timme.
     * 
     * @param antal max antal meddelanden. -1 innebär ingen begränsning.
     */
    public void setMaxAntalPerTimme(int antal) {
        this.maxAntalPerTimme = antal;
    }

    public String getBatchStorlekKey() {
        return "KANAL_" + namn + "_BATCHSTORLEK";
    }

    public int getBatchStorlek() {
        return batchStorlek;
    }

    public void setBatchStorlek(int antal) {
        this.batchStorlek = antal;
    }

    public String getBatchKvarKey() {
        // Denna parameter är ej statisk utan uppdateras regelbundet,
        // därför är nyckeln skriven med gemener.
        return "kanal_" + namn.toLowerCase() + "_batchkvar";
    }

    public int getBatchKvar() {
        return batchKvar;
    }

    public void setBatchKvar(int antal) {
        this.batchKvar = antal;
    }

    public String getSovtidKey() {
        return "KANAL_" + namn + "_SOVTID";
    }

    public int getSovtid() {
        return sovtid;
    }

    public void setSovtid(int sekunder) {
        this.sovtid = sekunder;
    }

    public int getSovtidKvar() {
        return Math.round((float) getSovtidKvarMillisekunder() / 1000);
    }

    public long getSovtidKvarMillisekunder() {
        if (soverTimestamp == null) {
            return 0;
        }
        return Math.max(0, soverTimestamp.getTime() + (sovtid * 1000L) - System.currentTimeMillis());
    }

    public boolean isSovande() {
        if (soverTimestamp != null) {
            Calendar nu = new GregorianCalendar();
            Calendar vakna = new GregorianCalendar();
            vakna.setTime(soverTimestamp);
            vakna.add(Calendar.SECOND, sovtid);
            return nu.before(vakna);
        }
        return false;
    }

    public String getSoverTimestampKey() {
        // Denna parameter är ej statisk utan uppdateras regelbundet,
        // därför är nyckeln skriven med gemener.
        return "kanal_" + namn.toLowerCase() + "_sover";
    }

    public Date getSoverTimestamp() {
        return soverTimestamp;
    }

    public void setSoverTimestamp(Date timestamp) {
        this.soverTimestamp = timestamp;
    }

    public void setAntalMarkerade(int antal) {
        this.markerade = antal;
    }

    public int getAntalMarkerade() {
        return markerade;
    }

    public String toString() {
        return "Kanal(namn = " + namn + ", öppettider = "
            + new SimpleDateFormat("HH:mm:ss").format(oppningstid.getTime())
            + " - "
            + new SimpleDateFormat("HH:mm:ss").format(stangningstid.getTime())
            + ", status = " + (isOppen() ? "öppen" : "stängd")
            + ", max per timme = " + maxAntalPerTimme
            + ", batch storlek = " + batchStorlek
            + ", batch kvar = " + batchKvar
            + ", sovtid = " + sovtid
            + ", senast markerade = " + markerade
            + ")";
    }

}
