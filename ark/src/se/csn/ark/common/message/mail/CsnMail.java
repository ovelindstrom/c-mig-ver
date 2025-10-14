/*
 * Created on 2005-feb-22
 *
 */
package se.csn.ark.common.message.mail;

import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.Log;

import javax.mail.Session;
import javax.mail.Transport;

/**
 * @author Magnus Storsjö
 * @since 20050223
 * @version 1 skapad
 *
 * Denna klass innehåller
 * Hämtar upp properties för att skicka mail och skapar och returnerar ett mimemessage.
 *
 */
public final class CsnMail {
    private static Log log = Log.getInstance(CsnMail.class);
	private static String mailserver = null;
	private static String mailuser = null;
	private static String mailpassword = null;
	private static String mailfrom = null;
	
	private static java.util.Properties props = null;
    /**
     * Instans ska ej skapas, private konstruktor skyddar detta 
     */
    private CsnMail() {
/*
		mailserver = Properties.getProperty("dao.mail.server");
		mailuser = Properties.getProperty("dao.mail.user");
		mailpassword = Properties.getProperty("dao.mail.password");
		mailfrom = Properties.getProperty("dao.mail.from");

		props = System.getProperties();
		
		props.put("mail.smtp.host", mailserver);
		props.put("user", mailuser);
		props.put("pwd", mailpassword);
*/
   }
    
    
	private static void init() {

		mailserver = Properties.getProperty("dao.mail.server");
		mailuser = Properties.getProperty("dao.mail.user");
		mailpassword = Properties.getProperty("dao.mail.password");
		mailfrom = Properties.getProperty("dao.mail.from");

		props = System.getProperties();
		
		props.put("mail.smtp.host", mailserver);
		props.put("user", mailuser);
		props.put("pwd", mailpassword);
		
	}

    /**
     * Skapar ett initierat MailMessage, som saknar epost adress, rubrik och text. 
    * @return MailMessage
    * @throws MailException Om något gick fel vid itoeringen 
    */
   public static synchronized MailMessage getMessage() throws MailException {

        MailMessage mailMessage = null;

		if (props == null) {
			init();
		}
		
        try {

            Session emailsession = Session.getDefaultInstance(props, null);

            mailMessage = new MailMessage(emailsession);
            mailMessage.setFrom(new javax.mail.internet.InternetAddress(mailfrom));
        } catch (Exception e) {
            log.error("Kunde ej skapa MailMessage", e);
            throw new MailException("Kunde ej skapa MailMessage", e);
        }

        return mailMessage;
    }




    /**
     * Skicka epostmeddelande till mail servern.
    * @param mailMessage Det initierade meddelande som skall skickas.
    * @throws MailException Om det av mågon anledning inte gick att skicka via transportlagret
    */
   public static synchronized void sendMessage(MailMessage mailMessage)
                            throws MailException {
        try {
            Transport.send(mailMessage);
        } catch (Exception e) {
            String logStr = "Mailet [" + mailMessage + "] gick inte skicka.";

            log.error(logStr);
            throw new MailException(logStr, e);
        }
    }
}