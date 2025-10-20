package se.csn.notmotor.admin.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.ListDataModel;

import se.csn.ark.common.util.logging.Log;
import se.csn.common.util.DateUtils;
import se.csn.notmotor.admin.ResourceFactory;
import se.csn.notmotor.admin.actions.ActionHelper;
import se.csn.notmotor.ipl.SkickaMeddelandeStateMachine;
import se.csn.notmotor.ipl.db.DAOImplBase;
import se.csn.notmotor.ipl.db.QueryProcessor;
import se.csn.notmotor.ipl.db.RowToObjectMapper;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;

public class StatistikBean {

    private static final String REQUEST_PARAMETER_VALD_DAG = "valdDag";
	private static final String DATUMFORMAT = "yyyy-MM-dd";
	private QueryProcessor qp;
	private ListDataModel<Datumrad> datumrader;
	private String startdatum, slutdatum;
	private final Log log = Log.getInstance(StatistikBean.class);
    
    private Statistik mottagna;
    private Statistik sant;
    private Statistik borttagna;
    private List<Statistik> kanalerMottagna;
    private List<Statistik> kanalerSant;
    private List<Statistik> kanalerBorttagna;
    private String valdDag;
    
	
    public static class Datumrad {
        private String datum, applikation;
        private int antal, instans, status;
        
        public Datumrad(String datum, int instans, int status, String applikation, int antal) {
            this.datum = datum;
            this.instans = instans;
            this.applikation = applikation;
            this.antal = antal;
            this.status = status;
        }
        
        public int getAntal() {
            return antal;
        }
        public void setAntal(int antal) {
            this.antal = antal;
        }
        public String getApplikation() {
            return applikation;
        }
        public void setApplikation(String applikation) {
            this.applikation = applikation;
        }
        public String getDatum() {
            return datum;
        }
        public void setDatum(String datum) {
            this.datum = datum;
        }
        public int getInstans() {
            return instans;
        }
        public void setInstans(int instans) {
            this.instans = instans;
        }
        public String getStatus() {
            return MeddelandeHandelse.getTyptext(status);
        }
    }
    
    public static class DatumradRowMapper implements RowToObjectMapper {
        public Object newRow(ResultSet rs) throws SQLException {
            return new Datumrad(rs.getString("DATUM"), rs.getInt("INSTANS"), rs.getInt("STATUS"), null, rs.getInt("ANTAL"));
        }
    }
    
    public static class StartadRowMapper implements RowToObjectMapper {
    	public Object newRow(ResultSet rs) throws SQLException {
             return rs.getTimestamp("STARTAD");
        }
    }
    
    public static class Statistikrad {
    	private String kanal;
    	private int antal;
    	
        public Statistikrad(String kanal, int antal) {
            this.kanal = kanal;
            this.antal = antal;
        }
        
        public String getKanal() {
    		return kanal;
    	}
        
        public int getAntal() {
    		return antal;
    	}        
    }
    
    public static class StatistikradRowMapper implements RowToObjectMapper {
        public Object newRow(ResultSet rs) throws SQLException {
        	return new Statistikrad(rs.getString("KANAL"), rs.getInt("ANTAL"));
        }
    }
        
    public static class Statistik {
    	public static final int SENASTE_TIMMEN = 1;
    	public static final int SEDAN_MIDNATT = 2;
    	public static final int SENASTE_24 = 3;
    	public static final int ANGIVEN_DAG = 4;
    	public static final int TOTALT = 5;
    	private String kanal;
    	private int senasteTimmen;
        private int sedanMidnatt;
        private int senaste24;
        private int totalt;
        private int angivenDag;
        
        public Statistik(String kanal) {
            this.kanal = kanal;
            this.senasteTimmen = 0;
            this.sedanMidnatt = 0;
            this.senaste24 = 0;
            this.angivenDag = 0;
            this.totalt = 0;
        }
        
        public String getKanal() {
    		return kanal;
    	}
        
        public int getSenasteTimmen() {
    		return senasteTimmen;
    	}
    	
    	public int getSedanMidnatt() {
    		return sedanMidnatt;
    	}
    	
    	public int getSenaste24() {
    		return senaste24;
    	}
    	
    	public int getTotalt() {
    	    return totalt;
    	}
    	
    	public int getAngivenDag() {
			return angivenDag;
		}
    	
        public void set(int field, int value) {
        	switch (field) {
			case Statistik.SENASTE_TIMMEN :
				this.senasteTimmen = value;
				break;
			case Statistik.SEDAN_MIDNATT :
				this.sedanMidnatt = value;
				break;
			case Statistik.SENASTE_24 :
				this.senaste24 = value;
				break;
			case Statistik.TOTALT :
				this.totalt = value;
				break;
			case Statistik.ANGIVEN_DAG :
				this.angivenDag = value;
				break;
			default :
				throw new IllegalArgumentException("Kunde inte sätta statistik, ogiltigt fält: " + field);
			}
        }

    	public String toString() {
    		return "Statistik [kanal=" + kanal
    				+ ", senaste timmen=" + senasteTimmen
    				+ ", sedan midnatt=" + sedanMidnatt
    				+ ", senaste 24=" + senaste24
    				+ ", angiven dag=" + angivenDag
    				+ ", totalt=" + totalt;
    	}
    }
    
        
	public StatistikBean() {
	    FacesContext context = FacesContext.getCurrentInstance();
	    Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
		ResourceFactory factory = ActionHelper.getResourceFactory();
	    factory.setTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED);
	    qp = ActionHelper.getResourceFactory().getQueryProcessor();
	    // Sätt TransactionLevel till READ_UNCOMMITTED, vi behöver ingen 
	    // vidare precision i dessa slagningar
	    datumrader = new ListDataModel<Datumrad>();
	    this.mottagna = null;
	    this.sant = null;
	    this.borttagna = null;
	    this.kanalerMottagna = null;
	    this.kanalerSant = null;
	    this.kanalerBorttagna = null;
	    if (requestParameterMap.containsKey(REQUEST_PARAMETER_VALD_DAG)) {
	    	this.valdDag = requestParameterMap.get(REQUEST_PARAMETER_VALD_DAG);
	    } else {
	    	this.valdDag = getDatumSomString(getDagensDatum());
	    }
	}
	
	/**
	 * Hämtar all tillgänglig statistik om mottagna, sända och borttagna meddelanden.
	 * All statistik lagras i bönan och finns tillgängliga via get-metoderna.
	 */
	private void hamtaStatistik() {
		long start = System.currentTimeMillis();
		Map<String, Statistik> statistikMottagna = new HashMap<String, Statistik>();
		Map<String, Statistik> statistikSant = new HashMap<String, Statistik>();
		Map<String, Statistik> statistikBorttagna = new HashMap<String, Statistik>();
		List<Statistikrad> statistikrader = null;
		Calendar cal = null;
				
	    /* Senaste timmen */
	    cal = GregorianCalendar.getInstance();
	    cal.add(Calendar.HOUR_OF_DAY, -1);
	    //    mottagna
	    statistikrader = getMottagnaSedan(cal.getTime());
	    setStatistik(statistikMottagna, statistikrader, Statistik.SENASTE_TIMMEN);
	    //    sänt
	    statistikrader = getSantSedan(cal.getTime());
	    setStatistik(statistikSant, statistikrader, Statistik.SENASTE_TIMMEN);
	    
	    /* Sedan midnatt */
	    cal = GregorianCalendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    //    mottagna
	    statistikrader = getMottagnaSedan(cal.getTime());
	    setStatistik(statistikMottagna, statistikrader, Statistik.SEDAN_MIDNATT);
	    //    sänt
	    statistikrader = getSantSedan(cal.getTime());
	    setStatistik(statistikSant, statistikrader, Statistik.SEDAN_MIDNATT);
	    
	    /* Senaste 24 timmarna */
	    cal = GregorianCalendar.getInstance();
	    cal.add(Calendar.HOUR_OF_DAY, -24);
	    //    mottagna
	    statistikrader = getMottagnaSedan(cal.getTime());
	    setStatistik(statistikMottagna, statistikrader, Statistik.SENASTE_24);
	    //    sänt
	    statistikrader = getSantSedan(cal.getTime());
	    setStatistik(statistikSant, statistikrader, Statistik.SENASTE_24);
	    
	    /* Angiven dag(default dagens datum) */
	    //sänt
	    statistikrader = getSantUnderEnDag(getDatumSomDate(valdDag));
	    setStatistik(statistikSant, statistikrader, Statistik.ANGIVEN_DAG);
	    //mottagna
	    statistikrader = getMottagnaUnderEnDag(getDatumSomDate(valdDag));
	    setStatistik(statistikMottagna, statistikrader, Statistik.ANGIVEN_DAG);
	    
	    /* Totalt */
	    //    mottagna
	    statistikrader = getMottagnaSedan(null);
	    setStatistik(statistikMottagna, statistikrader, Statistik.TOTALT);
	    //    sänt
	    statistikrader = getSantSedan(null);
	    setStatistik(statistikSant, statistikrader, Statistik.TOTALT);
	    //    borttagna
	    statistikrader = getBorttagnaTotalt();
	    setStatistik(statistikBorttagna, statistikrader, Statistik.TOTALT);
	    
	    
	    // Spara statistiken i bönan för enkel åtkomst från webbsidan
	    this.mottagna = null;
	    this.kanalerMottagna = new ArrayList<Statistik>();
	    for (Statistik s : statistikMottagna.values()) {
	    	if (s.getKanal() == null) {
	    		this.mottagna = s;
	    	} else {
	    		this.kanalerMottagna.add(s);
	    	}
	    }
	    this.sant = null;
	    this.kanalerSant = new ArrayList<Statistik>();
	    for (Statistik s : statistikSant.values()) {
	    	if (s.getKanal() == null) {
	    		this.sant = s;
	    	} else {
	    		this.kanalerSant.add(s);
	    	}
	    }
	    this.borttagna = null;
	    this.kanalerBorttagna = new ArrayList<Statistik>();
	    for (Statistik s : statistikBorttagna.values()) {
	    	if (s.getKanal() == null) {
	    		this.borttagna = s;
	    	} else {
	    		this.kanalerBorttagna.add(s);
	    	}
	    }
	    	    
	    long stop = System.currentTimeMillis();
	    log.info("Tid för att hämta statistik: " + (stop - start) + " ms");
	}
	
	/**
	 * Uppdaterar befintlig statistik med ny statistik.
	 * @param statistik befintlig statistik, där varje kanal mappas mot ett Statistik-objekt.
	 * 			Kanalen null motsvarar den totala summan av statistiken.
	 * 			Denna Map MÅSTE tillåta null-keys!!!
	 * @param statistikrader en lista med nya statistikrader. Varje Statistikrad-objekt representerar en kanal.
	 * @param field det statistikfält som ska sättas/uppdateras
	 */
	private void setStatistik(Map<String, Statistik> statistik, List<Statistikrad> statistikrader, int field) {
		// Sätt statistik för varje kanal. Skapa statistikobjekt om nödvändigt.
		for (Statistikrad srad : statistikrader) {
			String kanal = srad.getKanal();
			if (kanal != null) {
				Statistik s = statistik.get(kanal) != null ? statistik.get(kanal) : new Statistik(kanal);
				s.set(field, getAntal(statistikrader, kanal));
				statistik.put(kanal, s);
			}
		}
		// Sätt statistik för totalen. Skapa statistikobjekt om nödvändigt.
		Statistik s = statistik.get(null) != null ? statistik.get(null) : new Statistik(null);
		s.set(field, getAntal(statistikrader, null));
		statistik.put(null, s);
	}
	
	/**
	 * Returnerar aktuellt värde för given kanal utifrån lista med statistikradobjekt.
	 * Om kanal är null så returneras summan av alla statistikradobjekt.
	 * @param statistikrader lista med statistikradobjekt
	 * @param kanal den kanal som söks. Null ger totala summan.
	 * @return värdet (eller summan) från givna statistikradobjekt, beroende på kanal
	 */
	private int getAntal(List<Statistikrad> statistikrader, String kanal) {
		int antal = 0;
		for (Statistikrad srad : statistikrader) {
			if (kanal == null || kanal.equals(srad.getKanal())) {
				antal += srad.getAntal();
			}
		}
		return antal;
	}	
	
	@SuppressWarnings("unchecked")
	private List<Statistikrad> getSantSedan(Date tid) {
	    if (tid == null) {
	        return qp.processQuery("SELECT KANAL, COUNT(*) as ANTAL FROM MEDDELANDE "
	        		+ "WHERE SANTTIDPUNKT IS NOT NULL GROUP BY KANAL", new StatistikradRowMapper());
	    }
	    return qp.processQuery("SELECT KANAL, COUNT(*) as ANTAL FROM MEDDELANDE "
	    		+ "WHERE SANTTIDPUNKT >= " + DAOImplBase.quoteValue(tid) + " GROUP BY KANAL", new StatistikradRowMapper());
	}
	
	/**
	 * Hämtar alla sända meddelanden grupperade per kanal för angivet datum, dsv under aktuell dag.
	 * Skulle dag vara null kommer dagens datum att användas. 
	 * @param dag den dag som statistik ska hämas för
	 * @return alla meddelanden som sänts under angiven dag.
	 */
	@SuppressWarnings("unchecked")
	private List<Statistikrad> getSantUnderEnDag(Date dag) {
	    if (dag == null) {
	        dag = getDagensDatum();
	    }
	    
	    //Sätt starttid till vald dag med start 00:00:00:00
	    Calendar startTid = Calendar.getInstance();
	    startTid.setTime(dag);
	    startTid.set(Calendar.HOUR_OF_DAY, 0);
	    startTid.set(Calendar.MINUTE, 0);
	    startTid.set(Calendar.SECOND, 0);
	    startTid.set(Calendar.MILLISECOND, 0);
	    
	    //Sätt sluttid till dagen efter vald dag med start 00:00:00:00
	    Calendar slutTid = Calendar.getInstance();
	    slutTid.setTime(dag);
	    slutTid.set(Calendar.DAY_OF_YEAR, slutTid.get(Calendar.DAY_OF_YEAR) + 1);
	    slutTid.set(Calendar.HOUR_OF_DAY, 0);
	    slutTid.set(Calendar.MINUTE, 0);
	    slutTid.set(Calendar.SECOND, 0);
	    slutTid.set(Calendar.MILLISECOND, 0);
	    
	    //Sök all sänt data under en dag
	    return qp.processQuery("SELECT KANAL, COUNT(*) as ANTAL FROM MEDDELANDE "
	    		+ "WHERE SANTTIDPUNKT >= " + DAOImplBase.quoteValue(startTid.getTime()) 
	    		+ " AND SANTTIDPUNKT < " + DAOImplBase.quoteValue(slutTid.getTime()) 
	    		+ " GROUP BY KANAL", new StatistikradRowMapper());
	}
	
	@SuppressWarnings("unchecked")
	private List<Statistikrad> getMottagnaSedan(Date tid) {
	    if (tid == null) {
	        return qp.processQuery("SELECT KANAL, COUNT(*) as ANTAL FROM MEDDELANDE "
	        		+ "WHERE SKAPADTIDPUNKT IS NOT NULL GROUP BY KANAL", new StatistikradRowMapper());
	    }
	    return qp.processQuery("SELECT KANAL, COUNT(*) as ANTAL FROM MEDDELANDE "
	    		+ "WHERE SKAPADTIDPUNKT >= " + DAOImplBase.quoteValue(tid) + " GROUP BY KANAL", new StatistikradRowMapper());
	}
	
	/**
	 * Hämtar alla mottagna meddelanden grupperade per kanal för angivet datum, dsv under aktuell dag.
	 * Skulle dag vara null kommer dagens datum att användas. 
	 * @param dag den dag som statistik ska hämas för
	 * @return alla meddelanden som mottagits under angiven dag.
	 */
	@SuppressWarnings("unchecked")
	private List<Statistikrad> getMottagnaUnderEnDag(Date dag) {
	    if (dag == null) {
	        dag = getDagensDatum();
	    }
	    
	  //Sätt starttid till vald dag med start 00:00:00:00
	    Calendar startTid = Calendar.getInstance();
	    startTid.setTime(dag);
	    startTid.set(Calendar.HOUR_OF_DAY, 0);
	    startTid.set(Calendar.MINUTE, 0);
	    startTid.set(Calendar.SECOND, 0);
	    startTid.set(Calendar.MILLISECOND, 0);
	    
	    //Sätt sluttid till dagen efter vald dag med start 00:00:00:00
	    Calendar slutTid = Calendar.getInstance();
	    slutTid.setTime(dag);
	    slutTid.set(Calendar.DAY_OF_YEAR, slutTid.get(Calendar.DAY_OF_YEAR) + 1);
	    slutTid.set(Calendar.HOUR_OF_DAY, 0);
	    slutTid.set(Calendar.MINUTE, 0);
	    slutTid.set(Calendar.SECOND, 0);
	    slutTid.set(Calendar.MILLISECOND, 0);
	    
	    return qp.processQuery("SELECT KANAL, COUNT(*) as ANTAL FROM MEDDELANDE "
	    		+ "WHERE SKAPADTIDPUNKT >= " + DAOImplBase.quoteValue(startTid.getTime())
	    		+ " AND SKAPADTIDPUNKT < " + DAOImplBase.quoteValue(slutTid.getTime()) 
	    		+ " GROUP BY KANAL", new StatistikradRowMapper());
	}
	
	@SuppressWarnings("unchecked")
	private List<Statistikrad> getBorttagnaTotalt() {
	    return qp.processQuery("SELECT KANAL, COUNT(*) as ANTAL FROM MEDDELANDE "
	    		+ "WHERE STATUS=" + MeddelandeHandelse.BORTTAGET + " GROUP BY KANAL", new StatistikradRowMapper());
	}
	
	
	
		
	public void sok(ActionEvent e) {
	    // Sök ut datum etc
	    Date from = DateUtils.strToDate(startdatum, true);
	    Date tom = DateUtils.strToDate(slutdatum, false);
        String where = "";
        // Lägg till datumvillkor
        where = DAOImplBase.addRestriction(where, "M.SKAPADTIDPUNKT", ">=", from);
        where = DAOImplBase.addRestriction(where, "M.SKAPADTIDPUNKT", "<=", tom);

	    String sql = "SELECT COUNT(*) AS ANTAL, DATUM, INSTANS, STATUS FROM ( " +
	        "SELECT SUBSTR(TO_CHAR(M.SKAPADTIDPUNKT, 'YYYY-MM-DD HH24:MI:SS'), 1, 10) AS DATUM, " +
	    	"H.INSTANS AS INSTANS, H.TYP AS STATUS FROM MEDDELANDE M LEFT OUTER JOIN HANDELSE H " +
	    	"ON M.ID = H.MEDDELANDEID ";
	    if (where.length() > 0) {
	        sql += " WHERE " + where;
	    }
	    sql += ") AS DATUM GROUP BY DATUM, INSTANS, STATUS";
		log.debug("SQL: " + sql);
		List<Datumrad> rader = qp.processQuery(sql, new DatumradRowMapper());
		datumrader.setWrappedData(rader);
	}
	
	public void uppdatera(ActionEvent e) {
		log.info("uppdatera");
		hamtaStatistik();
	}
		
	public int getLevandeProcesser() {
	    return qp.getInt("SELECT COUNT(*) FROM STATUS WHERE STATUS NOT IN (" 
	            + SkickaMeddelandeStateMachine.STOPPING + "," + SkickaMeddelandeStateMachine.STOPPED + ")", -1);
	}
	
	public String getLangstaLivstid() {
	    String sql = "SELECT STARTAD FROM STATUS WHERE STATUS NOT IN (" 
	    		+ SkickaMeddelandeStateMachine.STOPPING + "," + SkickaMeddelandeStateMachine.STOPPED + ") "
	    		+ "ORDER BY STARTAD ASC";
	    List starttider = qp.processQuery(sql, new StartadRowMapper());
        if (starttider.size() == 0) {
        	return ""; 
        }
	    Date d = (Date) starttider.get(0);
	    return DateUtils.sekunderTillTidStrang((int) ((System.currentTimeMillis() - d.getTime()) / 1000));
	}

	public String getGenomsnittligLivstid() {
	    String sql = "SELECT STARTAD FROM STATUS WHERE STATUS NOT IN (" 
	            + SkickaMeddelandeStateMachine.STOPPING + "," + SkickaMeddelandeStateMachine.STOPPED + ")";
	    List starttider = qp.processQuery(sql, new StartadRowMapper());
	    if (starttider.size() == 0) {
	        return "";
	    }
	    
	    long summaMillis = 0;
	    long nu = System.currentTimeMillis();
	    for (Iterator it = starttider.iterator(); it.hasNext();) {
	        Date d = (Date) it.next();
	        summaMillis += nu - d.getTime(); 
	    }
	    int sekunder = (int) ((summaMillis / 1000) / starttider.size());
	    
	    // Formatera sekunderna till en tilltalande sträng:
	    return DateUtils.sekunderTillTidStrang(sekunder);
	}
	
	public int getAntalProcesser() {
	    return qp.getInt("SELECT COUNT(*) FROM STATUS", -1);
	}
	
	public Statistik getMottagna() {
		if (mottagna == null) {
			hamtaStatistik();
		}
		return mottagna;
	}
	
	public Statistik getSant() {
		if (sant == null) {
			hamtaStatistik();
		}
	    return sant;
	}
	
	public Statistik getBorttagna() {
		if (borttagna == null) {
			hamtaStatistik();
		}
		return borttagna;
	}
	
	public boolean getFinnsKanalerMottagna() {
		if (kanalerMottagna == null) {
			hamtaStatistik();
		}
		return kanalerMottagna.size() > 0;
	}
	
	public ListDataModel<Statistik> getKanalerMottagna() {
		if (kanalerMottagna == null) {
			hamtaStatistik();
		}
		return new ListDataModel<Statistik>(kanalerMottagna);
	}
	
	public boolean getFinnsKanalerSant() {
		if (kanalerSant == null) {
			hamtaStatistik();
		}
		return kanalerSant.size() > 0;
	}
	
	public ListDataModel<Statistik> getKanalerSant() {
		if (kanalerSant == null) {
			hamtaStatistik();
		}
		return new ListDataModel<Statistik>(kanalerSant);
	}
	
	public boolean getFinnsKanalerBorttagna() {
		if (kanalerBorttagna == null) {
			hamtaStatistik();
		}
		return kanalerBorttagna.size() > 0;
	}
	
	public ListDataModel<Statistik> getKanalerBorttagna() {
		if (kanalerBorttagna == null) {
			hamtaStatistik();
		}
		return new ListDataModel<Statistik>(kanalerBorttagna);
	}
	
	
	public ListDataModel<Datumrad> getDatumrader() {
		return datumrader;
	}
	public void setDatumrader(ListDataModel<Datumrad> datumrader) {
		this.datumrader = datumrader;
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
    
    public String getValdDag() {
		return valdDag;
	}
    
    public void setValdDag(String valdDag) {
		this.valdDag = valdDag;
	}
      
    /**
     * Returnerar föregående dag i kalendern baserat på attributet valdDag.
	 * 
     * @return föregående dag i kalendern för attributet valdDag.
     */
    public String getForegaendeDag() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(getDatumSomDate(valdDag));
    	cal.add(Calendar.DAY_OF_YEAR, -1);
    	return getDatumSomString(cal.getTime());
    }
    
    /**
     * Returnerar nästa dag i kalendern baserat på attributet valdDag.
	 * 
     * @return nästa dag i kalenderna för attributet valdDag.
     */
    public String getNastaDag() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(getDatumSomDate(valdDag));
    	cal.add(Calendar.DAY_OF_YEAR, 1);
    	return getDatumSomString(cal.getTime());
    }
    
    /**
     * Returnerar inskickat datum som en formaterad datumsträng på formen yyyy-MM-dd. Skulle 
     * datum vara null kommer dagens datum att användas.
     * @param datum det datum som ska formteras. Felaktigt datum eller null ger dagens datum.
     * @return datum formaterat enligt yyyy-MM-dd.
     */
    private String getDatumSomString(final Date datum) {
    	if (datum == null) {
    		return getDatumSomString(getDagensDatum());
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat(DATUMFORMAT);
    	return sdf.format(datum);
    }
    
    /**
     * Returnerar inskickad datumsträng som en Date. Skulle datumstängen vara felaktigt eller 
     * null kommer dagens datum att returneras. 
     * @param datum på formen yyyy-MM-dd. Felaktigt format eller null ger dagens datum.
     * @return inskickat datum som en Date.
     */
    private Date getDatumSomDate(final String datum) {
    	SimpleDateFormat sdf = new SimpleDateFormat(DATUMFORMAT);
    	Date parsatDatum = null;
    	try {
    		parsatDatum = sdf.parse(datum);
    	} catch (ParseException parseEx) {
    		parsatDatum = getDagensDatum();
    	}
    	return parsatDatum;
    }
    
   
    /**
     * Returnerar dagens datum.
     * @return dagens datum.
     */
    public Date getDagensDatum() {
    	Calendar dagensDatum = Calendar.getInstance();
    	return dagensDatum.getTime();
    }
}
