package se.csn.ark.common.base.caching;

import se.csn.ark.common.CsnApplicationException;

/**
 * @author Vincent Wong
 * @version 1.0
 * @since 2002-02-28, v1.0
 * 
 * Denna exception kan användas då en egenskriven cache (som då bör implementera 
 * IReloadableCache och som vid instansiering kan registrera sig i en SingletonKeeper) 
 * av någon anledning misslyckas.
 * 
 * @see se.csn.caching.IReloadableCache and se.csn.caching.SingletonKeeper.
 */
public class CacheReloadException extends CsnApplicationException {

	/**
	 * @param message skapa exception med meddelandet
	 */
	public CacheReloadException(String message) {
		super(message);
	}

	/**
	 * @param throwable skapa exception med orsak
	 */
	public CacheReloadException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * @param throwable orsak
	 * @param message meddelandeinfo
	 */
	public CacheReloadException(Throwable throwable, String message) {
		super(message, throwable);
	}

}