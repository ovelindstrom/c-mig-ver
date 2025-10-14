package se.csn.ark.common.base.credentials;

import se.csn.ark.common.base.caching.CacheReloadException;

/**
 * Denna factory-klass tillverkar instanser av olika klasser som implementerar ICredentials.
 * get*Credentials()-metoderna skall användas för att erhålla dylika instanser, snarare än
 * att själv instansiera dessa.
 * 
 * Av denna anledning bör klasser som implementerar ICredentials definiera sin 
 * default-constructor som protected (alternativt någon getInstance()-metod) och 
 * skall definieras i samma paket som denna klass.
 *
 * @author Vincent Wong, Iocore
 * @since 2002-08-27, v1.0
 * @version v1.0
 */
public final class CredentialsFactory {

	/**
	 * Konstant som anger att önskad typ av ICredentials är för RACF-autentisering.
	 */
	public static final int RACF = 1;

    /**
     * Ska inte gå att skapa, endast statiska metoder 
     */
    private CredentialsFactory() {
    }
    

	/**
	 * @return default-credentials
	 * @throws CredentialsException gick ej att skapa
	 */
	public static ICredentials getDefaultCredentials()
		throws CredentialsException {
		return getRACFCredentials();
	}


    /**
     * @return RACF-credentials
     * @throws CredentialsException gick ej att skapa
     */
	public static ICredentials getRACFCredentials() throws CredentialsException {
		try {
			return RACFCredentials.getInstance();
		} catch (CacheReloadException cre) {
			throw new CredentialsException(
				cre,
				cre.getClass().getName() + ": " + cre.getMessage());
		}
	}

/**
 * @param type anger typ av önskad returnerad ICredentials. Ett anrop med type=CredentialsFactory.
 * RACF är likvärdigt med ett anrop av getRACFCredentials().
 * @return Beroende på type returneras olika klasser implementerande ICredentials.
 * @throws CredentialsException gick ej att skapa 
 * @see se.csn.caching.CacheReloadException.RACF
 */
	public static ICredentials getCredentials(int type)
		throws CredentialsException {
		switch (type) {
			case RACF :
				return getRACFCredentials();
//				break;
			default :
			throw new CredentialsException(null, "Otillåtet värde (" + type 
                + ") på CredentialsFactory type.");
				}
	}
	// Addera fler get*Credentials()-metoder här då det dyker upp fler typer av autentiseringar!
}