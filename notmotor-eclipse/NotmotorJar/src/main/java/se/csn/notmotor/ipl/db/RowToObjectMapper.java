package se.csn.notmotor.ipl.db;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface RowToObjectMapper {
    /**
     * Anropas for varje ny rad i ResultSetet
     */
    public Object newRow(ResultSet rs) throws SQLException;
}
