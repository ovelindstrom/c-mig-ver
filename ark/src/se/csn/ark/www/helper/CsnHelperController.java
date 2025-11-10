package se.csn.ark.www.helper;

import javax.servlet.jsp.PageContext;

import se.csn.ark.www.jsptag.CsnJspException;


/**
 * Interface för hjälpklasser till jsp-sidorna. 
 *
 * @author Joakim Olsson
 * @since 20050103
 * @version 0.1 skapad
 */
public interface CsnHelperController {

	/**
     * Initierar helper-klassen från HelperInitTag.
     * 
	 * @param pageContext context för den jsp-sida som kör helpern
	 * @param pageid identifierare för helpern
	 * @return SKIP_PAGE eller 0 för att fortsätta
	 */
	public int init(PageContext pageContext, String pageid);


    /**
     * Här kan man lägga initieringskod som ska köras varje gång 
     * helpern laddas = varje gång sidan laddas.
     * 
     * @throws CsnJspException om helper inte initieras
     */
	public void init() throws CsnJspException;


    /**
     * Ska returnera helperns aktuella pageid.
     * @return pageid
     */
	public String getPageid();
	
}
