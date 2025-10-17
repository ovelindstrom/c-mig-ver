/*
 * @since 2007-okt-04
 */
package se.csn.notmotor.ipl;

import junit.framework.TestCase;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.Mottagare;

/**
 * @author Jonas åhrnell - csn7821
 */
public class TestEpostMeddelandeSenderImpl extends TestCase {

    public void testRemovePart() {
        String typer = "EPOST,SMS,EPOSTCC";
        String res = EpostMeddelandeSenderImpl.removePart(typer, "SMS", ",");
        assertEquals(res, "EPOST,EPOSTCC");

        res = EpostMeddelandeSenderImpl.removePart(typer, "EPOST", ",");
        assertEquals(res, "SMS,EPOSTCC");

        res = EpostMeddelandeSenderImpl.removePart(typer, "EPOSTCC", ",");
        assertEquals(res, "EPOST,SMS");

        res = EpostMeddelandeSenderImpl.removePart("EPOST", "EPOST", ",");
        assertEquals(res, "");

        res = EpostMeddelandeSenderImpl.removePart("NISSE", "EPOST", ",");
        assertEquals(res, "NISSE");
    }

    public void testTaBortOkandaMottagartyper() {
        Meddelande m = new Meddelande("Rubrik", "Text");
        Mottagare mott = new Mottagare();
        mott.setTyp("EPOST,SMS");
        m.addMottagare(mott);

        m = EpostMeddelandeSenderImpl.taBortOkandaOchSandaMottagartyper(m);
        assertEquals(m.getMottagare()[0].getTyp(), "EPOST");

        mott.setTyp("EPOST,SMS,EPOSTCC");
        m = EpostMeddelandeSenderImpl.taBortOkandaOchSandaMottagartyper(m);
        assertEquals(m.getMottagare()[0].getTyp(), "EPOST,EPOSTCC");

        mott.setTyp("SMS");
        m = EpostMeddelandeSenderImpl.taBortOkandaOchSandaMottagartyper(m);
        assertEquals(m.getMottagare().length, 0);
    }


}
