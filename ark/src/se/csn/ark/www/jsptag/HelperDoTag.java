package se.csn.ark.www.jsptag;

import se.csn.ark.common.util.logging.Log;
import se.csn.ark.www.helper.CsnHelperController;

import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;

/**
 * Tag som anropar specificerad metod i helperklassen.
 *
 * @author Joakim Olsson
 * @since 20050103
 * @version 0.1 skapad
 * @version 1.0 Förändringar av Jacob Nordin 2005-09-21. Se kommentarer i basklassen HelperTag.
 */
public class HelperDoTag extends HelperTag {
   private String helperCommandName = null;
   private String helperCommandArgument = null;
   private int doAfterStart = SKIP_BODY;
   private int doAfterEnd = EVAL_PAGE;
   
	private final Log log = Log.getInstance(HelperDoTag.class);

   /**
    * Sätter det kommando som ska exekveras
    *
    * @param  name kommandonamn
    */
   public void setCommand(String name) {
      this.helperCommandName = name;
   }

   /**
    * Sätter argument till kommando
    *
    * @param  commandArgument argument
    */
   public void setArg(String commandArgument) {
      this.helperCommandArgument = commandArgument;
   }

   /**
    * Anropar kommando.
    *
    * @return id som definierar hur app-servern ska hantera sidan.
    * @throws JspException om kommando ej går att exekvera
    */
   public int doStartTag() throws JspException {
      log.debug("==> tag " + this.getClass().getName() + "[command=" + helperCommandName + ", arg=" + helperCommandArgument + "]");

      CsnHelperController helper = getHelper();
      if (helper == null) {
         throw new CsnJspException("No helper initialized", CsnJspException.HELPER_EXCEPTION);
      }

      doCommand(helper);

      log.debug("<== tag " + this.getClass().getName());

      return doAfterStart;
   }

   /**
    * @see javax.servlet.jsp.tagext.Tag#doEndTag()
    */
   public int doEndTag() {
      return doAfterEnd;
   }

   /**
     * Kör kommando på helper-objektet
    * @param helper helper som exekverar kommandot
     * @throws JspException om kommando ej går att exekvera
    */
   private void doCommand(Object helper) throws JspException {
      String methodName = "do" + helperCommandName.substring(0, 1).toUpperCase() + helperCommandName.substring(1);

      Object obj = null;
      Class[] types = null;
      Object[] args = null;

      if (helperCommandArgument != null) {
         types = new Class[] {String.class};
         args = new Object[] {helperCommandArgument};
      }

      try {
         final Method doCommand = helper.getClass().getMethod(methodName, types);
         obj = doCommand.invoke(helper, args);

      } catch (Exception e) {
         String msg = "Unable to call " + methodName;

         if (helperCommandArgument != null) {
            msg += ("(String " + helperCommandArgument + ")");
         }

         msg += (" in " + helper.getClass().getName());
         throw new CsnJspException(msg, CsnJspException.HELPER_EXCEPTION, e);
      }

      // Titta på ev returvärde
      if ((obj != null) && obj instanceof Integer) {
         int val = ((Integer) obj).intValue();

         if ((val == SKIP_BODY) || (val == EVAL_BODY_INCLUDE)) {
            doAfterStart = val;

         } else if ((val == SKIP_PAGE) || (val == EVAL_PAGE)) {
            doAfterEnd = val;
         }
      }
   }
}