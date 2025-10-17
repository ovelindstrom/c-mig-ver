package se.csn.notmotor.ipl.sms;

import junit.framework.TestCase;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;
import se.csn.notmotor.ipl.model.KodText;
import se.csn.notmotor.ipl.validators.BasicMeddelandeValidator;
import se.csn.notmotor.ipl.validators.SMSValidator;

public class TestSMSValidator extends TestCase {

    private final String TEXT_200 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
    private static final String OK = "" + MeddelandeHandelse.OK;
    private static final String FELAKTIG_MOTTAGARE = "" + MeddelandeHandelse.FELAKTIG_MOTTAGARE;

    public void testGetFelkodForMeddelande() {
        // Testa meddelande med för lång text
        // Med mottagare med: 
        // null-adress
        // för kort SMS-adress
        Meddelande m = new Meddelande("Rubrik", TEXT_200);
        Mottagare mott = new Mottagare("0705976212");
        mott.setTyp("SMS");
        m.addMottagare(mott);

        String[][] dataFacit = {
            {null, FELAKTIG_MOTTAGARE},
            {"0705976212", OK},
            {"070-5976212", FELAKTIG_MOTTAGARE},
            {"07012345", FELAKTIG_MOTTAGARE},
            {"as07012345", FELAKTIG_MOTTAGARE},
            {"070asd12345", FELAKTIG_MOTTAGARE},
            {"07012345", FELAKTIG_MOTTAGARE},
            {"0+4547012345", FELAKTIG_MOTTAGARE},
            {"0123456789123456789", FELAKTIG_MOTTAGARE},
            {"+46705976212", OK},
        };

        //used to be SMSValidator class
        BasicMeddelandeValidator validator = new BasicMeddelandeValidator();

        KodText kt = validator.getFelkodForMeddelande(m);

        assertEquals(kt.getKod(), MeddelandeHandelse.FELAKTIG_AVSANDARE); //OLD: FELAKTIG_AVSANDARE
        m.setMeddelandetext("Testmeddelande");


        SMSValidator validator2 = new SMSValidator();
        for (int i = 0;i < dataFacit.length;i++) {
            mott.setAdress(dataFacit[i][0]);
            kt = validator2.getFelkodForMeddelande(m);
            if (kt == null) {
                kt = new KodText(MeddelandeHandelse.OK, "");
            }
            assertEquals("" + kt.getKod(), dataFacit[i][1]);
        }
        /*
        mott.setAdress("070-123456789");
        kt = validator.getFelkodForMeddelande(m);
        assertEquals(mh.getHandelsetyp().intValue(), DTOMeddelandeHandelse.MEDDELANDEFEL);
        assertEquals(mh.getFelkod().intValue(), DTOMeddelandeHandelse.FELAKTIG_MOTTAGARE);
        
        mott.setAdress("abc070123456789");
        kt = validator.getFelkodForMeddelande(m);
        assertEquals(mh.getHandelsetyp().intValue(), DTOMeddelandeHandelse.MEDDELANDEFEL);
        assertEquals(mh.getFelkod().intValue(), DTOMeddelandeHandelse.FELAKTIG_MOTTAGARE);

        mott.setAdress("0+70123456789");
        kt = validator.getFelkodForMeddelande(m);
        assertEquals(mh.getHandelsetyp().intValue(), DTOMeddelandeHandelse.MEDDELANDEFEL);
        assertEquals(mh.getFelkod().intValue(), DTOMeddelandeHandelse.FELAKTIG_MOTTAGARE);

        mott.setAdress("0123456789123456789");
        kt = validator.getFelkodForMeddelande(m);
        assertEquals(mh.getHandelsetyp().intValue(), DTOMeddelandeHandelse.MEDDELANDEFEL);
        assertEquals(mh.getFelkod().intValue(), DTOMeddelandeHandelse.FELAKTIG_MOTTAGARE);

        mott.setAdress("+46705976212");
        kt = validator.getFelkodForMeddelande(m);
        assertEquals(mh.getHandelsetyp().intValue(), DTOMeddelandeHandelse.MOTTAGET);
        assertEquals(mh.getFelkod().intValue(), DTOMeddelandeHandelse.OK);
        */
    }


}
