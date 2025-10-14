/**
 * Skapad 2007-feb-05
 * @author Jonas Öhrnell (csn7821)
 *
 */
package se.csn.common.util.cache;

/**
 * Interface för Cacheobjekt.
 * Nödvändigt med särskilt interface för att vi ska kunna styra cachebeteende.
 * Detta interface fyller en annan funktion än IReloadableCache. IReloadableCache-cachar
 * är wrappers runt en speciell sorts data. Denna typ av cache är bara en vanlig Map.
 * Skapad 2007-feb-05
 * @author Jonas Öhrnell (csn7821)
 */
public interface Cache {
    /**
     * Hämtar ett objekt i cachen
     * @param key Nyckel till objektet
     * @return ett cachat objekt om ett sådant fanns i cachen, annars null
     */
    Object get(Object key);
    /**
     * Lägger till ett objekt i cachen. Skriver över befintligt objekt om 
     * det redan fanns ett.
     * @param key Nyckel till objektet
     * @param value Det objekt som ska sparas i cachen.
     */
    void put(Object key, Object value);
    /**
     * Tömmer cachen på objekt.
     */
    void clear();
}