/**
 * @since 2007-maj-28
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;


public interface QueryListener {
    public void sqlQuery(String sql);

    public void sqlError(String error);
}
