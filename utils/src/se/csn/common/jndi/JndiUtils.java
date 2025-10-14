/**
 * Skapad 2007-apr-11
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JndiUtils {

    /**
     * @param factoryClass Klass som ska användas för att skapa InitialContext
     * @param url Sökväg till serverinstans 
     * @param user Den security principal som ska användas
     * @param credentials Lösenord eller motsvarande
     * @return Ett InitialContext för att hämta resurser från en appserver
     */
  	public static Context getContextFromAppserver(String factoryClass, String url, String user, String credentials) {
  	    // testa att URL:en kan nås
  	    if((url == null) || (url.length() == 0)) {
  	        throw new IllegalArgumentException("URL måste anges");
  	    }
  	    if(factoryClass == null) {
  	        throw new IllegalArgumentException("factoryClass måste anges");
  	    }
  	    //CommunicationTester.isPortOpen(url);
  	    
  		java.util.Properties env = new java.util.Properties();
  		if(user != null) {
  		    env.put(Context.SECURITY_PRINCIPAL, user);
  		}
  		if(credentials != null) {
  		    env.put(Context.SECURITY_CREDENTIALS, credentials);
  		}
		env.put(Context.INITIAL_CONTEXT_FACTORY, factoryClass);
		env.put(Context.PROVIDER_URL, url);
		try {
			return new InitialContext(env);
		} catch (NamingException e) {
			throw new IllegalStateException("Kunde inte skapa InitialContext: " + e);
		}
  	}
  	
  	
  	public static Context getSecureContextFromWAS(String url, String user, String credentials) {
  	    return getContextFromAppserver("com.ibm.websphere.naming.WsnInitialContextFactory", url, user, credentials);
  	}
  	
  	public static Context getContextFromWAS(String url) {
  	    return getSecureContextFromWAS(url, null, null);
  	}

  	
  	public static Context getContextFromLocalWAS() {
  	    return getContextFromWAS("iiop://localhost:2809");
  	    
  	}
  	
  	
  	
  	
  	
  	
}
