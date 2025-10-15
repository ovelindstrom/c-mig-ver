/**
 * Skapad 2007-mar-23
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sun.mail.smtp.SMTPAddressFailedException;
import com.sun.mail.smtp.SMTPAddressSucceededException;
import com.sun.mail.smtp.SMTPSendFailedException;
import com.sun.mail.smtp.SMTPTransport;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javax.mail.util.ByteArrayDataSource;

import se.csn.ark.common.util.logging.Log;
import se.csn.common.config.CommunicationTester;
import se.csn.common.serializing.ObjectSerializer;
import se.csn.notmotor.ipl.model.Bilaga;
import se.csn.notmotor.ipl.model.KodText;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.SandResultat;
import se.csn.notmotor.ipl.validators.EpostValidator;

/**
 * Mailsändare.
 * TODO: problematisk klass, för mycket kod, för lite abstraktion.
 * @author Jonas åhrnell - csn7821
 */
public class EpostMeddelandeSenderImpl implements MeddelandeSender {

    private static final int ANTAL_OMSANDNINGSFORSOK = 3;
    
    private Log log = Log.getInstance(EpostMeddelandeSenderImpl.class);
    private Session session;
    private Transport transport;
    private String mailserver;
    private int mailserverport;
    private EpostValidator validator;
    
    public EpostMeddelandeSenderImpl(String user, String password, String mailserver, int mailserverport, int mailserverTimeout) {
        //csnmail1.csnnet.int
        if (user == null) {
            throw new IllegalArgumentException("user måste anges");
        }
        if (password == null) {
            throw new IllegalArgumentException("password måste anges");
        }
        if (mailserver == null) {
            throw new IllegalArgumentException("mailserver måste anges");
        }
        if ((mailserverport < 1) || (mailserverport > 65535)) {
            throw new IllegalArgumentException("port måsta ha ett värde mellan 1 och 65535");
        }
    	this.mailserver = mailserver;
    	this.mailserverport = mailserverport;
        long startTid = System.currentTimeMillis();
        if (CommunicationTester.isPortOpen(mailserver, mailserverport, mailserverTimeout)) {
        	long stoppTid = System.currentTimeMillis();
            if (log.isInfoEnabled()) {
                log.info(mailserver + ":" + mailserverport + " svarade efter "
                        + (((double) stoppTid - startTid) / 1000) + " sekunder");
            }
        } else {
        	log.fatal("Kunde inte ansluta till " + mailserver + ":" + mailserverport + " inom " + mailserverTimeout + "ms.");
            throw new IllegalStateException("Kunde inte ansluta till " + mailserver + ":" + mailserverport);
        }
        
        Properties props = new Properties();
        props.put("mail.smtp.host", mailserver);
        props.put("mail.smtp.port", "" + mailserverport);
        // Nedanstående är saxat ur CsnMail-klassen, finns ingen stöd för dessa 
        // properties i JavaMail
        props.put("user", user);
        props.put("pwd", password);
        session = Session.getDefaultInstance(props, null);
        try {
            transport = session.getTransport("smtp");
        } catch (NoSuchProviderException e) {
            throw new IllegalStateException("Kunde inte hitta smtp-provider");
        }
        TransportListener listener = new TransportListenerImpl();
        transport.addTransportListener(listener);
        validator = new EpostValidator();
    }
    
    
    /**
     * Skickar meddelande. 
     * @param meddelande Meddelandet. 
     * @return Ett sändresultat. Om meddelandet gick iväg till mailservern är 
     * 		koden i resultatet SKICKAT_SERVER; annars någon typ av fel.
     */
    public SandResultat skickaMeddelande(Meddelande meddelande) {
        // Arbeta med en lokal kopia, vi kommer att stycka upp meddelandet
        meddelande = (Meddelande) ObjectSerializer.deepClone(meddelande);
        
        if (!validator.isValid(meddelande)) {
            KodText kt = validator.getFelkodForMeddelande(meddelande);
            return new SandResultat(MeddelandeHandelse.MEDDELANDEFEL, kt.getKod(), kt.getText(), this, null);
        }

        //log.debug("Validerat");
        // Transformera: ta bort mottagare som inte är av typ EPOST,EPOSTCC eller EPOSTBCC:
        meddelande = taBortOkandaOchSandaMottagartyper(meddelande);
        // Om det inte finns några mottagare kvar så ska inte meddelandet hanteras ->
        // returnera null
        if (meddelande.getMottagare().length == 0) {
            return null;
        }
        
        // Skapa MimeMessage
        MimeMessage msg = null;
        try {	
	        // Felen upptäcks normalt inte här; bara om de är gravt felformaterade
	        try {
		        // Kolla om det finns bilagor. Om ja -> multipart
		        if (!meddelande.hasBilagor()) {
		            msg = createSimpleMessage(meddelande);
		        } else {
		            msg = createMultipartMessage(meddelande);
		        }
		        msg.addHeader("csnid", "" + meddelande.getId());
		        // Sätt text, rubrik
		        if (meddelande.getRubrikEncoding() == null) {
		            msg.setSubject(meddelande.getRubrik());
		        } else {
		            msg.setSubject(meddelande.getRubrik(), meddelande.getRubrikEncoding());
		        }
	        } catch (MessagingException me) {
	            log.error("Kunde inte skapa meddelande " + meddelande, me);
	            return skapaSandResultat(meddelande, MeddelandeHandelse.TEKNISKT_FEL, MeddelandeHandelse.OKANT_FEL, me.toString());
	        }
	        
	        // Skapa avsändare:
	        if (meddelande.getAvsandare() == null) {
	            throw new IllegalArgumentException("Avsändare saknas");
	        }
	        try {
	            msg.setFrom(new InternetAddress(meddelande.getAvsandare().getEpostadress(), meddelande.getAvsandare().getNamn()));
	        } catch (UnsupportedEncodingException e) {
	            log.error("Kunde inte skapa avsändare för " + meddelande, e);
	            return skapaSandResultat(meddelande, MeddelandeHandelse.MEDDELANDEFEL, MeddelandeHandelse.FELAKTIG_AVSANDARE, e.toString());
	        } catch (MessagingException e) {
	            log.error("Kunde inte skapa avsändare för " + meddelande, e);
	            return skapaSandResultat(meddelande, MeddelandeHandelse.MEDDELANDEFEL, MeddelandeHandelse.FELAKTIG_AVSANDARE, e.toString());
	        }
	        
	        Address[] adresser = null;
	        try {
	            // Skapa addressater:
	            adresser = laggTillMottagare(msg, meddelande.getMottagare());
	        } catch (UnsupportedEncodingException e) {
	            log.error("Kunde inte skapa mottagare för " + meddelande, e);
	            return skapaSandResultat(meddelande, MeddelandeHandelse.MEDDELANDEFEL, MeddelandeHandelse.FELAKTIG_MOTTAGARE, e.toString());
	        } catch (MessagingException e) {
	            log.error("Kunde inte skapa mottagare för " + meddelande, e);
	            return skapaSandResultat(meddelande, MeddelandeHandelse.MEDDELANDEFEL, MeddelandeHandelse.FELAKTIG_MOTTAGARE, e.toString());
	        }
	        
	        return skicka(meddelande, msg, adresser);
        } catch (Exception e) {
            log.error("Kunde inte skicka meddelande " + meddelande, e);
            return skapaSandResultat(meddelande, MeddelandeHandelse.TEKNISKT_FEL, MeddelandeHandelse.OKANT_FEL, e.toString());
        }
    }
    
    SandResultat skapaSandResultat(Meddelande m, int handelsetyp, int kod, String text) {
        SandResultat sr = new SandResultat(handelsetyp, kod, text, this, null);
        // Sätt mottagare
        if (m.getMottagare() != null) {
            for (int i = 0; i < m.getMottagare().length; i++) {
                sr.addMottagare(m.getMottagare()[i]);
            }
        }
        return sr;
    }
    
    
    /**
     * @see se.csn.notmotor.ipl.MeddelandeSender#getFelkodForMeddelande(se.csn.notmotor.ipl.model.Meddelande)
     */
    
    boolean matchandeTyp(String typ) {
        // EpostMeddelandeSendern är defaultsändare -> accepterar NULL
        if (typ == null) {
            return true;
        }
        final String kandaTyper = "EPOST EPOSTCC EPOSTBCC";
        String[] typer = typ.split(",");
        for (int typloop = 0; typloop < typer.length; typloop++) {
            if (kandaTyper.indexOf(typer[typloop]) >= 0) {
                return true;
            }
        }
        return false;
    }
    
    /** 
     * @see se.csn.notmotor.ipl.MeddelandeSender#kanSkickaMeddelande(se.csn.notmotor.ipl.model.Meddelande)
     */
    public boolean kanSkickaMeddelande(Meddelande meddelande) {
        Mottagare[] mott = meddelande.getMottagare();
        if (mott == null) { return false; }
        for (int i = 0; i < mott.length; i++) {
            if (matchandeTyp(mott[i].getTyp())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Skapar ett MimeMeddelande som inte har bilagor.
     * @throws MessagingException
     */
    MimeMessage createSimpleMessage(Meddelande meddelande) throws MessagingException {
        // Skapa MimeMessage
    	MimeMessage msg = new MimeMessage(session);
    	
    	if (meddelande.getMimetyp() == null) {
	    	if (meddelande.getMeddelandeEncoding() == null) {
	            msg.setText(meddelande.getMeddelandetext());
	        } else {
	            msg.setText(meddelande.getMeddelandetext(), meddelande.getMeddelandeEncoding());
	        }
    	} else {
        	// Mimetyp är inte null, t.ex. "text/html;charset=iso-8859-1"
        	// Observera att man sätter encoding (charset) i samma sträng.
    		msg.setContent((Object) meddelande.getMeddelandetext(), meddelande.getMimetyp());
        }
        return msg;
     }
    
    MimeMessage createMultipartMessage(Meddelande meddelande) throws MessagingException {

    	// Create the message part
        Multipart multipart = new MimeMultipart();
        MimeBodyPart bp = new MimeBodyPart();
        
        // text
        if (meddelande.getMimetyp() == null) {
	        if (meddelande.getMeddelandeEncoding() == null) {
	            bp.setText(meddelande.getMeddelandetext());
	        } else {
	            bp.setText(meddelande.getMeddelandetext(), meddelande.getMeddelandeEncoding());
	        }
        } else {
        	// Mimetyp är inte null, t.ex. "text/html;charset=iso-8859-1"
        	// Observera att man sätter encoding (charset) i samma sträng.
	        bp.setContent((Object) meddelande.getMeddelandetext(), meddelande.getMimetyp());
        }
        multipart.addBodyPart(bp);
        
        // attachment
        for (int i = 0; i < meddelande.getBilagor().length; i++) {
            Bilaga bilaga = meddelande.getBilagor()[i];
            bp = new MimeBodyPart();
            String mimetyp = bilaga.getMimetyp();
            if (mimetyp == null) {
                mimetyp = "application/octet-stream";
            }
            bp.setDataHandler(new DataHandler(new ByteArrayDataSource(bilaga.getData(), mimetyp)));
            bp.setFileName(bilaga.getFilnamn());
            multipart.addBodyPart(bp);
        }
        
        MimeMessage msg = new MimeMessage(session);
        msg.setContent(multipart);
        return msg;
    }
    
    /**
     * Går igenom alla mottagare och plockar bort okända 
     * mottagartyper och mottagare med status SKICKAT_SERVER. 
     * Om en mottagare bara har okända typer så plockas den bort.
     * @param meddelande
     * @return Samma meddelande som skickades in, förändrat. 
     */
    static Meddelande taBortOkandaOchSandaMottagartyper(Meddelande meddelande) {
        final String kandaTyper = "EPOST EPOSTCC EPOSTBCC";
        Mottagare[] mott = meddelande.getMottagare();
        List<Mottagare> kvarvarande = new ArrayList<Mottagare>(); 
        for (int i = 0; i < mott.length; i++) {
            String typ = mott[i].getTyp(); 
            if (typ != null) {
                String[] typer = mott[i].getTyp().split(",");
                for (int typloop = 0; typloop < typer.length; typloop++) {
                    if (kandaTyper.indexOf(typer[typloop]) == -1) {
                        typ = removePart(typ, typer[typloop], ",");
                    }
                }
            } else {
                typ = "EPOST";
            }
            if ((typ.length() > 0) && ((mott[i].getStatus() == null) 
                    || (mott[i].getStatus().intValue() != MeddelandeHandelse.SKICKAT_SERVER))) {
                mott[i].setTyp(typ);
                kvarvarande.add(mott[i]);
            }
        }
        meddelande.setMottagare((Mottagare[]) kvarvarande.toArray(new Mottagare[0]));
        return meddelande;
    }
    
    
    static String removePart(String s, String part, String sep) {
        if ((s == null) || (s.length() == 0)) {
            return s;
        }
        int pos = s.indexOf(part);
        if (pos >= 0) {
            if (s.length() == part.length()) {
                return "";
            } else if (pos + part.length() == s.length()) { // Kolla om i början eller i slutet:
                return s.replaceAll(sep + part, "");
            } else {
                // Minst två termer, och inte den sista: 
                return s.replaceAll(part + sep, "");
            } 
        }
        return s;
    }
    
    Address[] laggTillMottagare(Message message, Mottagare[] mottagarlista) throws UnsupportedEncodingException, MessagingException {
        if ((mottagarlista == null) || (mottagarlista.length == 0)) {
            throw new IllegalArgumentException("Måste finnas minst en mottagare");
        }
        
        List<Address> adresser = new ArrayList<Address>();
	    for (int i = 0; i < mottagarlista.length; i++) {
	        Mottagare mott = mottagarlista[i];
	        String mailadress = mott.getAdress();
	        if (mailadress == null) {
	            throw new IllegalArgumentException("Mottagare saknade adress: " + mott.toString());
	        }
	        
	        // Loopa över alla adresstyper. Om ingen satt, använd TO
	        Address a = new InternetAddress(mott.getAdress(), mott.getNamn());
	        adresser.add(a);
	        if (mott.getTyp() == null) {
	            message.addRecipient(Message.RecipientType.TO, a);
	        } else {
	            String[] typer = mott.getTyp().split(",");
	            for (int typnummer = 0; typnummer < typer.length; typnummer++) {
	                if ("EPOST".equalsIgnoreCase(typer[typnummer])) {
	                    message.addRecipient(Message.RecipientType.TO, a);
	                } else if ("EPOSTCC".equalsIgnoreCase(typer[typnummer])) {
	                    message.addRecipient(Message.RecipientType.CC, a);
	                } else if ("EPOSTBCC".equalsIgnoreCase(typer[typnummer])) {
	                    message.addRecipient(Message.RecipientType.BCC, a);
	                } else {
	                    throw new IllegalArgumentException("Okänd mottagartyp: " + typer[typnummer]);
	                }
	            }
	        }
	    }
		return (Address[]) adresser.toArray(new Address[0]);
    }
    
    /**
     * Skickar meddelande. 
     * @param antalForsok Flagga som används för att undvika oändliga loopar vid omedelbart
     * 		omsändningsförsök mm  
     */
    
    private SandResultat skicka(Meddelande m, MimeMessage msg, Address[] adresser, int antalForsok) {
        int maxAntalLokalaOmsandningsforsok = ANTAL_OMSANDNINGSFORSOK;
        try {
        	long startTid = System.currentTimeMillis();
            // Försök ansluta:
            if (!transport.isConnected()) {
                log.debug("Connecting.");
                transport.connect();
            }
            
            transport.sendMessage(msg, adresser);
            long stoppTid = System.currentTimeMillis();
            if (log.isInfoEnabled()) {
                double tid = ((double) stoppTid - startTid) / 1000;
                if (tid > 0.5) {
                    log.info("Tid för att skicka meddelande till " + mailserver + ":" + mailserverport
                            + ": " + tid + " sekunder (visar tider > 0.5 sek)");
                }
            }
            return skapaSandResultat(m, MeddelandeHandelse.SKICKAT_SERVER, MeddelandeHandelse.OK, null);
        } catch (SendFailedException e) {
       		log.error("SendFailed: ", e);
            
        	String errormessage = getSendFailedExceptionString(e);
        	if (errormessage != "") {
        		return skapaSandResultat(m, MeddelandeHandelse.MEDDELANDEFEL, MeddelandeHandelse.FELAKTIG_MOTTAGARE, errormessage);
            }
            
            SandResultat sr = skapaSandResultat(m, MeddelandeHandelse.TEKNISKT_FEL, 
            		MeddelandeHandelse.OKANT_FEL, "SendFailed: " + e.getMessage());
            if (antalForsok >= maxAntalLokalaOmsandningsforsok) {
                return sr;
            } else {
                return skicka(m, msg, adresser, antalForsok + 1);
            }
        } catch (MessagingException e) {
            log.info("MessagingException: " + e.getMessage());
            SandResultat sr = skapaSandResultat(m, MeddelandeHandelse.TEKNISKT_FEL, MeddelandeHandelse.OKANT_FEL, "MessagingException: " + e.getMessage());
            if (antalForsok >= maxAntalLokalaOmsandningsforsok) {
                return sr;
            } else {
                return skicka(m, msg, adresser, antalForsok + 1);
            }
        } catch (IllegalStateException e) {
            log.warn("IllegalState: " + e.getMessage());
            SandResultat sr = skapaSandResultat(m, MeddelandeHandelse.TEKNISKT_FEL, MeddelandeHandelse.OKANT_FEL, "IllegalState: " + e.getMessage());
            if (antalForsok >= maxAntalLokalaOmsandningsforsok) {
                return sr;
            } else {
                return skicka(m, msg, adresser, antalForsok + 1);
            }
        }
    }
    
    /*
     * private method to log the extended SendFailedException introduced in JavaMail \
                1.3.2.
     */
    private String getSendFailedExceptionString(SendFailedException sfe) {
    	Exception ne;
    	MessagingException me = sfe;
    	String msg="";
    	while ((ne = me.getNextException()) != null && ne instanceof MessagingException) {
    		me = (MessagingException)ne;
    		if (me instanceof SMTPAddressFailedException) {
    			SMTPAddressFailedException e = (SMTPAddressFailedException)me;
    			msg += " Misslyckades att skicka till: " + e.getAddress() + "  RetCode: " + e.getReturnCode() + ",  Response: " + e.getMessage();
    		} else if (me instanceof SMTPAddressSucceededException) {
    			SMTPAddressSucceededException e = (SMTPAddressSucceededException)me;
    			msg += " Lyckades skicka till: " + e.getAddress();
    		}
    	}
    	return msg;
    }
    
    private SandResultat skicka(Meddelande m, MimeMessage msg, Address[] adresser) {
        return skicka(m, msg, adresser, 0);
    }
    
}
