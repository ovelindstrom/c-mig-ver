/**
 * Skapad 2007-apr-16
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.config;

import se.csn.ark.common.dal.db.DatabaseException;

/**
 * Kontrollerar att angivna tabeller och kolumner finns; kan kontrollera att visst data finns. 
 * Kan kontrollera dataformat. 
 * 
 * TODO: Ovanstående samt metod som validerar mot en ddl-fil
 */
public class DatabaseTester {

    public static boolean tableExists(String tablename, String conn) {
        // TODO: Implementera 
        return true;
    }
    
    public static boolean columnExists(String tablename, String columnname, String conn) {
        // TODO: Implementera
        return true;
    }
    
    public void checkTableExistsThrowException(String tablename, String conn) {
        if(!tableExists(tablename, conn)) {
            throw new DatabaseException("Kounde inte hitta tabell " + tablename);
        }
    }
    
    public void checkColumnExistsThrowException(String tablename, String columnname, String conn) {
        if(!columnExists(tablename, columnname, conn)) {
            throw new DatabaseException("Kounde inte hitta kolumn " + columnname + " i tabell " + tablename);
        }
    }

    
}
