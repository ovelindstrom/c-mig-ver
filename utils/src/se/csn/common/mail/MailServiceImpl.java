/**
 * Skapad 2007-mar-26
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.common.mail;


public class MailServiceImpl {

    private String user, password, serverUrl;
    
    public MailServiceImpl(String user, String password, String serverUrl) {
        this.user = user;
        this.password = password;
        this.serverUrl = serverUrl;
    }
    
}
