package se.csn.ark.common.manage;

import se.csn.ark.common.util.logging.Log;

import java.util.Date;


/**
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041130
 * @version 1 skapad
 *
 */
public class ManagableThreadDummyImpl extends CsnManagableThread {
	private static Log log = Log.getInstance(ManagableThreadDummyImpl.class);
	private boolean moreToDo = false;
	private boolean running = false;
	private int calls;

    private static final long SLEEP_TIME = 30000; 
    private static final int NO_EXCEPTIONS = 1; 

	/**
	 * skapa thread
	 */
	public ManagableThreadDummyImpl() {
		super(SLEEP_TIME, NO_EXCEPTIONS, null);
	}

	/**
	 * @see se.csn.ark.common.manage.CsnManagableThread#doInit()
	 */
	public void doInit() throws UnManagableException {
		if (log.isDebugEnabled()) {
			log.debug("doInit");
		}
	}




	/**
	 * @see se.csn.ark.common.manage.CsnManagableThread#doClose()
	 */
	public void doClose() throws UnManagableException {
		running = false;

		if (log.isDebugEnabled()) {
			log.debug("doClose");
		}
	}




	/** 
     * Simulera att det ibland finns mer att göra och ibland inte.
	 * @see se.csn.ark.common.manage.CsnManagableThread#hasMoreToDo()
	 */
	public boolean hasMoreToDo() throws UnManagableException {

		if (moreToDo) {
			moreToDo = false;
		} else {
			moreToDo = true;
		}

		if (log.isDebugEnabled()) {
			log.debug("hasMoreToDo=" + moreToDo);
		}

		return moreToDo;
	}




	/**
	 * @see se.csn.ark.common.manage.CsnManagableThread#doMore()
	 */
	public void doMore() throws UnManagableException {
		// Är detta första anropet eller har vi redan startat?
		if (!running) {
			running = true;
		}

		if (log.isDebugEnabled()) {
			log.debug("doMore " + new Date());
		}

		// Ännu ett låtsas anrop till något ;-)
		calls++;
	}




	/**
	 * @see se.csn.ark.common.manage.CsnManagableThread#doGetStatus()
	 */
	public String doGetStatus() {
		String status;

		status =
			this.getClass().getName() + " running=" + running + " calls="
			+ calls;

		return status;
	}




	/**
	 * @see se.csn.ark.common.manage.CsnManagable#getManagableClass()
	 */
	public Class getManagableClass() {
		return this.getClass();
	}
}