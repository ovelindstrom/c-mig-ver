package se.csn.ark.common.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import se.csn.ark.common.CsnArkBaseObjectImpl;

/**
 * Basklass för sessions bönor.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041201
 * @version 1 skapad
 *
 */
public class CsnSessionBeanImpl extends CsnArkBaseObjectImpl implements CsnSessionBean {

	private SessionContext sessionContext;

	/**
	 * skapa instans
	 */
	public CsnSessionBeanImpl() {
		super();
	}

	/**
	 * @return context för denna session-bean
	 */
	public SessionContext getSessionContext() {
		
		return sessionContext;
	}

	/**
	 * Implementation av javax.ejb.SessionBean
	 * 
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public void setSessionContext(SessionContext context)
		throws EJBException, RemoteException {

		this.sessionContext = context;
	}

	/**
	 * Implementation av javax.ejb.SessionBean
	 * 
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException, RemoteException {

	}

	/**
	 * Implementation av javax.ejb.SessionBean
	 * 
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException, RemoteException {

	}

	/**
	 * Implementation av javax.ejb.SessionBean
	 * 
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException, RemoteException {

	}

}
