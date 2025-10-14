<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <f:loadBundle basename="se.csn.notmotor.admin.resources.ApplicationResources" var="msgs"/>
	<div>
		<table>
			<tr><td><b>Processer</b></td></tr>
			<tr class="odd"><td>Antal levande processer</td><td><h:outputText value="#{statBean.levandeProcesser}"/></td></tr>
			<tr class="even"><td>Genomsnittlig livstid</td><td><h:outputText value="#{statBean.genomsnittligLivstid}"/></td></tr>
			<tr class="odd"><td>Längsta livstid</td><td><h:outputText value="#{statBean.langstaLivstid}"/></td></tr>
			<tr class="even"><td>Totalt antal processer</td><td><h:outputText value="#{statBean.antalProcesser}"/></td></tr>
		</table>
		<br/>
		<fieldset>
			<legend>
				<h:outputLink styleClass="noUnderline" value="statistik.jsf?valdDag=#{statBean.foregaendeDag}" >&lt;&lt;Bakåt</h:outputLink>
				&nbsp;&nbsp;#{statBean.valdDag}&nbsp;&nbsp;
				<h:outputLink styleClass="noUnderline" value="statistik.jsf?valdDag=#{statBean.nastaDag}">Framåt&gt;&gt;</h:outputLink></legend>
			<table>
				<tr><td><b>Mottagna meddelanden</b></td></tr>
				<tr class="odd"><td>Senaste timmen</td><td><h:outputText value="#{statBean.mottagna.senasteTimmen}"/></td></tr>
				<tr class="even"><td>Sedan midnatt</td><td><h:outputText value="#{statBean.mottagna.sedanMidnatt}"/></td></tr>
				<tr class="odd"><td>De senaste 24 timmarna</td><td><h:outputText value="#{statBean.mottagna.senaste24}"/></td></tr>
				<tr class="odd"><td>#{statBean.valdDag}</td><td><h:outputText value="#{statBean.mottagna.angivenDag}"/></td></tr>
				<tr class="even"><td>Totalt</td><td><h:outputText value="#{statBean.mottagna.totalt}"/></td></tr>
			</table>
			<h:dataTable rendered="#{statBean.finnsKanalerMottagna}" value="#{statBean.kanalerMottagna}" var="k" headerClass="header" rowClasses="odd,even">
				<h:column>
					<f:facet name="header"><h:outputText value="Kanal"/></f:facet>
					<h:outputText value="#{k.kanal}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="Senaste timmen"/></f:facet>
					<h:outputText value="#{k.senasteTimmen}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="Sedan midnatt"/></f:facet>
					<h:outputText value="#{k.sedanMidnatt}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="Senaste 24 timmarna"/></f:facet>
					<h:outputText value="#{k.senaste24}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{statBean.valdDag}"/></f:facet>
					<h:outputText value="#{k.angivenDag}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="Totalt"/></f:facet>
					<h:outputText value="#{k.totalt}"/>
				</h:column>
			</h:dataTable>
			<br/>
			<table>
				<tr><td><b>Sända meddelanden</b></td></tr>
				<tr class="odd"><td>Senaste timmen</td><td><h:outputText value="#{statBean.sant.senasteTimmen}"/></td></tr>
				<tr class="even"><td>Sedan midnatt</td><td><h:outputText value="#{statBean.sant.sedanMidnatt}"/></td></tr>
				<tr class="odd"><td>De senaste 24 timmarna</td><td><h:outputText value="#{statBean.sant.senaste24}"/></td></tr>
				<tr class="odd"><td>#{statBean.valdDag}</td><td><h:outputText value="#{statBean.sant.angivenDag}"/></td></tr>
				<tr class="even"><td>Totalt</td><td><h:outputText value="#{statBean.sant.totalt}"/></td></tr>
			</table>
			<h:dataTable rendered="#{statBean.finnsKanalerSant}" value="#{statBean.kanalerSant}" var="k" headerClass="header" rowClasses="odd,even">
				<h:column>
					<f:facet name="header"><h:outputText value="Kanal"/></f:facet>
					<h:outputText value="#{k.kanal}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="Senaste timmen"/></f:facet>
					<h:outputText value="#{k.senasteTimmen}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="Sedan midnatt"/></f:facet>
					<h:outputText value="#{k.sedanMidnatt}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="Senaste 24 timmarna"/></f:facet>
					<h:outputText value="#{k.senaste24}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{statBean.valdDag}"/></f:facet>
					<h:outputText value="#{k.angivenDag}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="Totalt"/></f:facet>
					<h:outputText value="#{k.totalt}"/>
				</h:column>
			</h:dataTable>
			<br/>
			<table>
				<tr><td><b>Borttagna meddelanden</b></td></tr>
				<tr class="even"><td>Totalt</td><td><h:outputText value="#{statBean.borttagna.totalt}"/></td></tr>
			</table>
			<h:dataTable rendered="#{statBean.finnsKanalerBorttagna}" value="#{statBean.kanalerBorttagna}" var="k" headerClass="header" rowClasses="odd,even">
				<h:column>
					<f:facet name="header"><h:outputText value="Kanal"/></f:facet>
					<h:outputText value="#{k.kanal}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="Totalt"/></f:facet>
					<h:outputText value="#{k.totalt}"/>
				</h:column>
			</h:dataTable>
		</fieldset>
	</div>
	<br/>
	<div>
		<h:form id="statistikForm">
		<table>
			<tr><td>Startdatum</td><td><h:inputText value="#{statBean.startdatum}"/></td></tr>
			<tr><td>Slutdatum</td><td><h:inputText value="#{statBean.slutdatum}"/></td></tr>
		</table>
	    <h:commandButton action="sok" actionListener="#{statBean.sok}" value="#{msgs.sok}"/>
	    <h:commandButton action="uppdatera" actionListener="#{statBean.uppdatera}" value="#{msgs.uppdatera}"/>
	    </h:form>
    </div>
    <div>
		<h:dataTable id="statistikTable" value="#{statBean.datumrader}" var="rad" headerClass="header" rowClasses="odd,even">
	    	<h:column><f:facet name="header"><h:outputText value="Datum"/></f:facet>
	    		<h:outputText value="#{rad.datum}"/>
	    	</h:column>
	    	<h:column ><f:facet name="header"><h:outputText value="Instans"/></f:facet>
	    		<h:outputText value="#{rad.instans}"/>
	    	</h:column>
	    	<h:column><f:facet name="header"><h:outputText value="Status"/></f:facet>
	    		<h:outputText value="#{rad.status}"/>
	    	</h:column>
	    	<h:column><f:facet name="header"><h:outputText value="Antal"/></f:facet>
	    		<h:outputText value="#{rad.antal}"/>
	    	</h:column>
	    	
	    </h:dataTable>
	</div>
