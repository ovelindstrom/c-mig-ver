package se.csn.ark.common.manage.ejb;

import se.csn.ark.common.ejb.CsnSessionBeanImpl;
import se.csn.ark.common.manage.CsnManagable;
import se.csn.ark.common.manage.CsnManagables;
import se.csn.ark.common.manage.UnManagableException;
import se.csn.ark.common.util.logging.Log;

import java.rmi.RemoteException;

import javax.ejb.EJBException;


/**
 * Basklass för bönor som skall starta upp och hantera tjänster.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041201
 * @version 1 skapad
 *
 */
public abstract class CsnManagerBean extends CsnSessionBeanImpl
	implements CsnManagable {
	private static Log log = Log.getInstance(CsnManagerBean.class);
	private CsnManagables managables;
	private long startTime;

	/**
	 * skapa instans
	 */
	public CsnManagerBean() {
		super();
		startTime = System.currentTimeMillis();
		managables = getManagables();

		if (log.isDebugEnabled()) {
			String debugStr = "\n" + getManagableClass().getName();

			debugStr += ("\nmanagables=\n[" + managables + "]");
			log.debug(debugStr);
		}
	}

	/**
	 * Ger den/de instanser som skall hanteras.
	 *
	 * @return En array med hanterabara kalsser.
	 */
	protected abstract CsnManagables getManagables();




	/**
	 * Implementation av se.csn.ark.common.manage.CsnManagable
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#doInit()
	 */
	public void init() throws UnManagableException {
		managables.init();
	}




	/**
	 * Implementation av se.csn.ark.common.manage.CsnManagable
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#doStart()
	 */
	public void start() throws UnManagableException {
		managables.start();
	}




	/**
	 * Implementation av se.csn.ark.common.manage.CsnManagable
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#doStop()
	 */
	public void stop() throws UnManagableException {
		managables.stop();
	}




	/**
	 * Implementation av se.csn.ark.common.manage.CsnManagable
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#doClose()
	 */
	public void close() throws UnManagableException {
		managables.close();
	}




	/**
	 * Implementation av se.csn.ark.common.manage.CsnManagable
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#doGetStatus()
	 */
	public String getStatus() {
		return managables.getStatus();
	}




	/**
	 * Implementation av se.csn.ark.common.manage.CsnManagable
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#doClose()
	 */
	public int getState() throws UnManagableException {
		return managables.getState();
	}




	/**
	 * Implementation av javax.ejb.SessionBean
	 *
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException, RemoteException {
		if (log.isDebugEnabled()) {
			log.debug("ejbRemove");
		}

		try {
			close();
		} catch (Exception e) {
			log.error(e);
			throw new RemoteException("CsnManagerBean.ejbRemove", e);
		}
	}




	/**
	 * Implementation av javax.ejb.SessionBean
	 *
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException, RemoteException {
		if (log.isDebugEnabled()) {
			log.debug("ejbActivate");
		}

		try {
			start();
		} catch (Exception e) {
			log.error(e);
			throw new RemoteException("CsnManagerBean.ejbActivate", e);
		}
	}




	/**
	 * Implementation av javax.ejb.SessionBean
	 *
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException, RemoteException {
        final long millisPerSecond = 1000;
        final long secondsPerMinute = 60;
		long stopTime;
		long totalMillis;
		long totalSec;
		long totalMin;

		if (log.isDebugEnabled()) {
			log.debug("ejbPassivate");
		}

		stopTime = System.currentTimeMillis();
		totalMillis = stopTime - startTime;
		totalSec = totalMillis / millisPerSecond;
		totalMin = totalSec / secondsPerMinute;

		String logStr =
			"PASSIVATED after" + totalMillis + "ms, ~ " + totalSec + "s, ~ "
			+ totalMin + "min";

		if (log.isDebugEnabled()) {
			log.debug(logStr);
		}

		//		try {
		if (log.isDebugEnabled()) {
			log.debug(logStr);
		}

		if (log.isDebugEnabled()) {
			log.debug("did not call doStop ...");
		}

		//			doStop();
		//		}
		//		catch (UnManagableException ume) {
		//			log.error(ume);
		//			throw new RemoteException("CsnManagerBean.ejbRemove", ume);
		//		}
	}
}