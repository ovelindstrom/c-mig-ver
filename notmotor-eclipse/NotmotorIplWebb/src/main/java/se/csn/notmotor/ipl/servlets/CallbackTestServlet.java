package se.csn.notmotor.ipl.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.csn.ark.common.util.logging.Log;
import se.csn.common.serializing.ObjectSerializer;
import se.csn.notmotor.ipl.model.Meddelande;

public class CallbackTestServlet extends HttpServlet implements Servlet {

    private static final Log log = Log.getInstance(CallbackTestServlet.class);

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public CallbackTestServlet() {
        super();
    }

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest arg0, HttpServletResponse arg1)
     */
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        handleRequest(arg0);
    }

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest arg0, HttpServletResponse arg1)
     */
    protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        handleRequest(arg0);
    }

    private void handleRequest(HttpServletRequest req) {
        log.debug("HandleRequest");
        String data = req.getParameter("MEDDELANDE");
        if (data == null) {
            log.error("Inget meddelandedata i förfrågan");
            return;
        }
        log.debug("Data: " + data);
        Meddelande m = (Meddelande) ObjectSerializer.toObjectFromBase64String(data);
        log.debug("Meddelande: " + m);
    }


}
