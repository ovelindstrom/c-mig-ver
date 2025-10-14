/**
 * Skapad 2007-mar-26
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.mail;

/**
 * Interface för mailservice.  
 */
public interface MailService {
    
    public void sendMessage(MailMessage msg);
    public MailMessage[] getMessages(String foldername);
    
    public MailMessage createEmptyMessage();
    

}
