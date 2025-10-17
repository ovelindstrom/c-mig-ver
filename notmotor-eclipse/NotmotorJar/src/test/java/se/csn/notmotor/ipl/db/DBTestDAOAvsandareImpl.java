/**
 * @since 2007-apr-12
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import se.csn.common.db.TestUtils;
import se.csn.notmotor.ipl.model.Avsandare;


public class DBTestDAOAvsandareImpl extends TestCase {

    private QueryProcessor getQueryProcessor() {
        return new SingleConnectionQueryProcessor(TestUtils.getTestDB2DataSource("csn24utv", 50000, "WDBUTV", "notmotor", "notmotor"));
    }

    public void testCRUD() {
        DAOAvsandareImpl dao = new DAOAvsandareImpl(getQueryProcessor());

        // Skapa
        Avsandare avs = new Avsandare();
        avs.setApplikation("Applikation");
        avs.setEpostadress("jonas.ohrnell@csn.se");
        avs.setKategori("Kategori");
        avs.setNamn("Namn");
        avs.setReplyTo("noreply@csn.se");
        int id = dao.createAvsandare(avs);

        // Hämta
        Avsandare avs2 = dao.getAvsandare(id);
        assertEquals(avs, avs2);

        // Ändra 
        avs.setApplikation("Annan app");
        avs.setEpostadress("annan@adress.se");
        avs.setKategori("Annan kategori åäö");
        avs.setNamn("Annat namn");
        avs.setReplyTo("annan.reply@csn.se");
        dao.updateAvsandare(avs);

        // Hämta
        avs2 = dao.getAvsandare(id);
        assertEquals(avs, avs2);

        // Ta bort
        dao.deleteAvsandare(avs);

        // Försök hämta
        int id2 = dao.getId(avs);
        assertEquals(id2, -1);
    }

    public void testGetId() {
        DAOAvsandareImpl dao = new DAOAvsandareImpl(getQueryProcessor());

        Avsandare avs = new Avsandare();
        avs.setApplikation("Applikation");
        avs.setEpostadress("jonas.ohrnell@csn.se");
        avs.setKategori("Kategori");
        avs.setNamn("Namn");
        avs.setReplyTo("noreply@csn.se");
        int id = dao.createAvsandare(avs);
        int id2 = dao.getId(avs);
        assertEquals(id, id2);
    }

    public void testGetAvsandareList() {
        DAOAvsandareImpl dao = new DAOAvsandareImpl(getQueryProcessor());

        // Hämta alla
        Avsandare avs = new Avsandare();
        avs.setReplyTo("%@csn.se");
        avs.setEpostadress("%nas%");
        List list = dao.getAvsandare(avs);
        for (Iterator it = list.iterator();it.hasNext();) {
            System.out.println(it.next().toString());
        }
    }

    public void testDeleteAll() {
        DAOAvsandareImpl dao = new DAOAvsandareImpl(getQueryProcessor());
        Avsandare avs = new Avsandare();
        List list = dao.getAvsandare(avs);
        for (Iterator it = list.iterator();it.hasNext();) {
            dao.deleteAvsandare((Avsandare) it.next());
            System.out.println("Tog bort avsändare.");
        }

        // Kontrollera:
        list = dao.getAvsandare(avs);
        assertEquals(list.size(), 0);
    }


}
