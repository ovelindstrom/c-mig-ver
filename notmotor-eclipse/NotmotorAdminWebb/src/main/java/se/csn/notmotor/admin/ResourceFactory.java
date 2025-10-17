/**
 * @since 2007-apr-16
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.admin;

import se.csn.notmotor.ipl.db.DAOHandelse;
import se.csn.notmotor.ipl.db.DAOMeddelande;
import se.csn.notmotor.ipl.db.DAOSchema;
import se.csn.notmotor.ipl.db.DAOServer;
import se.csn.notmotor.ipl.db.DAOStatus;
import se.csn.notmotor.ipl.db.ParameterKalla;
import se.csn.notmotor.ipl.db.QueryProcessor;


public interface ResourceFactory {

    void setTransactionIsolationLevel(int transactionIsolationLevel);

    ParameterKalla getParameterKalla();

    QueryProcessor getQueryProcessor();

    DAOSchema getDAOSchema();

    DAOServer getDAOServer();

    DAOStatus getDAOStatus();

    DAOMeddelande getDAOMeddelande();

    DAOHandelse getDAOHandelse();
}
