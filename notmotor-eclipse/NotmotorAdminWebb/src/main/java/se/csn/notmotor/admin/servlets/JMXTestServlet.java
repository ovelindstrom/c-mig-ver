//package se.csn.notmotor.admin.servlets;
//import java.io.IOException;
//
//import javax.servlet.Servlet;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class JMXTestServlet extends HttpServlet implements Servlet {
//	
//	private static final long serialVersionUID = 1L;
//
//	/* (non-Java-doc)
//	 * @see javax.servlet.http.HttpServlet#HttpServlet()
//	 */
//	public JMXTestServlet() {
//		super();
//	}
//
//	/* (non-Java-doc)
//	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest arg0, HttpServletResponse arg1)
//	 */
//	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
//        WASAdminClient ace = new WASAdminClient();
//
//        // Create an AdminClient
//        ace.createAdminClient();
//
//        // Find a NodeAgent MBean
//        ace.getNodeAgentMBean("wsaxp023Node03");
//
//        // Invoke launchProcess
//        //ace.invokeLaunchProcess("server1");
//
//        // Register for NodeAgent events
//        ace.registerNotificationListener();
//         
//        // Run until interrupted
//        ace.countNotifications();
//	    
//	    
//	}
//
//	/* (non-Java-doc)
//	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest arg0, HttpServletResponse arg1)
//	 */
//	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//	}
//
//}



