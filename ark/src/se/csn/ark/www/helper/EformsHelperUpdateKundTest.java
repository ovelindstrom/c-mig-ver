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
//public class EformsHelperUpdateKundTest extends EformsHelper {
//	/**
//     * Updaterar mobil-nummer med 'q' först och sist
//	 * @see se.csn.ark.www.helper.EformsHelper#update(iipax.business.shseforms.ParameterTree)
//	 */
//	public DTOValidateResult update(ParameterTree tree) {
//		Node node = tree.getNode("kund.mobil_nr", true, null);
//
//		node.setValue("q" + node.getValue() + "q");
//
//		return new DTOValidateResult();
//	}
//
//
//
//
//	/** Alltid null
//	 * @see se.csn.ark.www.helper.EformsHelper#validate(iipax.business.shseforms.ParameterTree)
//	 */
//	public DTOValidateResult validate(ParameterTree tree) {
//		return null;
//	}
//}