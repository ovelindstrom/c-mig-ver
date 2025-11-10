<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



    <f:loadBundle basename="se.csn.notmotor.admin.resources.ApplicationResources" var="msgs"/>
	<h:form id="msgForm" >  
	    	<div>
				<b><h:outputText value="#{msgs.meddelandeinfo}" /></b>
				<table>
					<tr><td><h:outputText value="#{msgs.id}" /></td><td><h:outputText value="#{msgBean.meddelande.id}" /></td></tr>
					<tr><td><h:outputText value="#{msgs.mottagetnotmotor}" /></td><td><h:outputText value="#{msgBean.meddelande.skapad}" /></td></tr>
					<tr><td><h:outputText value="#{msgs.skickatidigast}" /></td><td><h:outputText value="#{msgBean.meddelande.skickaTidigast}" /></td></tr>
					<tr><td><h:outputText value="#{msgs.skickatmailserver}" /></td><td><h:outputText value="#{msgBean.meddelande.skickat}" /></td></tr>
					<tr><td/><td/></tr>
					<tr><td><h:outputText value="#{msgs.avsandare}" /></td><td><h:outputText value="#{msgBean.avsandarstrang}" /></td></tr>
					<tr><td><h:outputText value="#{msgs.kanal}" /></td><td><h:outputText value="#{msgBean.meddelande.kanal}" /></td></tr>
					<tr><td><h:outputText value="#{msgs.applikation}" /></td><td><h:outputText value="#{msgBean.meddelande.avsandare.applikation}" /></td></tr>
					<tr><td><h:outputText value="#{msgs.kategori}" /></td><td><h:outputText value="#{msgBean.meddelande.avsandare.kategori}" /></td></tr>
					<tr><td><h:outputText value="#{msgs.mottagare}" /></td><td><h:outputText value="#{msgBean.mottagarstrang}" /></td></tr>
					<tr><td><h:outputText value="#{msgs.csnnummer}" /></td><td><h:outputText value="#{msgBean.csnnummer}" /></td></tr>
				</table>    	
				<b><h:outputText value="#{msgs.meddelandeinnehall}" /></b>
				<table>
					<tr><td><h:outputText value="#{msgs.rubrik}" /></td><td><h:outputText value="#{msgBean.meddelande.rubrik}" /></td></tr>
					<tr><td><h:outputText value="#{msgs.text}" /></td><td><h:outputText value="#{msgBean.meddelande.meddelandetext}" /></td></tr>
				</table>    	
	    	
		    	<div>
		    		<b><h:outputText value="#{msgs.visameddelande}" />:</b>
			    	<h:inputText value="#{msgBean.id}"/>
			    	<h:commandButton actionListener="#{msgBean.uppdatera}" value="#{msgs.visa}" />
		    	</div>
	
		    	<div>
			    	<h:commandButton rendered="#{msgBean.finnsForegaende}" actionListener="#{msgBean.visaForegaende}" value="#{msgs.foregaende}"/>
			    	<h:commandButton rendered="#{msgBean.finnsNasta}" actionListener="#{msgBean.visaNasta}" value="#{msgs.nasta}"/>
		    	</div>
		    	
		    	<h:dataTable rendered="#{msgBean.finnsHandelser}" id="handelseTable" value="#{msgBean.handelser}" var="handelse" headerClass="header" rowClasses="odd,even">
		    		<h:column><f:facet name="header"><h:outputText value="Ta bort"/></f:facet>
	    				<h:selectBooleanCheckbox value="#{handelse.delete}"/>
	    			</h:column>
		    		<h:column><f:facet name="header"><h:outputText value="Tidpunkt"/></f:facet>
		    			<h:outputText value="#{handelse.tidpunkt}"/>
		    		</h:column>
		    		<h:column><f:facet name="header"><h:outputText value="Typ"/></f:facet>
		    			<h:outputText value="#{handelse.typtext}"/>
		    		</h:column>
		    		<h:column><f:facet name="header"><h:outputText value="Kod"/></f:facet>
		    			<h:outputText value="#{handelse.kodtext}"/>
		    		</h:column>
		    		<h:column><f:facet name="header"><h:outputText value="Text"/></f:facet>
		    			<h:outputText value="#{handelse.text}"/>
		    		</h:column>
	    		</h:dataTable>
	    		
	    		<div>
	    			<h:commandButton rendered="#{msgBean.finnsHandelser}" id="handelse_delete" action="handelse_delete" actionListener="#{msgBean.taBortHandelse}" value="#{msgs.taborthandelse}"/>
					<h:commandButton rendered="#{msgBean.fel}" actionListener="#{msgBean.skickaOm}" value="#{msgs.skickaom}" />
					<br/><br/><h:commandButton rendered="#{msgBean.kunnaTaBortMeddelande}" actionListener="#{msgBean.taBortMeddelande}" value="#{msgs.taBortMeddelande}" />
				</div>
			</div>		
	</h:form>