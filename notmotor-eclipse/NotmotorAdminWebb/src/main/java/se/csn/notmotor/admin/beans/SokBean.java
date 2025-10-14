/**
 * Skapad 2007-jun-12
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.admin.beans;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import se.csn.ark.common.util.logging.Log;
import se.csn.common.util.DateUtils;
import se.csn.notmotor.admin.actions.ActionHelper;
import se.csn.notmotor.ipl.db.DAOHandelse;
import se.csn.notmotor.ipl.db.DAOImplBase;
import se.csn.notmotor.ipl.db.QueryProcessor;
import se.csn.notmotor.ipl.db.RowToObjectMapper;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;

public class SokBean {
    private Log log = Log.getInstance(SokBean.class);
    private final static int MAXANTALMEDDELANDEN = 1000;
    private SelectItem[] allaStatusar = {
        new SelectItem(new Integer(MeddelandeHandelse.MOTTAGET), "Mottaget"),
        new SelectItem(new Integer(MeddelandeHandelse.SKICKAT_SERVER), "Skickat till mailserver"),
        new SelectItem(new Integer(MeddelandeHandelse.MEDDELANDEFEL), "Meddelandefel"),
        new SelectItem(new Integer(MeddelandeHandelse.TEKNISKT_FEL), "Tekniskt fel"),
        new SelectItem(new Integer(MeddelandeHandelse.BORTTAGET), "Borttaget"),
        new SelectItem(new Integer(MeddelandeHandelse.BESVARAT), "Besvarat"),
        new SelectItem(new Integer(-1), "Under sändning"),
    };

    private String fromSkapat, tomSkapat, fromSkickat, tomSkickat, fromSkickaTidigast, tomSkickaTidigast;
    private String avsandaradress, avsandarnamn, mottagaradress, mottagarnamn;
    private String kanal, applikation, kategori;
    private int csnnummer;
    private int antalMeddelanden;
    private int maxAntalMeddelanden = MAXANTALMEDDELANDEN;
    private int[] valdaStatusar;
    
    private ListDataModel meddelanden;
    private String orderBy = "";
    private String orderByAscDesc = "";
    private String sorteringsordningForId = "ascendingOrder";
    private String sorteringsordningForSkapat = "ascendingOrder";
    private String sorteringsordningForSkickat = "ascendingOrder";
    
    
    public static class Meddelanderad {
        private long id;
        private String status, skickat, applikation, mottagare, rubrik, skapat, skickaTidigast, kanal;
        private boolean markeradForOmsandning;
        public Meddelanderad(long id, String status, String skapat, String kanal, String skickat, String skickaTidigast, String applikation, String mottagare, String rubrik) {
            this.id = id;
            this.status = status;
            this.skapat = skapat;
            this.kanal = kanal;
            this.skickat = skickat;
            this.skickaTidigast = skickaTidigast;
            this.applikation = applikation; 
            this.mottagare = mottagare;
            this.rubrik = rubrik;
            this.markeradForOmsandning = false;
        }
        
        /**
         * Returnerar true om meddelandet ska vara mojligt att sanda om. Omsandningen ar mojligt i de fall
         * ett meddelande har statuskod 32(Meddelandefel) eller 16(Tekniskt fel).
         * @return true om meddelandet kan sändas om, annars false.
         */
        public boolean isOmsandningMojlig() {
        	if (status != null) {
        		int intStatus = Integer.parseInt(status);
        		return intStatus == MeddelandeHandelse.MEDDELANDEFEL || intStatus == MeddelandeHandelse.TEKNISKT_FEL;
        	}
        	return false;
        }
        
        
        public String getApplikation() {
            return applikation;
        }
        public void setApplikation(String applikation) {
            this.applikation = applikation;
        }
        public long getId() {
            return id;
        }
        public void setId(long id) {
            this.id = id;
        }
        public String getMottagare() {
            return mottagare;
        }
        public void setMottagare(String mottagare) {
            this.mottagare = mottagare;
        }
        public String getRubrik() {
            return rubrik;
        }
        public void setRubrik(String rubrik) {
            this.rubrik = rubrik;
        }
        public String getSkickat() {
            return skickat;
        }
        public void setSkickat(String skickat) {
            this.skickat = skickat;
        }
        public String getSkapat() {
            return skapat;
        }
        public void setSkapat(String skapat) {
            this.skapat = skapat;
        }
        public String getSkickaTidigast() {
            return skickaTidigast;
        }
        public void setSkickaTidigast(String skickaTidigast) {
            this.skickaTidigast = skickaTidigast;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public String getKanal() {
            return kanal;
        }
        public void setKanal(String kanal) {
            this.kanal = kanal;
        }
        public boolean isMarkeradForOmsandning() {
			return markeradForOmsandning;
		}
        public void setMarkeradForOmsandning(boolean markeradForOmsandning) {
			this.markeradForOmsandning = markeradForOmsandning;
		}        
    }
    
    public SokBean() { 
        meddelanden = new ListDataModel();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date from = new Date(cal.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fromSkapat = sdf.format(from);
        Date tom = new Date();
        tomSkapat = sdf.format(tom);
    }
    
    public SelectItem[] getAllaStatusar() {
        return allaStatusar;
    }
    
    public String getStatusBeskrivning(String status) {
    	
        for(int i = 0; i < allaStatusar.length; i++) {
            if (status.equals(allaStatusar[i].getValue().toString())) {
            	return (String)allaStatusar[i].getLabel();
            }
        }    	
        return "Okänt";
    }
    
    public void setValdaStatusar(int[] statusar) {
       for(int i = 0; i < statusar.length; i++) {
           log.debug("" + statusar[i]);
       }
       valdaStatusar = statusar;
    }
    
    public int[] getValdaStatusar() {
        return valdaStatusar;
    }
    
    public static class MeddelandeRowMapper implements RowToObjectMapper {
        public Object newRow(ResultSet rs) throws SQLException {
            //log.debug("Ny rad");
            return new Meddelanderad(rs.getLong("ID"), 
                    				rs.getString("STATUS"),
                    				rs.getString("SKAPAT"),
                    				rs.getString("KANAL"),
                    				rs.getString("SKICKAT"),
                    				rs.getString("SKICKATIDIGAST"),
                    				rs.getString("APPLIKATION"),
                    				rs.getString("MOTTAGARE"),
                    				rs.getString("RUBRIK"));
        }
    }
    
    public void sok(ActionEvent e) {
        sorteringsordningForId = "descendingOrder";
        sorteringsordningForSkapat = "descendingOrder";
        sorteringsordningForSkickat = "descendingOrder";
        if (orderByAscDesc.equals("ASC")) {
        	if (orderBy.equals("ID")) {
                sorteringsordningForId = "ascendingOrder";        		
        	} else if (orderBy.equals("SKAPADTIDPUNKT")) {
                sorteringsordningForSkapat = "ascendingOrder";        		
        	} else if (orderBy.equals("SANTTIDPUNKT")) {
                sorteringsordningForSkickat = "ascendingOrder";        		
        	}
        } 

        sok();
    }

    /**
     * Utfor en sokning efter meddelanden enligt de villkor som fyllts pa webbsidan.
     */
	private void sok() {
		log.debug("sok");
        String sql = "SELECT MEDD.ID AS ID, MEDD.KANAL AS KANAL, MEDD.STATUS AS STATUS, "
        		+ "MEDD.RUBRIK AS RUBRIK, MEDD.SKAPADTIDPUNKT AS SKAPAT, "
        		+ "MEDD.SANTTIDPUNKT AS SKICKAT, MEDD.SKICKATIDIGAST AS SKICKATIDIGAST, "
        		+ "AVS.PROGRAMNAMN AS APPLIKATION, MOTT.ADRESS AS MOTTAGARE "
        		+ "FROM MEDDELANDE MEDD JOIN AVSANDARE AVS ON MEDD.AVSANDARE = AVS.ID "
        		+ "JOIN MOTTAGARE MOTT ON MEDD.ID = MOTT.MEDDELANDEID ";

        String where = skapaSokWherevillkor();
        if (where.length() > 0) {
            sql = sql + "WHERE " + where;
        }
        
        String orderBy = skapaOrderByVillkor();
        
        sql = sql + orderBy;

        log.debug("SQL: " + sql);
        // Slå upp meddelanden:
        QueryProcessor qp = ActionHelper.getResourceFactory().getQueryProcessor();
        List meddelanderader = qp.processQuery(sql, new MeddelandeRowMapper());
        antalMeddelanden = meddelanderader.size();
        meddelanden.setWrappedData(meddelanderader);
	}
    
    /**
     * @return en giltig SQL-sträng med de villkor som är angivna i bönan 
     */
    String skapaSokWherevillkor() {
        Date skapatFrom = DateUtils.strToDate(fromSkapat, true);
        Date skapatTom = DateUtils.strToDate(tomSkapat, false);
        Date skickatFrom = DateUtils.strToDate(fromSkickat, true);
        Date skickatTom = DateUtils.strToDate(tomSkickat, false);
        Date skickaTidigastFrom = DateUtils.strToDate(fromSkickaTidigast, true);
        Date skickaTidigastTom = DateUtils.strToDate(tomSkickaTidigast, false);
        
        String where = "";
        // Lägg till datumvillkor
        if (skapatFrom != null) {
        	where = DAOImplBase.addRestriction(where, "MEDD.SKAPADTIDPUNKT", ">=", skapatFrom);
        } else {
        	// Om man inte har angivit ett from-datum begränsar vi frågan. Vi hämtar ju ändå bara 1000 senaste medd.
        	where += "MEDD.SKAPADTIDPUNKT > ((CURRENT TIMESTAMP)-7 DAYS)";
        }
        where = DAOImplBase.addRestriction(where, "MEDD.SKAPADTIDPUNKT", "<=", skapatTom);
        where = DAOImplBase.addRestriction(where, "MEDD.SANTTIDPUNKT", ">=", skickatFrom);
        where = DAOImplBase.addRestriction(where, "MEDD.SANTTIDPUNKT", "<=", skickatTom);
        where = DAOImplBase.addRestriction(where, "MEDD.SKICKATIDIGAST", ">=", skickaTidigastFrom);
        where = DAOImplBase.addRestriction(where, "MEDD.SKICKATIDIGAST", "<=", skickaTidigastTom);

        // Lägg till adressvillkor:
        where = DAOImplBase.addLike(where, "AVS.NAMN", avsandarnamn, true);
        where = DAOImplBase.addLike(where, "AVS.EPOST", avsandaradress, true);
        where = DAOImplBase.addLike(where, "MOTT.NAMN", mottagarnamn, true);
        where = DAOImplBase.addLike(where, "MOTT.ADRESS", mottagaradress, true);
        
        // Lägg till kanal, programnamn, csnnummer etc.
        where = DAOImplBase.addLike(where, "MEDD.KANAL", kanal, true);
        where = DAOImplBase.addLike(where, "AVS.PROGRAMNAMN", applikation, true);
        where = DAOImplBase.addLike(where, "AVS.KATEGORI", kategori, true);
        if (csnnummer != 0) {
        	where = DAOImplBase.addRestriction(where, "MEDD.CSNNUMMER", "=",  new Integer(csnnummer));
        }
        
        // Lägg till tillståndsvillkor:
        if(valdaStatusar != null && valdaStatusar.length > 0) {
	        boolean underSandning = false;
	        String inlist = "";
	        for(int i = 0; i < valdaStatusar.length; i++) {
	            if(valdaStatusar[i] == -1) {
	                underSandning = true;
	                continue;
	            }
	            if(inlist.length() > 0) {
	                inlist += ",";
	            }
	            inlist = inlist + valdaStatusar[i];
	        }
	        // Här finns 4 möjligheter:
	        String statusrestriction = "";
	        if(inlist.length() > 0) {
	            statusrestriction = "MEDD.STATUS IN (" + inlist + ")";
	        }
	        if(underSandning) {
	            if(statusrestriction.length() > 0) {
		            statusrestriction += " OR ";
	            }
	            statusrestriction += "MEDD.STATUS < 0";
	        }
	        where = DAOImplBase.addRestriction(where, statusrestriction);
        }
        return where;
    }
    
    private String skapaOrderByVillkor(){    	
    	return " ORDER BY MEDD." + orderBy + " " + orderByAscDesc + " FETCH FIRST " + maxAntalMeddelanden + " ROWS ONLY";
    }
 
    public String getOrderBy(){
    	return orderBy;
    }
    public void setOrderBy(String inOrderBy){
    	orderBy = inOrderBy;
    }
    
    public String getOrderByAscDesc(){
    	return orderByAscDesc;
    }
    public void setOrderByAscDesc(String inOrderByAscDesc){
    	orderByAscDesc = inOrderByAscDesc;
    }
    
    /**
     * Skickar om ett flera meddelanden som valts pa webbsidan.
     * @param e action event från webbsidan
     */
    public void skickaOm(ActionEvent e) {
    	try {
    		DAOHandelse daoHandelse = ActionHelper.getResourceFactory().getDAOHandelse();
    		QueryProcessor qp = ActionHelper.getResourceFactory().getQueryProcessor();
    		for (Meddelanderad rad : getMeddelandenAsList()) {
    			if (rad.markeradForOmsandning) {
    				log.debug("skickaom, id:" + rad.getId());
			    	MeddelandeHandelse handelse = new MeddelandeHandelse(MeddelandeHandelse.MOTTAGET, MeddelandeHandelse.OK, "Omsändning");
			    	daoHandelse.createHandelse(handelse, rad.getId());
			    	//Läs alla händelser för aktuellt meddelande
			    	MeddelandeHandelse[] h = daoHandelse.getHandelserForMeddelande(rad.getId()).toArray(new MeddelandeHandelse[0]); 
			    	int forstaLikaMedd = 0;
			    	Integer typ = -1;
			    	Integer felkod = -1;
			    	String feltext = "";
			    	Date tidpunkt = null;
			    	int antalLikaHandelser = 1;
			    	for (int i = h.length - 1; i >= 0; i--) {
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
			    			+ " WHERE ID=" + rad.getId());
    			}
    		}
    	} catch (Exception t) {
    		log.error("Kunde inte skicka om meddelande, fel: ", t);
    	}
    	sok();
    }
    
    /**
     * Kontrollerar om nagra meddelanden som hittades i sokningen ar mojliga att skicka om.
     * @return true om något meddelande kan skickas om, annars false.
     */
    public boolean getFinnsMeddelandenMojligaForOmsandning() {
    	if (meddelanden != null && meddelanden.getWrappedData() != null) { 
	    	for (Meddelanderad rad : getMeddelandenAsList()) {
	    		if (rad.isOmsandningMojlig()) {
	    			return true;
	    		}
	    	}
    	}
    	return false;
    }
    
    public boolean getFinnsMeddelanden() {
    	return antalMeddelanden > 0;
    }
    
    public int getAntalMeddelanden() {
        return antalMeddelanden;
    }
    
    public ListDataModel getMeddelanden() {
        return meddelanden;
    }
    
    public String getSorteringsordningForId() {
    	return sorteringsordningForId;
    }
    
    public void setSorteringsordningForId(String inSorteringsordningForId) {
    	sorteringsordningForId = inSorteringsordningForId;
    }

    public String getSorteringsordningForSkapat() {
    	return sorteringsordningForSkapat;
    }
    
    public void setSorteringsordningForSkapat(String inSorteringsordningForSkapat) {
    	sorteringsordningForSkapat = inSorteringsordningForSkapat;
    }

    public String getSorteringsordningForSkickat() {
    	return sorteringsordningForSkickat;
    }
    
    public void setSorteringsordningForSkickat(String inSorteringsordningForSkickat) {
    	sorteringsordningForSkickat = inSorteringsordningForSkickat;
    }

    /* Jämförelse för sortering av listan med Id */
    public static Comparator<Meddelanderad> MeddelandeId_ascendingOrder = new Comparator<SokBean.Meddelanderad>() {
		public int compare(Meddelanderad m1, Meddelanderad m2) {
		   return m1.getId() > m2.getId() ? 1 : -1; 
	    }
	};
	public static Comparator<Meddelanderad> MeddelandeId_descendingOrder = new Comparator<SokBean.Meddelanderad>() {
		public int compare(Meddelanderad m1, Meddelanderad m2) {
		   return m2.getId() > m1.getId() ? 1 : -1;
	    }
	};
	
	/* Jämförelse för sortering av listan med Skapat */
    public static Comparator<Meddelanderad> MeddelandeSkapat_ascendingOrder = new Comparator<SokBean.Meddelanderad>() {
		public int compare(Meddelanderad m1, Meddelanderad m2) {
		   String SkapatName1 = m1.getSkapat();
		   String SkapatName2 = m2.getSkapat();

		   return SkapatName1.compareTo(SkapatName2); 
	    }
	};
	public static Comparator<Meddelanderad> MeddelandeSkapat_descendingOrder = new Comparator<SokBean.Meddelanderad>() {
		public int compare(Meddelanderad m1, Meddelanderad m2) {
		   String SkapatName1 = m1.getSkapat();
		   String SkapatName2 = m2.getSkapat();
		  
		   return SkapatName2.compareTo(SkapatName1);
	    }
	};
    
	
	/* Jämförelse för sortering av listan med Skickat */
	public static Comparator<Meddelanderad> MeddelandeSkickat_ascendingOrder = new Comparator<SokBean.Meddelanderad>() {
		public int compare(Meddelanderad m1, Meddelanderad m2) {
			String SkickatName1 = m1.getSkickat() != null ? m1.getSkickat() : "";
			String SkickatName2 = m2.getSkickat() != null ? m2.getSkickat() : "";
			
			return SkickatName1.compareTo(SkickatName2); 
	    }
	};
	public static Comparator<Meddelanderad> MeddelandeSkickat_descendingOrder = new Comparator<SokBean.Meddelanderad>() {
		public int compare(Meddelanderad m1, Meddelanderad m2) {
			String SkickatName1 = m1.getSkickat() != null ? m1.getSkickat() : "";
			String SkickatName2 = m2.getSkickat() != null ? m2.getSkickat() : "";
		  
		   return SkickatName2.compareTo(SkickatName1);
	    }
	};

	
    //Sortering 
    public void sorteraTabell(String kolumnNamn) throws ParseException{
    	List<Meddelanderad> meddelandeList = getMeddelandenAsList();
    	if (kolumnNamn.equals("Id")) {
    		this.sorteringsordningForSkapat = "descendingOrder";
    		this.sorteringsordningForSkickat = "descendingOrder";
    		
    		if (this.sorteringsordningForId.equals("ascendingOrder")) {
    			Collections.sort(meddelandeList, SokBean.MeddelandeId_descendingOrder);
    			this.sorteringsordningForId = "descendingOrder";
    		} else {
    			Collections.sort(meddelandeList, SokBean.MeddelandeId_ascendingOrder);
    			this.sorteringsordningForId = "ascendingOrder";
    		}	
    	} else if(kolumnNamn.equals("Skapat")) {
        	this.sorteringsordningForId = "descendingOrder";
        	this.sorteringsordningForSkickat = "descendingOrder";
    		
    		if (this.sorteringsordningForSkapat.equals("ascendingOrder")) {
    			Collections.sort(meddelandeList, SokBean.MeddelandeSkapat_descendingOrder);
    			this.sorteringsordningForSkapat = "descendingOrder";
    		} else {
    			Collections.sort(meddelandeList, SokBean.MeddelandeSkapat_ascendingOrder);
    			this.sorteringsordningForSkapat = "ascendingOrder";
    		}
    	} else if (kolumnNamn.equals("Skickat")) {
    		this.sorteringsordningForId = "descendingOrder";
    		this.sorteringsordningForSkapat = "descendingOrder";
    		
    		if (this.sorteringsordningForSkickat.equals("ascendingOrder")) {
    			Collections.sort(meddelandeList, SokBean.MeddelandeSkickat_descendingOrder);
    			this.sorteringsordningForSkickat = "descendingOrder";
    		} else {
    			Collections.sort(meddelandeList, SokBean.MeddelandeSkickat_ascendingOrder);
    			this.sorteringsordningForSkickat = "ascendingOrder";
    		}
    	}
    
    }
            
    /**
     * Returnerar sokta meddelanden i en lista.
     * @return akteulla meddelanden i form av en lista.
     */
    public List<Meddelanderad> getMeddelandenAsList() {
    	ArrayList<Meddelanderad> meddelandeList = new ArrayList<SokBean.Meddelanderad>();
    	if (meddelanden != null && meddelanden.getWrappedData() != null) { 
    		meddelandeList = (ArrayList<Meddelanderad>) meddelanden.getWrappedData();
    	}
    	return meddelandeList;
    }
    public void setMeddelanden(ListDataModel meddelanden) {
        this.meddelanden = meddelanden;
    }
    
    public String getFromSkapat() {
        return fromSkapat;
    }
    public void setFromSkapat(String fromSkapat) {
        this.fromSkapat = fromSkapat;
    }
    public String getFromSkickat() {
        return fromSkickat;
    }
    public void setFromSkickat(String fromSkickat) {
        this.fromSkickat = fromSkickat;
    }
    public String getFromSkickaTidigast() {
        return fromSkickaTidigast;
    }
    public void setFromSkickaTidigast(String fromSkickaTidigast) {
        this.fromSkickaTidigast = fromSkickaTidigast;
    }
    public String getTomSkapat() {
        return tomSkapat;
    }
    public void setTomSkapat(String tomSkapat) {
        this.tomSkapat = tomSkapat;
    }
    public String getTomSkickat() {
        return tomSkickat;
    }
    public void setTomSkickat(String tomSkickat) {
        this.tomSkickat = tomSkickat;
    }
    public String getTomSkickaTidigast() {
        return tomSkickaTidigast;
    }
    public void setTomSkickaTidigast(String tomSkickaTidigast) {
        this.tomSkickaTidigast = tomSkickaTidigast;
    }
    public String getKanal() {
        return kanal;
    }
    public void setKanal(String kanal) {
        this.kanal = kanal;
    }
    public String getApplikation() {
        return applikation;
    }
    public void setApplikation(String applikation) {
        this.applikation = applikation;
    }
    public String getAvsandaradress() {
        return avsandaradress;
    }
    public void setAvsandaradress(String avsandaradress) {
        this.avsandaradress = avsandaradress;
    }
    public String getAvsandarnamn() {
        return avsandarnamn;
    }
    public void setAvsandarnamn(String avsandarnamn) {
        this.avsandarnamn = avsandarnamn;
    }
    public String getKategori() {
        return kategori;
    }
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
    public String getMottagaradress() {
        return mottagaradress;
    }
    public void setMottagaradress(String mottagaradress) {
        this.mottagaradress = mottagaradress;
    }
    public String getMottagarnamn() {
        return mottagarnamn;
    }
    public void setMottagarnamn(String mottagarnamn) {
        this.mottagarnamn = mottagarnamn;
    }
	public int getCsnnummer() {
		return csnnummer;
	}
	public void setCsnnummer(int csnnummer) {
		this.csnnummer = csnnummer;
	}
	public int getMaxAntalMeddelanden() {
		return maxAntalMeddelanden;
	}
	public void setMaxAntalMeddelanden(int maxAntalMeddelanden) {
		this.maxAntalMeddelanden = maxAntalMeddelanden;
	}


}
