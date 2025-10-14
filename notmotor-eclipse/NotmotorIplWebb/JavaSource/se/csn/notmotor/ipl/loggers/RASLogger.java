///**
// * Skapad 2007-jun-04
// * @author Jonas Öhrnell (csn7821)
// * 
// */
//package se.csn.notmotor.ipl.loggers;
//
//import java.util.Enumeration;
//import java.util.Hashtable;
//
//import se.csn.ark.common.util.logging.Log;
//
//import com.ibm.ras.RASIEvent;
//import com.ibm.ras.RASIFormatter;
//import com.ibm.ras.RASIHandler;
//import com.ibm.ras.RASIMaskChangeListener;
//import com.ibm.ras.RASIMessageEvent;
//import com.ibm.ras.RASMaskChangeEvent;
//
//
//public class RASLogger implements RASIHandler {
//
//    private Log log = Log.getInstance(RASLogger.class);
//    private String prefix;
//    
//    public RASLogger(String prefix) {
//        this.prefix = prefix;
//    }
//    
//    public void addFormatter(RASIFormatter arg0) {
//        log.debug("addFormatter");
//    }
//    public void closeDevice() {
//        log.debug("closeDevice");
//    }
//    public Hashtable getConfig() {
//        log.debug("getConfig");
//        return null;
//    }
//    public Enumeration getFormatters() {
//        log.debug("getFormatters");
//        return null;
//    }
//    public int getMaximumQueueSize() {
//        log.debug("getMaximumQueueSize");
//        return 0;
//    }
//    public int getQueueSize() {
//        log.debug("getQueueSize");
//        return 0;
//    }
//    public int getRetryInterval() {
//        log.debug("getRetryInterval");
//        return 0;
//    }
//    public void logEvent(RASIEvent arg0) {
//       log.debug(prefix + "(log): " + arg0.getText());
//    }
//    public void openDevice() {
//        log.debug("openDevice");
//    }
//    public void removeFormatter(RASIFormatter arg0) {
//        log.debug("removeFormatter");
//    }
//    public void setConfig(Hashtable arg0) {
//        log.debug("setConfig");
//    }
//    public void setMaximumQueueSize(int arg0) throws IllegalStateException {
//        log.debug("setmaximumQueueSize" + arg0);
//    }
//    public void setRetryInterval(int arg0) {
//        log.debug("setRetryInterval " + arg0 );
//    }
//    public void stop() {
//        log.debug("stop");
//    }
//    public void writeEvent(RASIEvent arg0) {
//        log.debug(prefix + "(write): " + arg0.getText());
//    }
//    public void addMaskChangeListener(RASIMaskChangeListener arg0) {
//        log.debug("addMaskChangeListener");
//    }
//    public void addMessageEventClass(String arg0) {
//        log.debug("addMessageEventClass " + arg0);
//    }
//    public void addTraceEventClass(String arg0) {
//        log.debug("addTraceEventClass " + arg0);
//    }
//    public void fireMaskChangedEvent(RASMaskChangeEvent arg0) {
//        log.debug("fireMaskChangedEvent " + arg0);
//    }
//    public Enumeration getMaskChangeListeners() {
//        log.debug("getMaskChangeListeners");
//        return null;
//    }
//    public Enumeration getMessageEventClasses() {
//        log.debug("getMessageEventClasses");
//        return null;
//    }
//    public long getMessageMask() {
//        log.debug("getMessageMask");
//        return RASIMessageEvent.ALL_MESSAGE_MASK;
//    }
//    public Enumeration getTraceEventClasses() {
//        log.debug("getTraceEventClasses");
//        return null;
//    }
//    public long getTraceMask() {
//        log.debug("getTraceMask");
//        return 0;
//    }
//    public void removeMaskChangeListener(RASIMaskChangeListener arg0) {
//        log.debug("removeMaskChangeListener");
//    }
//    public void removeMessageEventClass(String arg0) {
//        log.debug("removeMessageEventClass " + arg0);
//    }
//    public void removeTraceEventClass(String arg0) {
//        log.debug("removeTraceEventClass " + arg0);
//    }
//    public void setMessageMask(long arg0) {
//        log.debug("setMessageMask " + arg0);
//    }
//    public void setTraceMask(long arg0) {
//        log.debug("setTraceMask " + arg0);
//    }
//    public String getDescription() {
//        log.debug("getDescription");
//        return null;
//    }
//    public String getGroup() {
//        log.debug("getGroup");
//        return null;
//    }
//    public String getName() {
//        log.debug("getName");
//        return null;
//    }
//    public void setDescription(String arg0) {
//        log.debug("setDescription " + arg0);
//    }
//    public void setName(String arg0) {
//        log.debug("setName " + arg0);
//    }
//}
