///**
// * Skapad 2007-feb-28
// * @author Jonas Öhrnell (csn7821)
// * 
// */
//package se.csn.common.fam;
//
//import se.csn.ark.common.dal.EdhException;
//import se.csn.ark.common.util.logging.Log;
//import se.csn.common.config.ClassDependencyTester;
//import se.csn.common.config.CommunicationTester;
//import se.csn.common.config.FileResource;
//
//import com.frontec.fam.FamCommException;
//import com.frontec.fam.FamConnection;
//import com.frontec.fam.FamConnectionPool;
//import com.frontec.fam.FamEnvironment;
//import com.frontec.fam.FamPackageException;
//import com.tietoenator.pcs.coor.Config;
//
//
//public class EDHConnectionFactoryImpl implements EDHConnectionFactory {
//
//    private Log log = Log.getInstance(EDHConnectionFactoryImpl.class);
//    
//    public FamConnection getFamConnection(String configFile, String user,
//            String password, String host, String port, String archive) {
//    	// Kontrollera närvaron av JFAM och COOR:
//        ClassDependencyTester.findClassThrowException(ClassDependencyTester.JFAM);
//        ClassDependencyTester.findClassThrowException(ClassDependencyTester.COOR);
//        
//    	FileResource.findReadableFileThrowException(configFile);
//        // Testa alla inparametrar:
//    	if(user == null) throw new IllegalArgumentException("User måste anges");
//    	if(password == null) throw new IllegalArgumentException("password måste anges");
//    	if(host == null) throw new IllegalArgumentException("host måste anges");
//    	int p = -1;
//    	try {
//    	    p = Integer.parseInt(port);
//    	} catch(NumberFormatException nfe) {
//    	    throw new IllegalArgumentException("Port var inte ett numeriskt värde");
//    	}
//    	if(port == null) throw new IllegalArgumentException("port måste anges");
//    	if(archive == null) throw new IllegalArgumentException("archive måste anges");
//
//    	// Testa connection mot server:
//    	if(!CommunicationTester.isPortOpen(host, p)) {
//    	    throw new IllegalStateException("Kunde inte koppla upp mot " + host + ":" + p);
//    	}
//    	
//		String famURI = "fam://" + user + ":" + password + "@" + host + ":" + 
//		port + "/" + archive;
//		if (log.isDebugEnabled()) {
//			log.debug("famURI = " + famURI);
//		}
//
//		return getFamConnection(configFile, famURI);
//    }
//    
//    private FamConnection getFamConnection(String configfile, String famURI) {
//    	try {
//            FamEnvironment env = new FamEnvironment(configfile);
//        } catch (FamPackageException e) {
//            throw new EdhException("Kunde inte skapa FamEnvironment", e);
//        }
//    	Config config;
//        try {
//            config = FamEnvironment.getConfig();
//        } catch (FamPackageException e) {
//            throw new EdhException("Kunde inte hämta FamConfig", e);
//        }
//        if (log.isDebugEnabled()) {
//	    	String debugconfig = config.toString();
//			log.debug("FAM-Config: " + debugconfig);
//		}
//		try {
//            return FamConnectionPool.getConnection(famURI);
//        } catch (FamCommException e) {
//            throw new EdhException("Kunde inte skapa FamConnection. URI: " + famURI, e);
//        } catch (FamPackageException e) {
//            throw new EdhException("Kunde inte skapa FamConnection. URI: " + famURI, e);
//        }
//    }
//    
//    
//    
//}
