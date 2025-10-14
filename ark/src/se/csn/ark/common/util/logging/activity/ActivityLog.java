/*
 * Created on 2007-apr-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.csn.ark.common.util.logging.activity;

import org.apache.log4j.Category;
import org.apache.log4j.MDC;

import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.LogLevel;

/**
 * 
 * Aktivitetsloggning som används för att kunna logga alla tjänster för att
 * kunna plocka ut statistik/spårbarhet för en person. Aktivitetsloggningen
 * sköts av log4j och skickas till fil eller DB beroende på val av appender.
 * Tabellen AKTIVITETSLOGG_ET är skapad i db. Överföring av data sker med MDCer i log4j,
 * se kommentar i LogWriter klassen nedan.
 * 
 * Används också för att logga inloggningen. Både om det går bra vilken typ av
 * e-legitimation som används och vid fel, vad felet beror på. 
 * 
 * @author csn7571
 * @since 2007-04-13
 * @version 1 Skapad - Marie-Helen Andersson
 */
public final class ActivityLog {

    private static boolean activityLog = false;

    static {

        init();
    }

    /**
     * initiera.
     */
    private static void init() {

        activityLog = Properties.getBooleanProperty("ark_log4j", "activity",
                false);
    }

    /**
     * Laddar in egenskaper för denna loggklass på nytt.
     */
    public static void reloadProperties() {

        init();
    }

    /**
     * Category är log4j klassen som används för loggningen av denna log
     * wrapper.
     */
    private static Category log;

    private static ActivityLog activityLogger;

    /**
     * Instans endast via getLoggerInstance.
     */
    private ActivityLog() {

        log = Category.getInstance(ActivityLog.class);
    }

    /**
     * Indikerar om spårbarhetsloggning är aktiverad.
     * 
     * @return true om Spårbarhetsloggen är påslagen.
     */
    public static boolean isTraceingActivities() {

        return activityLog;
    }

    /**
     * Ger en activity-logger.
     * 
     * @return activity-logger
     */
    public static synchronized ActivityLog getLoggerInstance() {

        if (activityLogger == null) {
            activityLogger = new ActivityLog();
        }
        return activityLogger;
    }

    /**
     * Skriver spårbarhetsinformation till loggen.
     * 
     * @param actRecord
     *            Innehåller allt som skall loggas.
     */
    public synchronized void activity(ActivityRecord actRecord) {

        if (isTraceingActivities()) {
            // Tråda av en loggskrivare så kan exekveringen fortsätta.
            new LogWriter(actRecord);
        }
    }

    /**
     * Loggar en aktivitet.
     * 
     * 
     * @param message
     *            Objektet kommer att konverteras till String innan det skrivs
     *            till loggen.
     */
    public void activity(Object message) {

        log.log(LogLevel.ACTIVITY, message);

    }

    /**
     * Loggar en aktivitet.
     * 
     * @param message
     *            Objektet kommer att konverteras till String innan det skrivs
     *            till loggen.
     * @param throwable
     *            Throwable som skall loggas
     */
    public void activity(Object message, Throwable throwable) {

        log.log(LogLevel.ACTIVITY, message, throwable);
    }

    /**
     * Tråd som sköter skrivandet till fil.
     */
    class LogWriter implements Runnable {
        private ActivityRecord activityObjekt;

        /**
         * Skapa tråd.
         * 
         * @param activityObjekt
         *            det som ska loggas
         */
        LogWriter(ActivityRecord activityObjekt) {

            // Spara data.
            this.activityObjekt = activityObjekt;
            // Skapa en tråd med detta objekt.
            Thread t = new Thread(this);
            // Kör
            t.start();
        }

        /**
         * Sköter loggandet.
         */
        public void run() {

            /**
             * 
             * Kontrollera att det som MDCparametern ska sättas till inte är
             * null Detta för att det i vissa lägen inte är relevant att sätta
             * alla parametrar MDC parametrarna mappas i INSERT-statementet i
             * ark_log4j.properties exempel: MDC.put("sys", ) mappas mot
             * 
             * @MDC:sys@
             */

            if (activityObjekt.getSystem() != null) {
                MDC.put("sys", activityObjekt.getSystem());
            }
            if (activityObjekt.getServer() != null) {
                MDC.put("serv", activityObjekt.getServer());
            }
            if (activityObjekt.getSessionId() != null) {
                MDC.put("usess", activityObjekt.getSessionId());
            }
            if (activityObjekt.getLastAccessedTime() != null) {
                MDC.put("lacct", activityObjekt.getLastAccessedTime()); 
            }
             if (activityObjekt.getCsnNummer() != null) {
                MDC.put("csnnr", activityObjekt.getCsnNummer());
            }
            if (activityObjekt.getPersonNummer() != null) {
                MDC.put("pnr", activityObjekt.getPersonNummer());
            }
            if (activityObjekt.getInloggnMetod() != null) {
                MDC.put("inlmtd", activityObjekt.getInloggnMetod());
            }
            if (activityObjekt.getTxId() != null) {
                MDC.put("txid", activityObjekt.getTxId());
            }
            if (activityObjekt.getStrutsAction() != null) {
                MDC.put("straction", activityObjekt.getStrutsAction());
            }
            if (activityObjekt.getStrutsKod() != null) {
                MDC.put("strkod", activityObjekt.getStrutsKod());
            }
            if (activityObjekt.getStrutsError() != null) {
                MDC.put("strerr", activityObjekt.getStrutsError());
            }
            if (activityObjekt.getHandelse() != null) {
                MDC.put("handelse", activityObjekt.getHandelse());
            }
            if (activityObjekt.getParams() != null) {
                MDC.put("params", activityObjekt.getParams());
            }

            if (activityObjekt.getHandlaggare() != null) {
                MDC.put("hndl", activityObjekt.getHandlaggare());
            }
            if (activityObjekt.getMethod() != null) {
                MDC.put("method", activityObjekt.getMethod());
            }
            if (activityObjekt.getEkundStatus() != null) {
                MDC.put("ekund", activityObjekt.getEkundStatus());
            }

             log.log(LogLevel.ACTIVITY, "");
        }

    }
}
