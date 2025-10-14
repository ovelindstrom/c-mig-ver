package se.csn.ark.www.helper;

import java.util.*;
import java.util.Map;

import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;

import se.csn.ark.common.CsnArkBaseObjectImpl;
import se.csn.ark.common.util.logging.Log;
import se.csn.ark.www.jsptag.CsnJspException;


/**
 * Tillhandahåller propertie, produkttyper och navigering till
 * hjälpklasser till jsp-sidorna.
 *
 * @author Joakim Olsson
 * @since 20050103
 * @version 0.1 skapad
 * @version 0.2	Kompletterad med ArrayList för fel-, varnings- och informationsmeddelanden, Fredrik Stenlund
 */
public class CsnHelperControllerImpl extends CsnArkBaseObjectImpl 
		implements CsnHelperController {
	
	protected static final Log log = Log.getInstance(CsnHelperControllerImpl.class);
	private PageContext pageContext;
	private PageProperties properties = null;
	private String pageid;
    private String refererPage; 



	/**
	 * @see se.csn.ark.www.helper.CsnHelperController#
     * init(javax.servlet.jsp.PageContext, java.lang.String)
	 */
	public int init(PageContext context, String id) { 	
		setPageContext(context);
		setPageid(id);
        
		properties = new PageProperties(pageid, getContextName());
        refererPage = updateReferer((String)properties.getNavigate().get("this"));

		return 0;
	}



    /**
     * @return refererande sida enl sida.properties
     */
    public String getReferer() {
        return refererPage;
    }




    /**
     * @param str refererande sida enl sida.properties
     */
    public void setReferer(String str) {
        refererPage = str;
    }




    /**
     * @param current innevarande sida
     * @return refererande sida enl sida.properties
     */
    public String updateReferer(String current) {
        String last = (String)getPageContext().getSession().getAttribute("refererPage");
        getPageContext().getSession().setAttribute("refererPage", current);
        return last;
    }




	/**
	 * @see se.csn.ark.www.helper.CsnHelperController#init()
	 */
	public void init() throws CsnJspException {
	}




	/**
	 * @see se.csn.ark.www.helper.CsnHelperController#getPageid()
	 */
	public String getPageid() {
		return pageid;
	}


    /**
     * @param id id för innevarande sida
     */
    public void setPageid(String id) {
        pageid = id;
    }




	/**
	 * @return properties för den här helpern
	 */
	public Map getProperties() {
		return properties.getProperties();
	}




    /**
     * @return alla properties för den här helpern (properties, products, navigate)
     */
    public PageProperties getPageProperties() {
        return properties;
    }




    /**
     * @return produkttyps-id
     */
	public Map getProducts() {
		return properties.getProducts();
	}




    /**
     * @return navigerings-properties
     */
	public Map getNavigate() {
		return properties.getNavigate();
	}




	/**
	 * @return pageContext för initierad sida
	 */
	protected PageContext getPageContext() {
		return pageContext;
	}




    /**
     * @param context pageContext för initierad sida
     */
    protected void setPageContext(PageContext context) {
        pageContext = context;
    }




    /**
     * @return namn på servlet context
     */
	public String getContextName() {
		String contextName = "";

		if (pageContext != null) {
			contextName = ((HttpServletRequest)(pageContext.getRequest())).getContextPath();
		}

		return contextName;
	}
}