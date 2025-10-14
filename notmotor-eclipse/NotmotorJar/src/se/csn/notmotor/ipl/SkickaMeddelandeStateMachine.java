package se.csn.notmotor.ipl;

import se.csn.common.servlet.RunControl;

/**
 * Klass som realiserar tillståndshanteringen för att skicka meddelanden.
 * Allt "arbete" utförs i andra klasser; det gör det möjligt att testa 
 * tillståndshanteringen för sig. 
 * @author Jonas åhrnell (csn7821)
 *
 */
public class SkickaMeddelandeStateMachine extends MeddelandeStateMachineBase {

    private SkickaMeddelandeServices services;
    
	public SkickaMeddelandeStateMachine(SkickaMeddelandeServices services, RunControl runControl) {
	    super(runControl);
	    this.services = services;
	}
	
	public void run() {
		mainLoop: while (runControl.isRunning()) {
		
			// Sätt watchdog timer (oavsett status):
			services.updateWatchdogFlag();
		
			// Läs parametrar (görs 1 gång per tick). 
			// Man skulle kunna sätta ett annat intervall för
			// parameteruppdatering, men det skulle bara komplicera
			// koden utan att tillföra nödvändig funktionalitet
			services.updateParameters();
			
			// Läs den status som är satt i databasen. 
			int status = services.getStatus();
			
			switch(status) {
			case INIT: 
			    // Markörtillstånd för första varvet. Gå till RUNNING:
			    services.makeTransition(INIT, RUNNING);
			    // faller igenom
			case RUNNING:
				// Kolla först om vi befinner oss i en schemalagd pause:
				if (services.inScheduledPause()) {
					services.makeTransition(RUNNING, SCHEDULED_PAUSE);
					break;
				}
				// Om inte, utför själva jobbet:
				boolean sentMail = services.skickaMeddelande();
				if (!sentMail) {
				    services.makeTransition(RUNNING, WAITING);
				}
				break;
			case WAITING: // Vänta angivet antal millisekunder, gå därefter till RUNNING:
				services.sleepWaittime();
				services.makeTransition(WAITING, RUNNING);
				break;
			case PAUSING: // Gå till paused
			    services.makeTransition(PAUSING, PAUSED);
				break;
			case PAUSED: // Gör inget, kommer bara härifrån om status ändras i databasen
				break; // NOSONAR
			case SCHEDULED_PAUSE: // Kolla om inte längre i schemafönstret
				if (!services.inScheduledPause()) {
				    services.makeTransition(SCHEDULED_PAUSE, RUNNING);
				}
				break;
			case STOPPING: // Gå till STOPPED
			    services.makeTransition(STOPPING, STOPPED);
				break;
			case STOPPED: // bryt ur loopen och anropa sedan avstängningsmetod
				break mainLoop; // NOSONAR
			case UNKNOWN: // Fortsätt som i PAUSED: kommer bara härifrån om status
			    		  // ändras i databasen
			    break; // NOSONAR
			default: throw new IllegalStateException("Okänt tillstånd: " + status); 
			}
			
			// Sov en ticktid, för att inte lasta i onödan
			services.sleepTick();
		}
		services.shutdown();
	}
	
}
