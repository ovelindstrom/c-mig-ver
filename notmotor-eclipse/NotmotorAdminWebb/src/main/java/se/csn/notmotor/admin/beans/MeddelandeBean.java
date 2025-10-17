/**
 * Skapad 2007-jun-18
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.admin.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.ListDataModel;

import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.admin.actions.ActionHelper;
import se.csn.notmotor.admin.beans.SokBean.Meddelanderad;
import se.csn.notmotor.ipl.db.DAOHandelse;
import se.csn.notmotor.ipl.db.DAOMeddelande;
import se.csn.notmotor.ipl.db.QueryProcessor;
import se.csn.notmotor.ipl.db.RowToObjectMapper;
import se.csn.notmotor.ipl.model.Avsandare;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;


public class MeddelandeBean {

    private long id;
    private Meddelande meddelande;
    private QueryProcessor qp;
    private DAOMeddelande dao;
    private DAOHandelse daoHandelse;
    private ListDataModel handelser;

    private Log log = Log.getInstance(MeddelandeBean.class);

    public static class Handelserad {
        private int typ, kod;
        private String text;
        private long id;
        private Date tidpunkt;
        private boolean delete;

        public Handelserad(long id, int typ, int kod, String text, Date tidpunkt) {
            this.id = id;
            this.typ = typ;
            this.kod = kod;
            this.text = text;
            this.tidpunkt = tidpunkt;
            delete = false;
        }

        public boolean isDelete() {
            return delete;
        }

        public void setDelete(boolean delete) {
            this.delete = delete;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getKod() {
            return kod;
        }

        public String getKodtext() {
            return MeddelandeHandelse.getKodtext(kod);
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

        public Date getTidpunkt() {
            return tidpunkt;
        }

        public void setTidpunkt(Date tidpunkt) {
            this.tidpunkt = tidpunkt;
        }

        public int getTyp() {
            return typ;
        }

        public String getTyptext() {
            return MeddelandeHandelse.getTyptext(typ);
        }

        public void setTyp(int typ) {
            this.typ = typ;
        }
    }

    public static class HandelseradMapper implements RowToObjectMapper {

        @Override
        public Object newRow(ResultSet rs) throws SQLException {
            return new Handelserad(rs.getLong("ID"), rs.getInt("TYP"), rs.getInt("KOD"), rs.getString("TEXT"), rs.getTimestamp("TIDPUNKT"));
        }
    }

    public MeddelandeBean() {
        // Skapa meddelandeHandler:
        qp = ActionHelper.getResourceFactory().getQueryProcessor();
        dao = ActionHelper.getResourceFactory().getDAOMeddelande();
        daoHandelse = ActionHelper.getResourceFactory().getDAOHandelse();
        FacesContext context = FacesContext.getCurrentInstance();
        Map params = context.getExternalContext().getRequestParameterMap();
        String idString = (String) params.get("id");
        log.debug("ID: " + idString);
        if (idString != null) {
            try {
                id = Integer.parseInt((String) params.get("id"));
            } catch (NumberFormatException nfe) {
                log.error("Tog emot id=" + idString + ", bugg eller hackingförsök?");
                id = qp.getLong("SELECT MAX(ID) FROM MEDDELANDE", -1);
            }
        } else {
            id = getSistaId();
        }
        lasMeddelandeFranDB();
    }

    public boolean getFinnsForegaende() {
        long i = getForegaendeMeddelande(id);
        log.debug("FinnsForegaende, id: " + i);
        return i > 0;
    }

    public boolean getFinnsNasta() {
        long i = getNastaMeddelande(id);
        log.debug("FinnsNasta, id: " + i);
        return i > 0;
    }

    public boolean getFel() {
        int status = qp.getInt("SELECT STATUS FROM MEDDELANDE WHERE ID=" + id, -1);
        return status > MeddelandeHandelse.SKICKAT_SERVER;
    }

    public void visaForegaende(ActionEvent e) {
        log.debug("ID före: " + id);
        id = getForegaendeMeddelande(id);
        log.debug("ID efter: " + id);
        uppdatera();
    }

    public void visaNasta(ActionEvent e) {
        log.debug("ID före: " + id);
        id = getNastaMeddelande(id);
        log.debug("ID efter: " + id);
        uppdatera();
    }

    public void testAction(ActionEvent e) {
        log.debug("TA: ID före: " + id);
        id = getNastaMeddelande(id);
        log.debug("TA: ID efter: " + id);
        uppdatera();
    }

    public void uppdatera(ActionEvent e) {
        uppdatera();
    }

    public void skickaOm(ActionEvent e) {
        log.debug("skickaom, id:" + id);
        try {
            MeddelandeHandelse handelse = new MeddelandeHandelse(MeddelandeHandelse.MOTTAGET, MeddelandeHandelse.OK, "Omsändning");
            daoHandelse.createHandelse(handelse, id);

            MeddelandeHandelse[] h = meddelande.getHandelser();
            int forstaLikaMedd = 0;
            Integer typ = -1;
            Integer felkod = -1;
            String feltext = "";
            Date tidpunkt = null;
            int antalLikaHandelser = 1;
            for (int i = h.length - 1;i >= 0;i--) {
                if ((h[i].getHandelsetyp().compareTo(typ) == 0)
                    && (h[i].getFelkod().compareTo(felkod) == 0)
                    && (h[i].getFeltext() != null && h[i].getFeltext().equals(feltext))) {
                    tidpunkt = h[i].getTidpunkt();
                    antalLikaHandelser++;
                    qp.executeThrowException("DELETE FROM HANDELSE WHERE ID=" + h[i].getId());
                    log.debug("delete handelse med id=" + h[i].getId());
                } else {
                    // Om tidpunkt är satt har vi tagit bort ett meddelande tidigare.
                    // Vi har hittat minst en likadan händelse och grupperar ihop dessa
                    if (tidpunkt != null) {
                        log.debug("TIDPUNKT=" + tidpunkt);
                        qp.executeThrowException("UPDATE HANDELSE SET TEXT='"
                            + h[forstaLikaMedd].getFeltext()
                            + ", Antal likadana händelser: " + antalLikaHandelser
                            + ", Första tidpunkt: " + tidpunkt
                            + "' WHERE ID=" + h[forstaLikaMedd].getId());
                        log.debug("UPDATE HANDELSE SET TEXT="
                            + h[forstaLikaMedd].getFeltext()
                            + "\nAntal likadana händelser: " + antalLikaHandelser
                            + "\nFörsta tidpunkt: " + tidpunkt
                            + " WHERE ID=" + h[forstaLikaMedd].getId());
                        tidpunkt = null;
                        antalLikaHandelser = 1;
                    } else {
                        forstaLikaMedd = i;
                    }
                }
                typ = h[i].getHandelsetyp();
                felkod = h[i].getFelkod();
                feltext = h[i].getFeltext();
            }
            qp.executeThrowException("UPDATE MEDDELANDE SET STATUS=" + MeddelandeHandelse.MOTTAGET
                + " WHERE ID=" + id);

            uppdatera();
        } catch (Exception t) {
            log.error("Kunde inte skicka om meddelande, fel: ", t);
        }
    }

    public void taBortHandelse(ActionEvent e) {
        List rader = (List) handelser.getWrappedData();
        for (int i = rader.size() - 1;i >= 0;i--) {
            Handelserad rad = (Handelserad) rader.get(i);
            if (rad.isDelete()) {
                rader.remove(i);
                qp.executeThrowException("DELETE FROM HANDELSE WHERE ID=" + rad.getId());
                log.debug("DELETE FROM HANDELSE WHERE ID=" + rad.getId());
            }
        }
        uppdatera();
    }

    public void taBortMeddelande(ActionEvent e) {
        log.debug("Ta bort meddelande, id:" + id);
        qp.executeThrowException("UPDATE MEDDELANDE SET STATUS="
            + MeddelandeHandelse.BORTTAGET + " WHERE ID=" + id);
        MeddelandeHandelse handelse = new MeddelandeHandelse(MeddelandeHandelse.BORTTAGET, MeddelandeHandelse.OK, "Meddelandet borttaget");
        daoHandelse.createHandelse(handelse, id);
        uppdatera();
    }


    long getNastaMeddelande(long nuvarandeId) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        SokBean sokBean = (SokBean) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "sokBean");
        List<Meddelanderad> meddelanden = sokBean.getMeddelandenAsList();

        if (meddelanden != null) {
            int i = 0;
            while (i < meddelanden.size() - 1) {
                if (meddelanden.get(i).getId() == nuvarandeId) {
                    return meddelanden.get(i + 1).getId();
                }
                i++;
            }
        }
        return 0;
    }


    long getForegaendeMeddelande(long nuvarandeId) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        SokBean sokBean = (SokBean) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "sokBean");
        List<Meddelanderad> meddelanden = sokBean.getMeddelandenAsList();

        if (meddelanden != null) {
            int i = meddelanden.size() - 1;
            while (i > 0) {
                if (meddelanden.get(i).getId() == nuvarandeId) {
                    return meddelanden.get(i - 1).getId();
                }
                i--;
            }
        }

        return 0;
    }

    public long getForstaId() {
        return qp.getLong("SELECT MIN(ID) FROM MEDDELANDE", 0);
    }

    public final long getSistaId() {
        return qp.getLong("SELECT MAX(ID) FROM MEDDELANDE", 0);
    }

    final void lasMeddelandeFranDB() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map params = context.getExternalContext().getRequestParameterMap();
        String idString = (String) params.get("id");
        if (idString != null) {
            try {
                id = Integer.parseInt(idString);
            } catch (NumberFormatException nfe) {
                log.error("Tog emot id=" + idString + ", bugg eller hackingförsök?");
                id = qp.getLong("SELECT MAX(ID) FROM MEDDELANDE", -1);
            }
        }
        if ((meddelande == null) || (meddelande.getId() == null) || (meddelande.getId().longValue() != id)) {
            meddelande = dao.getMeddelande(id);
            if (meddelande == null) {
                meddelande = new Meddelande();
            }
            if (handelser == null) {
                handelser = new ListDataModel(getHandelserFranMeddelande());
            } else {
                handelser.setWrappedData(getHandelserFranMeddelande());
            }
        }
    }

    public Meddelande getMeddelande() {
        lasMeddelandeFranDB();
        return meddelande;
    }

    /**
     * Specialare for att formatera ut mottagarna som en strang.
     * @return String med alla mottagare för meddelandet.
     */
    public String getMottagarstrang() {
        lasMeddelandeFranDB();
        String s = "";
        if (meddelande == null) {
            return "";
        }
        Mottagare[] mott = meddelande.getMottagare();
        if (mott == null || mott.length == 0) {
            return "Saknas";
        }
        for (int i = 0;i < mott.length;i++) {
            if (i > 0) {
                s += ", ";
            }
            s += mott[i].getAdress();
            if (mott[i].getNamn() != null) {
                s += " [" + mott[i].getNamn() + "]";
            }
            if ((mott.length > 1) && (mott[i].getStatus() != null)) {
                s += " (" + MeddelandeHandelse.getTyptext(mott[i].getStatus().intValue()) + ")";
            }
        }
        return s;
    }

    public String getAvsandarstrang() {
        lasMeddelandeFranDB();
        if (meddelande == null) {
            return "";
        }
        Avsandare avs = meddelande.getAvsandare();
        if (avs == null) {
            return "";
        }
        String s = avs.getEpostadress();
        if (avs.getNamn() != null) {
            s += " [" + avs.getNamn() + "]";
        }
        return s;
    }

    public String getCsnnummer() {
        lasMeddelandeFranDB();
        if (meddelande == null) {
            return "";
        }
        if (meddelande.getCsnnummer() == null || meddelande.getCsnnummer().intValue() == 0) {
            return "";
        }
        return meddelande.getCsnnummer().toString();
    }

    private List getHandelserFranMeddelande() {
        List<Handelserad> list = new ArrayList<Handelserad>();
        long tick = System.currentTimeMillis();
        MeddelandeHandelse[] h = meddelande.getHandelser();
        log.debug("Det tog " + (System.currentTimeMillis() - tick) + " ms att hämta händelser");
        if (h == null) {
            return null;
        }
        int maxAntalHandelser = 50;
        for (int i = h.length - 1, min = Math.max(0, h.length - maxAntalHandelser);i >= min;i--) {
            list.add(new Handelserad(h[i].getId().longValue(), h[i].getHandelsetyp().intValue(),
                h[i].getFelkod().intValue(), h[i].getFeltext(), h[i].getTidpunkt()));
        }
        return list;
    }

    public boolean getFinnsHandelser() {
        lasMeddelandeFranDB();
        if (meddelande == null || meddelande.getHandelser() == null) {
            return false;
        }
        return meddelande.getHandelser().length > 0;
    }

    public boolean getKunnaTaBortMeddelande() {
        int status = qp.getInt("SELECT STATUS FROM MEDDELANDE WHERE ID=" + id, 0);
        if ((status == MeddelandeHandelse.MOTTAGET || status > MeddelandeHandelse.SKICKAT_SERVER)
            && status != MeddelandeHandelse.BORTTAGET) {
            return true;
        }
        return false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        log.debug("SetId: " + id);
        this.id = id;
    }

    public ListDataModel getHandelser() {
        return handelser;
    }

    public void setHandelser(ListDataModel handelser) {
        this.handelser = handelser;
    }

    private void uppdatera() {
        meddelande = null;
        lasMeddelandeFranDB();
    }
}