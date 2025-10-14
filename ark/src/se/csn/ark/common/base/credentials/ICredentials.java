package se.csn.ark.common.base.credentials;

/**
 * Detta interface definierar det beteende som olika klasser skall uppvisa för 
 * att kunna användas vid autentisering i olika miljöer.
 * 
 * @author Vincent Wong, Iocore
 * @since 2002-08-27, v1.0
 * @version 1.0, initial
 */
public interface ICredentials {
	
	/**
	 * Denna metod returnerar ett användarid som skall användas vid autentisering 
     * mot exempelvis Racf eller annan miljö.
     * @return användar-id
	 */
	public String getUserid();
	
	/**
	 * Denna metod returnerar ett lösenord som skall användas vid autentisering mot 
     * exempelvis Racf i MVS eller annan miljö.
     * @return lösenord
	 */
	public String getPassword();

}

