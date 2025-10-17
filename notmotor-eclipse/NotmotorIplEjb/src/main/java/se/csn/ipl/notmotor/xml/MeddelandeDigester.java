/**
 * @since 2007-sep-18
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.ipl.notmotor.xml;



public class MeddelandeDigester {

    /*private static boolean dependenciesChecked = false;
    private Digester digester;
    
    public MeddelandeDigester() {
        if(!dependenciesChecked) {
            checkDependencies();
        }
        
        // Hantera datumformat:
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.S";
        Locale locale = Locale.getDefault();
        DateLocaleConverter converter = new DateLocaleConverter(locale, pattern);
        converter.setLenient(true);
        ConvertUtils.register(converter, java.util.Date.class);
        
        digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("meddelande", "se.csn.notmotor.dto.DTOMeddelande");
        digester.addSetProperties("meddelande");
        
        digester.addCallMethod("meddelande/rubrik", "setRubrik", 0);
        digester.addSetProperties( "meddelande/rubrik", "encoding", "rubrikEncoding" );
        digester.addCallMethod("meddelande/text", "setMeddelandetext", 0);
        digester.addSetProperties( "meddelande/text", "encoding", "meddelandeEncoding" );
        
        digester.addObjectCreate("meddelande/avsandare", "se.csn.notmotor.dto.DTOAvsandare");
        digester.addSetProperties("meddelande/avsandare", new String[]{"namn", "adress", "replyto"}, new String[]{"namn", "epostadress", "replyTo"});
        digester.addSetNext("meddelande/avsandare", "setAvsandare");	
        
        digester.addObjectCreate("meddelande/mottagare", "se.csn.notmotor.dto.DTOMottagare");
        digester.addSetProperties("meddelande/mottagare"); 
            //new String[]{"namn", "adress", "typ", "csnnummer"}, new String[]{"namn", "epostadress", "replyTo"});
        digester.addSetNext("meddelande/mottagare", "addMottagare");
        
        digester.addObjectCreate("meddelande/bilaga", "se.csn.notmotor.dto.DTOBilaga");
        digester.addSetProperties("meddelande/bilaga");
        digester.addSetNext("meddelande/bilaga", "addBilaga");

    }
    
    
    public DTOMeddelande xmlToDTOMeddelande(InputStream input) {
        if(input == null) {
            throw new IllegalArgumentException("input måste vara satt");
        }
        try {
            return (DTOMeddelande) digester.parse(input);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunde inte lästa InputStream: " + e);
        } catch (SAXException e) {
            throw new IllegalArgumentException("Kunde inte parsa indata: " + e);
        }
        
    }
    
    public void checkDependencies() {
        ClassDependencyTester.findClassesThrowException(new String[][]{
                {"org.apache.commons.beanutils.BeanUtils", "commons-beanutils.jar"},
                {"org.apache.commons.logging.Log", "commons-logging.jar"},
                {"org.apache.commons.collections.BagUtils", "commons-collections.jar"},});
        dependenciesChecked = true;
    }
    */
}
