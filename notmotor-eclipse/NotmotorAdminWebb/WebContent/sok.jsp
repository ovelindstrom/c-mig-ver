<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<f:view>
   	<html lang="en">
		<head> 
			<title>Sök meddelanden</title>
			<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/webbsvar.css" media="all" />
			<script type="text/javascript" src="<c:out value="${pageContext.request.contextPath}" />/javascript/jquery/jquery-1.11.1.min.js"></script>    
		</head>
    	<body>
	    	<f:subview id="header">	
	   			<div id="sidhuvudForSok">
					<div class="formatering"> 
						<h1 class="sidhuvudForSokH1Rubrik">Notifieringsmotor Admin</h1>
						<div id="logocsn"></div>
			        </div>
			    </div>
	   		</f:subview>
			<div id="sidansBreddForSok">	  
			    <!-- Yttre behållare -->
			    <div id="yttre-skal">
			      <!-- Behållare för huvudinnehåll och sidoruta -->
			      <div id="innehallsskal">
			        <!-- Sidans huvudinnehåll -->
			        <div id="innehall">
					  <div id="spalt1">
					  	<div class="formatering">
					    	<f:subview id="content1">	
				    			<%@ include file="/sok1.jsp" %>
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
	   	</body>
	</html>
</f:view>

