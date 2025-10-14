//package se.csn.ark.www.helper;
//
//import iipax.business.shseforms.FormData;
//import iipax.business.shseforms.Node;
//import iipax.business.shseforms.ParameterTree;
//
//import se.csn.ark.common.util.logging.Log;
//import se.csn.ark.common.util.logging.trace.TraceUtil;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.StringTokenizer;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import javax.servlet.jsp.PageContext;
//import javax.servlet.jsp.tagext.Tag;
//
//
//
//
//
//
//
///**
// * Tillhandahåller validering och uppdatering av eforms-träd.
// * Ärv och implementera update och validate.
// *
// * @author Joakim Olsson
// * @since 20050103
// * @version 1 skapad
// */
//public class EformsHelper extends CsnHelperControllerImpl
//	implements CsnHelperController {
//	private DTOValidateResult valid = null;
//	private DTOValidateResult updated = null;
//	private String eformsPath = "";
//	private boolean isAbsolutePath = false;
//
//	/**
//	 * @see se.csn.ark.www.helper.CsnHelperController
//	 * #init(javax.servlet.jsp.PageContext, java.lang.String)
//	 */
//	public int init(PageContext pageContext, String pageid) {
//        setPageContext(pageContext);
//        setPageid(pageid);
//        
//		int ret = reloadWhenPost();
//        
//        if (ret == 0) {
//            super.init(pageContext, pageid);
//        }
//
//		return ret;
//	}
//
//
//
//
//	/**
//	 * laddar om POST'ad sida med GET
//	 * @return SKIP_PAGE eller 0 för att fortsätta
//	 */
//	protected int reloadWhenPost() {
//		HttpServletRequest req =
//			(HttpServletRequest)getPageContext().getRequest();
//		int ret = 0;
//
//		if ("POST".equals(req.getMethod())) {
//            ret = forward(req.getRequestURI(), req.getQueryString());
//		} else {
//			;
//		}
//
//		return ret;
//	}
//
//
//
//
//	/**
//	 * Implementeras av sub-klasser. Körs första gången en sida laddas.
//	 *
//	 * @param tree eforms-trädet
//	 * @return default null, sub-klass kan returnera resultat-information till sidan
//	 * @throws ValidatorException vid misslyckad uppdatering
//	 */
//	public DTOValidateResult update(ParameterTree tree)
//	                         throws ValidatorException {
//		return null;
//	}
//
//
//
//
//	/**
//	 * Implementeras av sub-klasser. Körs alla gånger utom första gången en sida laddas.
//	 *
//	 * @param tree eforms-trädet
//	 * @return default null, sub-klass kan returnera resultat-information till sidan
//	 * @throws ValidatorException vid misslyckad uppdatering
//	 */
//	public DTOValidateResult validate(ParameterTree tree)
//	                           throws ValidatorException {
//		return null;
//	}
//
//
//
//
//	/**
//	 * Letar fram eforms-träd och anropar validate.
//	 * OBS första gången sker ingen validering
//     * 
//     * @return Tag.SKIP_BODY eller Tag.SKIP_PAGE om vi går
//     * till nästa sida
//	 */
//	public int doValidate() {
//		int ret = Tag.SKIP_BODY;
//		DTOValidateResult result = new DTOValidateResult();
//
//		result.setStatus(DTOValidateResult.NOT_EXECUTED);
//
//		HttpServletRequest request =
//			(HttpServletRequest)getPageContext().getRequest();
//		String query = request.getQueryString();
//
//		// Om det inte är första gången
//		if (
//		    (getValid() != null)
//		    && (query != null)
//		    && (query.indexOf("validate") >= 0)) {
//			FormData formData =
//				FormData.retrieve(getPageContext().getSession());
//			ParameterTree tree = formData.getTree();
//
//			// Kontroll av inparametrar
//			if (tree == null) {
//				String message =
//					"Det gick inte att validera. Inget träd finns.";
//
//				log.error(message);
//				result.couldNotExecute(message);
//
//				setValid(result);
//				log.debug("<=== validate [" + result + " ret=" + ret + "]");
//
//				return ret;
//			}
//
//			try {
//				result = validate(tree);
//			} catch (Exception e) {
//				final String message =
//					"Det gick inte att validera. "
//					+ "Validatorn misslyckades att köra";
//
//				log.error(message, e);
//				result = new DTOValidateResult();
//				result.couldNotExecute(message);
//
//				setValid(result);
//				log.debug("<=== validate [" + result + " ret=" + ret + "]");
//
//				return ret;
//			}
//
//			if (result.getValid()) {
//				ret = Tag.SKIP_PAGE;
//
//				/**
//				 * Om ingen ny url property är satt då körs den vanliga..default
//				 */
//				if (result.getSAlternativeUrlProperty() == null) {
//					forwardAppendContext(
//					                     getPageProperties().getNavigate("forward"),
//					                     null);
//				} else {
//					//Ny url property satt...
//					forwardAppendContext(getPageProperties().getNavigate(
//                                         result.getSAlternativeUrlProperty()),
//					                     null);
//				}
//			}
//		}
//
//		setValid(result);
//		log.debug("<=== validate [" + result + " ret=" + ret + "]");
//
//		return ret;
//	}
//
//
//
//
//	/**
//	 * Letar fram eforms-träd och anropar update.
//     * @return Tag.SKIP_BODY
//	 */
//	public int doUpdate() {
//		int ret = Tag.SKIP_BODY;
//		DTOValidateResult result = new DTOValidateResult();
//
//		result.setStatus(DTOValidateResult.NOT_EXECUTED);
//
//		FormData formData = FormData.retrieve(getPageContext().getSession());
//		ParameterTree tree = formData.getTree();
//
//		if (tree == null) {
//			String message = "Det gick inte att updatera. Inget eforms-träd";
//
//			log.error(message);
//			result.couldNotExecute(message);
//
//			setUpdated(result);
//			log.debug("<=== update [" + result + "]");
//
//			return ret;
//		}
//
//		HttpServletRequest req =
//			(HttpServletRequest)getPageContext().getRequest();
//		StringTokenizer st;
//
////		String refererPage = req.getHeader("referer");
//        String refererPage = getReferer();
//        
//		if (refererPage != null) {
//			st = new StringTokenizer(refererPage, "?");
//
//			if (st.hasMoreTokens()) {
//				refererPage = st.nextToken();
//			}
//
//			if (refererPage == null) {
//				refererPage = "";
//			}
//		}
//
//		String nextPage = (String)getNavigate().get("forward");
//
//		if (nextPage != null) {
//			st = new StringTokenizer(nextPage, "?");
//
//			if (st.hasMoreTokens()) {
//				nextPage = st.nextToken();
//			}
//
//			if (nextPage == null) {
//				nextPage = "";
//			}
//		}
//
//		String currentPage = (String)getNavigate().get("this");
//
//		if (currentPage != null) {
//			st = new StringTokenizer(currentPage, "?");
//
//			if (st.hasMoreTokens()) {
//				currentPage = st.nextToken();
//			}
//
//			if (currentPage == null) {
//				currentPage = "";
//			}
//		}
//
//		String sammanstallningPage =
//			(String)getNavigate().get("sammanstallning");
//
//		/*
//		 * Om referer, nextPage eller currentPage inte är satta i konfigurationen så körs
//		 * update.
//		 * Om man inte kommer framifrån (alltså backar in till sidan) och inte
//		 * kommer från samma sida (alltså sidan laddas om) så körs update.
//		 */
//		boolean doUpdate = false;
//
//		if (configParameterMissing(refererPage, nextPage, currentPage)) {
//			doUpdate = true;
//		} else if (
//		           refersToSamePage(refererPage, nextPage)
//		           || refersToSamePage(refererPage, currentPage)
//		           || ((sammanstallningPage != null)
//		           && refersToSamePage(refererPage, sammanstallningPage))) {
//			doUpdate = false;
//		} else {
//			doUpdate = true;
//		}
//
//		if (doUpdate) {
//			try {
//				result = update(tree);
//			} catch (Exception e) {
//				final String message =
//					"Det gick inte att uppdatera. "
//					+ "Updater misslyckades att köra";
//
//				log.error(message, e);
//
//				result = new DTOValidateResult();
//				result.couldNotExecute(message);
//
//				setUpdated(result);
//				log.debug("<=== update [" + result + "]");
//
//				return ret;
//			}
//		}
//
//		setUpdated(result);
//		log.debug("<=== update [" + result + "]");
//
//		return ret;
//	}
//
//
//
//
//	/**
//	 * @param referer refererande sida
//	 * @param page denna sida
//	 * @return true om denna sida är samma som refererande
//	 */
//	private boolean refersToSamePage(String referer, String page) {
//		return referer.endsWith(page);
//	}
//
//
//
//
//	/**
//	 * @param referer refererande sida
//	 * @param nextPage nästa sida
//	 * @param currentPage denna sida
//	 * @return true om ingen sida är null
//	 */
//	private boolean configParameterMissing(
//	                                       String referer,
//	                                       String nextPage,
//	                                       String currentPage) {
//		return (referer == null) || (nextPage == null) || (currentPage == null);
//	}
//
//
//
//
//	/**
//     * Sätter ev trace-parametrar i trädet.
//     * 
//	 * @param event namn på trace-händelse
//	 * @return Tag.SKIP_BODY
//	 */
//	public int doInitTrace(String event) {
//		int ret = Tag.SKIP_BODY;
//
//		HttpSession session = getPageContext().getSession();
//		FormData formData = FormData.retrieve(session);
//		ParameterTree tree = formData.getTree();
//
//		if (
//		    (tree != null)
//		    && (tree.getNode("trace.transactionid", false, null) == null)) {
//			Node txidNode = tree.getNode("trace.transactionid", true, null);
//
//			txidNode.setValue(TraceUtil.createCsnTxId(session));
//
//			Node eventNode = tree.getNode("trace.event", true, null);
//
//			eventNode.setValue(event);
//		}
//
//		return ret;
//	}
//
//
//
//
//	/**
//     * Sätter pathen i eforms-trädet
//	 * @param path path i eforms-trädet
//	 * @param isAbsolute true om path är absolut, false om relativ
//	 */
//	public void setPath(String path, boolean isAbsolute) {
//		eformsPath = path;
//		isAbsolutePath = isAbsolute;
//	}
//
//
//
//
//	/**
//	 * @return värde på nod som pekas ut av setPath
//	 */
//	public String getNodeValue() {
//		String nodeValue = "";
//
//		FormData formData = FormData.retrieve(getPageContext().getSession());
//		ParameterTree tree = formData.getTree();
//
//		if (tree != null) {
//			String path = formData.getTree().getCurrentPath(getPageContext());
//
//			if (isAbsolutePath || (path == null) || (path.length() == 0)) {
//				path = eformsPath;
//			} else {
//				path = path + "." + eformsPath;
//			}
//
//			Node node = tree.getNode(path, false, null);
//
//			if (node != null) {
//				nodeValue = node.getValue();
//			}
//		}
//
//		return nodeValue;
//	}
//
//
//
//
//	/**
//	 * @return "&trace.event="
//	 */
//	public String getTraceDataBegin() {
//		String traceBegin = "";
//
//		traceBegin += "&trace.event=";
//
//		return traceBegin;
//	}
//
//
//
//
//	/**
//	 * @return "&trace.transactionid=" + sessionid
//	 */
//	public String getTraceDataEnd() {
//		String traceEnd = "";
//
//		traceEnd += "&trace.transactionid=";
//		traceEnd += TraceUtil.createCsnTxId(getPageContext().getSession());
//
//		return traceEnd;
//	}
//
//
//
//
//	/**
//	 * @return resultat av update
//	 */
//	public DTOValidateResult getUpdated() {
//		return updated;
//	}
//
//
//
//
//	/**
//	 * @return resultat av validate
//	 */
//	public DTOValidateResult getValid() {
//		return valid;
//	}
//
//
//
//
//	/**
//	 * @param result resultat av update
//	 */
//	public void setUpdated(DTOValidateResult result) {
//		updated = result;
//	}
//
//
//
//
//	/**
//	 * @param result resultat av validate
//	 */
//	public void setValid(DTOValidateResult result) {
//		valid = result;
//	}
//
//
//
//
//	/**
//     * Gör forward till fileName med query som query-sträng,
//     * lägger till context framför fileName.
//     * 
//	 * @param fileName dit vi ska göra forward
//	 * @param query query-sträng
//	 */
//	public void forwardAppendContext(String fileName, String query) {
//		HttpServletRequest request =
//			(HttpServletRequest)getPageContext().getRequest();
//
//		forward(request.getContextPath() + fileName, query);
//	}
//
//
//
//
//	/**
//	 * Gör en forward med requesten anpassad för eforms.
//	 *
//     * @param fileName dit vi ska göra forward
//     * @param query query-sträng
//     * @return SKIP_PAGE eller 0 för att fortsätta
//	 */
//	public int forward(String fileName, String query) {
//		log.debug("forward to " + fileName);
//
//		if (query == null) {
//			query = "resetFormData=false";
//		} else if (query.indexOf("resetFormData") < 0) {
//			query += "&resetFormData=false";
//		}
//
//        int ret = 0;
//        
//		//HttpSession session = getPageContext().getSession();
//		//AbstractSession genSess = (AbstractSession)session.getAttribute(AbstractSession.SESSION_KEY);
//		
//
//		try {
//			HttpServletResponse response =
//				(HttpServletResponse)getPageContext().getResponse();
//
//			response.sendRedirect(response.encodeRedirectURL(fileName + "?"
//			                                                 + query));
//			response.flushBuffer();
//
//            ret = Tag.SKIP_PAGE;
//		} catch (Exception e) {
//			log.error("Exception: Gick inte att göra forward till " + fileName + 
//			"Query = " + query + "Felet som kastades = " + e.getLocalizedMessage());
//		}
//        
//        return ret;
//	}
//
//
//
//
//	/**
//	 * Laddar om sidan page men med GET och eforms-trädet på query-strängen.
//	 * Syftet är att kunna skapa ett eforms-träd och sedan använda parametrar
//	 * ur trädet till <efroms:import> som bara körs på GET och med parametrar
//	 * från querysträngen
//	 *
//	 * @param page sida att ladda om
//	 * @param path del av trädet att skicka på querysträngen
//     * @throws Exception om forward misslyckas
//	 */
//	protected void reloadPostAsGet(String page, String path)
//	                        throws Exception {
//		HttpServletRequest request =
//			(HttpServletRequest)getPageContext().getRequest();
//		HttpServletResponse response =
//			(HttpServletResponse)getPageContext().getResponse();
//		HttpSession session = getPageContext().getSession();
//
//		try {
//			PostAsGetRequestWrapper r =
//				new PostAsGetRequestWrapper(request, session, path);
//
//			javax.servlet.RequestDispatcher dispatcher =
//				request.getRequestDispatcher(page);
//
//			dispatcher.forward(r, response);
//		} catch (Exception e) {
//			log.error("Misslyckades vid omladdning av sida med GET", e);
//			throw new Exception(
//			                    "Misslyckades vid omladdning av sida med GET",
//			                    e);
//		}
//	}
//}
//
//
//
//
///**
// * Private class för att anpassa/wrappa requesten till eforms.
// *
// * @author Joakim Olsson
// * @since 20050103
// * @version 0.1 skapad
// */
//class GetWithTraceRequest extends HttpServletRequestWrapper {
//	private String qString;
//	private String txId;
//
//	/**
//     * Constructor för requesten
//	 * @param request original-request
//	 * @param txId transaktions id
//	 * @param event trace-händelse
//	 */
//	GetWithTraceRequest(HttpServletRequest request, String txId, String event) {
//		super(request);
//
//		qString = request.getQueryString();
//
//		if (qString == null) {
//			qString = "trace.event=" + event + "&trace.transactionid=" + txId;
//		}
//	}
//
//
//	/**
//	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
//	 */
//	public String getQueryString() {
//		return qString;
//	}
//}
//
//
//
//
///**
// * Privat klass för att anpassa/wrappa requesten till eforms.
// *
// * @author Joakim Olsson
// * @since 20050103
// * @version 0.1 skapad
// *
// */
//class PostAsGetRequestWrapper extends HttpServletRequestWrapper {
//	private HttpSession session;
//	private String path;
//	private StringBuffer qString;
//	protected static final Log log = Log.getInstance(PostAsGetRequestWrapper.class);
//
//	/**
//     * Konstruera en request 
//	 * @param req original-request
//	 * @param session nuvarande session
//	 * @param path path i eforms-trädet
//	 */
//	public PostAsGetRequestWrapper(HttpServletRequest req,
//	                               HttpSession session,
//	                               String path) {
//		super(req);
//		this.session = session;
//		this.path = path;
//
//		createQueryString();
//	}
//
//	/**
//	 * Skapar en query-strän från noden "path" i trädet och nedåt
//     * Dessutom noden "trace" om den finns
//	 */
//	public void createQueryString() {
//		ParameterTree tree = FormData.retrieve(session).getTree();
//        qString = new StringBuffer("");
//        String name = "";
//        
//        Node node = tree.getNode("trace", false, null);;
//        if (node != null) {
//            name = addNode(qString, node, "");
//            addChildren(qString, node, name);
//        }
//		
//        node = tree.getNode(path, false, null);
//		if (node != null) {
//			name = addNode(qString, node, "");
//			addChildren(qString, node, name);
//		}
//	}
//
//
//
//
//	/**
//	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
//	 */
//	public String getQueryString() {
//		return qString.toString();
//	}
//
//
//
//
//	/**
//	 * @param query lägger till all child-noder till query-strängen
//	 * @param node den nod vi börjar vid
//	 * @param startPath den path vi börjar vid
//	 */
//	private void addChildren(StringBuffer query, Node node, String startPath) {
//		int nChildren = node.numberOfChildren();
//		String name = "";
//
//		for (int i = 0; i < nChildren; ++i) {
//			Node child = node.getChildAt(i);
//
//			if (child != null) {
//				name = addNode(query, child, startPath);
//				addChildren(query, child, startPath);
//			}
//		}
//	}
//
//
//
//
//    /**
//     * @param query lägger till noden till query-strängen
//     * @param node den nod vi börjar vid
//     * @param startPath den path vi börjar vid
//     * 
//     * @return namn på noden som lagts till
//     */
//	private String addNode(StringBuffer query, Node node, String startPath) {
//		String name;
//
//		if (startPath.length() == 0) {
//			name = node.getName();
//		} else {
//			name = startPath + "." + node.getName();
//		}
//
//		String value = node.getValue();
//		String urlEncodedValue = null;
//		
//		try {
//			urlEncodedValue = URLEncoder.encode(value, "ISO-8859-1");
//		} catch (UnsupportedEncodingException e) {
//			log.error("Misslyckades med att URLenkoda följande sträng :" + value, e);
//			urlEncodedValue = value;
//		}
//
//		if ((urlEncodedValue != null) && (urlEncodedValue.length() > 0)) {
//			if (query.length() > 0) {
//				query.append('&');
//			}
//
//			query.append(name + "=" + urlEncodedValue);
//		}
//
//		return name;
//	}
//
//
//	
//
//	/**
//     * Alltid get
//	 * @see javax.servlet.http.HttpServletRequest#getMethod()
//	 */
//	public String getMethod() {
//		return "GET";
//	}
//}