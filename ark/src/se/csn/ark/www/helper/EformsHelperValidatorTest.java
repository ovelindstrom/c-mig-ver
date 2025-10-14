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
//public class EformsHelperValidatorTest extends EformsHelper {
//    
//    
//    
//	/**
//     * Alltid en nytt, tomt result
//	 * @see se.csn.ark.www.helper.EformsHelper#validate(iipax.business.shseforms.ParameterTree)
//	 */
//	public DTOValidateResult validate(ParameterTree tree) {
//		return new DTOValidateResult();
//	}
//
//
//
//
//	/**
//	 * @see se.csn.ark.www.helper.EformsHelper#update(iipax.business.shseforms.ParameterTree)
//	 */
//	public DTOValidateResult update(ParameterTree tree) {
//		Node n = tree.getNode("testnod.msg", true, null);
//
//		n.setValue("testvärde");
//
//		return new DTOValidateResult();
//	}
//}