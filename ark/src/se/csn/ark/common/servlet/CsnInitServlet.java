package se.csn.ark.common.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * @author Joakim Olsson
 * @since 2005-02-01
 * @version 1 skapad
 *
 * För initiering av applikation
 */
public abstract class CsnInitServlet extends CsnServletImpl {

	/**
	 * Körs när applikationen startas. 
	 */
	protected abstract void onStartUp();




    /**
     * Körs när applikationen avslutas. 
     */
	protected abstract void onShutDown();

	/** 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 * Anropar initApplication
	 */
	public void init(ServletConfig arg0) throws ServletException {
		super.init(arg0);
		
		onStartUp();
	}

	/** 
	 * @see javax.servlet.GenericServlet#destroy()
	 * Anropar onShutDown
	 */
	public void destroy() {
		super.destroy();
		
		onShutDown();
	}
	
}
