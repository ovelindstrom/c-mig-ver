    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <f:loadBundle basename="se.csn.notmotor.admin.resources.ApplicationResources" var="msgs"/>
    <h:form id="statusForm" >
    
    	<div>
    	<p>Servertid: <h:outputText value="#{statusBean.servertid}"/></p>
    	<h:dataTable id="statusTable" value="#{statusBean.statusrader}" var="status" headerClass="header" rowClasses="odd,even">
    		<h:column id="c1"><f:facet name="header"><h:outputText value="Rensa"/></f:facet>
    			<h:selectBooleanCheckbox value="#{status.rensa}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Nr"/></f:facet>
    			<h:outputText value="#{status.nr}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Status"/></f:facet>
    			<h:outputText value="#{status.statustext}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Server"/></f:facet>
    			<h:outputText value="#{status.server}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Typ"/></f:facet>
    			<h:outputText value="#{status.typ}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Startad"/></f:facet>
    			<h:outputText style="width: 70px" value="#{status.starttid}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Stoppad"/></f:facet>
    			<h:outputText style="width: 70px" value="#{status.stopptid}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Tidsstämpel"/></f:facet>
    			<h:outputText value="#{status.watchdog}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Stoppa"/></f:facet>
    			<h:commandLink rendered="#{!status.stoppad}" action="stoppa" actionListener="#{statusBean.stoppaProcess}">
    				<h:graphicImage value="/bilder/Stoppa.gif"/>
    			</h:commandLink>
				<h:outputText rendered="#{status.stoppad}" escape="false" value="&nbsp;"></h:outputText>    			
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Pausa"/></f:facet>
    			<h:commandLink rendered="#{!status.pausad && !status.stoppad}" action="pausa" actionListener="#{statusBean.pausaProcess}">
    				<h:graphicImage value="/bilder/Pausa.gif"/>
    			</h:commandLink>
    			<h:commandLink rendered="#{status.pausad}" action="fortsatt" actionListener="#{statusBean.fortsattProcess}">
    				<h:graphicImage value="/bilder/Starta.gif"/>
    			</h:commandLink>
				<h:outputText rendered="#{status.stoppad}" escape="false" value="&nbsp;"></h:outputText>
			</h:column>
    	</h:dataTable>
    	<h:commandButton action="status_delete" actionListener="#{statusBean.taBortStatusar}" value="#{msgs.tabortstatusrad}"/>
    	<h:commandButton action="status_deletestoppad" actionListener="#{statusBean.taBortStoppadeStatusar}" value="#{msgs.tabortstoppadstatusrad}"/>
		</div>
		<p style="color: red; width: 500px">
			<b><h:outputText escape="false" value="#{statusBean.watchdogVarning}"/></b>
		</p>
    	<h:commandButton action="status_uppdatera" actionListener="#{statusBean.uppdatera}" value="#{msgs.uppdatera}"/>
		<div>
    	<h:dataTable id="serverTable" value="#{statusBean.serverrader}" var="server" headerClass="header" rowClasses="odd,even">
    		<h:column><f:facet name="header"><h:outputText value="Ta bort"/></f:facet>
    			<h:selectBooleanCheckbox value="#{server.delete}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Server"/></f:facet>
    			<h:outputText value="#{server.id}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Serveradress"/></f:facet>
    			<h:outputText value="#{server.adress}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Prestanda"/></f:facet>
    			<h:outputText value="#{server.prestanda}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Antal processer"/></f:facet>
    			<h:outputText value="#{server.processer}"/>
    		</h:column>
    		<h:column><f:facet name="header"><h:outputText value="Starta ny"/></f:facet>
    			<h:commandButton  action="starta" actionListener="#{statusBean.startaProcess}" value="#{msgs.startany}"/>
    		</h:column>
    	</h:dataTable>
    	<h:commandButton id="server_delete" action="server_delete" actionListener="#{statusBean.taBortServrar}" value="#{msgs.tabortserverrad}"/>
		</div>
		<br/>
		<b><h:outputText value="Kanaler"/></b>
		<h:dataTable id="kanalerTable" value="#{statusBean.kanaler}" var="kanal" headerClass="header" rowClasses="odd,even">
    		<h:column>
    			<f:facet name="header"><h:outputText value="Kanal"/></f:facet>
    			<h:outputText value="#{kanal.namn}"/>
    		</h:column>
    		<h:column>
    			<f:facet name="header"><h:outputText value="Status"/></f:facet>
    			<h:outputText value="#{kanal.status}"/>
    		</h:column>
    		<h:column>
    			<f:facet name="header"><h:outputText value="Öppningstid"/></f:facet>
    			<h:outputText value="#{kanal.oppningstidString}"/>
    		</h:column>
    		<h:column>
    			<f:facet name="header"><h:outputText value="Stängningstid"/></f:facet>
    			<h:outputText value="#{kanal.stangningstidString}"/>
    		</h:column>
    		<h:column>
    			<f:facet name="header"><h:outputText style="white-space:nowrap;" value="Sovtid kvar (s)"/></f:facet>
    			<h:outputText value="#{kanal.sovtidKvar}"/>
    		</h:column>
    	</h:dataTable>

    </h:form>
