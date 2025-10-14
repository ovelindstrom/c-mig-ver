/**
 * Skapad 2007-apr-02
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.ft;

import javax.sql.DataSource;

import se.csn.notmotor.ipl.MeddelandeMottagare;
import se.csn.notmotor.ipl.db.DAOAvsandareImpl;
import se.csn.notmotor.ipl.db.DAOBilagaImpl;
import se.csn.notmotor.ipl.db.DAOHandelseImpl;
import se.csn.notmotor.ipl.db.DAOMeddelande;
import se.csn.notmotor.ipl.db.DAOMeddelandeImpl;
import se.csn.notmotor.ipl.db.DAOMottagareImpl;
import se.csn.notmotor.ipl.db.QueryListenerImpl;
import se.csn.notmotor.ipl.db.QueryProcessor;
import se.csn.notmotor.ipl.db.WebServiceQueryProcessor;

/**
 * Factory-klass som hanterar skapandet av nya notifieringProxies.
 * Poolar underliggande proxy. Returnerar en Decorator som returnerar
 * proxyn till poolen automatiskt. 
 * 
 */
public class NotifieringProxyFactoryImpl implements NotifieringProxyFactory {

    //private Log log = Log.getInstance(NotifieringProxyFactoryImpl.class);
    
    private static int noofcreates = 0;
    private DataSource ds;

    public NotifieringProxyFactoryImpl(DataSource ds) {
        this.ds = ds;
    }

    public synchronized NotifieringProxy getNotifieringProxy() {
        noofcreates++;
        //log.debug("Levererade  proxy no " + noofcreates);
        QueryProcessor qp = new WebServiceQueryProcessor(ds);
        qp.addQueryListener(new QueryListenerImpl("WEBSERVICE"));
        DAOMeddelande dao = new DAOMeddelandeImpl(
            qp,
            new DAOAvsandareImpl(qp),
            new DAOMottagareImpl(qp),
            new DAOBilagaImpl(qp),
            new DAOHandelseImpl(qp));
        MeddelandeMottagare mm = new MeddelandeMottagare();
        return new NotifieringProxyImpl(dao, qp, mm);
    }
}
