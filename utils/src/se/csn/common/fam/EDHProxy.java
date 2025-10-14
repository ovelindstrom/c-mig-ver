package se.csn.common.fam;

import se.csn.ark.common.dal.EdhException;

/**
 * Skapad 2007-feb-16
 * @author Jonas Öhrnell (csn7821)
 * 
 * EDHProxy är det interface som används när man skickar meddelanden till EDH. 
 */
public interface EDHProxy {
    /**
     * Lagrar ett dokument med datadelen i filen utpekad av fileName i EDH.
     * @param doc Viktiga data för objektet
     * @param fileName 
     */
    public DTOFamId storeDocument(String archive, DTOSignedDocument doc) throws EdhException;
    
    /**
     * Hämtar data från EDH. 
     * @param famID för dokumentet
     */
    public DTORetrievedDocument retrieveDocument(String archive, String famId) throws EdhException;
    
    /**
     * Committar det angivna dokumentet till arkivet
     * @param famID för dokumentet
     */
    public void commit(String archive, String famId) throws EdhException;
    
    /**
     * Gör rollback på det angivna dokumentet 
     * @param famID för dokumentet
     */
    public void rollback(String archive, String famId) throws EdhException;
    
}
