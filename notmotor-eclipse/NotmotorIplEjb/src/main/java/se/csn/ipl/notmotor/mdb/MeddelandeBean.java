package se.csn.ipl.notmotor.mdb;

import java.io.StringReader;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;

import javax.jms.TextMessage;
//import javax.transaction.UserTransaction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

//import com.ibm.jms.JMSBytesMessage;
import javax.jms.BytesMessage;

import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionManagement;

import se.csn.ark.common.CsnApplicationException;
import se.csn.ark.common.dal.CsnDAOWebServiceImpl;
import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.Log;
import se.csn.webservice.bas.hanteraEDH.HamtaFranEDHFraga;
import se.csn.webservice.bas.hanteraEDH.HamtaFranEDHSvar;
import se.csn.webservice.bas.hanteraEDH.HanteraEDH_PortType;
import se.csn.webservice.bas.hanteraEDH.HanteraEDH_ServiceLocator;
import se.csn.webservice.bas.notmotor.skicka.DTOAvsandare;
import se.csn.webservice.bas.notmotor.skicka.DTOBilaga;
import se.csn.webservice.bas.notmotor.skicka.DTOMeddelande;
import se.csn.webservice.bas.notmotor.skicka.DTOMottagare;
import se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat;
import se.csn.webservice.bas.notmotor.skicka.Skicka_PortType;
import se.csn.webservice.bas.notmotor.skicka.Skicka_ServiceLocator;

/**
 * Bean implementation class for Enterprise Bean: Meddelande.
 * Transaktionshantering ar satt till container for att fMessageDrivenCtx.setRollbackOnly() ska
 * lagga tillbaka MQ meddelandet pa kon igen.
 */
@MessageDriven
public class MeddelandeBean extends CsnDAOWebServiceImpl implements javax.ejb.MessageDrivenBean, javax.jms.MessageListener {

    private static final long serialVersionUID = 1L;

    private MessageDrivenContext fMessageDrivenCtx;
    private static Log log = Log.getInstance(MeddelandeBean.class);
    private String dataVarde = null;
    private static final String PROPERTIES_FIL = "notmotor-ipl";
    private static final String PROPERTIES_FIL_ARK = "ark";

    /** Namn pa inkommande kanal, om inget annat ges. */
    private static final String KANAL_NAMN_DEFAULT = "MQMAAAUD";

    /** 
     * getMessageDrivenContext.
     * @return mdc
     */
    public javax.ejb.MessageDrivenContext getMessageDrivenContext() {
        return fMessageDrivenCtx;
    }

    /**
     * setMessageDrivenContext.
     * @param ctx MessageDrivenContext
     */
    public void setMessageDrivenContext(javax.ejb.MessageDrivenContext ctx) {
        fMessageDrivenCtx = ctx;
    }

    /**
     * ejbCreate.
     */
    public void ejbCreate() {
    }

    /**
     * onMessage.
     * @param inMessage Message
     */
    public void onMessage(javax.jms.Message inMessage) {
        String refnr = null;
        String meddsaett = null;
        String epostadr = null;
        String mobilnr = null;
        String rubrik = null;
        String motiv = null;
        String skickatidigast = null;
        String xml = null;
        String docId = null;
        String charset = null;

        try {
            // Hämta aktuellt character set
            charset = Properties.getProperty(PROPERTIES_FIL, "notmotor.mq.characterset");
            if (charset == null) {
                log.error("Värdet notmotor.mq.characterset saknas i propertiesfil");
            }

            if (log.isDebugEnabled()) {
                log.debug("Meddelande mottaget");
            }
            if (inMessage instanceof TextMessage || inMessage instanceof BytesMessage) {
                if (inMessage instanceof TextMessage) {
                    xml = ((TextMessage) inMessage).getText();
                } else {
                    BytesMessage byteMessage = (BytesMessage) inMessage;
                    byte[] bytes = new byte[(int) byteMessage.getBodyLength()];
                    byteMessage.readBytes(bytes);
                    xml = new String(bytes, charset);
                }

                if (log.isDebugEnabled()) {
                    log.debug("Meddelandetext: " + xml);
                }
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setValidating(false);
                    factory.setNamespaceAware(false);
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(new InputSource(new StringReader(xml)));

                    refnr = plockaUtData(doc, "REFNR");
                    meddsaett = plockaUtData(doc, "MEDDSAETT");
                    epostadr = plockaUtData(doc, "EPOSTADR");
                    mobilnr = plockaUtData(doc, "MOBILNR");
                    rubrik = plockaUtData(doc, "RUBRIK");
                    motiv = plockaUtData(doc, "MOTIV");
                    skickatidigast = plockaUtData(doc, "SKICKATIDIGAST");
                    docId = plockaUtData(doc, "DOKUMENTID");
                } catch (ParserConfigurationException e) {
                    log.error("The underlying parser does not support the requested features.");
                    throw new IllegalStateException(e);
                } catch (FactoryConfigurationError e) {
                    log.error("Error occurred obtaining Document Builder Factory.");
                    throw new IllegalStateException(e);
                } catch (SAXParseException e) {
                    log.error("Error occurred parsing xml.(SAXParseException)" + xml);
                    throw new IllegalStateException(e);
                } catch (Exception e) {
                    log.error("Error occurred parsing xml.(Exception)" + xml);
                    throw new IllegalStateException(e);
                }
            } else {
                String message = "Meddelandetyp skall vara TextMessage eller JMSBytesMessage, men "
                    + "mottaget meddelande är " + inMessage.getClass().getName();
                log.error(message);

                throw new Exception(message);
            }

            DTOMeddelande meddelande = new DTOMeddelande();
            Skicka_PortType client;
            Skicka_ServiceLocator serviceLocator = new Skicka_ServiceLocator();

            try {
                client = serviceLocator.getSkickaSOAP(getURL(serviceLocator.getPorts()));
                // CHECKSTYLE/OFF: MagicNumber
                meddelande.setCsnnummer(new Integer(refnr.substring(0, 8)));
                meddelande.setRubrik(rubrik);
                meddelande.setMeddelandetext(motiv);
                if (skickatidigast.length() >= 10) {
                    int year = Integer.parseInt(skickatidigast.substring(0, 4));
                    int month = Integer.parseInt(skickatidigast.substring(5, 7));
                    int day = Integer.parseInt(skickatidigast.substring(8, 10));
                    Calendar skickaT = Calendar.getInstance();
                    skickaT.set(year, month, day, 0, 0);
                    meddelande.setSkickaTidigast(skickaT);
                }
                if (log.isDebugEnabled()) {
                    log.debug("Skicka tidigast :" + skickatidigast);
                }
                DTOMottagare[] mottArr = new DTOMottagare[1];
                DTOMottagare mott = new DTOMottagare();
                mott.setTyp(meddsaett.toUpperCase());
                if (meddsaett.toUpperCase().equals("EPOST")) {
                    mott.setAdress(epostadr);
                } else if (meddsaett.toUpperCase().equals("MINMYNDPOST")) {
                    mott.setAdress("");
                } else {
                    mott.setAdress(mobilnr);
                }
                mott.setCsnnummer(new Integer(refnr.substring(0, 8)));
                mottArr[0] = mott;
                meddelande.setMottagare(mottArr);
                // CHECKSTYLE/ON: MagicNumber

                // Sätt kanal (Hämta namn från Correlation-ID om det är satt, annars används standardnamnet)
                String kanalnamn = KANAL_NAMN_DEFAULT;
                String correlationIDtext = new String(inMessage.getJMSCorrelationIDAsBytes(), charset).trim();
                if (log.isDebugEnabled()) {
                    log.debug("Correlation-ID: " + correlationIDtext);
                }
                if (correlationIDtext.length() > 0) {
                    // filtrera bort ogiltiga tecken
                    String correlationIDtextFiltrerad = correlationIDtext.replaceAll("[^a-zA-Z0-9_ ]", "");
                    if (correlationIDtext.equals(correlationIDtextFiltrerad)) {
                        // använd correlations-ID som kanalnamn, men ersätt först alla blanksteg med "_" och gör om till versaler
                        kanalnamn = correlationIDtext.replaceAll("\\s", "_").toUpperCase();
                    } else {
                        log.warn("Ogiltiga tecken i Correlation-ID: " + correlationIDtext + "\n"
                            + "Kontrollera att teckenkodningen är korrekt konfigurerad; aktuellt värde: notmotor.mq.characterset=" + charset + "\n"
                            + "I nuläget kommer kanalnamnet sättas till standardvärdet '" + KANAL_NAMN_DEFAULT + "'");
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("Sätter kanalnamn till '" + kanalnamn + "'");
                }
                meddelande.setKanal(kanalnamn);

                // Sätt avsändare
                DTOAvsandare avsandare = new DTOAvsandare();
                avsandare.setApplikation(Properties.getProperty(PROPERTIES_FIL, "notmotor.mq.mail.applikation"));
                avsandare.setEpostadress(Properties.getProperty(PROPERTIES_FIL, "notmotor.mq.mail.from"));
                avsandare.setNamn(Properties.getProperty(PROPERTIES_FIL, "notmotor.mq.mail.namn"));
                meddelande.setAvsandare(avsandare);
                if (log.isDebugEnabled()) {
                    log.debug("\nKanal:" + meddelande.getKanal()
                        + "\nApplikation:" + avsandare.getApplikation()
                        + "\nAvsändare namn:" + avsandare.getNamn()
                        + "\nAvsändare epostadress:" + avsandare.getEpostadress());
                }
            } catch (RuntimeException e1) {
                String err = "Felaktigt indata!\n" + xml + "\n" + e1.getMessage();
                log.error(err);
                throw new IllegalStateException(e1);
            }
            if (meddsaett.equals("MINMYNDPOST")) {

                //Hämta dokumentet i fam och lagra ner det i Bilaga
                
                byte[] doc = null;
                try {
                    HanteraEDH_PortType clientEDH;
                    HanteraEDH_ServiceLocator service = new HanteraEDH_ServiceLocator();

                    URL basEndpoint = new URL(Properties.getProperty(PROPERTIES_FIL_ARK, "dao.ws.bas.ipl.csn.se") + "/hanteraEDHSOAP");

                    clientEDH = service.gethanteraEDHSOAP(basEndpoint);
                    HamtaFranEDHFraga fraga = new HamtaFranEDHFraga(docId);
                    HamtaFranEDHSvar svar = clientEDH.hamtaFranEDH(fraga);
                    doc = svar.getData();

                    if (svar.getReturkod() != 0) {
                        throw new IllegalStateException("Lyckades anropa webservice för att hämta dokument, "
                            + "men den gav fel tillbaka - returkod "
                            + svar.getReturkod());
                    }

                    int antalBilagor = 1;
                    DTOBilaga[] bilagor = new DTOBilaga[antalBilagor];
                    DTOBilaga bilaga = new DTOBilaga();

                    if (doc != null && doc.length > 0) {
                        log.debug("buf längd: " + doc.length);

                        //Uppdatera bilaga
                        bilaga.setData(doc);
                        bilaga.setFilnamn(docId + ".pdf");
                        bilaga.setMimetyp("application/pdf");
                        bilagor[0] = bilaga;
                        meddelande.setBilagor(bilagor);
                    } else {
                        log.error("PDF dokument saknas. DocId: " + docId);
                        throw new CsnApplicationException("PDF dokument saknas. DocId: " + docId);
                    }
                    //**/
                } catch (ServiceException s) {
                    log.error("Fångat ett ServiceException." + s.getMessage());
                    throw new IllegalStateException("Notmotorn kunde hämta dokument. ", s);
                    /**
                }  catch (RemoteException e) {
                    log.error("Fångat ett RemoteException." + e.getMessage());
                    throw new IllegalStateException("Notmotorn kunde hämta dokument. ", e);
                    **/
                }
            }

            DTONotifieringResultat resultat;
            try {
                resultat = client.skickaMeddelande(meddelande);
                if (log.isDebugEnabled()) {
                    log.debug("\nResultat från Notmotorn: " + resultat.getResultat()
                        + "\nInfo:" + resultat.getInfo()
                        + "\nMeddelandeId:" + resultat.getMeddelandeId());
                }
            } catch (RemoteException e) {
                log.error("Fångat ett RemoteException." + e.getMessage());
                throw new IllegalStateException("Notmotorn kunde inte ta emot meddelande. ", e);
            }
            if (resultat.getResultat().intValue() > 2) {
                log.error("Fått ett resultat > 2" + resultat.getInfo());
                throw new IllegalStateException("Notmotorn kunde inte ta emot meddelande. " + resultat.getInfo());
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // Ett exception måste kastas för att MQ meddelandet ska läggas tillbaka på kön
            throw new RuntimeException("Backout on error");
        }
    }

    /**
     * ejbRemove.
     */
    public void ejbRemove() {
    }

    /**
     * plockaUtData.
     * @param document b
     * @param namn b
     * @return b
     * @throws Exception b
     */
    private String plockaUtData(Document document, String namn) {
        dataVarde = "";
        if (document != null) {
            NodeList nl = document.getElementsByTagName(namn);
            if (nl.getLength() > 0) {
                sattVarde(nl.item(0));
            }
        }
        return dataVarde;
    }

    /**
     * saettVarde.
     * @param node b
     * @throws Exception b
     */
    private void sattVarde(Node node) {
        if (node != null) {
            switch(node.getNodeType()) {
                case Node.DOCUMENT_NODE:
                    NodeList nodes = node.getChildNodes();
                    if (nodes != null) {
                        sattVarde(nodes.item(0));
                    }
                    break;

                case Node.ELEMENT_NODE:
                    NodeList children = node.getChildNodes();
                    if (children != null) {
                        sattVarde(children.item(0));
                    }
                    break;

                case Node.TEXT_NODE:
                    dataVarde = node.getNodeValue();
                    break;
                default :
            }
        }
    }

}
