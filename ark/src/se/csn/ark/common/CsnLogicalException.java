package se.csn.ark.common;

/**
 * @author Jacob Nordin, Steria
 * @since 2005-09-22
 * @version 1.0 Klassen ska egentligen inte ärva från CsnSystemException utan utan vara en egen
 *              undantagstyp som ärver från java.lang.RuntimeException på rotnivån tillsammans med
 *              CsnSystemException och CsnApplicationException. Av praktiska skäl ärver den
 *              från CsnSystemException nu för att vi inte ska behöva ändra i webservice-hanteringen.
 */
public class CsnLogicalException extends CsnSystemException {

   // TODO Ändra arv till java.lang.RuntimeException

   /**
    * @param message felmeddelande
    */
   public CsnLogicalException(String message) {

      super(message, LOGICAL_ERROR);
   }

   /**
    * @param message felmeddelande
    * @param errorId id på felet
    */
   public CsnLogicalException(String message, Integer errorId) {
      super(message, errorId);
   }

   /**
    * @param message felmeddelande
    * @param cause exception som orsakade felet
    */
   public CsnLogicalException(String message, Throwable cause) {

      super(message, LOGICAL_ERROR, cause);
   }

   /**
    * @param message felmeddelande
    * @param errorId id på felet
    * @param cause exception som orsakade felet
    */
   public CsnLogicalException(String message, Integer errorId, Throwable cause) {
      super(message, errorId, cause);
   }
}
