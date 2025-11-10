package se.csn.notmotor.ipl.db;

import se.csn.ark.common.util.logging.Log;


public class QueryListenerImpl implements QueryListener {

    private Log log = Log.getInstance(QueryListenerImpl.class);
    private String logPrefix;

    /**
     * @param logPrefix En sträng som kommer att skrivas ut före varje 
     *   sql- eller error-sträng.
     * Exempel: Om du anger prefixet "NOTMOTOR #1" och sql-satsen 
     * SELECT ID FROM MEDDELANDE körs, så loggas följande:
     * NOTMOTOR #1: SELECT ID FROM MEDDELANDE
     */
    public QueryListenerImpl(String logPrefix) {
        this.logPrefix = logPrefix;
    }

    public void sqlError(String error) {
        log.error(logPrefix + ": ** ERROR **: " + error);
    }

    public void sqlQuery(String sql) {
        if (sql.indexOf("PARAMETER") >= 0) {
            return;
        }
        log.debug(logPrefix + ": " + sql);
    }
}
