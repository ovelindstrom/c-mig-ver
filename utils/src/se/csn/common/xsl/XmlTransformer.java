/**
 * Skapad 2007-maj-29
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.common.xsl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.fop.apps.Driver;
import org.apache.fop.messaging.MessageHandler;

import se.csn.common.config.ClassDependencyTester;


public class XmlTransformer {
    
    private static boolean dependenciesChecked = false;

    public static String transformXml(String xsl, String xml) {
        if(xsl == null) {
            throw new IllegalArgumentException("xsl måste vara satt");
        }
        if(xml == null) {
            throw new IllegalArgumentException("xml måste vara satt");
        }
        
        InputStream xslStream = new ByteArrayInputStream(xsl.getBytes());
        InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
        return transformXml(xslStream, xmlStream);
    }
    
    public static String transformXml(InputStream xsl, InputStream xml) {
        checkDependencies();
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer;
        try {
            transformer = factory.newTransformer(new StreamSource(xsl));
        } catch (TransformerConfigurationException e) {
            throw new IllegalArgumentException("Kunde inte skapa Transformer: " + e);
        }
        Source src = new StreamSource(xml);
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		try {
            transformer.transform(src, result);
        } catch (TransformerException e1) {
            throw new IllegalArgumentException("Kunde inte transformera. Kontrollera indata: " + e1);
        }

		return sw.toString();
    }
    
    public static byte[] xmlToPdf(String xsl, String xml) {
        if(xsl == null) {
            throw new IllegalArgumentException("xsl måste vara satt");
        }
        if(xml == null) {
            throw new IllegalArgumentException("xml måste vara satt");
        }
        
        InputStream xslStream = new ByteArrayInputStream(xsl.getBytes());
        InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
        return xmlToPdf(xslStream, xmlStream);
    }
    
    public static byte[] xmlToPdf(InputStream xsl, InputStream xml) {
        checkDependencies();
		byte[] result = new byte[0];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Driver driver = new Driver();
		Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
		driver.setLogger(logger);
		MessageHandler.setScreenLogger(logger);
      
		driver.setRenderer(Driver.RENDER_PDF);
		
		driver.setOutputStream(out);

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer;
        try {
            transformer = factory.newTransformer(new StreamSource(xsl));
        } catch (TransformerConfigurationException e) {
            throw new IllegalArgumentException("Kunde inte skapa Transformer: " + e);
        }
        Source src = new StreamSource(xml);
		Result res = new SAXResult(driver.getContentHandler());
		try {
            transformer.transform(src, res);
        } catch (TransformerException e1) {
            throw new IllegalArgumentException("Kunde inte transformera. Kontrollera indata: " + e1);
        }

		result = out.toByteArray();
		return result;
	}
    
    private static void checkDependencies() {
        if(dependenciesChecked) {
            return;
        }
        String[][] classes = {{"org.apache.fop.apps.Driver", "fop.jar"},
                {"org.apache.avalon.framework.logger.Logger", "avalon-framework-api.jar"},
        		{"org.apache.avalon.framework.logger.ConsoleLogger", "avalon-framework-impl.jar"},
        		{"org.apache.batik.dom.AbstractAttr", "batik.jar", "fop.jar"},};
        ClassDependencyTester.findClassesThrowException(classes);
        dependenciesChecked = true;
    }
}
