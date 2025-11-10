<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <f:loadBundle basename="se.csn.notmotor.admin.resources.ApplicationResources" var="msgs"/>
    <h:form id="paramsForm" >

    	<div>
    	<b><h:outputText value="#{msgs.params_rubrik}" /></b>
    	<h:dataTable id="paramTable" value="#{paramBean.parametrar}" var="p" headerClass="header" rowClasses="odd,even">
    		<h:column><f:facet name="header"><h:outputText value="Inställning"/></f:facet>
    			<h:outputText value="#{p.namn}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Värde"/></f:facet>
    			<h:inputText value="#{p.varde}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Beskrivning"/></f:facet>
    			<h:outputText value="#{p.beskrivning}"/>
    		</h:column>
    	</h:dataTable>
    	<h:commandButton action="uppdatera" actionListener="#{paramBean.uppdatera}" value="#{msgs.uppdatera}"/>
    	</div>
    	<br/>
    	<div>
    	<b><h:outputText value="#{msgs.params_kanaler_rubrik}" /></b>
    	<h:dataTable id="paramKanalerTable" value="#{paramBean.parametrarKanaler}" var="pk" headerClass="header" rowClasses="odd,even">
    		<h:column><f:facet name="header"><h:outputText value="Inställning"/></f:facet>
    			<h:outputText value="#{pk.namn}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Värde"/></f:facet>
    			<h:inputText value="#{pk.varde}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Beskrivning"/></f:facet>
    			<h:outputText value="#{pk.beskrivning}"/>
    		</h:column>
    	</h:dataTable>
    	<h:commandButton action="uppdatera" actionListener="#{paramBean.uppdateraKanaler}" value="#{msgs.params_kanaler_uppdatera}"/>
		</div>

    </h:form>
