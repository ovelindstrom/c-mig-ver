package se.csn.ark.www.jsptag;

import se.csn.ark.common.util.logging.Log;
import se.csn.ark.www.helper.CsnHelperController;

import javax.servlet.jsp.JspException;

/**
 * Tag som laddar en helper-klass till sidan.
 * Den utför den logik/"anropar neråt" som krävs för sidan.
 *
 * @author Joakim Olsson
 * @since 20050103
 * @version 0.1 skapad
 * @version 1.0 Förändringar av Jacob Nordin 2005-09-21. Se kommentarer i basklassen HelperTag.
 */
public class HelperInitTag extends HelperTag {

   private String helperClassName;

   private int doAfterEnd = EVAL_PAGE;

   private final Log log = Log.getInstance(HelperInitTag.class);

   /**
    * Sätter hjälp-klass-namn
    *
    * @param  name namn på hjälp-klass
    */
   public void setName(String name) {
      this.helperClassName = name;
   }

   /**
    * Initierar hjälpklass.
    *
    * @return id som definierar hur app-servern ska hantera sidan.
     * @throws JspException om initiering av helper-klass ej går att exekvera
    */
   public int doStartTag() throws JspException {

      log.debug("==> tag " + this.getClass().getName());

      CsnHelperController helper = getHelper();
      if (helper != null && helper.getClass().getName().equals(helperClassName)) {
         log.debug("Använder befintlig helper. " + "[helper=" + helper.getClass().getName() + ", "                   + "pageid=" + getPageid() + ", scope=" + getScope() + "]");
      } else {
         try {
            helper = (CsnHelperController) Class.forName(helperClassName).newInstance();
         } catch (Exception e) {
            throw new CsnJspException("Kan inte instantiera helper " + helperClassName, CsnJspException.HELPER_EXCEPTION, e);
         }

         log.debug("Skapar ny helper. " + "[helper=" + helper.getClass().getName() 
                   + ", pageid=" + getPageid() + ", scope=" + getScope() + "]");
      }

      setHelper(helper);

      // Gör alltid init på hjälpklassen även om hjälpklassen återanvänds pga bakåtkompatibelitet
      initHelper(helper);

      log.debug("<== tag " + this.getClass().getName());

      return SKIP_BODY;
   }

   /**
    * @see javax.servlet.jsp.tagext.Tag#doEndTag()
    */
   public int doEndTag() {
      return doAfterEnd;
   }

   /**
    * @param helper helper-klass som initieras
    * @throws CsnJspException om initiering ej går att exekvera
    */
   private void initHelper(CsnHelperController helper) throws CsnJspException {
      if (helper.init(pageContext, getPageid()) == SKIP_PAGE) {
         doAfterEnd = SKIP_PAGE;
      } else {
         helper.init();
      }
   }
}