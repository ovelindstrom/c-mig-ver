package se.csn.notmotor.ipl;

import se.csn.common.servlet.RunControl;

public class MeddelandeStateMachineBase {
    // Statuskoder för skickamotorns tillstånd. 
    // Dessa koder används även i databasen.
    public static final int
        // Det tillstånd som används innan motorn gått in i sin 
        // arbetsloop, när inställningar läses mm
        INIT = 0,

        // Status när skickamotorn läser/skriver databas och skickar meddelanden
        RUNNING = 1,

        // Sätts av tråd när tråden sover och inte läser/skickar meddelanden.
        // Ska hamna i detta tillstånd om det för tillfället inte finns några mail
        // att skicka
        WAITING = 2,

        // Sätts i databas för att signalera att skickamotorn ska gå till PAUSED
        PAUSING = 3,

        // Tillstånd då skickamotorn sover och inte gör någonting. 
        // Det enda som görs här är att kontrollera om det är dags att gå till
        // tillstånd RUNNING (
        PAUSED = 4,

        // Sätts i datanas för att signalera att skickamotorn ska gå till RUNNING
        STOPPING = 5,

        // Tillstånd då skickamotorn är avstängd, kan inte startas igen.
        STOPPED = 6,

        // Tillstånd som anges för en schemalagd paus. Enda sättet att komma ur den är att 
        // tidpunkten inte längre befinner sig i en schemalagd paus
        SCHEDULED_PAUSE = 7,

        // Tillstånd som returneras av tjänsten om databasen inte kan nås.
        // Här kan inget arbete utföras överhuvudtaget, men tjänsten ska
        // ändå inte stängas ner utan fortsätta läsa status och hoppas att
        // databasen går upp igen
        // Denna status ska aldrir representeras i databasen
        UNKNOWN = -1;

    protected RunControl runControl;

    public MeddelandeStateMachineBase(RunControl runControl) {
        this.runControl = runControl;
    }
}
