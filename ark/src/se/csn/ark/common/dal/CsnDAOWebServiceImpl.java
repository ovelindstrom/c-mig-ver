package se.csn.ark.common.dal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

import se.csn.ark.common.CsnApplicationException;
import se.csn.ark.common.CsnException;
import se.csn.ark.common.CsnSystemException;
import se.csn.ark.common.dt.CsnDataTransferObjectImpl;
import se.csn.ark.common.util.FormatConvertException;
import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.Log;

/**
 * Basklass för Web Service klient.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20040907
 * @version 1 skapad
 * @version 1.1 2008-02-22 borttag av logik för att skicka över web service.
 * 			Ska ligga i koden för respektive projekt.
 * 			Ny funktionalitet för att returnera endpoint för ipl.
 * 			/Anders Berglund, csn7823 
 *
 */
public class CsnDAOWebServiceImpl extends CsnDataAccessObjectImpl {
	private static final long serialVersionUID = 1L;
	private static Log log = Log.getInstance(CsnDAOWebServiceImpl.class);


	/**
	 * Konstruktor.
	 */
	public CsnDAOWebServiceImpl() {
	}


	/**
	 * Hämtar endpoint.
	 * @return end-point url'en
	 * @param aPorts Iterator
	 * @throws MalformedURLException om url-sträng felaktig
	 */
	public URL getURL(Iterator aPorts) throws MalformedURLException {
		String aUrl = null;
		java.util.Properties props = Properties.getProperties("dao.ws.");
		Enumeration keys = props.keys();
		
		if (aPorts != null && aPorts.hasNext()) {
			javax.xml.namespace.QName aQName = (javax.xml.namespace.QName) aPorts.next();
			String aNamespaceURI = aQName.getNamespaceURI();
			String aNamespaceURIReplace = aNamespaceURI.replaceAll("/", ".");
			String service = aQName.getLocalPart();

			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = props.getProperty(key);
				
				if (key != null && value != null) {
					if (aNamespaceURIReplace.indexOf(key) != -1) {
						aUrl = value + "/" + service;
					}
				}
			}
		}
		if (log.isDebugEnabled() && aUrl != null) {
			log.debug("Hittat endpoint: " + aUrl);
		}
		
		return new URL(aUrl);
	}


	/**
	 * 
	 * @param csnDataTransferObjectImplResult c
	 * @return CsnDataTransferObjectImpl
	 * @throws CsnApplicationException e
	 */
	protected CsnDataTransferObjectImpl checkRespons(CsnDataTransferObjectImpl csnDataTransferObjectImplResult)
			throws CsnApplicationException {
		if (csnDataTransferObjectImplResult instanceof CsnDataTransferObjectImpl) {
			CsnDataTransferObjectImpl dto = (CsnDataTransferObjectImpl) csnDataTransferObjectImplResult;

			if (dto.isException()) {
				CsnException ce = FormatConvertException.dtoExceptionToCsnException(dto.getDTOException());
				if (ce.getType().equals(CsnException.APP)) {
					CsnApplicationException cae = (CsnApplicationException) ce;

					throw cae;
				} else if (ce.getType().equals(CsnException.SYSTEM)) {
					CsnSystemException cse = (CsnSystemException) ce;

					throw cse;
				}
			} else {
				return csnDataTransferObjectImplResult;
			}
		}
		return null;
	}

}
