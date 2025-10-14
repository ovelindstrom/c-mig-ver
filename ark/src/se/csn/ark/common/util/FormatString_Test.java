package se.csn.ark.common.util;

import junit.framework.TestCase;

/**
 * ""
 *
 * @author Fredrik Stenlund Steria AB
 * @since 2005-nov-22
 * @version 1 skapad
 *
 */
public class FormatString_Test extends TestCase {

	/**
	 * Constructor for FormatString_Test.
	 * @param arg0
	 */
	public FormatString_Test(String arg0) {
		super(arg0);
	}

	public void testFormateraNamn() {
		String formateratNamn = "";
		String[] oformateradeNamn = new String[11];
		oformateradeNamn[0] = "FREDRIK, STENLUND";
		oformateradeNamn[1] = "FREDRIK, EK";
		oformateradeNamn[2] = "FREDRIK VON EK";
		oformateradeNamn[3] = "FREDRIK, VON EK";
		oformateradeNamn[4] = "FREDRIK E EK";
		oformateradeNamn[5] = "FREDRIK E EKLUND";
		oformateradeNamn[6] = "FREDRIK AF EK";
		oformateradeNamn[7] = "FREDRIK,STENLUND";
		oformateradeNamn[8] = "HANS-ANDERS PERSSON";
		oformateradeNamn[9] = "TAGE G PETTERSSON";
		oformateradeNamn[10] = " ";

		try {
			for (int i = 0; i < oformateradeNamn.length; i++) {
				System.out.println(FormatString.formateraNamn(oformateradeNamn[i]));
			}
		} catch (FormatException formEx) {
			fail("FormatException fångades!: " + formEx.getMessage());
		}

	}

	public void testFormateraAdress() {
		String[] oformateradeAdresser = new String[5];
		oformateradeAdresser[0] = "DALGATAN 40 85239 SUNDSVALL";
		oformateradeAdresser[1] = "STORA BERGSG. 30 88888 NÅGONSTANS";
		oformateradeAdresser[2] = "FREDRIK-ÅKARES VÄG 20 84440 INGEMANSLAND";
		oformateradeAdresser[3] = "Ö. LÅNGGATAN 20, 21110 SOMEWARE";
		oformateradeAdresser[4] = " ";
		
		try {
			for (int i = 0; i < oformateradeAdresser.length; i++) {
				System.out.println(FormatString.formateraNamn(oformateradeAdresser[i]));
			}
		} 
		catch (FormatException formEx) {
			fail("FormatException fångades!: " + formEx.getMessage());
		}
	}
}
