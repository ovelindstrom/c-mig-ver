/*
 * Skapad 2007-sep-24
 */
package se.csn.notmotor.ipl;

import junit.framework.TestCase;

import org.easymock.MockControl;

import se.csn.notmotor.ipl.db.DAOMeddelande;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.NotifieringResultat;

/**
 * @author Jonas åhrnell - csn7821
 */
public class TestMeddelandeMottagare extends TestCase {

    public void testSkickaMeddelande() {

        MockControl daoControl = MockControl.createControl(DAOMeddelande.class);
        DAOMeddelande dao = (DAOMeddelande) daoControl.getMock();

        // Kolla att validering görs
        // Validering fel
        Meddelande m = new Meddelande();
        MeddelandeMottagare mm = new MeddelandeMottagare();
        NotifieringResultat res = mm.skickaMeddelande(m, dao);
        assertEquals(res.getMeddelandeId().intValue(), -1);
        daoControl.reset();
        // Validering rätt
        m = new Meddelande("Rubrik", "Text", "mottagare@csn.se", "avs@csn.se", "Avsandare");
        long id = 12345;
        m.setId(new Long(id));
        // Rigga mocken: 
        dao.createMeddelande(m);
        daoControl.setReturnValue(id);

        daoControl.replay();

        res = mm.skickaMeddelande(m, dao);
        assertEquals(res.getMeddelandeId().intValue(), id);

        daoControl.verify();
        daoControl.reset();

        // Kolla att det läggs till händelse
        // Inga händelser innan
        m.setHandelser(new MeddelandeHandelse[0]);
        dao.createMeddelande(m);
        daoControl.setReturnValue(id);
        daoControl.replay();
        res = mm.skickaMeddelande(m, dao);
        assertEquals(m.getHandelser().length, 1);
        daoControl.verify();
        daoControl.reset();

        // Händelser innan
        m.setHandelser(new MeddelandeHandelse[3]);
        dao.createMeddelande(m);
        daoControl.setReturnValue(id);
        daoControl.replay();
        res = mm.skickaMeddelande(m, dao);
        assertEquals(m.getHandelser().length, 4);
        daoControl.verify();
        daoControl.reset();

        // Kolla att det görs lagring
        // -> gjort ovan
    }

    public void testMottagareStatusSattTillMottaget() {
        MockControl daoControl = MockControl.createControl(DAOMeddelande.class);
        DAOMeddelande dao = (DAOMeddelande) daoControl.getMock();

        // Kolla att validering görs
        // Validering fel
        Meddelande m = new Meddelande("Rubrik", "Text", "mottagare@csn.se", "avs@csn.se", "Avsandare");
        // Sätter status till nåt annat än MOTTAGET:
        m.getMottagare()[0].setStatus(new Integer(MeddelandeHandelse.ALLA_HANDELSER));
        long id = 12345;
        m.setId(new Long(id));
        // Rigga mocken: 
        dao.createMeddelande(m);
        daoControl.setReturnValue(id);

        daoControl.replay();
        MeddelandeMottagare mm = new MeddelandeMottagare();
        NotifieringResultat res = mm.skickaMeddelande(m, dao);
        daoControl.verify();

        assertEquals(m.getMottagare()[0].getStatus(), new Integer(MeddelandeHandelse.MOTTAGET));
    }

}
