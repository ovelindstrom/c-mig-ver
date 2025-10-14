package se.csn.ark.common.manage;

import se.csn.ark.common.util.logging.Log;

/**
 * Grundklass för att köra hanterbara (CsnManagable) klasser i en tråd.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041130
 * @version 1 skapad
 *
 * @see CsnManagable
 */
public abstract class CsnManagableThread implements CsnManagable, Runnable {
	private static Log log = Log.getInstance(CsnManagableThread.class);
	private Thread managableThread;
	private long tradId;

	// Är tråden startad?
	private boolean threadStarted = false;

	// Flagga som visar om tråden körs.
	private boolean running = false;

	// Grundstillståndet.
	private int state = STATE_NOT_INITIATED;

	// Sätter sovtiden till 30 sekunder så att vi har ett grundvärde
	// för säkerhets skull. Detta värde ska dock sättas via konstruktorn.
	private static final long DEFAULT_SLEEP = 30000;
	private long sleepTime = DEFAULT_SLEEP;

	// Sätter antalet tillåtna exceptions till ett startvärde. 
	// Detta värde ska dock sättas via konstruktorn.
	private static final int DEFAULT_NO_EXCEPTIONS = 20;
	private int allowedNumberOfExceptions = DEFAULT_NO_EXCEPTIONS;

	// Räknare för antalet exceptions
	private int exceptionCounter = 0;

	//Flagga för run loop
	private boolean runForever = true;

	// Hanterare/övervakare för callback.
	private CsnManager manager;

	/**
	 * Skapar en ny hanterbar tråd.
	 *
	 * @param sleepTime Antalet millisekunder som tråden skall sova när det
	 * inte finns något att göra.
	 *
	 * @param allowedNumberOfExceptions Antalet tillåtna exceptions innan tråden
	 * inte kan räknas som hanterbar.
	 *
	 * @param manager En referens till det som hanterar/övervakar denna tråd.
	 * För enkelhets skull så skickas en referens med istället för att registrera
	 * CsnManager som lyssnare enligt, addManagableListener(CsnManager manager) och
	 * removeManagableListener(CsnManager manager), vilket skulle innebära att flera
	 * kan hantera/övervaka.
	 * Det får dock bli en senare implementation om behov finns.
	 *
	 */
	protected CsnManagableThread(
		long sleepTime,
		int allowedNumberOfExceptions,
		CsnManager manager) {
		this.sleepTime = sleepTime;
		this.allowedNumberOfExceptions = allowedNumberOfExceptions;
		this.manager = manager;
	}
	/**
	 * Lägger till id på denna tråd för kunna identifiera tråd
	 * @see se.csn.ark.common.manage.CsnManagableThread#CsnManagableThread(long, int, CsnManager)
	 */
	protected CsnManagableThread(
		long sleepTime,
		int allowedNumberOfExceptions,
		CsnManager manager, long tradId) {
			
			this(sleepTime, allowedNumberOfExceptions, manager);
			
			this.tradId = tradId;
			
			
	}
	/**
	 * Kör tråden?
	 *
	 * SKALL användas i doMore metoden för att kontrollera att tråden körs.
	 * Bör användas mellan varje tabellrad i databasen, varje köpost i JMS osv ...
	 * Körs det inte så avsluta doMore metoden.
	 *
	 * @return true om tråden körs.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Läs in konfiguration och sätt upp alla resurser så att allt är
	 * klart att köra.
	 *
	 * @throws UnManagableException Indikerar att något gått så pass fel
	 * att tjänsten inte längre kan hanteras.
	 * Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 *
	 */
	public abstract void doInit() throws UnManagableException;

	/**
	 * Stäng ner alla resurser.
	 *
	 * @throws UnManagableException Indikerar att något gått så pass fel
	 * att tjänsten inte längre kan hanteras.
	 * Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 *
	 */
	public abstract void doClose() throws UnManagableException;

	/**
	 * Frågar om det finns mer att göra.
	 * Om det inte finns mer för tillfället kommer tråden att sova en
	 * stund innan den frågar på nytt.
	 *
	 * @return true om det finns mer att göra.
	 *
	 * @throws UnManagableException Indikerar att något gått så pass fel
	 * att tjänsten inte längre kan hanteras.
	 * Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 *
	 */
	public abstract boolean hasMoreToDo() throws UnManagableException;

	/**
	 * Om det fanns mer att göra så skall det göra i denna metod.
	 *
	 *
	 * @throws UnManagableException Indikerar att något gått så pass fel
	 * att tjänsten inte längre kan hanteras.
	 * Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 *
	 */
	public abstract void doMore() throws UnManagableException;

	/**
	 * Strängrepresentation av status för det som hanteras.
	 *
	 * @return Information som talar status för denna tjänst.
	 */
	public abstract String doGetStatus();

	/**
	 * Initierar det som skall hanteras.
	 *
	 * @throws UnManagableException Indikerar att något gått så pass fel att tjänsten 
	 * inte kan initieras. Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 *
	 */
	public void init() throws UnManagableException {
		managableThread = new Thread(this, getManagableClass().getName() + ":" + tradId);
		runForever = true;
		try {
			doInit();
		} catch (UnManagableException ume) {
			log.error("doInit", ume);
			throw ume;
		} catch (Exception e) {
			log.error("doInit", e);
			throw new UnManagableException(e);
		}
		// Nollställare Räknare för antalet exceptions.
		exceptionCounter = 0;
		state = STATE_INITIATED;

		// Meddela tillståndet.
		if (manager != null) {
			manager.initiated(this);
		}
	}

	/**
	 * Startar exekveringen.
	 *
	 * @throws UnManagableException Indikerar att något gått så pass fel 
	 * att tjänsten inte kan startas.Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 *
	 */
	public void start() throws UnManagableException {
		try {
			running = true;

			if (!threadStarted) {
				managableThread.start();
				threadStarted = true;
			} else {
				try {
					synchronized (this) {
						notify();
					}
				} catch (IllegalMonitorStateException imse) {
					// Vi loggar inget här då tråden i alla fall startar.
				} catch (Exception e) {
					// Vi loggar inget här då tråden i alla fall startar.
				}
			}
		} catch (Exception e) {
			log.error("start", e);
			throw new UnManagableException(e);
		}

		state = STATE_RUNNING;

		// Meddela tillståndet.
		if (manager != null) {
			manager.started(this);
		}
	}

	/**
	 * Stoppar exekveringen utan att stänga ner resurser.
	 *
	 * @throws UnManagableException Indikerar att något gått så pass fel att tjänsten 
	 * inte kan stoppas. Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 *
	 */
	public void stop() throws UnManagableException {
		running = false;
		state = STATE_STOPPED;

		// Meddela tillståndet.
		if (manager != null) {
			manager.stopped(this);
		}
	}

	/**
	 * Stänger ner alla resurser för det som hanteras.
	 *
	 * @throws UnManagableException Indikerar att något gått så pass fel att tjänsten 
	 * inte kan stängas. Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 *
	 */
	public void close() throws UnManagableException {
		try {
			doClose();
		} catch (UnManagableException ume) {
			log.error("doClose", ume);
			throw ume;
		} catch (Exception e) {
			log.error("doClose", e);
			throw new UnManagableException(e);
		}

		// Nollställ till utgångsläge.
		threadStarted = false;
		running = false;

		//Stäng run loop
		runForever = false;
		//Se till att tråden vaknar ifrån ev wait läge 
		synchronized (this) {
			notify();
		}

		//Lämna ingen referens till trådobjektet, låt sopbilen städa upp det.
		managableThread = null;
		state = STATE_CLOSED;

		// Meddela tillståndet.
		if (manager != null) {
			manager.closed(this);
		}
	}

	/**
	 * Kör doMore och sover
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		if (log.isDebugEnabled()) {
			log.debug("run-" + tradId);
		}

		while (runForever) {
			try {
				synchronized (this) {
					if (!running) {
						if (log.isDebugEnabled()) {
							log.debug("wait-" + tradId);
						}
						wait();
					}
				}

				if (running) {
					if (hasMoreToDo()) {
						doMore();
					} else {
						sleep();
					}
				}
			} catch (InterruptedException ie) {
				log.error("run", ie);
			} catch (UnManagableException ume) {
				log.error("run", ume);

				// Kan vi inte hantera längre så slutar vi köra.
				// Meddela att det är kört!
				if (manager != null) {
					manager.unManagable(this, ume);
				}
			} catch (Exception e) {
				log.error("run", e);

				// Om vi får ett oväntat fel så slutar vi köra.
				// Meddela att det är kört!
				if (manager != null) {
					manager.unManagable(this, new UnManagableException(e));
				}
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Exit run-" + tradId);
		}
	}

	/**
	 * sov sleepTime
	 */
	private void sleep() {
		try {
			Thread.sleep(sleepTime);
		} catch (Exception e) {
			log.error("sleep", e);
		}
	}

	/**
	 * @see se.csn.ark.common.manage.CsnManagable#getStatus()
	 */
	public String getStatus() {
		String status = "--------------------------------------------------------\n";

		status += (getManagableClass().getName() + "\n");
		status += ("Tillstånd : " + STATES_TEXTS[state] + "\n");

		try {
			status += ("Status : " + doGetStatus() + "\n");

			// Något kan gå fel även då status skall hämtas så vi fångar för säkerhets skull.
		} catch (Exception e) {
			log.error("getStatus", e);
		}

		status += "--------------------------------------------------------";

		return status;
	}

	/**
	 * Get tillståndet för denna tjänst.
	 *
	 * @see se.csn.ark.common.manage.CsnManagable#getState()
	 *
	 * @return tillstånd
	 * @throws UnManagableException Indikerar att något gått så pass fel
	 * att tjänsten inte längre kan hanteras.
	 * Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 *
	 */
	public int getState() throws UnManagableException {
		return state;
	}

	/**
	 * Ökar på antalet fel och kastar exception när fler fel än tillåtet 
	 * har raporterats.
	 * @throws UnManagableException Indikerar att något gått så pass fel
	 * att tjänsten inte längre kan hanteras.
	 * Detta skall alltså INTE kastas om tjänsten kan hanteras.
	 */
	protected void incExceptionCounter() throws UnManagableException {
		exceptionCounter++;

		if (exceptionCounter > allowedNumberOfExceptions) {

			String excMesStr =
				"Exception nr "
					+ exceptionCounter
					+ " Antalet tillåtna exceptions ("
					+ allowedNumberOfExceptions
					+ ") överskridet!";
			// Nollställare Räknare för antalet exceptions, för ev återstart.
			exceptionCounter = 0;

			throw new UnManagableException(excMesStr);
		}
	}
}