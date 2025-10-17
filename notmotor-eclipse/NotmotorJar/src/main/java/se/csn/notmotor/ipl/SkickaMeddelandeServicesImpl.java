package se.csn.notmotor.ipl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import se.csn.ark.common.dal.db.DatabaseException;
import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.db.ControlledCommitQueryProcessor;
import se.csn.notmotor.ipl.db.DAOHandelse;
import se.csn.notmotor.ipl.db.DAOImplBase;
import se.csn.notmotor.ipl.db.DAOMeddelande;
import se.csn.notmotor.ipl.db.DAOMottagare;
import se.csn.notmotor.ipl.db.ParameterKalla;
import se.csn.notmotor.ipl.model.Kanal;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.SandResultat;

/**
 * .
 *
 */
public class SkickaMeddelandeServicesImpl extends MeddelandeServicesImplBase implements SkickaMeddelandeServices {

	private List<MeddelandeSender> sandare;
	private DAOHandelse daohandelse;
	private DAOMottagare daomottagare;
	private Log log = Log.getInstance(SkickaMeddelandeServicesImpl.class);
	private CallbackClient callbackClient;

	public SkickaMeddelandeServicesImpl(ControlledCommitQueryProcessor qp, ParameterKalla paramSource, 
	        DAOMeddelande daomeddelande, DAOHandelse daohandelse, DAOMottagare daomottagare, int instansnummer) {
	    
	    super(qp, paramSource, daomeddelande, instansnummer);
	    if (daohandelse == null) {
	        throw new IllegalArgumentException("HandelseHandler får inte vara null");
	    }
	    sandare = new ArrayList<MeddelandeSender>();
	    this.daohandelse = daohandelse;
	    this.daomottagare = daomottagare;
	    callbackClient = new CallbackClient();
	}
	
	public void addMeddelandeSender(MeddelandeSender sender) {
	    sandare.add(sender);
	}
	public void removeMeddelandeSender(MeddelandeSender sender) {
	    sandare.remove(sender);
	}
	
	/**
	 * 
	 * @return true om minst ett mail skickades
	 */
	public boolean skickaMeddelande() {
	    // OBS! Kom ihåg att watchdog-timern måste sättas; vi anropar den setter-koden 
	    // hela tiden. Den cachar ju och gör inga onödiga databasslagningar. 
		// Läs max så många meddelanden som anges i maxMeddelanden
	    
	    // Ta ut connection
	    // Set autocommit false
	    // Uppdatera watchdog
	    // Kör: Select for update på x antal meddelanden
	    // Om inga meddelanden: returnera false
	    // Uppdatera watchdog
	    // Loopa:
	    	// Sänd meddelande
	    	// uppdatera watchdog
	    	// Uppdatera db
	    	// uppdatera watchdog
	    
	    // Sätt om status på misslyckade meddelanden:
	    
	    try {
		    Connection conn = qp.getConnection();
		    boolean foundMessages = false;
		    int batchStorlek = paramSource.getIntParam("BATCHSTORLEK", DEFAULT_BATCHSTORLEK);
		    try {
		    
			    updateWatchdogFlag();
			    conn.commit();
			    // Starta transaction scope:
			    qp.setCommitConnection(conn, false);
			    
			    long startTid = System.currentTimeMillis();
			    
			    // Initiera nedprioriterade inkanaler med begränsningar
			    List<Kanal> kanalerMedBegransningar = new ArrayList<Kanal>();
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
			    		if (!kanal.isSovande()) {
			    			// Om vi har sovit men nyligen vaknat så nollställer vi sov-tidsstämpeln i parameterkällan.
			    			paramSource.setStringParam(kanal.getSoverTimestampKey(), null);
			    		}
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
			    
			    daomeddelande.markeraMeddelandenForInstans(instansnummer, batchStorlek, kanalerMedBegransningar);
			    
			    // Uppdatera parametrar för kanal
			    for (Kanal kanal : kanalerMedBegransningar) {
			    	if (!kanal.isSovande() && kanal.getBatchStorlek() > 0) {
		    			// Subtrahera antalet markerade meddelanden från aktuell batch
		    			paramSource.setIntParam(kanal.getBatchKvarKey(), kanal.getBatchKvar() - kanal.getAntalMarkerade());
			    		if (paramSource.getIntParam(kanal.getBatchKvarKey(), kanal.getBatchStorlek()) <= 0) {
			    			// Om hela batchen nu har behandlats, vänta 'sovtid' innan ny batch påbörjas
			    			paramSource.setLongParam(kanal.getSoverTimestampKey(), new Date().getTime());
			    			paramSource.setIntParam(kanal.getBatchKvarKey(), kanal.getBatchStorlek());
			    		}
			    	}
			    }
			    
			    long stoppTid = System.currentTimeMillis();
			    List meddelanden = daomeddelande.getMarkeradeMeddelanden(instansnummer);
			    long stoppTid2 = System.currentTimeMillis();
	            
			    foundMessages = meddelanden.size() > 0;
				if (log.isInfoEnabled() && foundMessages) {
					Map<String, Integer> kanaler = new HashMap<String, Integer>();
				    StringBuffer sb = new StringBuffer();
				    for (Iterator it = meddelanden.iterator(); it.hasNext();) {
				        Meddelande meddelande = (Meddelande) it.next();
				        sb.append("," + meddelande.getId().longValue());
				        String kanal = meddelande.getKanal() != null ? meddelande.getKanal() : "";
				        if (kanal.length() > 0) {
				        	if (!kanaler.containsKey(kanal)) {
				        		kanaler.put(kanal, 0);
				        	}
				        	kanaler.put(kanal, kanaler.get(kanal) + 1);
				        }
				    }
				    // TODO: logga antal per kanal ...
				    log.debug("Hittade " + meddelanden.size() + " meddelanden: " + sb);
				    log.info("Hittade " + meddelanden.size() + " meddelanden att skicka");
				    long tid = stoppTid - startTid;
				    long tid2 = stoppTid2 - startTid;
	                log.info("Tid för att markera och hämta meddelanden: " + tid2 + " ms (varav " + tid + " ms för att markera).");
				}
			    updateWatchdogFlag();
			    conn.commit();
			    			    
				// Skicka meddelandena mha meddelandesenders:
				for (Iterator it = meddelanden.iterator(); it.hasNext();) {
				    MeddelandeHandelse handelse = null;
				    Meddelande meddelande = (Meddelande) it.next();
				    long id = meddelande.getId().longValue();
				    if (forMangaSandningar(meddelande)) {
				        continue;
				    }
				    if (nyligenSantMedTeknisktFel(meddelande)) {
				        continue;
				    }
				    List sandresultat = skickaMeddelandeTillSandare(meddelande); 
				    
				    // Om det inte finns några sändresultat i listan så har inte 
				    // meddelandet sänts; det fanns inga sändare som kunde hantera det.
					if (sandresultat.size() == 0) {
					    log.warn("Meddelandet saknar leveranstyp: " + meddelande);
					    handelse = new MeddelandeHandelse(MeddelandeHandelse.MEDDELANDEFEL,
					            						MeddelandeHandelse.FELAKTIG_MOTTAGARE,
					            						"Meddelandet saknar leveranstyp");
					} else {
					// Loopa över sändresultaten; uppdatera varje mottagare med resp. status
					    handelse = new MeddelandeHandelse(MeddelandeHandelse.SKICKAT_SERVER, MeddelandeHandelse.OK, null);
					    for (Iterator resit = sandresultat.iterator(); resit.hasNext();) {
					        SandResultat sr = (SandResultat) resit.next();
					        Mottagare[] mott = sr.getMottagare();
					        for (int mottloop = 0; mottloop < mott.length; mottloop++) {
					            mott[mottloop].setStatus(new Integer(sr.getHandelsetyp()));
					            daomottagare.updateMottagare(mott[mottloop]);
					        }
					        // Uppdatera status. Fel har prioritet över SKICKAT_SERVER
					        if (sr.getHandelsetyp() != MeddelandeHandelse.SKICKAT_SERVER) {
					            handelse.setHandelsetyp(new Integer(sr.getHandelsetyp()));
					            handelse.setFelkod(new Integer(sr.getKod()));
					            handelse.setFeltext(sr.getText());
					        }
					    }
					}

					handelse.setInstans(new Integer(instansnummer));
					meddelande.addHandelse(handelse);
					daohandelse.createHandelse(handelse, id, conn);
					
					updateWatchdogFlag();
					conn.commit();
					
					// Om skickat, sätt skickatdatum
					if (handelse.getHandelsetyp().intValue() == MeddelandeHandelse.SKICKAT_SERVER) {
					    qp.executeThrowException("UPDATE MEDDELANDE SET STATUS=" + MeddelandeHandelse.SKICKAT_SERVER 
					            + ",SANTTIDPUNKT=" + DAOImplBase.quoteValue(new Date()) + " WHERE ID=" + id);
					} // Om meddelandefel, sätt status till meddelandefel 
					else if (handelse.getHandelsetyp().intValue() == MeddelandeHandelse.MEDDELANDEFEL) {
					    qp.executeThrowException("UPDATE MEDDELANDE SET STATUS=" + MeddelandeHandelse.MEDDELANDEFEL 
					            + " WHERE ID=" + id);
					} // Om tekniskt fel och felkod är STOPPANDE_FEL, sätt status tekniskt fel
					else if ((handelse.getHandelsetyp().intValue() == MeddelandeHandelse.TEKNISKT_FEL)
							&& (handelse.getFelkod().intValue() == MeddelandeHandelse.STOPPANDE_FEL)) {
					    qp.executeThrowException("UPDATE MEDDELANDE SET STATUS=" + MeddelandeHandelse.TEKNISKT_FEL 
					            + " WHERE ID=" + id);
					} // else: Om tekniskt fel och inte stoppande: försök sända om.

					conn.commit();
					if (log.isDebugEnabled()) {
					    log.debug("Skickat meddelande, status " + handelse.getFelkod() + ":" 
					            + getMeddelandeSummary(meddelande));
					}
					
					// Rapportera händelsen till callbackinterface:
					if (paramSource.getBooleanParam("CALLBACKS", false)) {
					    makeCallback(meddelande);
					}
				}
		    } catch (DatabaseException de) {
	            log.error("Fångat DatabaseException", de);
	            if (conn != null) {
	                try {
	                    conn.rollback();
	                } catch (SQLException e2) {
	                    log.error("Försökte köra rollback men misslyckades", e2);
	                }
	            }
	        } catch (SQLException e) {
	            log.error("Fångat SQLException", e);
	            if (conn != null) {
	                try {
	                    conn.rollback();
	                } catch (SQLException e2) {
	                    log.error("Försökte köra rollback men misslyckades", e2);
	                }
	            }
	        } finally {
	            qp.setCommitConnection(conn, true);
	        }
	        return foundMessages;
	    } catch (Exception e) {
	        log.error("skickaMeddelande: Fångat throwable, returnerar FALSE", e);
	        return false;
	    }
	}
	
	/**
	 * Metod som kontrollerar om antal sändningar har överskridit en viss gräns, MAXSANDFORSOK.
	 * Sätter status till TEKNISKT_FEL om gränsen överskridits.
	 * max antal sändningsförsök styrs av parametern MAXSANDFORSOK.
	 * @param meddelande DTOMeddelande
	 * @return true om meddelandet har försökt sändas för många gånger,
	 * 			annars false.
	 */
	boolean forMangaSandningar(Meddelande meddelande) {
	    // Kolla om det finns händelser:
	    if ((meddelande.getHandelser() == null) || (meddelande.getHandelser().length == 0)) {
	        return false;
	    }

	    // Kolla om det finns för många händelser:
	    int maxSandForsok = paramSource.getIntParam("MAXSANDFORSOK", 150);
	    if (meddelande.getHandelser().length > maxSandForsok) {
	        MeddelandeHandelse h = new MeddelandeHandelse(MeddelandeHandelse.TEKNISKT_FEL, MeddelandeHandelse.OKANT_FEL, "Satte status till TEKNISKT_FEL pga för många sändningsförsök");
	        h.setInstans(new Integer(instansnummer));
	        meddelande.addHandelse(h);
	        daohandelse.createHandelse(h, meddelande.getId().longValue());
	        log.error("Sätter status för meddelande " + meddelande.getId().longValue() + " till TEKNISKT_FEL pga för många händelser");
	        qp.executeThrowException("UPDATE MEDDELANDE SET STATUS = " + MeddelandeHandelse.TEKNISKT_FEL + " WHERE ID = " + meddelande.getId().longValue());
	        try {
	            qp.getConnection().commit();
	        } catch (SQLException e) {
	        	throw new DatabaseException("Kunde inte commita", e);
	        }
	        makeCallback(meddelande);
	        return true;
	    }
	    return false;
	}
	/**
	 * Metod som kollar om meddelandet nyligen sänts med tekniskt fel. 
	 * Tid till senaste händelse styrs av parametern MINTIDTILLSENASTEFEL; 
	 * @param meddelande DTOMeddelande
	 * @return true om meddelandet "nyligen" sändes med resultatet TEKNISKT_FEL
	 * 		och då väntar vi med att skicka det till nästa tick,
	 * 		annars false vilket innebär att det kommer att försöka sändas igen på en gång.
	 */
	boolean nyligenSantMedTeknisktFel(Meddelande meddelande) {
	    // Kolla om det finns händelser:
	    if ((meddelande.getHandelser() == null) || (meddelande.getHandelser().length == 0)) {
	        return false;
	    }
	    // Kolla om senaste händelsen är ett tekniskt fel:
	    // I så fall kontrolleras min. tid till senaste fel
	    MeddelandeHandelse handelse = meddelande.getHandelser()[meddelande.getHandelser().length - 1];
	    if (handelse.getHandelsetyp().intValue() != MeddelandeHandelse.TEKNISKT_FEL) {
	        return false;
	    }
	    
	    // kolla om senaste händelse ligger tillräckligt långt bort i tiden:
	    int minTidTillSenasteFel = paramSource.getIntParam("MINTIDTILLSENASTEFEL", 600);
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.SECOND, -minTidTillSenasteFel);
	    return handelse.getTidpunkt().after(cal.getTime());
	}
	
	/**
	 * Gör callbackanrop om sådana efterfrågats. 
	 * @param meddelande DTOMeddelande
	 * @param handelse DTOMeddelandeHandelse
	 */
	void makeCallback(Meddelande meddelande) {
	    if (log.isDebugEnabled()) {
	        log.debug("Gör callback");
	    }
	    
	    MeddelandeHandelse handelse = meddelande.getHandelser()[meddelande.getHandelser().length - 1];
	    if (handelse == null) {
	        return;
	    }
	    if ((meddelande.getCallbackURL() == null) || (meddelande.getCallbackURL().length() == 0)) {
	        log.debug("Ingen callback-URL: ignorerar");
	        return;
	    }
	    if (meddelande.getCallbackMask() == null) {
	        log.debug("Ingen callback-mask: ignorerar");
	        return;
	    }
	    int mask = meddelande.getCallbackMask().intValue();
	    if ((handelse.getHandelsetyp().intValue() & mask) == 0) {
	        log.debug("Fanns callback-URL och -mask, men inget intresse för denna händelse");
	        return;
	    }
	    
	    // Plocka upp callback-sändare: 
	    callbackClient.rapporteraHandelseWS(meddelande);
	}
	
	/**
	 * 
	 * @param meddelande
	 * @return En lista av sändresultat, som sedan ska bearbetas till databas. 
	 */
	List skickaMeddelandeTillSandare(Meddelande meddelande) {
	    List<SandResultat> list = new ArrayList<SandResultat>();
	    for (Iterator it = sandare.iterator(); it.hasNext();) {
	        MeddelandeSender sender = (MeddelandeSender) it.next();
            log.debug("Testar " + sender.getClass().getName());
	        if (sender.kanSkickaMeddelande(meddelande)) {
	            log.debug("Sender " + sender.getClass().getName() + " hittad.");
	            SandResultat sr = sender.skickaMeddelande(meddelande);
	            log.debug("Resultat: " + sr);
	            if (sr != null) {
	                list.add(sr);
	            }
	        }
	    }
	    return list;
	}

}
