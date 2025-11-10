/*
 * Created on 2005-sep-19
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package se.csn.ark.common.util;

/**
 * @author CSN7543
 * @version 15:07:26
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FormatString {

	public static String formateraAdress(String adress) throws FormatException {
		try {
			String formateradAdress = "";
			Character tecken;
			Character foregaendeTecken;
			Character[] avslutsTecken = new Character[5];
			boolean gorStorBokstav = true;
			boolean avslutsTeckenFunnet = false;

			if (adress != null) {
				if (adress.length() > 0) {
					avslutsTecken[0] = new Character(' ');
					avslutsTecken[1] = new Character('.');
					avslutsTecken[2] = new Character(',');
					avslutsTecken[3] = new Character('\'');
					avslutsTecken[3] = new Character('-');

					//Första bokstaven i namnet ska alltid börja med stor bokstav (om det inte är ett undantagsord)
					foregaendeTecken = new Character(' ');

					adress = adress.toUpperCase();

					for (int i = 0; i < adress.length(); i++) {
						tecken = new Character(adress.charAt(i));

						if ((finnsCharacterIArray(foregaendeTecken, avslutsTecken)) && (!finnsCharacterIArray(tecken, avslutsTecken))) {
							//Föregående tecken var ett avslutstecken och nuvarande är inte det
							formateradAdress += tecken.toString().toUpperCase();

						} else if ((finnsCharacterIArray(foregaendeTecken, avslutsTecken)) && (finnsCharacterIArray(tecken, avslutsTecken))) {
							//Föregående och nuvarande tecken är avslutstecken 
							formateradAdress += tecken.toString();

						} else if (!finnsCharacterIArray(foregaendeTecken, avslutsTecken)) {
							//Föregående tecken var inte ett avslutstecken
							formateradAdress += tecken.toString().toLowerCase();
						}

						foregaendeTecken = tecken;
					}
				}
			}

			return formateradAdress;

		} catch (Throwable tex) {
			throw new FormatException("Fel i formateraAdress", tex);
		}
	}

	public static String formateraNamn(String namn) throws FormatException {
		try {
			String formateratNamn = "";
			Character tecken;
			Character foregaendeTecken;
			Character[] avslutsTecken = new Character[4];
			String[] undantagsOrd = new String[2];
			boolean undantagsOrdFunnet = false;

			if (namn != null) {
				if (namn.length() > 0) {

					avslutsTecken[0] = new Character(' ');
					avslutsTecken[1] = new Character('-');
					avslutsTecken[2] = new Character(',');
					avslutsTecken[3] = new Character('\'');

					//Undantagsord kan aldrig börja med ett avslutstecken
					undantagsOrd[0] = "VON";
					undantagsOrd[1] = "AF";

					//Första bokstaven i namnet ska alltid börja med stor bokstav (om det inte är ett undantagsord)
					foregaendeTecken = new Character(' ');

					namn = namn.toUpperCase();

					for (int i = 0; i < namn.length(); i++) {
						tecken = new Character(namn.charAt(i));
						undantagsOrdFunnet = false;

						if ((finnsCharacterIArray(foregaendeTecken, avslutsTecken)) && (!finnsCharacterIArray(tecken, avslutsTecken))) {
							//Föregående tecken var ett avslutstecken och nuvarande är inte det

							//Kolla om det är början på ett ord som aldrig ska skrivas med stora bokstäver. 
							//Ett sånt ord efterföljs alltid med ett avslutstecken (vanligtvis mellanslag).
							for (int x = 0; x < undantagsOrd.length; x++) {
								//Kontrollera att resterande del av namnet är tillräckligt många tecken för 
								//att kunna vara ett undantagsord inkl. avslutstecken
								if ((namn.length() - i) >= (undantagsOrd[x].length() + 1)) {
									for (int z = 0; z < avslutsTecken.length; z++) {
										String undantagsOrdMedAvslutstecken = undantagsOrd[x] + avslutsTecken[z];
										if (undantagsOrdMedAvslutstecken.equals(new String(namn.substring(i, i + undantagsOrdMedAvslutstecken.length())))) {
											//Ett undantagsord har hittats
											undantagsOrdFunnet = true;
											break;
										}
									}
								}
							}

							if (undantagsOrdFunnet) {
								//Undantagsord ska alltid bestå av små bokstäver
								formateratNamn += tecken.toString().toLowerCase();
							} else {
								//Första bokstaven efter ett avslutstecken ska vara med stor bokstav
								formateratNamn += tecken.toString().toUpperCase();
							}

						} else if ((finnsCharacterIArray(foregaendeTecken, avslutsTecken)) && (finnsCharacterIArray(tecken, avslutsTecken))) {
							//Föregående och nuvarande tecken är avslutstecken 
							formateratNamn += tecken.toString();

						} else if (!finnsCharacterIArray(foregaendeTecken, avslutsTecken)) {
							//Föregående tecken var inte ett avslutstecken
							formateratNamn += tecken.toString().toLowerCase();
						}

						foregaendeTecken = tecken;
					}
				}
			}

			return formateratNamn;

		} catch (Throwable tex) {
			throw new FormatException("Fel i formateraNamn", tex);
		}
	}

	private static boolean finnsCharacterIArray(Character c, Character[] charArr) {
		boolean teckenFunnet = false;
		for (int y = 0; y < charArr.length; y++) {
			if (c.equals(charArr[y])) {
				teckenFunnet = true;
				break;
			}
		}
		return teckenFunnet;
	}

}
