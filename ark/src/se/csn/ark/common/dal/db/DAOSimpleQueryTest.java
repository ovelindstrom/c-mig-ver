package se.csn.ark.common.dal.db;

import junit.framework.TestCase;


/**
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041228
 * @version 1 skapad
 *
 */
public class DAOSimpleQueryTest extends TestCase {
	private DAOSimpleQuery daoSimpleQuery;

	/**
	 * skapa test-fall
	 */
	public DAOSimpleQueryTest() {
		super();
	}




	/**
     * skapa testfall
     * 
	 * @param arg0 namn på testfall
	 */
	public DAOSimpleQueryTest(String arg0) {
		super(arg0);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		daoSimpleQuery = new DAOSimpleQuery();
		DbConnectionFactory.createStandAloneConnection();
	}




	/**
	 * test av query
	 */
	public void testQuery() {
		try {
			String result = daoSimpleQuery.getTestResult();

            assertNotNull("inget svar", result);
		} catch (Exception e) {
			e.printStackTrace();
            fail("Ska ej kasta exception " + e);
		}
	}
}