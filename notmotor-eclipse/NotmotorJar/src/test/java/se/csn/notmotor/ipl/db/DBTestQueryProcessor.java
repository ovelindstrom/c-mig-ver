/**
 * @since 2007-apr-12
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import se.csn.common.db.TestUtils;
import junit.framework.TestCase;


public class DBTestQueryProcessor extends TestCase {

    public void testGetCounter() {
        SingleConnectionQueryProcessor qp = new SingleConnectionQueryProcessor(TestUtils.getTestDB2DataSource("csn24utv", 50000, "WDBUTV", "notmotor", "notmotor"));

        String countername = "Test" + System.currentTimeMillis();
        // Hämta testvärde
        long cnt = qp.getCounter("SEKVENS", countername);
        // Kolla att värdet är 1
        assertEquals(cnt, 1L);
        // Hämta igen
        cnt = qp.getCounter("SEKVENS", countername);
        // Kolla att värdet är 2
        assertEquals(cnt, 2L);

        // Loopa 1000 st, kolla lite prestanda
        long tick = System.currentTimeMillis();
        for (int i = 0;i < 1000;i++) {
            cnt = qp.getCounter("SEKVENS", countername);
        }
        System.out.println("Tid: " + (System.currentTimeMillis() - tick));
    }

}
