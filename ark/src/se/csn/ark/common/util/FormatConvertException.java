/*
 * Created on 2006-aug-29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package se.csn.ark.common.util;

import se.csn.ark.common.CsnApplicationException;
import se.csn.ark.common.CsnException;
import se.csn.ark.common.CsnSystemException;
import se.csn.ark.common.DTOException;

/**
 * @author csn7543
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class FormatConvertException {

	/**
	 * konvertera från dto till exception
	 * @param dtoException dto att konvertera
	 * @return exception-representation av dto
	 */
	public static CsnException dtoExceptionToCsnException(DTOException dtoException) {
		CsnException csnException = null;

		if (dtoException.getType().equals(CsnException.APP)) {
			csnException = CsnApplicationException.reCreateCsnException(dtoException.getMessage(), dtoException.getErrorId());

		} else if (dtoException.getType().equals(CsnException.SYSTEM)) {
			csnException = CsnSystemException.reCreateCsnException(dtoException.getMessage(), dtoException.getErrorId());
		}

		return csnException;
	}
	
	/**
	 * konvertera från exception till dto
	 * @param csnException exception att konvertera
	 * @return dto-representation av exception
	 */
	public static DTOException csnExceptioToDTOException(CsnException csnException) {

		DTOException dtoException = new DTOException();

		if (csnException.getCause() == null) {
			dtoException.setCauseName(null);
		
		} else {
			dtoException.setCauseName(csnException.getCause().getClass().getName());
		}

		dtoException.setErrorId(csnException.getErrorId());
		dtoException.setMessage(csnException.getMessage());
		dtoException.setType(csnException.getType());

		return dtoException;
	}
}
