///*
// * Skapad 2007-okt-16
// */
//package se.csn.notmotor.admin.servlets;
//
//import java.util.Date;
//import java.util.Properties;
//import java.util.Set;
//
//import javax.management.InstanceNotFoundException;
//import javax.management.MalformedObjectNameException;
//import javax.management.Notification;
//import javax.management.NotificationListener;
//import javax.management.ObjectName;
//
//import se.csn.ark.common.util.logging.Log;
//
//import com.ibm.websphere.management.AdminClient;
//import com.ibm.websphere.management.AdminClientFactory;
//import com.ibm.websphere.management.exception.ConnectorException;
//
//public class WASAdminClient implements NotificationListener {
//
//    private AdminClient adminClient;
//    private ObjectName nodeAgent;
//    private long ntfyCount = 0;
//    private Log log = Log.getInstance(WASAdminClient.class);
//
//    public void createAdminClient() {
//        // Set up a Properties object for the JMX connector attributes
//        Properties connectProps = new Properties();
//        connectProps.setProperty(AdminClient.CONNECTOR_TYPE, AdminClient.CONNECTOR_TYPE_SOAP);
//        connectProps.setProperty(AdminClient.CONNECTOR_HOST, "localhost");
//        connectProps.setProperty(AdminClient.CONNECTOR_PORT, "8881");
//        
//        // Get an AdminClient based on the connector properties
//        try {
//            log.debug("Getting client from factory");
//            adminClient = AdminClientFactory.createAdminClient(connectProps);
//            log.debug("Client retrieved");
//        } catch (ConnectorException e) {
//            log.error("Exception creating admin client", e);
//            throw new IllegalStateException(e);
//        }
//        
//        log.debug("Connected to DeploymentManager");
//    }
//    
//    
//    public void getNodeAgentMBean(String nodeName) {
//        // Query for the ObjectName of the NodeAgent MBean on the given node
//        try {
//            String query = "WebSphere:type=NodeAgent,node=" + nodeName + ",*";
//            ObjectName queryName = new ObjectName(query);
//            Set s = adminClient.queryNames(queryName, null);
//            if (!s.isEmpty()) {
//                nodeAgent = (ObjectName)s.iterator().next();
//            } else {
//                System.out.println("Node agent MBean was not found");
//                throw new IllegalStateException("Node agent MBean was not found. Check host, port, version etc");
//            }
//        } catch (MalformedObjectNameException e) {
//            System.out.println("Bad Object: " + e);
//        } catch (ConnectorException e) {
//            System.out.println("Connection failed: " + e);
//        }
//        
//        log.debug("Found NodeAgent MBean for node " + nodeName);
//    }
//    
//    public void invokeLaunchProcess(String serverName) {
//        // Use the launchProcess operation on the NodeAgent MBean to start
//        // the given server
//        String opName = "launchProcess";
//        String[] signature = {"java.lang.String"};
//        String[] params = {serverName};
//        boolean launched = false;
//        try {
//            Boolean b = (Boolean) adminClient.invoke(nodeAgent, opName, params, signature);
//            launched = b.booleanValue();
//            if (launched) {
//                System.out.println(serverName + " was launched");
//            } else {
//                System.out.println(serverName + " was not launched");
//            }
//
//        } catch (Exception e) {
//            System.out.println("Exception invoking launchProcess: " + e);
//        }
//    }
//    
//    public void registerNotificationListener() {
//        // Register this object as a listener for notifications from the
//        // NodeAgent MBean.  Don't use a filter and don't use a handback
//        // object.
//        try {
//            adminClient.addNotificationListener(nodeAgent, this, null, null);
//            log.debug("Registered for event notifications");
//        } catch (InstanceNotFoundException e) {
//            log.error("Couldnt find NodeAgent MBean", e);
//        } catch (ConnectorException e) {
//            log.error("Couldnt make JMX connection", e);
//        }
//    }
//    
//    public void handleNotification(Notification ntfyObj, Object handback) {
//        // Each notification that the NodeAgent MBean generates will result in
//        // this method being called
//        ntfyCount++;
//        log.debug("***************************************************");
//        log.debug("* Notification received at " + new Date().toString());
//        log.debug("* type      = " + ntfyObj.getType());
//        log.debug("* message   = " + ntfyObj.getMessage());
//        log.debug("* source    = " + ntfyObj.getSource());
//        log.debug("* seqNum    = " + Long.toString(ntfyObj.getSequenceNumber()));
//        log.debug("* timeStamp = " + new Date(ntfyObj.getTimeStamp()));
//        log.debug("* userData  = " + ntfyObj.getUserData());
//        log.debug("***************************************************");
//
//    }
//    
//    public void countNotifications() {
//        // Run until killed
//        try {
//        	Thread.sleep(10000);
//        	log.info(ntfyCount + " notifications have been received");
//        } catch (InterruptedException e) {
//        }
//    }
//    
//    public static void exampleUsage(String nodename) {
//        WASAdminClient ace = new WASAdminClient();
//
//        // Create an AdminClient
//        ace.createAdminClient();
//
//        // Find a NodeAgent MBean
//        ace.getNodeAgentMBean(nodename);
//
//        // Invoke launchProcess
//        //ace.invokeLaunchProcess("server1");
//
//        // Register for NodeAgent events
//        ace.registerNotificationListener();
//         
//        // Run until interrupted
//        ace.countNotifications();
//    }
//    
//}
