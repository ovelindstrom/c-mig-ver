/**
 * Skapad 2007-feb-05
 * Bas från http://java.sun.com/blueprints/corej2eepatterns/Patterns/ServiceLocator.html
 * 
 * Ska lägga till pluggbar cachestrategi
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.jndi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.Reference;
//import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import se.csn.common.config.ClassDependencyTester;
import se.csn.common.config.PropertyDependencyTester;

//import com.ibm.websphere.naming.CannotInstantiateObjectException;

/**
 * Skapad 2007-feb-05
 * @author Jonas Öhrnell (csn7821)
 */
public class ServiceLocator {
    private Context context = null;
    
    public ServiceLocator(Context ctx) {
        context = ctx;
    }
    
    /**
	 * Skapar en ServiceLocator med default Context; används i appserverkod
	 */
    public ServiceLocator() {
        try {
            context = new InitialContext();
        } catch (NamingException e) {
            throw new RuntimeException("Kunde inte skapa InitialContext", e);
        }
    }
  
  
	// Converts the serialized string into EJBHandle 
	// then to EJBObject.
	public EJBObject getService(String id) {
	  if (id == null) {
	      throw new IllegalArgumentException("Id får inte vara null");
	  }
	  try {
	      byte[] bytes = new String(id).getBytes();
	      InputStream io = new ByteArrayInputStream(bytes);
	      ObjectInputStream os = new ObjectInputStream(io);
	      javax.ejb.Handle handle = (javax.ejb.Handle)os.readObject();
	      return handle.getEJBObject();
	  } catch(Exception ex) {
	      throw new IllegalStateException("Kunde inte skapa EJBObject från id: " + ex);
	  }
	}
  
	// Returns the String that represents the given 
	// EJBObject's handle in serialized format.
	protected String getId(EJBObject session) {
	    try {
	        javax.ejb.Handle handle = session.getHandle();
	        ByteArrayOutputStream fo = new ByteArrayOutputStream();
	        ObjectOutputStream so = new ObjectOutputStream(fo);
	        so.writeObject(handle);
	        so.flush();
	        so.close();
	        return new String(fo.toByteArray());
	  } catch(RemoteException ex) {
	      throw new IllegalArgumentException("Kunde inte serialisera session: " + ex);
	  } catch(IOException ex) {
	      throw new IllegalArgumentException("Kunde inte serialisera session: " + ex);
	  }
	}
  
	// Returns the EJBHome object for requested service 
	// name. Throws ServiceLocatorException If Any Error 
	// occurs in lookup
    public EJBHome getHome(String name, Class clazz) {
        try {
	        Object objref = context.lookup(name);
	        System.out.println("DEBUGGING - THIS IS BEING CALLED");
	        EJBHome home = null;// (EJBHome)PortableRemoteObject.narrow(objref, clazz);
	        return home;
        } catch(NamingException ex) {
	        throw new IllegalStateException("Kunde inte hitta objekt med namn " + name + ": " + ex);
	    }
    }
    
    public DataSource getDatasource(String jndiName) {
        return (DataSource)getJndiObject(jndiName);
    }
    
    public QueueConnectionFactory getQueueConnectionFactory(String jndiName) {
        return (QueueConnectionFactory)getJndiObject(jndiName);
    }
    
    public Queue getQueue(String jndiName) {
        return (Queue)getJndiObject(jndiName);
    }
    
    public Object getJndiObject(String jndiName) {
        if(jndiName == null) {
            throw new IllegalArgumentException("Jndi-namnet får inte vara null");
        }
        try {
            return context.lookup(jndiName);
        } catch (NameNotFoundException e) {
	        throw new IllegalStateException("Kunde inte hitta jndi-objekt med namn " + jndiName + ": " + e);
//        } catch (CannotInstantiateObjectException e) {
//            Throwable root = e.getRootCause();
//            if(root instanceof NoClassDefFoundError) {
//                Reference ref = e.getReference();
//                String factoryname = ref.getFactoryClassName();
//                String felmeddelande = "Hittade " + jndiName + ", men kunde inte dereferera objektet.\n";
//                if(!ClassDependencyTester.classFound(factoryname)) {
//                    felmeddelande += "Klassen " + factoryname + " måste ligga på classpath för att derefereringen ska fungera.\n";
//                }
//                if(!PropertyDependencyTester.isPropertySetInFile("com.ibm.websphere.csi.J2EENameFactory", "implfactory.properties")) {
//                    felmeddelande += "Filen implfactory.properties finns inte på classpath. Den används vid dereferering.\n";
//                }
//                throw new IllegalStateException(felmeddelande + e);                
//            }
//            if(root instanceof IllegalAccessError) {
//                throw new IllegalStateException("Hittade " + jndiName + ", men kunde inte skapa en koppling till det.\n" +
//                		"Mest troligt fel på user eller credentials: " + e);
//            }
//	        throw new IllegalStateException("Hittade " + jndiName + ", men kunde inte skapa en koppling till det: " + e);
        }catch (NamingException e) {
	        throw new IllegalStateException("Kunde inte hitta DataSource med namn " + jndiName + ": " + e);
        }
    }
  
    
}

  /*The client code to use the Service Locator for this strategy may look like the code in Example 8.35.

  Example 8.35 Client Code for Using the Service Locator

  public class ServiceLocatorTester {
    public static void main( String[] args ) {
      ServiceLocator serviceLocator = 
        ServiceLocator.getInstance();
      try {
        ProjectHome projectHome = (ProjectHome)
          serviceLocator.getHome(
            ServiceLocator.Services.PROJECT );
      }
      catch( ServiceException ex ) {
        // client handles exception
        System.out.println( ex.getMessage( ));
      }
    }
  }*/

