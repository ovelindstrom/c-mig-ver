package se.csn.ark.broker.plugin.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;


/**
 * CsnPluginTestBase innehåller hjälpfunktioner för att kör
 * junit-tester mot iipax-plugginer.
 *
 * @author Joakim Olsson
 * @since 20041007
 * @version 0.1 skapad
 */
public class CsnPluginTestBase extends TestCase {
	private String dir;

	/**
	 * Constructor for TestDataOnlinePluginTest.
	 * @param arg0 argument till TestCase
	 */
	public CsnPluginTestBase(String arg0) {
		super(arg0);
	}




    /**
     * @return mapp där filer sparas ner
     */
    public String getDir() {
        return dir;
    }




    /**
     * @param directory mapp där filer sparas ner
     */
    public void setDir(String directory) {
        dir = directory;
    }




	/**
     * Bygger ett soap-svar från xml på fil.
     * 
	 * @param method skapar svar till denna metod
	 * @param fileName där svarsdatat ligger
	 * @return komplett soap-svar
	 * @throws IOException vid läsfel från filen
	 */
	protected InputStream createCorrectResponse(String method, String fileName)
	                                     throws IOException {
		String head =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope "
            + "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" "            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "            + "SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"            + "<SOAP-ENV:Body><"
			+ method + "><return>";
		String foot =
			"</return></" + method + "></SOAP-ENV:Body></SOAP-ENV:Envelope>";
		InputStream correct = addHeadFoot(fileName, head, foot);

		return correct;
	}




	/**
	 * Bygger en xml-push från xml på fil.
     * 
     * @param fileName där xml-datat ligger
     * @return komplett xml-push-dokument
     * @throws IOException vid läsfel från filen
	 */
	protected InputStream createCorrectRequest(String fileName)
	                                    throws IOException {
		String method = "Test1";
		String head = "<" + method + ">";
		String foot = "</" + method + ">";
		InputStream correct = addHeadFoot(fileName, head, foot);

		return correct;
	}




	/**
	 * Bygger en soap-fråga från xml på fil.
     * 
     * @param method skapar fråga till denna metod
     * @param fileName där fråge-datat ligger
     * @return komplett soap-request
     * @throws IOException vid läsfel från filen
	 */
	protected InputStream createInputRequest(String method, String fileName)
	                                  throws IOException {
		String head =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope "            + "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" "            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "            + "SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"            + "<SOAP-ENV:Body><"
			            + method + ">";
		String foot = "</" + method + "></SOAP-ENV:Body></SOAP-ENV:Envelope>";
		InputStream input = addHeadFoot(fileName, head, foot);

		return input;
	}




	/**
     * Bygger ström head + fildata + foot.
     * 
	 * @param file namn på fil
	 * @param head sträng före fildatat
	 * @param foot sträng efter fildatat
	 * @return head + fildata + foot
	 * @throws IOException misslyckad filläsning
	 */
	protected InputStream addHeadFoot(String file, String head, String foot)
	                           throws IOException {
		InputStream input = new FileInputStream(dir + "/" + file);
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        final int copySize = 1024;
		byte[] array = new byte[copySize];

		tmp.write(head.getBytes());

		int i = input.read(array, 0, copySize);

		while (i > 0) {
			tmp.write(array, 0, i);
			i = input.read(array, 0, copySize);
		}

		tmp.write(foot.getBytes());
		input = new ByteArrayInputStream(tmp.toByteArray());

		return input;
	}

}