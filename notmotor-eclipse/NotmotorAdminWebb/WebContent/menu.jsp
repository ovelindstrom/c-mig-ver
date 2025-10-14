<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:loadBundle basename="se.csn.notmotor.admin.resources.ApplicationResources" var="msgs"/>
<div id="undermeny">
<ul>
<li><a href="<%=request.getContextPath()%>/status.jsf">Visa status</a></li>
<li><a href="<%=request.getContextPath()%>/statistik.jsf">Hämta statistik</a></li>
<li><a href="<%=request.getContextPath()%>/oppnaStang.jsf">Öppna/Stäng Notifieringsmotorn</a></li>
<li><a href="<%=request.getContextPath()%>/stangningsschema.jsf">Schemalägg stängningar</a></li>
<li><a href="<%=request.getContextPath()%>/params.jsf">Ändra styrparametrar</a></li>
<li><a href="<%=request.getContextPath()%>/sok.jsf">Sök meddelanden</a></li>
<li><a href="<%=request.getContextPath()%>/visameddelande.jsf">Visa meddelande</a></li>
</ul>
</div>