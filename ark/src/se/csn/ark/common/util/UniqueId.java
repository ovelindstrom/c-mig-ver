package se.csn.ark.common.util;

/**
 * 
 * Skapar ett unikt id
 * 
 * @author Daniel Nordkvist
 * @since 20041110
 * @version 1 skapad
 *
 */
public final class UniqueId {
	
	private static long current = System.currentTimeMillis();
	
    /**
     * Privat konstruktor, endast statisk åtkomst
     */
    private UniqueId() {
    }




	/**
	 * @return ett unikt id
	 */
	public static synchronized long get() {
		return current++;
	}
	
}
