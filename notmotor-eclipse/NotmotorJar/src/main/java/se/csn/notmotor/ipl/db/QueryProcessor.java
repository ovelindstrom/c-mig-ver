package se.csn.notmotor.ipl.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface QueryProcessor {
    /**
     * @param SQL SQL-kommando som ska köras
     * @return Antalet rader i databasen som påverkades, -1 om 
     * anropet misslyckades utan exception
     * @throws RuntimeException om något gick fel
     */
    public int executeThrowException(String SQL);

    public int executeSafely(String SQL);

    public int getInt(String SQL, int returnIfNoResult);

    public long getLong(String SQL, long returnIfNoResult);

    public boolean getBoolean(String SQL, boolean returnIfNoResult);

    public String getString(String SQL, String returnIfNoResult);

    public String[] getStringArray(String SQL, String[] returnIfNoResult);

    /**
     * Skapar java-objekt från en databasfråga. 
     * @param SQL sql-fråga
     * @param mapper Ett objekt som kan översätta en rad i svaret till 
     * 		  ett Java-objekt
     *
     */
    public List processQuery(String SQL, RowToObjectMapper mapper);
    
    public Object getObject(String SQL, RowToObjectMapper mapper);
    
    public long getSequence(String sequenceName); 
    
    public long getCounter(String table, String counterName,
            String nameCol, String valueCol);

    public long getCounter(String table, String counterName);
    
    public String getClob(ResultSet rs, String colName) throws SQLException;
    
    public byte[] getBlob(ResultSet rs, String colName) throws SQLException;
    
    /**
     * @throws RuntimeException om det inte gick att få en ny connection. 
     */
    public Connection getConnection();

    /**
     * Sätter den connection som ska användas. QueryProcessorn kommer att använda 
     * denna connection tills en annan connection satts.  
     * @param conn Connection-objektet som ska användas
     * @param handleConnection true om det nya Connection-objektet ska hanteras som 
     *        vanligt, false annars
     */
    public void setConnection(Connection conn, boolean handleConnection);
    
    public void addQueryListener(QueryListener ql);
    public void removeQueryListener(QueryListener ql);
    public List getQueryListeners();
    
    

}