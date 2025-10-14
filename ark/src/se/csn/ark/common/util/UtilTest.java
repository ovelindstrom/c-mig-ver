package se.csn.ark.common.util;

import static se.csn.ark.common.util.UtilTest.transactionId;

import java.io.Serializable;

import junit.framework.TestCase;
import se.csn.ark.common.dal.CsnDataAccessObjectImpl;
import se.csn.ark.common.dt.CsnDataTransferObjectImpl;
import se.csn.ark.common.fl.CsnFacadeObjectImpl;
import se.csn.ark.common.util.logging.Log;
import se.csn.ark.common.util.logging.LogLevel;
import se.csn.ark.common.util.logging.time.TimeLog;
import se.csn.ark.common.util.logging.trace.TraceLog;
import se.csn.ark.common.util.logging.trace.TraceRecord;


/**
 *
 * Testklass för sådant som ligger i util eller underliggande package.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041129
 * @version 1 skapad
 *
 */
public class UtilTest extends TestCase {
	
	private static Log log = Log.getInstance(UtilTest.class);

	/**
	 * Skapa test utan namn
	 */
	public UtilTest() {
		super();
	}

	/**
     * Skapa test med namn
	 * @param arg0 namn på testet
	 */
	public UtilTest(String arg0) {
		super(arg0);
	}

	/**
	 * Testar loggning
	 */
	public void testAllLogs() {
	    
        final int nThreads = 40;
        final int sleepTime = 2000;
    
		log.debug("Startar testLogAndTimeLog");
		log.debug("TimeLog.isTiming()=" + TimeLog.isTiming());
		log.debug("TraceLog.isTraceing()=" + TraceLog.isTraceing());
		TimeLog timeLog = TimeLog.getLogger(this);
		Integer logId = timeLog.startClock("testLogAndTimeLog");
        
		log.debug("Start 40 testtrådar");
		// Vi startar ett antal trådar som gör anrop till fasaden.
		for (int i = 0; i < nThreads; i++) {
			Thread t = new Thread(new FakeFacadeCall());
			t.start();
		}
		timeLog.stopClock(logId);
		// Kör vi trådat måste huvudprogrammet vänta in alla trådar ...
        log.debug("Väntar in trådarna ...");
		try {
			Thread.sleep(sleepTime);
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("Avslutar testLogAndTimeLog");
        log.debug("TimeLog status : " + timeLog);
        log.debug("***** testLogAndTimeLog avslutad *****");
	}

		
	/**
	 * Testar log-nivåer
     * Sätt nivå i ark_log4j.properties och se att det kommer ut meddelanden på rätt nivå.
     * FATAL skall komma i vanliga loggen och i en egen fil.
     * 
	 */
	public void testSimpleLogLevelsTest() {
		writeSimpleMessageToAllLogLevels();
	}

	/**
	 * Skriver till alla lognivåer
	 */
	private void writeSimpleMessageToAllLogLevels() {
		String logMsg = "Detta är ett meddelande för logtest.";

		log.debug(logMsg);
		log.info(logMsg);
		log.warn(logMsg);
		log.error(logMsg);
		log.fatal(logMsg);
		log.log(LogLevel.CRITICAL, logMsg);
		log.log(LogLevel.TIME_LOG, logMsg);
	}

	/**********************************
	 ***** Intern klasser för test *****
	 ***********************************/
	
	
    /**
     * 
     */
	class FakeFacadeCall implements Runnable {
								
		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			
			FakeFacade fakeFacade = new FakeFacade();
			fakeFacade.test();
		}

	}
	
    private static final int CSN_NR_START = 987654321;
	private static int csnNr = CSN_NR_START;

	/**
     * Facade för test
	 */
	class FakeFacade extends CsnFacadeObjectImpl implements Serializable{
		private Log log = Log.getInstance(FakeFacade.class);

		/**
		 * testar trace-log i objectet
		 */
		void test() {
			FakeDTO fakeDTO;

			if (isTiming()) {
				startClock( this, "test");
			}
			if (log.isDebugEnabled()) {
				log.debug("test ==>");
			}
			FakeDAO fakeDAO = new FakeDAO();
			fakeDTO = fakeDAO.getDto();
			// detta är förvisso bara en läsning och kanske
			// därmed inte en riktig transaktion men detta
			// är bara ett enkelt test så ...
			if (isTraceing()) {
				TraceRecord rec = 
					new TraceRecord(new Integer(csnNr), fakeDTO);
				trace(rec);
				// Se till att det är ny kund nästa gång ... ;-)
				csnNr++;
			}
			if (log.isDebugEnabled()) {
				log.debug("test <==");
			}
			if (isTiming()) {
				stopClock();
			}
		}
	}

    /**
     * DAO för test
     */
	class FakeDAO extends CsnDataAccessObjectImpl {
		private Log log = Log.getInstance(FakeDAO.class);
        private static final int SLEEP_TIME = 100;
				
        /**
         * testar time-log i objectet
         * @return låtsas-hämtat dto
         */
		FakeDTO getDto() {
			FakeDTO fakeDTO;

			if (isTiming()) {
				startClock(this, "test");
			}
			if (log.isDebugEnabled()) {
				log.debug("getDto ==>");
			}
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Nu fejkar vi att vi faktiskt har hämtat data.
			fakeDTO = new FakeDTO();
			if (log.isDebugEnabled()) {
				log.debug("getDto <==");
			}
			if (isTiming()) {
				stopClock();
			}
			return fakeDTO;
		}
	}


    private static final int HANDELSE_START = 0;
    private static final int TXID_START = 10000000;
    
	private static int handelseNr = HANDELSE_START;
	public static int transactionId = TXID_START;

    /**
     * DTO för test
     */
	class FakeDTO extends CsnDataTransferObjectImpl {
		
		/**
		 * Skapa dto
		 */
		FakeDTO() {
		
			setEvent("Händelse nr " + handelseNr);	
			handelseNr++;
			setTransactionId(String.valueOf(transactionId));
			transactionId++;
		}
		
	}
	
}