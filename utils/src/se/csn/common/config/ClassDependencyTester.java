/**
 * Skapad 2007-feb-20
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.config;

/**
 * Utilitymetoder för att kontrollera att alla klasser/jarfiler finns på plats.
 */
public class ClassDependencyTester {

    /**
     * Några vanliga klassbibliotek:
     */
    public static final String[] LOG4J = new String[] {"org.apache.log4j.Appender", "log4j.jar"};
    public static final String[] ARKALL = new String[] {"se.csn.ark.common.DTOException", "ark-all.jar"};
    public static final String[] JFAM = new String[] {"com.frontec.fam.FamCommand", "jfam.jar"};
    public static final String[] COOR = new String[] {"com.tietoenator.pcs.coor.Config", "coor.jar", "jfam.jar"};
    public static final String[] SOAP = new String[] {"org.apache.soap.Fault", "soap.jar"};
    public static final String[] XSDBEANRUNTIME = new String[] {"com.ibm.etools.xsd.bean.runtime.AnyType", "xsd.bean.runtime.jar"};
    public static final String[] WEBSERVICERUNTIME = new String[] {"com.ibm.etools.webservice.runtime.XSDAnyTypeSerializer", "webservice-runtime.jar"};
    public static final String[] WEBSERVICEUTILS = new String[] {"webserviceutils.com.ibm.etools.webservice.util.BuildInfo", "webserviceutils.jar"};
    
    // Commons:
    public static final String[] COMMONS_DBCP = new String[] {"org.apache.commons.dbcp.BasicDataSource", "commons-dbcp.jar"};
    public static final String[] COMMONS_POOL = new String[] {"org.apache.commons.pool.impl.GenericObjectPool", "commons-pool.jar", "commons-dbcp.jar"};
    public static final String[] COMMONS_LANG = new String[] {"org.apache.commons.lang.builder.ToStringBuilder", "commons-lang.jar"};
    
    /**
     * @param classname Namn på den sökta klassen; ska vara fully qualified.
     * @return true om klassen hittades, false annars
     */
    public static boolean classFound(String classname) {
        try {
            Class.forName(classname);
            return true;
        } catch (ClassNotFoundException e) {
            
        }
        return false;
    }
    
    /**
     * @param classname Namn på den sökta klassen; ska vara fully qualified.  
     * @param jarfile Namn på den jarfil som innehåller klassen; bara filnamnet, men MED
     *        filändelse (kan ju vara .zip också)
     * @return null om klassen finns, annars en feltext.
     */
    public static String findClassReturnErrorMessage(String classname, String jarfile) {
        if(classFound(classname)) return null;
        return "Kunde inte hitta " + classname + " som hör till filen " + jarfile;
    }
    
    /**
     * @param classname Namn på den saknade klassen; ska vara fully qualified.  
     * @param jarfile Namn på den jarfil som innehåller klassen; bara filnamnet, men MED
     *        filändelse (kan ju vara .zip också)
     * @param usedBy Används om jarfilen ifråga är en dependency av en annan jarfil. Bra att
     *        använda för att tydliggöra var en klass används om den inte används i 
     *        applikationskod. 
     * @return null om klassen finns, annars en feltext.
     */
    public static String findClassReturnErrorMessage(String classname, String jarfile, String usedBy) {
        if(classFound(classname)) return null;
        return findClassReturnErrorMessage(classname, jarfile) + ". Denna klass används i " + usedBy;
    }

    /**
    * @param classAndJarfile Första stärngen ska innehålla namn på den saknade klassen; 
    * 		 ska vara fully qualified.  
    * 		 Andra parametern ska innehålla namn på den jarfil som innehåller klassen; 
    * 		 bara filnamnet, men MED filändelse (kan ju vara .zip också)
    * 		 Tredje parametern används om jarfilen ifråga är en dependency av en 
    * 		 annan jarfil. 
    * @return null om klassen finns, annars en feltext.
    */
    public static String findClassReturnErrorMessage(String[] classAndJarfile) {
        if((classAndJarfile == null) || (classAndJarfile.length < 1)) {
            throw new IllegalArgumentException("Listan måste innehålla minst ett element");
        }
        if(classAndJarfile.length == 1) return findClassReturnErrorMessage(classAndJarfile[0], "okänd jarfil");
        if(classAndJarfile.length == 2) return findClassReturnErrorMessage(classAndJarfile[0], classAndJarfile[1]);
        if(classAndJarfile.length == 3) return findClassReturnErrorMessage(classAndJarfile[0], classAndJarfile[1], classAndJarfile[2]);
        if(classAndJarfile.length > 3) {
            throw new IllegalArgumentException("Listan ska innehålla som mest 3 argument.");
        }
        return null;
    }

    
    /**
     * @param classname Namn på den saknade klassen; ska vara fully qualified.  
     * @param jarfile Namn på den jarfil som innehåller klassen; bara filnamnet, men MED
     *        filändelse (kan ju vara .zip också)
     * @throws MissingClassException om klassen inte finns
     */
    public static void findClassThrowException(String classname, String jarfile) {
        if(!classFound(classname)) {
            throw new MissingClassException(classname, jarfile);
        }
    }
    
    /**
     * @param classname Namn på den saknade klassen; ska vara fully qualified.  
     * @param jarfile Namn på den jarfil som innehåller klassen; bara filnamnet, men MED
     *        filändelse (kan ju vara .zip också)
     * @param usedBy Används om jarfilen ifråga är en dependency av en annan jarfil. Bra att
     *        använda för att tydliggöra var en klass används om den inte används i 
     *        applikationskod. 
     * @throws MissingClassException om klassen inte finns
     */
    public static void findClassThrowException(String classname, String jarfile, String usedBy) {
        if(!classFound(classname)) {
            throw new MissingClassException(classname, jarfile, usedBy);
        }
    }
    
    public static void findClassThrowException(String[] classAndJarfile) {
        if((classAndJarfile == null) || (classAndJarfile.length < 1)) {
            throw new IllegalArgumentException("Listan måste innehålla minst ett element");
        }
        if(classAndJarfile.length == 1) findClassThrowException(classAndJarfile[0], "okänd jarfil");
        if(classAndJarfile.length == 2) findClassThrowException(classAndJarfile[0], classAndJarfile[1]);
        if(classAndJarfile.length == 3) findClassThrowException(classAndJarfile[0], classAndJarfile[1], classAndJarfile[2]);
        if(classAndJarfile.length > 3) {
            throw new IllegalArgumentException("Listan ska innehålla som mest 3 argument.");
        }
    }

    
    /**
     * @param classesAndJarfiles Varje rad ska innehålla klassnamn och jarfil (samt användande 
     *        jarfil i förekommande fall). 
     * @return null om alla klasser/jarfiler hittades, annars en feltext med en rad per saknad
     *        klass/jarfil; raderna separeras av \n
     */
    public static String findClassesReturnErrorMessage(String[][] classesAndJarfiles) {
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < classesAndJarfiles.length; i++) {
            String error = findClassReturnErrorMessage(classesAndJarfiles[i]);
            if(error != null) {
                buf.append(error);
                buf.append("\n");
            }
        }
        if(buf.length() > 0) {
            return buf.toString();
        } else {
            return null;
        }
    }
    
    public static void findClassesThrowException(String[][] classesAndJarfiles) {
        for(int i = 0; i < classesAndJarfiles.length; i++) {
           findClassThrowException(classesAndJarfiles[i]);
        }
    }


}

