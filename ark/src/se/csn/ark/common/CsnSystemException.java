package se.csn.ark.common;

/**
 *
 * Basklass för system fel. Skall användas för att indikera oväntade fel som
 * är utanför applikationens kontroll som gör att det inte går att komma åt
 * databas server eller annan resurs som är nödvändig för att applikationen
 * skall kunna fungera.
 *
 * Den klass skall aldrig själv användas för att påvisa fel.
 * Ärv av denna klass och skapa ett specifikt fel.
 *
 * @author K-G Sjöström
 * @since 040809
 * @version 1 skapad
 * @version 1.1 Förändringar av Jacob Nordin 2005-09-23. Lagt till LOGICAL_ERROR som feltyp.
 * @see se.csn.ark.common.CsnApplikationException
 *
 */
public class CsnSystemException extends RuntimeException implements CsnException {
   public static final Integer SYSTEM_ERROR = new Integer(1000);
   public static final Integer COOLGEN_ERROR = new Integer(1001);
   public static final Integer WEBSERVICE_ERROR = new Integer(1002);
   public static final Integer EDH_ERROR = new Integer(1003);
   public static final Integer DB_ERROR = new Integer(1004);

   // CsnLogicalException ska egentligen inte ärva från CsnSystemException utan utan vara en egen
   // undantagstyp på rotnivån tillsammans med CsnSystemException och CsnApplicationException. Av
   // praktiska skäl ärver den från CsnSystemException nu för att vi inte ska behöva ändra i
   // webservice-hanteringen.
   // TODO Ta bort den här konstanten när CsnLogicalException inte längre ärver från
   // CsnSystemException.
   public static final Integer LOGICAL_ERROR = new Integer(1005);

   private Integer errorId = null;

   /**
    * Skapar ett system fel.
    *
    * @param message Felmeddelande
    */
   protected CsnSystemException(String message) {
      this(message, SYSTEM_ERROR);
   }

   /**
    * Skapar ett system fel.
    *
    * @param message Felmeddelande
    * @param cause Orsak
    */
   protected CsnSystemException(String message, Throwable cause) {
      this(message, SYSTEM_ERROR, cause);
   }

   /**
    * Skapar ett system fel.
    *
    * @param message Felmeddelande
    * @param errorId Identitiet för att kunna hämta feltext.
    */
   protected CsnSystemException(String message, Integer errorId) {
      super(message);
      this.errorId = errorId;
   }

   /**
    * Skapar ett system fel.
    *
    * @param message Felmeddelande
    * @param errorId Identitiet för att kunna hämta feltext
    * @param cause Orsak
    */
   protected CsnSystemException(String message, Integer errorId, Throwable cause) {
      super(message, cause);
      this.errorId = errorId;
   }

   /**
    * @see se.csn.ark.common.CsnException#getType()
    */
   public Integer getType() {
      return CsnException.SYSTEM;
   }

   /**
    * Fel id för detta system fel som kan användas för att hämta motsvarande
    * felmeddelande från fil eller databas.
    *
    * @see se.csn.ipl.webbansokan.arkitektur.CsnException#getFelId()
    * @return Returnerar <code>null</code> om id ej definierat
    */
   public Integer getErrorId() {
      return errorId;
   }

   /**
    * Returnerar en sträng representation av detta objekt innehållande
    * alla eventuellt nästade <code>Exception</code>
    *
    * @return En sträng med all felbeskrivning
    */
   public String toString() {
      String msg = this.getClass().getName() + " (Typ= " + getType() + ", Id=" + getErrorId() + "): " + getMessage();

      if (getCause() != null) {
         msg += (", orsakat av -> " + getCause().toString());
      }

      return msg;
   }

   /**
    * Återskapar CsnException utifrån indata
    *
    * @param message exception-meddelande
    * @param errorId id på CsnException
    * @return återskapt exception
    */
   public static CsnException reCreateCsnException(String message, Integer errorId) {
      CsnException csnException;

      csnException = new CsnSystemException(message, errorId, null);

      return csnException;
   }
}