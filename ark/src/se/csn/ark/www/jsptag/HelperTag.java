package se.csn.ark.www.jsptag;

import javax.servlet.jsp.PageContext;

import se.csn.ark.common.servlet.jsp.tagext.CsnTagSupport;
import se.csn.ark.www.helper.CsnHelperController;
import se.csn.ark.common.util.logging.Log;

/**
 * Basklass för helper-taggar.
 *
 * @author Joakim Olsson
 * @since 20050103
 * @version 0.1 skapad
 * @version 1.0 Förändringar av Jacob Nordin 2005-09-21. Skrivit om klassen så att den
 *              stödjer lagring av hjälpklassen i olika scope (application, session,
 *              request och page). För att vara bakåtkompatibel så är fortfarande
 *              default-beteendet att om inget scope anges så sparas hjälpklassen i sessionen
 *              på en gemensam nyckel ("helper"), vilket innebär att hjälpklassen återanvänds
 *              om samma sida anropas två gånger efter varandra, tex i en wizard.
 */
public abstract class HelperTag extends CsnTagSupport {

   public static final String HELPER = "helper";

   public static final String HELPER_TAG = "helperTag";
   public static final String PAGE_ID = HELPER_TAG + "_pageid";
   public static final String SCOPE = HELPER_TAG + "_scope";

   public static final String APPLICATION_SCOPE_STR = "application";
   public static final String SESSION_SCOPE_STR = "session";
   public static final String REQUEST_SCOPE_STR = "request";
   public static final String PAGE_SCOPE_STR = "page";

   private final Log log = Log.getInstance(HelperTag.class);

   /**
   	 * @return Unikt id för sidan
   	 */
   protected String getPageid() {
      return (String) pageContext.getAttribute(PAGE_ID);
   }

   /**
     * @return Scope för hjälpklassen
   	 */
   protected Integer getScope() {
      return (Integer) pageContext.getAttribute(SCOPE);
   }

   /**
    * Hämta hjälpklass
    *
    * @return hjälpklass
    * @throws CsnJspException om ingen hjälpklass finns
    */
   protected CsnHelperController getHelper() throws CsnJspException {

      CsnHelperController helper = null;

      // Börjar med försöka hämta hjälpklassen i PAGE_SCOPE
      helper = (CsnHelperController) pageContext.getAttribute(HELPER);

      if (helper == null) {
         // Försök att hämta hjälpklassen i ett större scope
         Integer scope = getScope();
         if (scope == null) {
            helper = (CsnHelperController) pageContext.getAttribute(HELPER, PageContext.SESSION_SCOPE);
         } else {
            helper = (CsnHelperController) pageContext.getAttribute(HELPER + "_" + getPageid(), scope.intValue());
         }
         if (helper != null) {
            log.debug("Hämtar helper i scope " + scope + ".");
         }
      } else {
         log.debug("Hämtar helper i PAGE_SCOPE.");
      }

      return helper;
   }

   /**
    * @param  pageid Unikt id för sidan
    */
   public void setPageid(String pageid) {
      pageContext.setAttribute(PAGE_ID, pageid);
   }

   /**
   	 * @param scopeStr Scope för hjälpklassen
   	 */
   public void setScope(String scopeStr) {
      Integer scope = null;
      if (scopeStr.equals(APPLICATION_SCOPE_STR)) {
         scope = new Integer(PageContext.APPLICATION_SCOPE);
      } else if (scopeStr.equals(SESSION_SCOPE_STR)) {
         scope = new Integer(PageContext.SESSION_SCOPE);
      } else if (scopeStr.equals(REQUEST_SCOPE_STR)) {
         scope = new Integer(PageContext.REQUEST_SCOPE);
      } else if (scopeStr.equals(PAGE_SCOPE_STR)) {
         scope = new Integer(PageContext.PAGE_SCOPE);
      }

      if (scope != null) {
         pageContext.setAttribute(SCOPE, scope);
      }
   }

   /**
    * Sparar hjälpklass till det scope som är valt
    *
    * @param helper hjälpklass
    */
   protected void setHelper(CsnHelperController helper) {

      // Sparar alltid hjälpklassen i PAGE_SCOPE med generell nyckel ("helper") så den blir
      // tillgänglig på JSP-sidan med EL enligt ${helper}
      pageContext.setAttribute(HELPER, helper, PageContext.PAGE_SCOPE);
      log.debug("Spara helper i PAGE_SCOPE.");

      // Om scope är större än PAGE_SCOPE så lagras hjälpklassen även i det scopet
      // med pageid som nyckel
      Integer scope = getScope();
      if (scope == null) {
         pageContext.setAttribute(HELPER, helper, PageContext.SESSION_SCOPE);
      } else if (scope.intValue() != PageContext.PAGE_SCOPE) {
         pageContext.setAttribute(HELPER + "_" + getPageid(), helper, scope.intValue());
      }
      log.debug("Spara helper i scope: " + scope + ".");
   }
}