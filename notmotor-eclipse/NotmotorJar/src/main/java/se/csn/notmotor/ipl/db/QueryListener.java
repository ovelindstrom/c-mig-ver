package se.csn.notmotor.ipl.db;


public interface QueryListener {
    public void sqlQuery(String sql);

    public void sqlError(String error);
}
