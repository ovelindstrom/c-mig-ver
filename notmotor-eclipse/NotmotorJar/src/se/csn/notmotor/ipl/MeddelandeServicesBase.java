
package se.csn.notmotor.ipl;
public interface MeddelandeServicesBase {

	/**
	 * 
	 * @return true om nuvarande tidpunkt befinner sig i ett eller flera 
	 * 			underhållsfönster; false annars
	 */
	public boolean inScheduledPause();
	
	/**
	 * Sätter fältet WATCHDOGTIMESTAMP i tabellen STATUS för denna instans till nuvarande tid. 
	 * Detta fält används för att kontrollera att alla instanser lever och gör det de ska.
	 * En servlet läser fältet för alla aktiva instanser och kollar att de lever.
	 * 
	 * OBS! Denna tjänst skriver bara till databas då det bedöms som nödvändigt. Den jämför 
	 * nuvarande timestamp med det timestamp då det senast skrevs till databas och skriver 
	 * bara om det är dags. Det är alltså rekommenderat att anropa denna metod ofta, då den 
	 * är väldigt lättviktig. 
	 */
	public void updateWatchdogFlag();
	
	/**
	 * @param milliseconds Det antal millisekunder som tråden ska sova
	 * @throws RuntimeException om sovandet misslyckades
	 */
	public void sleepTick();
	public void sleepWaittime();
	
	/**
	 * Ska anropas periodiskt av state-machine-tråden så att parametrarna
	 * hålls rimligt uppdaterade
	 *
	 */
	public void updateParameters();
	
	/**
	 * Läser den rad i STATUS-tabellen som matchar denna instans
	 * @return aktuell status som den är satt i databasen för den här instansen
	 */
	public int getStatus();
	
	/**
	 * Eftersom ett webb-gui kan gå in och modifiera tillstånd medan vi gör något
	 * måste vi kontrollera att vi fortfarande befinner oss i samma tillstånd som 
	 * vi trodde innan vi går till ett nytt.
	 */
	public boolean makeTransition(int fromState, int intoState);
	
	/**
	 * Anropas när processen avslutas. Stäng öppna connections, skriv stängningstidpunkt till
	 * databasen mm.
	 */
	public void shutdown();
}
