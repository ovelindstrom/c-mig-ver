/*
 * Skapad 2007-nov-29
 */
package se.csn.common.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import se.csn.ark.common.util.logging.Log;
import se.csn.common.jndi.ServiceLocator;

/**
 * @author Jonas Öhrnell - csn7821
 * Använder SAX för att läsa upp en config.xml-fil och skapa configobjekt.
 * Varför SAX?
 * 1. finns i Java 1.4 och senare. 
 * 2. Snabbt.
 * 
 * Varför en (krånglig) eventhantering?
 * För att få ordning på koden. 
 * 
 * Varför inte commons-digester?
 * 1. För att dessa Resources inte är bönor. 
 * 2. För att mappningen inte är helt enkel att göra. 
 * 
 * Varför inte xmlbeans? 
 * Av samma skäl. 
 */
public class ResourceFactory extends DefaultHandler {
    
    private Log log = Log.getInstance(ResourceFactory.class);
    private ResourceRepository repo;
    private ServiceLocator serviceLocator;
    private Map configListeners;
    private List activeListeners;
    private String currentEnvironment;
    
    private Map variables;
    
    public ResourceFactory(ServiceLocator sl, ResourceRepository repository) {
        serviceLocator = sl;
        repo = repository;
        variables = new HashMap();
        
        configListeners = new HashMap();
        activeListeners = new ArrayList();
        addConfigListener("variable", new VariableListener());
    }
    
    public void addConfigListener(String element, ConfigXmlParseListener listener) {
        configListeners.put(element.toUpperCase(), listener);
    }
    
    /**
     * Ska funka oavsett case på namn. Ersätter variabler
     * @param attr
     * @param name
     * @return
     */
    public String getAttr(Attributes attrs, String name) {
        // 1. Hitta variabeln
        String val = null;
        for(int i = 0; i < attrs.getLength(); i++) {
            if(attrs.getLocalName(i).equalsIgnoreCase(name)) {
                val = attrs.getValue(i);
                break;
            }
        }
        if(val == null) {
            return null;
        }
        return replaceVar(val);
    }
    
    public String replaceVar(String value) {
        // 2. Ersätt variabelvärde (om det finns ett sådant):
        // OBS! Antar nu att det första tecknet är $. Om ingen matchande 
        // variabel finns så görs ingen ersättning. 
        // Om variabeln fanns så ersätts den med värdet om environment fanns, 
        // annars med VÄRDE SAKNAS FÖR ENVIRONMENT [environment]
        if((value.charAt(0) == '$') && (value.charAt(1) != '{')) {
            ConfigVariable var = (ConfigVariable)variables.get(value.substring(1).toUpperCase());
            String rep = var.getVariableForEnvironment(currentEnvironment.toUpperCase());
            if(rep != null) {
                return rep;
            } else {
                return "VÄRDE SAKNAS FÖR ENVIRONMENT " + currentEnvironment;
            }
        } else {
            return value;
        }
        
    }
    
    public void build(String configfil, String environment) {
        currentEnvironment = environment;
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(this);
            reader.parse(configfil);
        } catch (SAXException e) {
            throw new IllegalArgumentException("Fel vid parsning: " + e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunde inte läsa fil: " + FileResource.getAbsolutSokvag(configfil) + ":" + e);
        }
    }
    
    class VariableListener implements ConfigXmlParseListener {
        
        private ConfigVariable variable;

        public void end(String element) {
            if(element.equals("VARIABLE")) {
                variables.put(variable.getName().toUpperCase(), variable);
            }
        }
        public void start(String element, Attributes attrs) {
            if(element.equals("VARIABLE")) {
                variable = new ConfigVariable(attrs.getValue("name"), attrs.getValue("description"));
            }
            if(element.equals("ENVIRONMENT")) {
                variable.addValue(getAttr(attrs, "name"), getAttr(attrs, "value"));
            }
        }
    }
    
    class DatabaseListener implements ConfigXmlParseListener {
        
        private DBResource res;
        
        public void start(String element, Attributes attrs) {
            if(element.equals("DATABASE")) {
                res  = createDBResource(attrs.getValue("name"), attrs.getValue("description"));
            }
            //if(element.equals())

        }
        
        public void end(String element) {
            
        }
    }
    
    /*
	<database name="$DBNAMN" environment="WebbDB" server="$DBSERVER">
	<task>Använd aktuella tabeller i WDBGRON som mall</task>
	<tables schema="BREVAS">
		<table name="ARENDEN" privileges="SELECT,INSERT,UPDATE,DELETE" data="tom">
			<column name="NYCKEL" type="VARCHAR" length="32" null="no"/>
			<column name="ARENDEID" type="BIGINT" null="no" comment="AE_BREV_AERENDE.r_ae_brev_aerende_id_foeraelder"/>
			<column name="FAMID" type="VARCHAR" length="20" null="no"/>
			<column name="GILTIG" type="INTEGER" null="no"/>
			<column name="SKICKAT" type="TIMESTAMP" null="no"/>
			<column name="LAEST" type="TIMESTAMP" null="yes"/>
			<column name="ID_ARENDE" type="BIGINT" null="yes" comment="AE_BREV_AERENDE.identitet"/>
			<column name="REGISTRERINGSTID" type="TIMESTAMP" null="yes" comment="AE_BREV_HAENDELSE.registreringstid"/>
			<column name="EPOST" type="VARCHAR" length="254" null="yes" comment="AE_BREV_AERENDE.epostadress_ut"/>
		</table>
		<table name="INKOMMANDE" privileges="SELECT,  INSERT, UPDATE" data="tom" comment="För uppföljning av inkommande ärenden">
			<column name="TIDLEVERERAD" type="TIMESTAMP" null="no" comment="Tid levererad"/>
			<column name="AVSANDARE" type="VARCHAR" length="254" null="no" comment="Avsändarens e-postadress eller hash-nyckel"/>
			<column name="SYSTEM" type="VARCHAR" length="6" null="no" comment="Levererande system"/>
			<column name="MOTTAGEN" type="TIMESTAMP" null="yes" comment="Tid för mottagning på IPL"/>
			<column name="MOTTAGARE" type="VARCHAR" length="254" null="yes" comment="Mottagare av ärendet"/>
		</table>
		<table name="MEDDELANDETEXTER" privileges="SELECT" data="Data från WDBGRON" comment="Texter till svarsmeddelanden som skickas till kunden via e-post">
			<column name="ID" type="SMALLINT" null="no" key="primary"/>
			<column name="TYP" type="VARCHAR" length="40" null="no" key="unique"/>
			<column name="AMNE" type="VARCHAR" length="100" null="no"/>
			<column name="TEXT" type="VARCHAR" length="3800" null="no"/>
		</table>
		<table name="ARENDETYPER" privileges="SELECT" data="Data från WDBGRON" comment="Tillgängliga ärendetyper (ämnesval) som skall visas i ämneslistan i webbformuläret.">
			<column name="ID" type="INTEGER" null="no" key="primary"/>
			<column name="GRUPP" type="VARCHAR" length="40" null="yes"/>
			<column name="NAMN" type="VARCHAR" length="40" null="no"/>
			<column name="NYCKEL" type="VARCHAR" length="40" null="no"/>
			<column name="EPOST" type="VARCHAR" length="254" null="yes"/>
		</table>
		<table name="TEXTER" privileges="SELECT" data="Data från WDBGRON" comment="Texter till de olika webbsidorna.">
			<column name="ID" type="SMALLINT" null="no" key="primary"/>
			<column name="SIDA" type="VARCHAR" length="32" null="no"/>
			<column name="TITEL" type="VARCHAR" length="32" null="yes"/>
			<column name="RUBRIK" type="VARCHAR" length="64" null="yes"/>
			<column name="UNDER_RUBRIK" type="VARCHAR" length="64" null="yes"/>
			<column name="TEXT" type="VARCHAR" length="1536" null="yes"/>
			<column name="ARENDETYP_LABEL" type="VARCHAR" length="64" null="yes"/>
			<column name="ARENDE_LABEL" type="VARCHAR" length="64" null="yes"/>
			<column name="FORNAMN_LABEL" type="VARCHAR" length="64" null="yes"/>
			<column name="EFTERNAMN_LABEL" type="VARCHAR" length="64" null="yes"/>
			<column name="EPOST_LABEL" type="VARCHAR" length="64" null="yes"/>
			<column name="EPOST2_LABEL" type="VARCHAR" length="64" null="yes"/>
			<column name="PERSONNUMMER_LABEL" type="VARCHAR" length="64" null="yes"/>
			<column name="TELEFON_LABEL" type="VARCHAR" length="64" null="yes"/>
			<column name="SLUT_RUBRIK" type="VARCHAR" length="64" null="yes"/>
			<column name="SLUT_TEXT" type="VARCHAR" length="1536" null="yes"/>
			<column name="KNAPP_NASTA" type="VARCHAR" length="32" null="yes"/>
			<column name="KNAPP_TILLBAKA" type="VARCHAR" length="32" null="yes"/>
		</table>
		<table name="VALIDERINGSFEL" privileges="SELECT" data="Data från WDBGRON" comment="Texter som visas vid olika typer av valideringsfel.">
			<column name="ID" type="INTEGER" null="no" key="primary"/>
			<column name="NYCKEL" type="VARCHAR" length="32" null="no"/>
			<column name="TEXT" type="VARCHAR" length="2048" null="yes"/>
		</table>
	</tables>
	<tables schema="WEBBSVAR">
		<table name="TJANST" privileges="SELECT" data="Lägg till:\n\nID = 42\nNAMN = E-posta oss\nOPPEN = 1\nSENAST_ANDRAD = &lt;aktuell tid&gt;\nSKAPAD = &lt;aktuell tid&gt;\nOMRADE = E-posta oss" comment="Aktuell driftstatus (tjänst öppen/stängd)">
			<column name="ID" type="INTEGER" null="no" key="primary"/>
			<column name="NAMN" type="VARCHAR" length="128" null="no"/>
			<column name="OPPEN" type="SMALLINT" null="no"/>
			<column name="SENAST_ANDRAD" type="TIMESTAMP" null="yes"/>
			<column name="SKAPAD" type="TIMESTAMP" null="yes"/>
			<column name="OMRADE" type="CHAR" length="20" null="no"/>
		</table>
		<table name="WEBBSERVER" privileges="SELECT,INSERT" data="Lägg till:\n\nCONTEXT_URL = http://csnwasp01.csnnet.ext:8082/epostaoss\nOPPEN = 1\nSENAST_ANDRAD = &lt;aktuell tid&gt;\nSKAPAD = &lt;aktuell tid&gt;\nAPPLIKATION = E-posta oss (Brevärendesystemet)\n\nCONTEXT_URL = http://csnwasp02.csnnet.ext:8082/epostaoss\nOPPEN = 1\nSENAST_ANDRAD = &lt;aktuell tid&gt;\nSKAPAD = &lt;aktuell tid&gt;\nAPPLIKATION = E-posta oss (Brevärendesystemet)" comment="Innehåller URL:en till context-root för EW-applikationen och aktuell driftstatus för denna (öppen/stängd)">
			<column name="CONTEXT_URL" type="CHAR" length="64" null="no" key="primary"/>
			<column name="OPPEN" type="SMALLINT" null="no"/>
			<column name="SENAST_ANDRAD" type="TIMESTAMP" null="no"/>
			<column name="SKAPAD" type="TIMESTAMP" null="no"/>
			<column name="APPLIKATION" type="CHAR" length="64" null="no"/>
		</table>
	</tables>
</database>
*/
    
    // -------  PARSING -------
    
    public void startElement(String uri, String localname, String qName,
            Attributes attrs) throws SAXException {
        log.debug("StartElement: " + uri + "," + localname +"," + qName);
        dumpAttrs(attrs);
        // Kolla om det finns någon listener som ska aktiveras:
        ConfigXmlParseListener listener = (ConfigXmlParseListener)configListeners.get(localname.toUpperCase());
        if(listener != null) {
            log.debug("Adding listener for " + localname + ": " + listener.getClass().getName());
            activeListeners.add(listener);
        }
        
        // Meddela alla aktiva lyssnare att det kommit ett element
        for(Iterator it = activeListeners.iterator(); it.hasNext();) {
            listener = (ConfigXmlParseListener)it.next();
            listener.start(localname.toUpperCase(), attrs);
        }
    }
    
    private void dumpAttrs(Attributes attrs) {
        for(int i = 0; i < attrs.getLength(); i++) {
            log.debug("Name: " + attrs.getLocalName(i) + " Value: " + attrs.getValue(i));
        }
    }

    
    public void endElement(String uri, String localname, String qName) throws SAXException {
        // Meddela alla aktiva lyssnare att det kommit en sluttag
        for(Iterator it = activeListeners.iterator(); it.hasNext();) {
            ConfigXmlParseListener listener = (ConfigXmlParseListener)it.next();
            listener.end(localname.toUpperCase());
        }        
        ConfigXmlParseListener listener = (ConfigXmlParseListener)configListeners.get(localname.toUpperCase());
        if(listener != null) {
            log.debug("Removing listener for " + localname + ": " + listener.getClass().getName());
            activeListeners.remove(listener);
        }
    }

    public void endDocument() throws SAXException {
        super.endDocument();
    }

    // --------- FACTORYMETODER ---------
    
    public DBResource createDBResource(String name, String jndiname) {
        DBResource res = new DBResource(name, jndiname, serviceLocator);
        return res;
    }


    
    
}
