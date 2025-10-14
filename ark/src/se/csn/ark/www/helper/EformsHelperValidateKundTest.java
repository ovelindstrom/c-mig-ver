//package se.csn.ark.www.helper;
//
//import iipax.business.shseforms.Node;
//import iipax.business.shseforms.ParameterTree;
//
//
///**
// * För att fixa tester av EformsHelper.
// *
// * @author Joakim Olsson
// * @since 20041202
// * @version 0.1 skapad
// */
//public class EformsHelperValidateKundTest extends EformsHelper {
//	/**
//	 * Validerar mobil-nummer och epost-adress.
//	 *
//	 * @param  tree eforms-trädet
//	 * @return resultatet av valideringen
//	 */
//	public DTOValidateResult validate(ParameterTree tree) {
//		DTOValidateResult result = new DTOValidateResult();
//
//		result = validateMobilNr(tree, result);
//		result = validateEpost(tree, result);
//
//		return result;
//	}
//
//
//
//
//	/**
//	 * Validerar epost-adress.
//	 *
//	 * @param  tree eforms-trädet
//	 * @param  result resultat-objektet
//	 * @return resultatet av valideringen
//	 */
//	private DTOValidateResult validateEpost(
//	                                        ParameterTree tree,
//	                                        DTOValidateResult result) {
//		Node node = tree.getNode("kund.epost_adress", false, null);
//
//		if (node == null) {
//			log.debug("epost=null");
//			result.addErrorMessage("Inget kund.epost_adress");
//		} else {
//			String value = node.getValue();
//
//			log.debug("epost=" + value);
//
//			int i = value.indexOf('@');
//
//			if (i < 0) {
//				result.addErrorMessage("Inget '@' i epost-adress: "
//				                       + node.getValue());
//			}
//		}
//
//		return result;
//	}
//
//
//
//
//	/**
//	 * Validerar mobilnummer.
//	 *
//	 * @param  tree eforms-trädet
//	 * @param  result resultat-objektet
//	 * @return resultatet av valideringen
//	 */
//	private DTOValidateResult validateMobilNr(
//	                                          ParameterTree tree,
//	                                          DTOValidateResult result) {
//		Node node = tree.getNode("kund.mobil_nr", false, null);
//
//		if (node == null) {
//			log.debug("mobil=null");
//			result.addErrorMessage("Inget kund.mobil_nr");
//		} else {
//			String value = node.getValue();
//
//			log.debug("mobil=" + value);
//			value = value.replaceAll("[\\s-/]", "");
//
//			try {
//				int v = Integer.parseInt(value);
//			} catch (NumberFormatException e) {
//				result.addErrorMessage("Felaktigt mobilnummer: "
//				                       + node.getValue());
//			}
//		}
//
//		return result;
//	}
//
//
//
//
//	/**
//	 * @see java.lang.Object#toString()
//	 */
//	public String toString() {
//		final String separator = ", ";
//
//		return "ValidateKund(mobilnummer & epostadress)";
//	}
//
//
//
//
//	/**
//     * Alltid null
//	 * @see se.csn.ark.www.helper.EformsHelper#update(iipax.business.shseforms.ParameterTree)
//	 */
//	public DTOValidateResult update(ParameterTree tree) {
//		return null;
//	}
//}