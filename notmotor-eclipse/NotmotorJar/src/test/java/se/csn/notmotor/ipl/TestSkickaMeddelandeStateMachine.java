package se.csn.notmotor.ipl;

import junit.framework.TestCase;
import se.csn.common.servlet.RunControl;
import org.easymock.MockControl;

/**
 * @since 2007-mar-12
 * @author Jonas åhrnell (csn7821)
 * 
 */

public class TestSkickaMeddelandeStateMachine extends TestCase {

    private MockControl control;
    private SkickaMeddelandeServices mock;
    private SkickaMeddelandeStateMachine sm;

    private void doSleepWDFLagParamsOnMock(SkickaMeddelandeServices mock) {
        mock.sleepTick();
        mock.updateWatchdogFlag();
        mock.updateParameters();
    }

    private void goToStopped(SkickaMeddelandeServices mock) {
        doSleepWDFLagParamsOnMock(mock);
        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.STOPPED);
        mock.shutdown();
    }

    public void setUp() {
        control = MockControl.createControl(SkickaMeddelandeServices.class);
        mock = (SkickaMeddelandeServices) control.getMock();
        sm = new SkickaMeddelandeStateMachine(mock, new RunControl());
    }

    private void verify() {
        control.replay();
        // Starta
        sm.run();
        control.verify();
    }

    public void testLifecycleRunning() {
        // Kolla att watchdog flag sätts
        mock.updateWatchdogFlag();
        // Kolla att parametrarna uppdateras
        mock.updateParameters();
        // Kolla att status läses
        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.RUNNING);
        // STATUS = RUNNING:
            mock.inScheduledPause();
        control.setReturnValue(false);
        // Kolla att sendMail anropas
        mock.skickaMeddelande();
        control.setReturnValue(true);
        // Tomt varv
        doSleepWDFLagParamsOnMock(mock);
        // Gå till status STOPPED
        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.STOPPED);
        mock.shutdown();

        verify();
    }


    public void testStoppingStopped() {
        mock.updateWatchdogFlag();
        mock.updateParameters();
        // Kolla att status läses
        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.STOPPING);
        // STATUS = STOPPING:
            mock.makeTransition(SkickaMeddelandeStateMachine.STOPPING, SkickaMeddelandeStateMachine.STOPPED);
        control.setReturnValue(true);

        // Tomt varv
        doSleepWDFLagParamsOnMock(mock);
        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.STOPPED);
        mock.shutdown();

        // Kontrollera resultat:
        verify();
    }

    public void testScheduledPause() {
        // Första stegen:
        mock.updateWatchdogFlag();
        mock.updateParameters();

        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.RUNNING);
        // STATUS = RUNNING:
            mock.inScheduledPause();
        control.setReturnValue(true);
        // Kolla att status SCHEDULED_PAUSE sätts
        mock.makeTransition(SkickaMeddelandeStateMachine.RUNNING, SkickaMeddelandeStateMachine.SCHEDULED_PAUSE);
        control.setReturnValue(true);

        // Håll status:
        doSleepWDFLagParamsOnMock(mock);
        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.SCHEDULED_PAUSE);
        mock.inScheduledPause();
        control.setReturnValue(true);

        // Gå ur scheduled pause:
        doSleepWDFLagParamsOnMock(mock);
        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.SCHEDULED_PAUSE);
        mock.inScheduledPause();
        control.setReturnValue(false);
        mock.makeTransition(SkickaMeddelandeStateMachine.SCHEDULED_PAUSE, SkickaMeddelandeStateMachine.RUNNING);
        control.setReturnValue(true);

        // Gå till status STOPPED
        goToStopped(mock);

        verify();
    }

    public void testPause() {

    }

    public void testRunningToPausedScheduledPause() {
        // Kontrollera att RUNNING -> PAUSED -> RUNNING funkar.
        // Första stegen:
        mock.updateWatchdogFlag();
        mock.updateParameters();

        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.RUNNING);
        // STATUS = RUNNING:
            mock.inScheduledPause();
        control.setReturnValue(false);
        mock.skickaMeddelande();
        control.setReturnValue(true);

        // gå till PAUSED
        doSleepWDFLagParamsOnMock(mock);
        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.PAUSING);
        mock.makeTransition(SkickaMeddelandeStateMachine.PAUSING, SkickaMeddelandeStateMachine.PAUSED);
        control.setReturnValue(true);

        doSleepWDFLagParamsOnMock(mock);
        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.PAUSED);

        // gå ur PAUSED till RUNNING, men i SCHEDULED_PAUSE
        doSleepWDFLagParamsOnMock(mock);
        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.RUNNING);
        mock.inScheduledPause();
        control.setReturnValue(true);
        mock.makeTransition(SkickaMeddelandeStateMachine.RUNNING, SkickaMeddelandeStateMachine.SCHEDULED_PAUSE);
        control.setReturnValue(true);

        // Gå till status STOPPED
        goToStopped(mock);

        verify();
    }

    public void testWaiting() {
        // Första stegen:
        mock.updateWatchdogFlag();
        mock.updateParameters();

        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.RUNNING);
        // STATUS = RUNNING:
            mock.inScheduledPause();
        control.setReturnValue(false);
        mock.skickaMeddelande();
        control.setReturnValue(false);

        // gå till WAITING
        mock.makeTransition(SkickaMeddelandeStateMachine.RUNNING, SkickaMeddelandeStateMachine.WAITING);
        control.setReturnValue(true);

        doSleepWDFLagParamsOnMock(mock);
        mock.getStatus();
        control.setReturnValue(SkickaMeddelandeStateMachine.WAITING);
        mock.sleepWaittime();
        mock.makeTransition(SkickaMeddelandeStateMachine.WAITING, SkickaMeddelandeStateMachine.RUNNING);
        control.setReturnValue(true);

        // Gå till status STOPPED
        goToStopped(mock);
        verify();
    }
}
