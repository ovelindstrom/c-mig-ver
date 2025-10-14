package se.csn.ark.common.base.credentials;

import se.csn.ark.common.CsnApplicationException;

/**
 * @author Vincent Wong
 * @version 1.0
 * @since 2002-02-28, v1.0
 * 
 * Denna exception kan användas då en egenskriven klass för Credentials 
 * (som då bör implementera ICredentials) av någon anledning misslyckas.
 * 
 * @see se.csn.credentials.ICredentials
 */
public class CredentialsException extends CsnApplicationException {

    /**
     * @param message skapa exception med meddelandet
     */
	public CredentialsException(String message) {
		super(message);
	}

    /**
     * @param throwable skapa exception med orsak
     */
	public CredentialsException(Throwable throwable) {
		super(throwable);
	}

    /**
     * @param throwable orsak
     * @param message meddelandeinfo
     */
	public CredentialsException(Throwable throwable, String message) {
		super(message, throwable);
	}
}