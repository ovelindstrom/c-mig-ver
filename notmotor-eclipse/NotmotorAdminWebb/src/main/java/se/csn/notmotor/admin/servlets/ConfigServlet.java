package se.csn.notmotor.admin.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.csn.ark.common.util.Properties;
import se.csn.common.config.ClassDependencyTester;
import se.csn.common.config.ConfigException;
import se.csn.common.config.PropertyDependencyTester;
import se.csn.common.jndi.ServiceLocator;
import se.csn.notmotor.admin.actions.ActionHelper;

public class ConfigServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String PROPERTIESFIL = "notmotor-iw";

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public ConfigServlet() {
        super();
    }

    protected void doGet(HttpServletRequest arg0, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
    }

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.Servlet#init(ServletConfig arg0)
     */
    public void init(ServletConfig arg0) throws ServletException {
        System.out.println("NotmotorAdmin init");
        checkExternalDependencies();
        setupDependencies();
    }

    /**
     * Kontrollerar att alla externa beroenden ar uppsatta enligt krav.
     * 
     * @return true om alla dependencies fanns och var rätt uppsatta
     */
    public boolean checkExternalDependencies() {
        // Kolla properties:
        try {
            // 0. Kolla att utils finns:
            Class.forName("se.csn.common.config.ConfigException");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Kunde inte ens hitta utils, klassen se.csn.common.config.ConfigException finns inte...", e);
        }
        // Kolla klasser:
        String[][] CLASSTEST = new String[][]{
                ClassDependencyTester.LOG4J,
                ClassDependencyTester.ARKALL,
        };
        ClassDependencyTester.findClassesThrowException(CLASSTEST);

        ClassDependencyTester.findClassThrowException("se.csn.common.util.cache.TimeoutCache", "Utils");

        String[] properties = new String[]{
                "notmotor.ds.jndinamn",
        };

        // Anropa med propertiesfilen som argument för att sätta upp Log-klassens
        // interna cache:
        Properties.init(PROPERTIESFIL);
        PropertyDependencyTester.dumpPropertiesThrowIfMissing(properties);

        System.out.println("Kontrollerat dependencies i NotmotorAdmin");

        return true;
    }

    public boolean setupDependencies() {
        ServiceLocator sl = new ServiceLocator();
        String dsnamn = Properties.getProperty(PROPERTIESFIL, "notmotor.ds.jndinamn");
        DataSource ds = sl.getDatasource(dsnamn);
        if (ds == null) {
            throw new ConfigException("Kunde inte hitta " + dsnamn, "Kontrollera att datasourcen finns i JNDI-trädet");
        }
        System.out.println("Datasource: " + ds);
        ActionHelper.setDatasource(ds);
        return true;
    }

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.Servlet#destroy()
     */
    public void destroy() {
    }

}
