//package se.csn.ark.www.helper;
//
//import iipax.business.shseforms.ParameterTree;
//import junit.framework.TestCase;
//
//
///**
// * Testklass för EformsHelper.
// *
// * @author Joakim Olsson
// * @since 20041202
// * @version 0.1 skapad
// */
//public class EformsHelperTest extends TestCase {
//	private EformsHelper helper;
//
//	/**
//	 * Constructor for EformsHelperTest.
//	 * @param arg0 namn på testfall
//	 */
//	public EformsHelperTest(String arg0) {
//		super(arg0);
//	}
//
//	/**
//	 * @see TestCase#setUp()
//	 */
//	protected void setUp() throws Exception {
//		super.setUp();
//		se.csn.ark.common.util.Properties.init("sida");
//		helper = new EformsHelperValidatorTest();
//		try {
//			helper.init(null, "testconfirm");
//		} catch (NullPointerException e) {
//		}
//	}
//
//
//
//
//	/**
//	 * @see TestCase#tearDown()
//	 */
//	protected void tearDown() throws Exception {
//		super.tearDown();
//		helper = null;
//	}
//
//
//
//
//	/**
//	 * Test av korrekt validering
//     * @throws Exception vid fel i validate
//	 */
//	public void testValidate() throws Exception {
//		ParameterTree tree = new ParameterTree();
//
//		DTOValidateResult result = helper.validate(tree);
//
//		assertNotNull(result);
//		assertEquals(true, result.getExecuted());
//		assertEquals(true, result.getValid());
//		assertEquals(false, result.getNotValid());
//	}
//
//
//
//
//	/**
//	 * Test korrekt update
//     * @throws Exception vid fel i update
//	 */
//	public void testUpdate() throws Exception {
//		ParameterTree tree = new ParameterTree();
//
//		helper.update(tree);
//
//		assertNotNull(tree.getNode("testnod.msg", false, null));
//		assertEquals(
//		             "testvärde",
//		             tree.getNode("testnod.msg", false, null).getValue());
//	}
//
//
//
//
//	/**
//	 * Test för properties
//	 */
//	/*public void testProperties() {
//		assertEquals(
//		             "/TestPageTag/getKund.jsp",
//		             helper.getNavigate().get("forward"));
//		assertEquals(
//		             "eae0bc9f-1e69-fffe-4150-cbafe04b77c9",
//		             helper.getProducts().get("kund"));
//		assertEquals("Kalle Anka", helper.getProperties().get("testproperty"));
//		assertEquals("Musse Pigg", helper.getProperties().get("testprop2"));
//	}*/
//	
//}
