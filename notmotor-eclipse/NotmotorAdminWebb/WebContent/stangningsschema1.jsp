<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <f:loadBundle basename="se.csn.notmotor.admin.resources.ApplicationResources" var="msgs"/>
    <h:form id="schemaForm" >

    	<div>
    	<h:dataTable id="schemaTable" value="#{schemaBean.schemarader}" var="schema" headerClass="header" rowClasses="odd,even">
    		<h:column id="c1"><f:facet name="header"><h:outputText value="Ta bort"/></f:facet>
    			<h:selectBooleanCheckbox value="#{schema.delete}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Stängt från"/></f:facet>
    			<h:outputText value="#{schema.from}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Stängt till"/></f:facet>
    			<h:outputText value="#{schema.tom}"/>
    		</h:column>
    	</h:dataTable>
    	<h:commandButton action="tabort" actionListener="#{schemaBean.taBortRader}" value="#{msgs.tabort}"/>
		</div>

		<div>
			<table class="datagrid">
				<tr class="rows">
					<td>Stängningstid</td>
					<td><h:inputText value="#{schemaBean.startdatum}"/></td>
			</tr>
			<tr class="rows">
				<td>Starttid</td>
				<td><h:inputText value="#{schemaBean.slutdatum}"/></td>
				</tr>
				
			</table>
			<h:commandButton action="laggtill" actionListener="#{schemaBean.laggTillRad}" value="#{msgs.laggtill}"/>			
		</div>
    </h:form>
