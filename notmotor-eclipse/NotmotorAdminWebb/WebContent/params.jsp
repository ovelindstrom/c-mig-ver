<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<HTML>
    <f:view>
		<head> <title>Inställningar</title>
				<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/webbsvar.css" media="all" />    
		</head>
	    <body>
	    	<f:subview id="header">	
    			<%@ include file="/header.jsp" %>
    		</f:subview>
			<div id="sidansBredd">	  
			    <!-- Yttre behållare -->
			    <div id="yttre-skal">
			      <!-- Behållare för huvudinnehåll och sidoruta -->
			      <div id="innehallsskal">
			        <!-- Sidans huvudinnehåll -->
			        <div id="innehall">
					  <div id="spalt1">
					  	<div class="formatering">
					    	<f:subview id="content1">	
				    			<%@ include file="/params1.jsp" %>
					    	</f:subview>
						</div>
					  </div>
					</div>
				  </div>		    	
			  	  <f:subview id="menu">	
  	      			  <%@ include file="/menu.jsp" %>
				  </f:subview>
			  </div>
			</div>		    
    	</body>    </f:view>
</HTML>  

