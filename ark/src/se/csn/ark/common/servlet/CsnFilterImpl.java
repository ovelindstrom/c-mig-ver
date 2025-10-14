package se.csn.ark.common.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import se.csn.ark.common.CsnArkBaseObjectImpl;

/**
 * Basklass för Filter.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20050324
 * @version 1 skapad
 *
 */
public class CsnFilterImpl extends CsnArkBaseObjectImpl implements CsnFilter {

	private FilterConfig filterConfig;

	 /** 
	  *  Implementation av javax.servlet.Filter
	  * 
	  * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	  */
	public void init(FilterConfig config) throws ServletException {
	
		this.filterConfig = config;
	}

	/** 
	 *  Implementation av javax.servlet.Filter
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {

	}

	/** 
	 *  Implementation av javax.servlet.Filter
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, 
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(
		ServletRequest servletRequest,
		ServletResponse servletResponse,
		FilterChain filterChain)
		throws IOException, ServletException {

	}

	/**
	 * @return filtrets konfiguration
	 */
	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	/**
	 * @param config filtrets konfiguration
	 */
	public void setFilterConfig(FilterConfig config) {
		filterConfig = config;
	}

}
