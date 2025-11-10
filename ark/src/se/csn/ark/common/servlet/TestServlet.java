package se.csn.ark.common.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import se.csn.ark.common.util.FormatDate;
//import se.csn.ark.common.util.logging.Log;


/**
 * Med denna servlet testas grundläggande konfiguration i WAS som
 * t.ex. att databasen är rätt konfigurerad.
 *
 * @author K-G Sjöström - AcandoFrontec
 * @since 20041212
 * @version 1 skapad
 *
 */
//public class TestServlet extends CsnServletImpl {
public class TestServlet extends HttpServlet {
    
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    //private Log log = Log.getInstance(TestServlet.class);

	/**
	 * Skapar ny servletinstans.
	 */
	public TestServlet() {
		super();
	}

	/**
	 * Implementation av HttpServlet.
	 * Gör anrop mot databasen med testfråga.
	 *
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, 
     *          javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	              throws ServletException, IOException {
		try {
			/*
            if (log.isDebugEnabled()) {
				log.debug("doGet");
			}
            */

			ServletOutputStream out = resp.getOutputStream();

			out.println("<font color=#1E861D>");
			out.println("<h1>TestServlet</h1>");
			out.println("<h2>Testar grundläggande konfiguration i WAS</h2>");
			//out.println("Tid : " + FormatDate.getCurrentTimeString());
			out.println("<h3>JDBC</h3>");

			/*
            DAOSimpleQuery dbTest = new DAOSimpleQuery();
			String result = dbTest.getTestResult();

			out.println(result + "<br>");
			out.println("<h3>JMS</h3>");

			Queue queue = QueueFactory.getQueue(Queues.QUEUE1);
            */
            
			//out.println("JMS Queue1:<br>" + queue + "<br>");
			out.println("</font>");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	/**
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		//if (log.isDebugEnabled()) {
		//	log.debug("init");
		//}
	}
}