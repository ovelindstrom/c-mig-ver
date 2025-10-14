/**
 * Skapad 2007-jun-11
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.notmotor.admin.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.ListDataModel;

import se.csn.common.util.DateUtils;
import se.csn.notmotor.admin.actions.ActionHelper;
import se.csn.notmotor.ipl.db.DAOSchema;
import se.csn.notmotor.ipl.model.Tidsintervall;


public class SchemaBean {

    private ListDataModel model;
    private String startdatum, slutdatum;
    
    public static class Schemarad {
        private boolean delete;
        private Date from, tom;
        public Schemarad(Date start, Date stop) {
            this.from = start;
            this.tom = stop;
            delete = false;
        }
        
        public boolean getDelete() {
            return delete;
        }
        public void setDelete(boolean delete) {
            this.delete = delete;
        }

        public Date getFrom() {
            return from;
        }
        public void setFrom(Date from) {
            this.from = from;
        }
        public Date getTom() {
            return tom;
        }
        public void setTom(Date tom) {
            this.tom = tom;
        }
    }
    
    public SchemaBean() { 
        DAOSchema dao = ActionHelper.getResourceFactory().getDAOSchema();
        List intervall = dao.getIntervall();
        List<Schemarad> schemarader = new ArrayList<Schemarad>(intervall.size());
        for(Iterator it = intervall.iterator();it.hasNext();) {
            Tidsintervall ti = (Tidsintervall)it.next();
            schemarader.add(new Schemarad(ti.getStarttid(), ti.getSluttid()));
            
        }
        model = new ListDataModel(schemarader);
        
        // Sätt defaultvärden för datumraderna:
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startdatum = sdf.format(new Date());
        slutdatum = sdf.format(new Date());
        
    }
    
    Tidsintervall skapaIntervall() {
        Date from = DateUtils.strToDate(startdatum, true);
        Date tom = DateUtils.strToDate(slutdatum, false);
        return new Tidsintervall(from, tom);
    }
    
    public void taBortRader(ActionEvent e) {
        DAOSchema dao = ActionHelper.getResourceFactory().getDAOSchema();
        List rader = (List)model.getWrappedData();
        for(int i = rader.size()-1; i >= 0; i--){
            Schemarad rad = (Schemarad)rader.get(i);
            if(rad.getDelete()) {
                rader.remove(i);
                dao.delete(new Tidsintervall(rad.getFrom(), rad.getTom()));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
	public void laggTillRad(ActionEvent e) {
        Tidsintervall intervall = skapaIntervall();
        DAOSchema dao = ActionHelper.getResourceFactory().getDAOSchema();
        dao.skapaIntervall(intervall);
        List<Schemarad> rader = (List<Schemarad>) model.getWrappedData();
        rader.add(new Schemarad(intervall.getStarttid(), intervall.getSluttid()));
        model.setWrappedData(rader);
    }
    
  
    public ListDataModel getSchemarader() {
        return model;
    }
    public void setSchemarader(ListDataModel schemarader) {
        this.model = schemarader;
    }
    
    
    public String getSlutdatum() {
        return slutdatum;
    }
    public void setSlutdatum(String slutdatum) {
        this.slutdatum = slutdatum;
    }
    public String getStartdatum() {
        return startdatum;
    }
    public void setStartdatum(String startdatum) {
        this.startdatum = startdatum;
    }
}
