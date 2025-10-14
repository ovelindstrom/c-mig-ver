package se.csn.ark.common.base.genproxy;

import se.csn.ark.common.CsnSystemException;


/**
 * Indikerar fel på COOL/GEN service.
 * 
 * @author K-G Sjöström
 * @since 20040901
 * @version 1.1 ändrad typ från CsnApplicationException till CsnSystemException
 * @version 1 skapad
 *
 */
public class GenProxyException extends CsnSystemException {
	private String serviceName;
	
	/**
	 * @param message felmeddelande
	 * @param serviceName den coolgen-tjänst som felar
	 */
	public GenProxyException(String message, String serviceName) {
		super("[" + serviceName + "] " + message, COOLGEN_ERROR);
		this.serviceName = serviceName;
	}

    /**
     * @param message felmeddelande
     * @param serviceName den coolgen-tjänst som felar
     * @param cause exception som orsakat felet
     */
	public GenProxyException(String message, String serviceName, Throwable cause) {
		super("[" + serviceName + "] " + message, COOLGEN_ERROR, cause);
		this.serviceName = serviceName;
	}

	/**
	 * @return den tjänst som felar
	 */
	public String getServiceName() {
		return serviceName;
	}

	
}