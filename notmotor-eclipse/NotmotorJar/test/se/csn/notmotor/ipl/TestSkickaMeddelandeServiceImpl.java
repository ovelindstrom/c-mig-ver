/**
 * Skapad 2007-apr-02
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl;

import junit.framework.TestCase;

import org.easymock.MockControl;

import se.csn.notmotor.ipl.db.ControlledCommitQueryProcessor;
import se.csn.notmotor.ipl.db.DAOHandelse;
import se.csn.notmotor.ipl.db.DAOMeddelande;
import se.csn.notmotor.ipl.db.DAOMottagare;
import se.csn.notmotor.ipl.db.ParameterKalla;


public class TestSkickaMeddelandeServiceImpl extends TestCase {

    private ControlledCommitQueryProcessor qp;
    private ParameterKalla pk;
    private MeddelandeSender sender;
    private DAOMeddelande mh;
    private DAOHandelse hh;
    private DAOMottagare motth;
    
    private MockControl qpControl, pkControl, senderControl, mhControl, hhControl, mottControl;
    
    
    private SkickaMeddelandeServicesImpl skapaImplOchMocks() {
        qpControl = MockControl.createControl(ControlledCommitQueryProcessor.class);
        qp = (ControlledCommitQueryProcessor)qpControl.getMock();
        pkControl = MockControl.createControl(ParameterKalla.class);
        pk = (ParameterKalla)pkControl.getMock();
        mhControl = MockControl.createControl(DAOMeddelande.class);
        mh = (DAOMeddelande)mhControl.getMock();
        hhControl = MockControl.createControl(DAOHandelse.class);
        hh = (DAOHandelse)hhControl.getMock();
        mottControl = MockControl.createControl(DAOMottagare.class);
        motth = (DAOMottagare)mottControl.getMock();
        return new SkickaMeddelandeServicesImpl(qp, pk, mh, hh, motth, 1);
    }
    
    /* TODO: Hitta ett annat sätt att testa kod som inkluderar sleepcykler. 
     * Timingen är inte tillräckligt exakt.
    public void testWatchdogTimer() {
        // Mocka:
        SkickaMeddelandeServicesImpl impl = skapaImplOchMocks();
        impl.setInstansnummer(0);
        
        // Kontrollera att den inte sover längre än watchdogtiden:
        pk.getIntParam("WATCHDOGTID", SkickaMeddelandeServicesImpl.DEFAULT_WATCHDOGTID);
        pkControl.setReturnValue(3);
        
        pk.getIntParam("SOVTID", SkickaMeddelandeServicesImpl.DEFAULT_SOVTID);
        pkControl.setReturnValue(8);
        
        // Kommer att fråga 3 gånger: 
        pk.getIntParam("WATCHDOGTID", SkickaMeddelandeServicesImpl.DEFAULT_WATCHDOGTID);
        pkControl.setReturnValue(3);
        qp.executeThrowException("UPDATE STATUS SET WATCHDOGTSTAMP = CURRENT TIMESTAMP WHERE INSTANS = 0");
        qpControl.setReturnValue(1);
        pk.getIntParam("WATCHDOGTID", SkickaMeddelandeServicesImpl.DEFAULT_WATCHDOGTID);
        pkControl.setReturnValue(3);
        qp.executeThrowException("UPDATE STATUS SET WATCHDOGTSTAMP = CURRENT TIMESTAMP WHERE INSTANS = 0");
        qpControl.setReturnValue(1);
        pk.getIntParam("WATCHDOGTID", SkickaMeddelandeServicesImpl.DEFAULT_WATCHDOGTID);
        pkControl.setReturnValue(3);
        qp.executeThrowException("UPDATE STATUS SET WATCHDOGTSTAMP = CURRENT TIMESTAMP WHERE INSTANS = 0");
        qpControl.setReturnValue(1);
        
        
        // Verifiera:
        pkControl.replay();
        qpControl.replay();
        impl.sleepWaittime();
        pkControl.verify();
        qpControl.verify();
    }
     */

        
    public void testSleeptime() {
        SkickaMeddelandeServicesImpl impl = skapaImplOchMocks();
        pk.getIntParam("WATCHDOGTID", SkickaMeddelandeServicesImpl.DEFAULT_WATCHDOGTID);
        pkControl.setReturnValue(120);
        
        pk.getIntParam("SOVTID", SkickaMeddelandeServicesImpl.DEFAULT_SOVTID);
        pkControl.setReturnValue(1);

        pk.getIntParam("WATCHDOGTID", SkickaMeddelandeServicesImpl.DEFAULT_WATCHDOGTID);
        pkControl.setReturnValue(6);
        qp.executeThrowException("UPDATE STATUS SET WATCHDOGTSTAMP = CURRENT TIMESTAMP WHERE INSTANS = 1");
        qpControl.setReturnValue(1);
        
        // Kolla att anropet tar minst 1000 millisekunder
        pkControl.replay();
        qpControl.replay();
        long tick = System.currentTimeMillis();
        impl.sleepWaittime();
        // OBS! Maven-testet gick på 999 ms...
        assertTrue(System.currentTimeMillis()-tick >= 990);
        pkControl.verify();
        qpControl.verify();
    }
    
    public void testTicktime() {
        SkickaMeddelandeServicesImpl impl = skapaImplOchMocks();
        pk.getIntParam("WATCHDOGTID", SkickaMeddelandeServicesImpl.DEFAULT_WATCHDOGTID);
        pkControl.setReturnValue(120);
        
        pk.getIntParam("TICKTID", SkickaMeddelandeServicesImpl.DEFAULT_TICKTID);
        pkControl.setReturnValue(2);

        pk.getIntParam("WATCHDOGTID", SkickaMeddelandeServicesImpl.DEFAULT_WATCHDOGTID);
        pkControl.setReturnValue(120);
        qp.executeThrowException("UPDATE STATUS SET WATCHDOGTSTAMP = CURRENT TIMESTAMP WHERE INSTANS = 1");
        qpControl.setReturnValue(1);
        
        // Kolla att anropet tar minst 1500 millisekunder
        pkControl.replay();
        qpControl.replay();
        long tick = System.currentTimeMillis();
        impl.sleepTick();
        assertTrue(System.currentTimeMillis()-tick >= 1500);
        pkControl.verify();
        qpControl.verify();
    }
    
    public void testInScheduledPause() {
        SkickaMeddelandeServicesImpl impl = skapaImplOchMocks();

        qp.getInt("SELECT COUNT(*) FROM KORSCHEMA WHERE STANGNINGSTID < CURRENT TIMESTAMP AND OPPNINGSTID > CURRENT TIMESTAMP", 0);
        qpControl.setReturnValue(0);
        
        qpControl.replay();
        assertFalse(impl.inScheduledPause());
        qpControl.verify();
        
        qpControl.reset();
        qp.getInt("SELECT COUNT(*) FROM KORSCHEMA WHERE STANGNINGSTID < CURRENT TIMESTAMP AND OPPNINGSTID > CURRENT TIMESTAMP", 0);
        qpControl.setReturnValue(1);
        
        qpControl.replay();
        assertTrue(impl.inScheduledPause());
        qpControl.verify();

        qpControl.reset();
        qp.getInt("SELECT COUNT(*) FROM KORSCHEMA WHERE STANGNINGSTID < CURRENT TIMESTAMP AND OPPNINGSTID > CURRENT TIMESTAMP", 0);
        qpControl.setReturnValue(4);
        
        qpControl.replay();
        assertTrue(impl.inScheduledPause());
        qpControl.verify();
    }
    
    public void testUpdateParameters() {
        SkickaMeddelandeServicesImpl impl = skapaImplOchMocks();
        pk.reload();
        
        pkControl.replay();
        impl.updateParameters();
        pkControl.verify();
    }
    
    public void testSetGetStatus() {
        SkickaMeddelandeServicesImpl impl = skapaImplOchMocks();
        int nr = 77;
        impl.setInstansnummer(nr);
        int status = SkickaMeddelandeStateMachine.RUNNING;
        qp.getInt("SELECT STATUS FROM STATUS WHERE INSTANS = " + nr, -1);
        qpControl.setReturnValue(status);
        qp.executeThrowException("UPDATE STATUS SET STATUS = " + status + " WHERE INSTANS = " + nr);
        qpControl.setReturnValue(1);
        qp.getInt("SELECT STATUS FROM STATUS WHERE INSTANS = " + nr, -1);
        qpControl.setReturnValue(status);
        
        qpControl.replay();
        impl.makeTransition(status, status);
        assertEquals(impl.getStatus(), status);
        qpControl.verify();
    }

    
}