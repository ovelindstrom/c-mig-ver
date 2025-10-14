/**
 * Skapad 2007-mar-13
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface RowToObjectMapper {
    /**
     * Anropas för varje ny rad i ResultSetet
     */
    public Object newRow(ResultSet rs) throws SQLException;
}
