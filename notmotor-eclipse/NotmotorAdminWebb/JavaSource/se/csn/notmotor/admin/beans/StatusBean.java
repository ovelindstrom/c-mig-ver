/**
 * Skapad 2007-apr-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.admin.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.ListDataModel;

import se.csn.ark.common.util.logging.Log;
import se.csn.common.config.CommunicationTester;
import se.csn.common.servlet.ServletUtils;
import se.csn.notmotor.admin.actions.ActionHelper;
import se.csn.notmotor.ipl.SkickaMeddelandeStateMachine;
import se.csn.notmotor.ipl.db.DAOServer;
import se.csn.notmotor.ipl.db.DAOStatus;
import se.csn.notmotor.ipl.db.ParameterKalla;
import se.csn.notmotor.ipl.model.Kanal;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Server;
import se.csn.notmotor.ipl.model.Status;


public class StatusBean {

    public class Statusrad {
        private boolean rensa, stoppa, pausa, aktiv;
        private int nr, status;
        private String server, typ;
        private Date starttid, stopptid, watchdog;
        
        public Statusrad() {
            
        }
        
        public Statusrad(int nr, int status, String server, Date startad, Date stoppad, Date watchdog, String typ, boolean isAktiv) {
            this.nr = nr;
            this.status = status;
            this.server = server;
            this.starttid = startad;
            this.stopptid = stoppad;
            this.watchdog = watchdog;
            this.typ = typ;
            this.aktiv = isAktiv;
        }
        
        
        public boolean getRensa() {
            return rensa;
        }
        public void setRensa(boolean rensa) {
            this.rensa = rensa;
        }
        public String getServer() {
            return server;
        }
        public void setServer(String server) {
            this.server = server;
        }
        public String getTyp() {
            return typ;
        }
        public void setTyp(String typ) {
            this.typ = typ;
        }
        public Date getStarttid() {
            return starttid;
        }
        public void setStarttid(Date starttid) {
            this.starttid = starttid;
        }
        public Date getStopptid() {
            return stopptid;
        }
        public void setStopptid(Date stopptid) {
            this.stopptid = stopptid;
        }
        public Date getWatchdog() {
            return watchdog;
        }
        public void setWatchdog(Date watchdog) {
            this.watchdog = watchdog;
        }
        public boolean getStoppa() {
            return stoppa;
        }
        public void setStoppa(boolean stoppa) {
            this.stoppa = stoppa;
        }
        public int getNr() {
            return nr;
        }
        public void setNr(int nr) {
            this.nr = nr;
        }
        public boolean isPausad() {
            return (status == SkickaMeddelandeStateMachine.PAUSING) || (status == SkickaMeddelandeStateMachine.PAUSED);
        }
        public boolean getPausa() {
            return pausa;
        }
        public void setPausa(boolean pausa) {
            this.pausa = pausa;
        }
        
        public boolean isStoppad() {
            return (status == SkickaMeddelandeStateMachine.STOPPING) || (status == SkickaMeddelandeStateMachine.STOPPED);
        }
        
        public int getStatus() {
            return status;
        }
        public void setStatus(int status) {
            this.status = status;
        }
        
        private String getMsg(String msg) {
            return messages.getString(msg);
        }
        public String getStatustext() {
        	
            switch(status) {
            	case SkickaMeddelandeStateMachine.INIT : return getMsg("status.init");
            	case SkickaMeddelandeStateMachine.RUNNING : return getMsg("status.running");
            	case SkickaMeddelandeStateMachine.WAITING : return getMsg("status.waiting");
            	case SkickaMeddelandeStateMachine.PAUSED : return getMsg("status.paused");
            	case SkickaMeddelandeStateMachine.PAUSING : return getMsg("status.pausing");
            	case SkickaMeddelandeStateMachine.STOPPED : return getMsg("status.stopped");
            	case SkickaMeddelandeStateMachine.STOPPING : return getMsg("status.stopping");
            	case SkickaMeddelandeStateMachine.SCHEDULED_PAUSE : return getMsg("status.scheduledpause");
            	
            	default : return "status.unknown";
            }
        }
        public boolean getAktiv() {
            return aktiv;
        }
        public void setAktiv(boolean aktiv) {
            this.aktiv = aktiv;
        }
    }
    
    public static class Serverrad {
        private String adress;
        private int prestanda, processer;
        private boolean delete;
        private int id;
        
        public boolean getDelete() {
            return delete;
        }
        public void setDelete(boolean delete) {
            this.delete = delete;
        }
        public Serverrad(){};
        public Serverrad(int id, String adress, int prestanda, int processer) {
            this.id = id;
            this.adress = adress;
            this.prestanda = prestanda; 
            this.processer = processer;
            delete = false;
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
        public int getProcesser() {
            return processer;
        }
        public void setProcesser(int processer) {
            this.processer = processer;
        }
        public String getAdress() {
            return adress;
        }
        public void setAdress(String adress) {
            this.adress = adress;
        }
    }
    
    public static final int MAX_INSTANSER_PER_SERVER = 5;
    private int maxalder;
    
    private ResourceBundle messages = ResourceBundle.getBundle("se.csn.notmotor.admin.resources.ApplicationResources");
    
    private Log log = Log.getInstance(StatusBean.class);
    private ListDataModel statusrader, serverrader;

    public StatusBean() { 
        statusrader = new ListDataModel(hamtaStatusraderFranDB());
        serverrader = new ListDataModel(hamtaServerraderFranDB());
    }
    
    final List<Statusrad> hamtaStatusraderFranDB() {
        DAOStatus dao = ActionHelper.getResourceFactory().getDAOStatus();
        List statuslist = dao.getStatus(null, null);
        List<Statusrad> rader = new ArrayList<Statusrad>();
        for (Iterator it = statuslist.iterator(); it.hasNext();) {
            Status s = (Status) it.next();
            rader.add(new Statusrad(s.getInstans(), s.getStatus(), "" 
                    + s.getServer(), s.getStartad(), s.getStoppad(), s.getWatchdog(), s.getTyp(), s.isAktiv()));
        }
        return rader;
    }
    
    final List<Serverrad> hamtaServerraderFranDB() {
        DAOServer dao = ActionHelper.getResourceFactory().getDAOServer();
        List servers = dao.getAktiva(true);
        List<Serverrad> rader = new ArrayList<Serverrad>();
        for (Iterator it = servers.iterator(); it.hasNext();) {
            Server s = (Server) it.next();
            rader.add(new Serverrad(s.getId(), s.getServleturl(), s.getPrestanda(), dao.getLevandeProcesser(s.getId())));
        }
        return rader;
    }
    
    /**
     * Kontrollerar att processernas watchdogflaggor är tillräckligt färska. 
     * Tillräckligt färsk anses vara inom 60 sekunder från den satta parametern
     */
    public String getWatchdogVarning() {
        Date jamforelsetid = getJamforelsetid();
        if (jamforelsetid == null) {
            return null;
        }
        DAOStatus dao = ActionHelper.getResourceFactory().getDAOStatus();
        List statuslist = dao.getStatus(null, null);
        String varning = "";
        for (Iterator it = statuslist.iterator(); it.hasNext();) {
            Status s = (Status) it.next();
            if (s.getStatus() == SkickaMeddelandeStateMachine.STOPPED) { continue; }
            
            if (s.getWatchdog() == null) {
                varning += "Process " + s.getInstans() + " har ingen tidsstämpel alls<br/>";
            } else if (s.getWatchdog().before(jamforelsetid)) {
                varning += "Process " + s.getInstans() + " har en tidsstämpel som är äldre än " + maxalder + " sekunder<br/>";
            } else {
                log.debug("Watchdog: " + s.getWatchdog().getTime());
            }
        }
        return varning;
    }
    
    void uppdatera() {
        serverrader.setWrappedData(hamtaServerraderFranDB());
        statusrader.setWrappedData(hamtaStatusraderFranDB());
    }
    
    public void uppdatera(ActionEvent e) {
        //uppdatera(); Kommentar: Konstruktorn körs så ett anrop till uppdatera är onödigt eftersom inget annat görs i denna metod.
    }
    
    /**
     * Kan bara ta bort en statusrad om status är stoppad eller watchdogtidpunkten har passerats så
     * att varningstexten är utlagd samtidigt som status är stoppar.
     * @param rad Statusrad
     * @return true om villkoren uppfylls
     */
    private boolean kontrolleraBorttag(Statusrad rad) {
        Date jamforelsetid = getJamforelsetid();
        if (jamforelsetid == null) {
            return false;
        }
        if ((rad.getStatus() == SkickaMeddelandeStateMachine.STOPPED) && (rad.getStopptid() != null)) {
            return true;
        }
        if ((rad.getWatchdog() != null) && (rad.getWatchdog().before(jamforelsetid))
                && ((rad.getStatus() == SkickaMeddelandeStateMachine.STOPPING)
                		|| (rad.getStatus() == SkickaMeddelandeStateMachine.STOPPED))) {
            return true;
        }
        return false;
    }
    
    public void taBortStoppadeStatusar(ActionEvent e) {
        log.debug("taBortStangdaStatusar");
        DAOStatus dao = ActionHelper.getResourceFactory().getDAOStatus();
        List rader = (List) statusrader.getWrappedData();
        for (int i = rader.size() - 1; i >= 0; i--) {
            Statusrad rad = (Statusrad) rader.get(i);
            if (kontrolleraBorttag(rad)) {
                rader.remove(i);
                dao.delete(rad.getNr());
            }
        }
        statusrader.setWrappedData(rader);
        uppdatera();
    }
    
    public void taBortStatusar(ActionEvent e) {
        log.debug("taBortStatusar");
        DAOStatus dao = ActionHelper.getResourceFactory().getDAOStatus();
        List rader = (List) statusrader.getWrappedData();
        for (int i = rader.size() - 1; i >= 0; i--) {
            Statusrad rad = (Statusrad) rader.get(i);
            if (rad.getRensa() && kontrolleraBorttag(rad)) {
                rader.remove(i);
                dao.delete(rad.getNr());
            }
        }
        statusrader.setWrappedData(rader);
        uppdatera();
    }
    
    public void taBortServrar(ActionEvent e) {
        DAOServer dao = ActionHelper.getResourceFactory().getDAOServer();
        List rader = (List) serverrader.getWrappedData();
        for (int i = rader.size() - 1; i >= 0; i--) {
            Serverrad rad = (Serverrad) rader.get(i);
            if (rad.getDelete()) {
                rader.remove(i);
                dao.delete(rad.getId());
            }
        }
        serverrader.setWrappedData(rader);
        uppdatera();
    }

    int getComponentNr(ActionEvent e) {
        // Format: statusForm:statusTable:1:_id13
        FacesContext context = FacesContext.getCurrentInstance();
        String clientId = e.getComponent().getClientId(context);
        try {
            int pos = clientId.lastIndexOf(':');
            String s = clientId.substring(0, pos);
            pos = s.lastIndexOf(':');
            return Integer.parseInt(s.substring(pos + 1, s.length()));
        } catch  (Exception t) {
            throw new IllegalArgumentException("Kunde inte hitta komponentnummer", t);
        }
    }
    

    Serverrad getServerrad(int radnr) {
        List rader = (List) serverrader.getWrappedData();
        return (Serverrad) rader.get(radnr);
    }
    
    Statusrad getStatusrad(int radnr) {
        List rader = (List) statusrader.getWrappedData();
        return (Statusrad) rader.get(radnr);
    }
    
    public void startaProcess(ActionEvent e) {
        int radnr = getComponentNr(e);
        Serverrad rad = getServerrad(radnr);
        
        DAOServer daoserver = ActionHelper.getResourceFactory().getDAOServer();
        Server server = daoserver.get(rad.getId());

//        DAOStatus daostatus = ActionHelper.getResourceFactory().getDAOStatus();
//        List statuses = daostatus.getStatus(null, new Integer(rad.getId()));
//        // Kontrollera att det inte blir för många motorer i samma maskin:
//        int count = 0;
//        for (Iterator it = statuses.iterator(); it.hasNext();) {
//            Status status = (Status) it.next();
//            if (status.getStatus() != SkickaMeddelandeStateMachine.STOPPED) {
//                count++;
//            }
//        }
        //if (count >= MAX_INSTANSER_PER_SERVER) { Kommentar: Kommenterar bort if-sats eftersom den var tom.
            //return false;
        //}
        
        
        if (server != null) {
            log.info("Startar ny instans på URL " + server.getServleturl());
            ServletUtils.anropaServletAsynkront(server.getServleturl() + "?start=true");
        } else {
            log.error("Felaktigt serverid: " + rad.getId());
        }
        uppdatera();
    }
    
    void styrProcess(ActionEvent e, int nyStatus) {
        Statusrad rad = getStatusrad(getComponentNr(e)); 
        log.debug("Styr process, status nr " + rad.getNr());
        DAOStatus daostatus = ActionHelper.getResourceFactory().getDAOStatus();
        Status s = daostatus.getStatus(rad.getNr());
        if (s == null) {
            throw new IllegalArgumentException("Finns ingen status med numret " + rad.getNr());
        }
        s.setStatus(nyStatus);
        
        // Kolla watchdogtstamp
        kontrolleraWatchdogtid(s);
        
        daostatus.uppdatera(s);
    }
    
    private Date getJamforelsetid() {
        int watchdogtid = 0;
    	try {
    		watchdogtid = ActionHelper.getResourceFactory().getParameterKalla().getIntParam("WATCHDOGTID", 0);
    	} catch (Exception t) {
    		log.error("Fel i uppslagning av watchdogtid: ", t);
    		return null;
    	}
        int marginal = 60;
        maxalder = watchdogtid + marginal;
        Date jamforelsetid = new Date(System.currentTimeMillis() - 1000L * maxalder);
        
        return jamforelsetid;
    }
    
    private void kontrolleraWatchdogtid(Status s) {
        Date jamforelsetid = getJamforelsetid();
        if (jamforelsetid == null) {
            return;
        }
        if ((s.getWatchdog() != null) && (s.getWatchdog().before(jamforelsetid))) {
            //DAOMeddelande daomeddelande = ActionHelper.getResourceFactory().getDAOMeddelande();
            String sql = "UPDATE MEDDELANDE SET STATUS = " + MeddelandeHandelse.MOTTAGET
        	+ " WHERE STATUS = -" + s.getInstans();
	        log.info("Återställer meddelandestatus för avbrutna sändningar för instans: " + s.getInstans());
	        ActionHelper.getResourceFactory().getQueryProcessor().executeThrowException(sql);
	        s.setStatus(SkickaMeddelandeStateMachine.STOPPED);
        }
    }
    
    public void pausaProcess(ActionEvent e) {
        styrProcess(e, SkickaMeddelandeStateMachine.PAUSING);
        uppdatera();
    }
    
    public void stoppaProcess(ActionEvent e) {
        styrProcess(e, SkickaMeddelandeStateMachine.STOPPING);
        uppdatera();
    }
    
    public void fortsattProcess(ActionEvent e) {
        styrProcess(e, SkickaMeddelandeStateMachine.RUNNING);
        uppdatera();
    }

//    private boolean testAdress(String adress) {
//        try {
//            return CommunicationTester.isPortOpen(adress);
//        } catch (Exception t) {
//            return false;
//        }
//    }
//    
    
    public ListDataModel getServerrader() {
        return serverrader;
    }
    public void setServerrader(ListDataModel serverrader) {
        this.serverrader = serverrader;
    }
    public ListDataModel getStatusrader() {
        return statusrader;
    }
    public void setStatusrader(ListDataModel statusrader) {
        this.statusrader = statusrader;
    }
    
    public String getServertid() {
        return new Date().toString();
    }
    
    
    
    public ListDataModel getKanaler() {
    	// Initiera nedprioriterade inkanaler med begränsningar
	    List<Kanal> kanalerMedBegransningar = new ArrayList<Kanal>();
	    ParameterKalla paramSource = ActionHelper.getResourceFactory().getParameterKalla();
	    for (String namn : paramSource.getStringParam("KANALER_MED_BEGRANSNINGAR", "").split("[, ]+")) {
	    	if (namn.trim().length() == 0) {
	    		break;
	    	}
	    	Kanal kanal = new Kanal(namn);
	    	// Hämta parametrar
	    	int pt = paramSource.getIntParam(kanal.getMaxAntalPerTimmeKey(), -1);
	    	int bs = paramSource.getIntParam(kanal.getBatchStorlekKey(), -1);
	    	int bk = paramSource.getIntParam(kanal.getBatchKvarKey(), bs);
	    	int st = paramSource.getIntParam(kanal.getSovtidKey(), -1);
	    	long soverTimestamp = paramSource.getLongParam(kanal.getSoverTimestampKey(), -1);
	    	String otid = paramSource.getStringParam(kanal.getOppningstidKey(), "00:00:00");
	    	String stid = paramSource.getStringParam(kanal.getStangningstidKey(), "23:59:59");
	    	// Initiera kanal
	    	kanal.setMaxAntalPerTimme(pt);
	    	kanal.setBatchStorlek(bs);
	    	kanal.setBatchKvar(bk);
	    	kanal.setSovtid(st);
	    	if (soverTimestamp > 0) {
	    		kanal.setSoverTimestamp(new Date(soverTimestamp));
	    	}
	    	try {
	    		kanal.setOppningstid(otid);
	    	} catch (IllegalArgumentException e) {
	    		log.error("Kunde inte sätta öppningstid på kanalen " + namn, e);
	    	}
	    	try {
	    		kanal.setStangningstid(stid);
	    	} catch (IllegalArgumentException e) {
	    		log.error("Kunde inte sätta stängningstid på kanalen " + namn, e);
	    	}
	    	kanalerMedBegransningar.add(kanal);
	    }
	    return new ListDataModel(kanalerMedBegransningar);
    }
    
}
