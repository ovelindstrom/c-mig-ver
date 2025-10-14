<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


    <f:loadBundle basename="se.csn.notmotor.admin.resources.ApplicationResources" var="msgs"/>
    <h:form id="sokForm" >
    	<div>
    	<table style="border:0;	padding:0.3em;padding-left:0; width:auto;">
    	<tr>
    	<td style="border:0; padding:0.3em;padding-left:0; width:auto;">
	    	<table>
		    	<thead>
		    		<td>Tidsstämpel</td>
		    		<td>Fr.o.m</td>
		    		<td>T.o.m</td>
		    	</thead>
		    	<tr>
		    		<td>Skapat</td>
		    		<td><h:inputText value="#{sokBean.fromSkapat}"/></td>
		    		<td><h:inputText value="#{sokBean.tomSkapat}" /></td>
		    	</tr>
		    	<tr>
		    		<td>Skickat</td>
		    		<td><h:inputText value="#{sokBean.fromSkickat}"/></td>
		    		<td><h:inputText value="#{sokBean.tomSkickat}" /></td>
		    	</tr>
		    	<tr>
		    		<td>Skicka tidigast</td>
		    		<td><h:inputText value="#{sokBean.fromSkickaTidigast}"/></td>
		    		<td><h:inputText value="#{sokBean.tomSkickaTidigast}" /></td>
		    	</tr>
			</table>
			<br/>
	    	<table style="border:0;	padding:0.3em;padding-left:0; width:auto;">
		    	<tr>
		    		<td>Avsändarnamn</td>
		    		<td><h:inputText value="#{sokBean.avsandarnamn}"/></td>
		    	</tr>
		    	<tr>
		    		<td>Avsändaradress</td>
		    		<td><h:inputText value="#{sokBean.avsandaradress}"/></td>
		    	</tr>
		    	<tr>
		    		<td>Mottagarnamn</td>
		    		<td><h:inputText value="#{sokBean.mottagarnamn}"/></td>
		    	</tr>
		    	<tr>
		    		<td>Mottagaradress</td>
		    		<td><h:inputText value="#{sokBean.mottagaradress}"/></td>
		    	</tr>
		    	<tr>
		    		<td>Kanal</td>
		    		<td><h:inputText value="#{sokBean.kanal}"/></td>
		    	</tr>
		    	<tr>
		    		<td>Applikation</td>
		    		<td><h:inputText value="#{sokBean.applikation}"/></td>
		    	</tr>
		    	<tr>
		    		<td>Kategori</td>
		    		<td><h:inputText value="#{sokBean.kategori}"/></td>
		    	</tr>
		    	<tr>
		    		<td>CSN Nummer</td>
		    		<td><h:inputText value="#{sokBean.csnnummer}"></h:inputText></td>
		    	</tr>
			</table>
			</td>
			<td style="border:0;padding:0.3em;padding-left:20px; width:auto;">
			<table style="width:450px;">
				<div style="font-weight:bold;">Statuskoder</div>
				<tr>
					<th>Kod</th>
					<th>Kortbeskrivning</th>
					<th>Beskrivning</th>
				</tr>
				<tr>
					<td>1</td>
					<td>Mottaget</td>
					<td>Notmotorn har tagit emot meddelandet och lagt det i databasen</td>
				</tr>
				<tr>
					<td>2</td>
					<td>Skickat till mailserver</td>
					<td>Meddelandet skickat till mailserver el dyl för vidare befordran</td>
				</tr>
				<tr>
					<td>4</td>
					<td>Besvarat</td>
					<td>Meddelandet kom i retur; detta ska normalt inte hända. Kan bero på att mottagarens mailbox är full, att meddelandet fastnat i spamfilter, att användaren inte finns, autoreply </td>
				</tr>
				<tr>
					<td>8</td>
					<td>Borttaget</td>
					<td>Meddelandet togs bort av användande applikation</td>
				</tr>
				<tr>
					<td>16</td>
					<td>Meddelandefel</td>
					<td>Kunde inte sända meddelandet till mailservern pga fel i meddelandet</td>
				</tr>
				<tr>
					<td>32</td>
					<td>Tekniskt fel</td>
					<td>Kunde inte sända meddelandet till mailservern pga bruten koppling</td>
				</tr>
				<tr>
					<td>-1</td>
					<td>Under sändning</td>
					<td>Meddelandet håller på att skickas</td>
				</tr>
			</table>
			<br>
			<h:selectManyCheckbox style="width:450px;" value="#{sokBean.valdaStatusar}">
	    		<f:selectItems value="#{sokBean.allaStatusar}"/>
	    	</h:selectManyCheckbox>			
			</td>
			</tr>
			</table>	    	

			<table style="border:0;	padding:0.3em;padding-left:0; width:auto;">
				<tr>
					<td style="border:0;padding:0.3em;padding-left:0; width:auto;"><h:commandButton actionListener="#{sokBean.sok}" value="#{msgs.sok}"></h:commandButton></td>
		    		<td style="border:0;padding:0.3em;padding-left:20px; width:auto;">Sortera på</td>
		    		<td style="border:0;padding:0.3em;padding-left:0; width:auto;">
		    			<h:selectOneMenu value="#{sokBean.orderBy}">
						    <f:selectItem itemValue="ID" itemLabel="Id" />
						    <f:selectItem itemValue="SKAPADTIDPUNKT" itemLabel="Skapat" />
						    <f:selectItem itemValue="SANTTIDPUNKT" itemLabel="Skickat" />
						</h:selectOneMenu>
		    		</td>
		    		<td style="border:0;padding:0.3em;padding-left:10; width:auto;">
						<h:selectOneMenu value="#{sokBean.orderByAscDesc}">
							<f:selectItem itemValue="DESC" itemLabel="Fallande"/>
							<f:selectItem itemValue="ASC" itemLabel="Stigande" />
						</h:selectOneMenu>		    			
		    		</td>
				</tr>
			</table>	    	
			
			<br/>    	
	    	<h:outputText value="Hittat #{sokBean.antalMeddelanden} meddelanden." />
	    	<br/>
	    	<h:outputText value="Visar maximalt 1000 meddelanden." />
	    	<c:set var="visaOmsandning" value="${sokBean.finnsMeddelandenMojligaForOmsandning}" />
	    	
	    	
			
	    	<h:dataTable id="meddelandeTable" value="#{sokBean.meddelanden}" var="medd" headerClass="header" style="width:1044px;">
	    		<c:if test="${sokBean.finnsMeddelanden and visaOmsandning}">
		    		<h:column>
		    			<f:facet name="header">
		    				<h:outputLink id="skickaOmHeaderLank" value="#">Skicka om</h:outputLink>
		    			</f:facet>
	    				<h:selectBooleanCheckbox disabled="#{!medd.omsandningMojlig}" value="#{medd.markeradForOmsandning}" />
		    		</h:column>
		    	</c:if>
	    	

	    		<h:column>
	    			<f:facet name="header">
	    				<h:outputText value="">
		    				<h:commandLink  actionListener="#{sokBean.sorteraTabell('Id')}" styleClass="taBortLankForSokresultatTabel" >
		    					<span class="taBortLankForSokresultatTabel">ID</span>
								<h:graphicImage styleClass="#{sokBean.sorteringsordningForId eq 'ascendingOrder' ? 'ascendingOrder' : 'ascendingOrder doljImg'}" value="img/spin-up-black.gif"></h:graphicImage>
								<h:graphicImage styleClass="#{sokBean.sorteringsordningForId eq 'descendingOrder' ? 'descendingOrder' : 'descendingOrder doljImg'}" value="img/spin-down-black.gif"></h:graphicImage>
							</h:commandLink>
        				</h:outputText>
	    			</f:facet>
	    			
	    			<h:outputLink value="visameddelande.jsf">
	    				<h:outputText value="#{medd.id}"/>
		    			<f:param name="id" value="#{medd.id}"/>
		    		</h:outputLink>
	    		</h:column>  		
	    		<h:column><f:facet name="header"><h:outputText value="Status"/></f:facet>
		    		<span class="tooltip"><h:outputText value="#{medd.status}" id="myTest"/><span class="tooltiptext"><p><h:outputText value="#{medd.status} = #{sokBean.getStatusBeskrivning(medd.status)}"/></p></span></span>
	    		</h:column> 	    		
	    		<h:column>
		    		<f:facet name="header">
			    		<h:commandLink actionListener="#{sokBean.sorteraTabell('Skapat')}" styleClass="taBortLankForSokresultatTabel">
		    				<span class="taBortLankForSokresultatTabel">Skapat</span>
							<h:graphicImage styleClass="#{sokBean.sorteringsordningForSkapat eq 'ascendingOrder' ? 'ascendingOrder' : 'ascendingOrder doljImg'}" value="img/spin-up-black.gif"></h:graphicImage>
							<h:graphicImage styleClass="#{sokBean.sorteringsordningForSkapat eq 'descendingOrder' ? 'descendingOrder' : 'descendingOrder doljImg'}" value="img/spin-down-black.gif"></h:graphicImage>
						</h:commandLink>
			    	</f:facet>
		    		<h:outputText value="#{medd.skapat}"/>
		    	</h:column>
		    		
	    		<h:column><f:facet name="header"><h:outputText value="Kanal"/></f:facet>
	    			<h:outputText value="#{medd.kanal}"/>
	    		</h:column>
	    		
	    		<h:column>
	    			<f:facet name="header">
			    		<h:commandLink actionListener="#{sokBean.sorteraTabell('Skickat')}" styleClass="taBortLankForSokresultatTabel" style="white-space: nowrap;">
		    				<span class="taBortLankForSokresultatTabel">Skickat</span>
							<h:graphicImage styleClass="#{sokBean.sorteringsordningForSkickat eq 'ascendingOrder' ? 'ascendingOrder' : 'ascendingOrder doljImg'}" value="img/spin-up-black.gif"></h:graphicImage>
							<h:graphicImage styleClass="#{sokBean.sorteringsordningForSkickat eq 'descendingOrder' ? 'descendingOrder' : 'descendingOrder doljImg'}" value="img/spin-down-black.gif"></h:graphicImage>
						</h:commandLink>
        			</f:facet>
	    			<h:outputText  value="#{medd.skickat}"/>
	    		</h:column>
	    		
	    		<h:column><f:facet name="header"><h:outputText value="Från"/></f:facet>
	    			<h:outputText value="#{medd.applikation}"/>
	    		</h:column>
	    		
	    		<h:column><f:facet name="header"><h:outputText value="Till"/></f:facet>
	    			<h:outputText value="#{medd.mottagare}"/>
	    		</h:column>
	    		
	    		<h:column><f:facet name="header"><h:outputText value="Rubrik"/></f:facet>
	    			<h:outputText style="width: 100px" value="#{medd.rubrik}"/>
	    		</h:column>
	    		
	    	</h:dataTable>    	
	    	<c:if test="${sokBean.finnsMeddelanden and visaOmsandning}">
	    		<h:commandButton actionListener="#{sokBean.skickaOm}" value="Skicka om markerade meddelanden"/>
	    	</c:if>
		</div>    
    </h:form>
    
    
    <script type="text/javascript">
    	$(document).ready(function() {
    		var skickaOmHref = $('[id$=skickaOmHeaderLank]');
    		skickaOmHref.attr("href", "javascript:void(0)");
    		$('[id$=skickaOmHeaderLank]').click(function() {
    			var checked = false;
    			var element = $('[id$=meddelandeTable] input:checkbox');
    			//console.log(element);
    			$('[id$=meddelandeTable] input:checkbox:enabled').each(function(index, element) {
    				if(this.checked) {
    					checked = true;
    				}    				
    			});
    			var enabledCheckboxes = $('[id$=meddelandeTable]').find('input:checkbox:enabled');
    			if(checked) {
    				enabledCheckboxes.prop("checked", false);
    			} else {
    				enabledCheckboxes.prop("checked", true);
    			}
    			
    		});
    	});
	</script>