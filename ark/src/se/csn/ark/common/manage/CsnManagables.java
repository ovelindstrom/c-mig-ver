package se.csn.ark.common.manage;

import se.csn.ark.common.util.FormatDate;
import se.csn.ark.common.util.logging.Log;


/**
 * Grupperar ihop en eller flera hanterbara funktioner
 * till en sammanhållen tjänst.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20050404
 * @version 1 skapad
 *
 */
public class CsnManagables implements CsnManagable {
	private static Log log = Log.getInstance(CsnManagables.class);
	private CsnManagable[] managables;
	private StringBuffer statusBuffer = new StringBuffer();
	private int state = STATE_NOT_INITIATED;

	/**
	 * Sätter de Managables som skall hanteras.
	 *
	 * @param theManagables Array med det som skall hanteras.
	 */
	protected void setManagables(CsnManagable[] theManagables) {
		this.managables = theManagables;
	}




	/**
	 * Anropar doInit för alla CsnManagables i den här tjänsten.
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#doInit()
	 */
	public void init() throws UnManagableException {
		int i = 0;

		statusBuffer.append("init - " + FormatDate.getCurrentTimeSeparatedString()
		                    + "\n");

		try {
			for (i = 0; i < managables.length; i++) {
				managables[i].init();
			}
		} catch (Exception e) {
			log.error(
			          "\n\t" + managables[i].getManagableClass().getName()
			          + " failed!",
			          e);
			throw new UnManagableException(e);
		}

		state = STATE_INITIATED;
	}




	/**
	 * Anropar doStart för alla CsnManagables i den här tjänsten.
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#doStart()
	 */
	public void start() throws UnManagableException {
		int i = 0;

		statusBuffer.append("start - " + FormatDate.getCurrentTimeSeparatedString()
		                    + "\n");

		try {
			for (i = 0; i < managables.length; i++) {
				managables[i].start();
			}
		} catch (Exception e) {
			log.error(
			          "\n\t" + managables[i].getManagableClass().getName()
			          + " failed!",
			          e);
			throw new UnManagableException(e);
		}

		state = STATE_RUNNING;
	}




	/**
	 * Anropar doStop för alla CsnManagables i den här tjänsten.
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#doStop()
	 */
	public void stop() throws UnManagableException {
		int i = 0;

		statusBuffer.append("stop - " + FormatDate.getCurrentTimeSeparatedString()
		                    + "\n");

		try {
			for (i = 0; i < managables.length; i++) {
				managables[i].stop();
			}
		} catch (Exception e) {
			log.error(
			          "\n\t" + managables[i].getManagableClass().getName()
			          + " failed!",
			          e);
			throw new UnManagableException(e);
		}

		state = STATE_STOPPED;
	}




	/**
	 * Anropar doClose för alla CsnManagables i den här tjänsten.
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#doClose()
	 */
	public void close() throws UnManagableException {
		int i = 0;

		statusBuffer.append("close - " + FormatDate.getCurrentTimeSeparatedString()
		                    + "\n");

		try {
			for (i = 0; i < managables.length; i++) {
				managables[i].close();
			}
		} catch (Exception e) {
			log.error(
			          "\n\t" + managables[i].getManagableClass().getName()
			          + " failed!",
			          e);
			throw new UnManagableException(e);
		}

		state = STATE_CLOSED;
	}




	/**
	 * Anropar doGetStatus för alla CsnManagables i den här tjänsten.
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#doGetStatus()
	 */
	public String getStatus() {
		int currentState = STATE_NOT_INITIATED;
		int i = 0;

		String str = "STATUS:\n";

		str += statusBuffer.toString();

		try {
			currentState = getState();
		} catch (UnManagableException ume) {
			log.error("Failed to get state", ume);
		}

		str += ("State : " + STATES_TEXTS[currentState] + "\n");
		str += "Managables status:\n";

		for (i = 0; i < managables.length; i++) {
			try {
				str += (managables[i].getStatus() + "\n");
			} catch (Exception e) {
				log.error(
				          "\n\t" + managables[i].getManagableClass().getName()
				          + " failed!",
				          e);
			}
		}

		return str;
	}




	/**
	 * Denna metod är inte relevant för denna i klass som innehåller
	 * innehåller en eller flera Managables.
	 *
	 * @return null
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#getManagableClass()
	 */
	public Class getManagableClass() {
		return null;
	}




	/**
	 * Returnerar tillståndet för tjänsten.
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#getState()
	 */
	public int getState() throws UnManagableException {
		return state;
	}
}