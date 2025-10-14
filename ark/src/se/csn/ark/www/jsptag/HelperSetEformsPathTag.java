//package se.csn.ark.www.jsptag;
//
//import se.csn.ark.common.util.logging.Log;
//import se.csn.ark.www.helper.CsnHelperController;
//import se.csn.ark.www.helper.EformsHelper;
//
//import javax.servlet.jsp.JspException;
//
///**
// * Tag som hämtar data ur eforms-trädet.
// *
// * @author Joakim Olsson
// * @since 20050221
// * @version 0.1 skapad
// * @version 1.0 Förändringar av Jacob Nordin 2005-09-21. Se kommentarer i basklassen HelperTag.
// */
//public class HelperSetEformsPathTag extends HelperTag {
//   private static final Log log = Log.getInstance(HelperSetEformsPathTag.class);
//   private String path = "";
//   private boolean isAbsolute = false;
//
//   /**
//    * Sätter eforms-pathen
//    *
//    * @param  nodePath den nod som man ska hämta värdet från
//    */
//   public void setPath(String nodePath) {
//      this.path = nodePath;
//   }
//
//   /**
//    * Sätter om den givna eforms-pathen är absolut
//    *
//    * @param  absolute true=absolut, false=relativ
//    */
//   public void setAbsolute(boolean absolute) {
//      this.isAbsolute = absolute;
//   }
//
//   /**
//    * Anropar kommando.
//    *
//    * @return id som definierar hur app-servern ska hantera sidan.
//     * @throws JspException om ingen helper finns
//    */
//   public int doStartTag() throws JspException {
//      log.debug("==> tag " + this.getClass().getName());
//
//      CsnHelperController helper = getHelper();
//      if (helper == null) {
//         throw new CsnJspException("No helper initialized", CsnJspException.HELPER_EXCEPTION);
//      }
//
//      setEformsPath(helper);
//
//      log.debug("<== tag " + this.getClass().getName());
//
//      return SKIP_BODY;
//   }
//
//   /**
//     * Sätter path i eforms-trädet
//    * @param helper den helper som används
//     * @throws JspException om ingen riktig helper finns
//    */
//   private void setEformsPath(Object helper) throws JspException {
//      // Hantera fel
//      if (helper == null) {
//         throw new CsnJspException("Helper == null", CsnJspException.HELPER_EXCEPTION);
//      } else if (!(helper instanceof EformsHelper)) {
//         throw new CsnJspException("GetFromTree måste utföras på helper " + EformsHelper.class.getName() 
//                                   + " ej på " + helper.getClass().getName(), 
//                                   CsnJspException.HELPER_EXCEPTION);
//      }
//
//      EformsHelper eHelper = (EformsHelper) helper;
//
//      eHelper.setPath(path, isAbsolute);
//   }
//}