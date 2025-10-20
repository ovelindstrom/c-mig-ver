package se.csn.notmotor.admin;

import javax.sql.DataSource;

import se.csn.notmotor.ipl.db.DAOAvsandare;
import se.csn.notmotor.ipl.db.DAOAvsandareImpl;
import se.csn.notmotor.ipl.db.DAOBilaga;
import se.csn.notmotor.ipl.db.DAOBilagaImpl;
import se.csn.notmotor.ipl.db.DAOHandelse;
import se.csn.notmotor.ipl.db.DAOHandelseImpl;
import se.csn.notmotor.ipl.db.DAOMeddelande;
import se.csn.notmotor.ipl.db.DAOMeddelandeImpl;
import se.csn.notmotor.ipl.db.DAOMottagare;
import se.csn.notmotor.ipl.db.DAOMottagareImpl;
import se.csn.notmotor.ipl.db.DAOSchema;
import se.csn.notmotor.ipl.db.DAOSchemaImpl;
import se.csn.notmotor.ipl.db.DAOServer;
import se.csn.notmotor.ipl.db.DAOServerImpl;
import se.csn.notmotor.ipl.db.DAOStatus;
import se.csn.notmotor.ipl.db.DAOStatusImpl;
import se.csn.notmotor.ipl.db.ParameterCache;
import se.csn.notmotor.ipl.db.ParameterKalla;
import se.csn.notmotor.ipl.db.QueryListener;
import se.csn.notmotor.ipl.db.QueryListenerImpl;
import se.csn.notmotor.ipl.db.QueryProcessor;
import se.csn.notmotor.ipl.db.QueryProcessorImpl;


public class ResourceFactoryImpl implements ResourceFactory {

    private final DataSource ds;
    private int transactionIsolationLevel;


    public int getTransactionIsolationLevel() {
        return transactionIsolationLevel;
    }

    public void setTransactionIsolationLevel(int transactionIsolationLevel) {
        this.transactionIsolationLevel = transactionIsolationLevel;
    }

    public ResourceFactoryImpl(DataSource ds, int transactionIsolationLevel) {
        this.ds = ds;
        this.transactionIsolationLevel = transactionIsolationLevel;
    }

    public ParameterKalla getParameterKalla() {
        return new ParameterCache(getQueryProcessor());
    }

    /**
     * QueryListenern som kommer att skriva all SQL och alla fel till loggen.
     * 
     * @return En query processor med tillhörande QueryListener. 
     */
    public QueryProcessor getQueryProcessor() {
        QueryListener ql = new QueryListenerImpl("ADMINWEBB");
        QueryProcessor qp = new QueryProcessorImpl(ds, transactionIsolationLevel);
        qp.addQueryListener(ql);
        return qp;
    }

    public DAOSchema getDAOSchema() {
        return new DAOSchemaImpl(getQueryProcessor());
    }

    public DAOServer getDAOServer() {
        return new DAOServerImpl(getQueryProcessor());
    }

    public DAOStatus getDAOStatus() {
        return new DAOStatusImpl(getQueryProcessor());
    }

    public DAOMeddelande getDAOMeddelande() {
        QueryProcessor qp = getQueryProcessor();
        DAOAvsandare daoavsandare = new DAOAvsandareImpl(qp);
        DAOMottagare daomottagare = new DAOMottagareImpl(qp);
        DAOBilaga daobilaga = new DAOBilagaImpl(qp);
        DAOHandelse daohandelse = new DAOHandelseImpl(qp);
        return new DAOMeddelandeImpl(qp, daoavsandare, daomottagare, daobilaga, daohandelse);
    }

    public DAOHandelse getDAOHandelse() {
        return new DAOHandelseImpl(getQueryProcessor());
    }
}
