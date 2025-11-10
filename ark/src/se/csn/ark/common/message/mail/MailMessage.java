/*
 * Created on 2005-feb-23
 *
 */
package se.csn.ark.common.message.mail;

import se.csn.ark.common.message.CsnMessage;

import java.io.InputStream;

import java.util.Date;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;


/**
 * Namn : MailMessage <br>
 *
 * Beskrivning: Wrapper för MimeMessage för att förenkla skickande
 * av epostmeddelanden.
 * @author CSN7504
 * @version 1.01
 *
 */
public class MailMessage extends MimeMessage implements CsnMessage {
    /**
     * @param arg0 session
     */
    public MailMessage(Session arg0) {
        super(arg0);
    }




    /**
     * @param arg0 session
     * @param arg1 The message input stream
     * @throws javax.mail.MessagingException ?
     */
    public MailMessage(Session arg0, InputStream arg1)
                throws MessagingException {
        super(arg0, arg1);
    }




    /**
     * @param arg0 source - the message to copy content from 
     * @throws javax.mail.MessagingException ?
     */
    public MailMessage(MimeMessage arg0) throws MessagingException {
        super(arg0);
    }




    /**
     * @param arg0 Folder
     * @param arg1 Message number of this message within its folder
     */
    public MailMessage(Folder arg0, int arg1) {
        super(arg0, arg1);
    }




    /**
     * @param arg0 Folder 
     * @param arg1 The message input stream
     * @param arg2 Message number of this message within its folder
     * @throws javax.mail.MessagingException ?
     */
    public MailMessage(Folder arg0, InputStream arg1, int arg2)
                throws MessagingException {
        super(arg0, arg1, arg2);
    }




    /**
     * @param arg0 Folder
     * @param arg1 InternetHeaders
     * @param arg2 The message content
     * @param arg3 Message number of this message within its folder 
     * @throws javax.mail.MessagingException ?
     */
    public MailMessage(
                       Folder arg0,
                       InternetHeaders arg1,
                       byte[] arg2,
                       int arg3) throws MessagingException {
        super(arg0, arg1, arg2, arg3);
    }

    /**
    * @param rubrik Rubrik på epostmeddelande
    * @param meddelande Brödtexten i meddelandet
    * @throws MailException Mailinnehåll gick inte att sätta av nogon orsak
    */
   public void setMailContent(String rubrik, String meddelande)
                        throws MailException {
       Integer errorCode = null;
        try {
            errorCode = MailException.EPOSTRUBRIK_ERR;
            setSubject(rubrik, "ISO-8859-4");
            
            errorCode = MailException.EPOSTDATUM_ERR;
            setSentDate(new Date());
            
            errorCode = MailException.EPOSTMEDDELANDETEXT_ERR;
            setText(meddelande, "ISO-8859-4");
        } catch (Exception e) {
            throw new MailException(
                                    "Mailinnehåll gick inte att sätta. Följande fel fångades : " 
                                    + e.getMessage(),
                                    errorCode,
                                    e);
        }
    }




    /**
     * Sätter epostadress
    * @param mailadress Epostadress som meddelandet skall skickas till. 
    * @throws MailException misslyckades med att sätta epost adress
    */
   public void setMailAdress(String mailadress) throws MailException {
        try {
            InternetAddress[] address = {new InternetAddress(mailadress)};

            setRecipients(Message.RecipientType.TO, address);
        } catch (Exception e) {
            throw new MailException(
                        "Mailadress (" + mailadress 
                        + ") gick inte att sätta. Följande fel fångades : "
                        + e.getMessage(),
                        MailException.FELAKTIGT_ADRESSFORMAT,
                        e);
        }
    }

    /**
     * Sätter Avsändarens epostadress (from)
     * @param mailadress Epostadress som meddelandet skall ha som avsändare. 
     * @throws MailException misslyckades med att sätta epost fromadress
     */
    public void setMailFromAdress(String mailadress) throws MailException {
        try {
            InternetAddress address = new InternetAddress(mailadress);
            setFrom(address);
            
        } catch (Exception e) {
            throw new MailException(
                        "MailFromAdress gick inte att sätta. Följande fel fångades : "
                        + e.getMessage(),
                        MailException.FELAKTIGT_ADRESSFORMAT,
                        e);
        }
}


    /**
     * Sträng representation av objektet.
    * @see java.lang.Object#toString()
    */
   public String toString() {
        String str = "";
        Address[] internetAdressFrom;
        Address[] internetAdressTo;

        try {
            str += ("subject <" + getSubject() + ">\n");
            str += ("content <" + getContent() + ">\n");
            str += "adress <";
            internetAdressTo = getRecipients(RecipientType.TO);

            for (int i = 0; i < internetAdressTo.length; i++) {
                str += internetAdressTo[i];
            }

            str += ">\n";
            str += "from <";
            internetAdressFrom = getFrom();

            for (int i = 0; i < internetAdressFrom.length; i++) {
                str += internetAdressFrom[i];
            }

            str += ">\n";
        } catch (Exception e) {
            ;
        }

        return str;
    }
}