/**
 * Skapad 2007-apr-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.model;


public class Server {

    private int id, prestanda;
    private String servleturl;
    private boolean aktiv;

    public Server() {
    }

    public Server(int id, boolean aktiv, int prestanda, String url) {
        this.id = id;
        this.aktiv = aktiv;
        this.prestanda = prestanda;
        this.servleturl = url;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrestanda() {
        return prestanda;
    }

    public void setPrestanda(int prestanda) {
        this.prestanda = prestanda;
    }

    public String getServleturl() {
        return servleturl;
    }

    public void setServleturl(String servleturl) {
        this.servleturl = servleturl;
    }
}
