package se.csn.notmotor.ipl;

public interface MeddelandeServicesBase {

    /**
     * 
     * @return true om nuvarande tidpunkt befinner sig i ett eller flera
     *         underhållsfönster; false annars
     */
    public boolean inScheduledPause();


    /**
     * Satter faltet WATCHDOGTIMESTAMP i tabellen STATUS for denna instans till
     * nuvarande tid.
     * <p>
     * Detta falt anvands for att kontrollera att alla instanser lever och gor det
     * de ska.
     * </p>
     * En servlet laser faltet for alla aktiva instanser och kollar att de lever.
     * <p>
     * OBS! Denna tjanst skriver bara till databas da det bedoms som nodvandigt. Den
     * jamfor nuvarande timestamp med det timestamp da det senast skrevs till
     * databas och skriver bara om det ar dags. Det ar alltsa rekommenderat att
     * anropa denna metod ofta, da den ar valdigt lattviktig.
     * </p>
     */
    public void updateWatchdogFlag();

    /**
     * Ska anropas periodiskt av state-machine-traden sa att sleep-tider
     * halls rimligt uppdaterade
     */
    public void sleepTick();

    public void sleepWaittime();

    /**
     * Ska anropas periodiskt av state-machine-traden sa att parametrarna
     * halls rimligt uppdaterade
     */
    public void updateParameters();

    /**
     * Laser den rad i STATUS-tabellen som matchar denna instans
     * 
     * @return aktuell status som den är satt i databasen för den här instansen
     */
    public int getStatus();

    /**
     * Eftersom ett webb-gui kan ga in och modifiera tillstand medan vi gor nagot
     * maste vi kontrollera att vi fortfarande befinner oss i samma tillstand som
     * vi trodde innan vi gar till ett nytt.
     * 
     * @param fromState det tillstånd vi trodde vi befann oss i
     * @param intoState det tillstånd vi vill gå in i
     * @return true om vi fortfarande befinner oss i fromState och övergången
     *         därför kunde göras; false om vi inte längre befinner oss i fromState
     *         och övergången därför inte kunde göras
     */
    public boolean makeTransition(int fromState, int intoState);

    /**
     * Anropas nar processen avslutas. Stang oppna connections, skriv
     * stangningstidpunkt till databasen mm.
     */
    public void shutdown();
}
