package se.csn.ark.common.base.caching;

import java.util.Enumeration;
import java.util.Vector;
/**
 * ObjectKeeper skall instansieras när man startar den virtuella maskinen (t.ex. Websphere-servern).
 * Den håller sedan reda på (referenser till) alla singleton-objekt eller andra objekt som man 
 * vill förhindra att garbage collectorn tar bort.
 * Detta t.ex. för att tillse att olika egna cachar inte försvinner då ingen längre för tillfället
 * råkar referera till dem.
 * 
 * Den följer själv Singleton-mönstret.
 * 
 * Alla objekt som man vill skall refereras till av detta objekt (= ej kunna tas bort av GC:n) 
 * måste själva "registrera" sig hos denna klass medelst metoden addObject().
 * 
 * Alla objekt som registrerats kan man sedan (om de implementerar IReloadableCache) fås att
 * ladda om sig medelst metoden reloadAllObjects.
 * 
 * @author Vincent Wong, Iocore
 * @since 2002-08-27, v1.0
 * @version 1.0
 */
public final class ObjectKeeper {
	
	// Självreferensen:
	private static ObjectKeeper myself;
	
	// Alla registerade objekt vi har referenser till:
	private Vector objects;

	/**
	 * Skapa ObjectKeeper. Privat, använd getInstance istället 
	 */
	private ObjectKeeper() {
		objects = new Vector();
	}
	
	/**
	 * Använd denna metod för att erhålla en instans av denna klass(den enda).
     * @return singelton-instansen
	 */
	public static ObjectKeeper getInstance() {
		if (myself == null) {
			myself = new ObjectKeeper();
		}
		return myself;
	}
	
	/**
	 * Använd denna metod för att "registrera" ett godtyckligt java-objekt hos ObjectKeepern.
	 * Registrerade objekt är sedan refererade till av denna klass för att förhindra att garbage
	 * collectorn städar bort dem (under förutsättning av ObjectKeepern själv inte städas bort).
     * @param object som ska registreras
	 */
	public void addObject(Object object) {
		objects.add(object);
	}
	
	/**
	 * Går igenom alla object som registrerats med addObject() och ifall de implementerar 
     * IReloadableCache så anropas dess reload-metod.
     * @throws CacheReloadException gick ej att ladda om
	 */
	public void reloadAllObjects() throws CacheReloadException {
		Enumeration enumOld = objects.elements();
		
		while (enumOld.hasMoreElements()) {
			try {
				IReloadableCache iReloadableCache = (IReloadableCache) enumOld.nextElement();
				iReloadableCache.reload();
			} catch (ClassCastException c) {
				// Om detta inte var ett objekt som implementerade IReloadableCache 
                // så ignorerar vi det bara.
			}
		}
	}
	/**
	 * @return Returnerar en Vector innehållande alla Object som registrerats medelst addObject.
	 */
	public Vector getObjects() {
		return objects;
	}

}

