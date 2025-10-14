/**
 * Skapad 2007-mar-26
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.mail;

import java.util.List;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Wrapper runt MimeMessage; implementerar servicemetoder för 
 * Mime-meddelandet 
 */
public class MailMessage {

    private List bilagor;
    private String text, rubrik, textencoding, rubrikencoding;
    private Session session;
    
    public MimeMessage getMimeMessage() {
        MimeMessage msg = new MimeMessage(session);
        
        /*
//      Define message
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, 
          new InternetAddress(to));
        message.setSubject("Hello JavaMail Attachment");

//         Create the message part 
        BodyPart messageBodyPart = new MimeBodyPart();

//         Fill the message
        messageBodyPart.setText("Pardon Ideas");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

//         Part two is attachment
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);

//         Put parts in message
        message.setContent(multipart);

//         Send the message
        Transport.send(message);        
        */
        return msg;
    }
    
    public MailMessage(Session session) {
        if(session == null) {
            throw new IllegalArgumentException("Session måste vara satt");
        }
        this.session = session;
    }
    
}
