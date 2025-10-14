<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
 <html>
  <style>
  	body { font: 12pt georgia; color: #000; }
	 h1 {color:#00F; font: 24pt georgia; margin: 60px 0px 5px 0px; }
	 h2 {color:#00A; font: 20pt georgia; margin-bottom: 2px; }
	 h3 {color:#006; font: 16pt georgia; margin-bottom: 5px; }
	 p {margin: 5px;}
	table {
		border-width: 1px;
		border-spacing: 0px;
		border-style: solid;
		border-color: black;
		border-collapse: collapse;
	}
	 th {border: 1px solid black; padding: 2px; }
	 td {border: 1px solid black; padding: 2px; }
  	.label {font-weight: bold;}
  	.comment {color: #0D0;}
  	.miljo {font-size: 18pt; font-family: georgia;}
  </style>

  <body>
  	<xsl:for-each select="config/project">
	  	<h1>Projekt:<xsl:value-of select="@name"/></h1>
	  	<h1>Miljö:<xsl:value-of select="@environment"/></h1>
	</xsl:for-each>
	
	<hr/>  
	
    <xsl:for-each select="config/application">
		<h1><xsl:value-of select="@name"/></h1>
		<p><span class="label miljo">Miljö: </span><span class="value miljo"><xsl:value-of select="@environment"/></span></p>

		<ul>
		<xsl:for-each select="task">
			<li><xsl:copy-of select="."/></li>
		</xsl:for-each>
		</ul>

		<h2>Propertyfiler</h2>		
		<xsl:for-each select="propertyfile">
			<h3><span class="label">Fil: </span><span class="value"><xsl:value-of select="@name"/></span></h3>
			<xsl:for-each select="property">
				<xsl:if test="@description != ''">
				<span class="comment">#<xsl:value-of select="@description"/></span><br/>				
				</xsl:if>
				<xsl:value-of select="@name"/>=<xsl:value-of select="@value"/><br/>
			</xsl:for-each>
		</xsl:for-each>

		<xsl:if test="sharedlib" >
			<h2>Shared Libraries</h2>
			<xsl:for-each select="sharedlib">
				<xsl:for-each select="library">
					<xsl:if test="@description != ''">
					<span class="comment">#<xsl:value-of select="@description"/></span><br/>				
					</xsl:if>
					<xsl:value-of select="@name"/><br/>
				</xsl:for-each>
			</xsl:for-each>
		</xsl:if>

		<xsl:if test="logging">
			<h2>Logginställningar</h2>
			<ul>
			<xsl:for-each select="logging/task">
				<li><xsl:copy-of select="."/></li>
			</xsl:for-each>
			</ul>
		</xsl:if>

		<xsl:if test="database">
			<h2>Databas</h2>
			<ul>
			<xsl:for-each select="database/task">
				<li><xsl:copy-of select="."/></li>
			</xsl:for-each>
			</ul>

			<xsl:if test="database/datasources">
				<h3>Datasources</h3>
				<table>
					<thead>
						<th>Namn</th>
						<th>JNDI-namn</th>
						<th>Host</th>
						<th>Port</th>
						<th>Databas</th>
					</thead>
					<xsl:for-each select="database/datasources/datasource">
						<tr>
							<td><xsl:value-of select="@name"/></td>
							<td><xsl:value-of select="@jndiname"/></td>
							<td><xsl:value-of select="@host"/></td>
							<td><xsl:value-of select="@port"/></td>
							<td><xsl:value-of select="@db"/></td>
						</tr>
					</xsl:for-each>
				</table>
			</xsl:if>
			
			<xsl:if test="database/tables">
				<h3>Tabeller</h3>
				<table>
					<thead>
						<th>Tabellnamn</th>
						<th>Schema</th>
					</thead>
					<xsl:for-each select="database/tables/table">
						<tr>
							<td><xsl:value-of select="@name"/></td>
							<td><xsl:value-of select="@schema"/></td>
						</tr>
					</xsl:for-each>
				</table>
			</xsl:if>
			
		</xsl:if>
		
		<xsl:if test="userregistry">
			<h2>User registry</h2>
			<h3>Anslutning</h3>
			<table>
				<thead>
					<th>Server</th>
					<th>Användare</th>
					<th>Lösen</th>
				</thead>
				<tr>
					<td><xsl:value-of select="userregistry/connection/@host"/></td>
					<td><xsl:value-of select="userregistry/connection/@user"/></td>
					<td><xsl:value-of select="userregistry/connection/@password"/></td>
				</tr>
			</table>
	
			<h3>Roller</h3>
			<table>
				<thead>
					<th>Roll</th>
					<th>Användare</th>
				</thead>
				<xsl:for-each select="userregistry/role">
					<tr><td><xsl:value-of select="@name"/></td><td><xsl:value-of select="@user"/></td></tr>
				</xsl:for-each>
			</table>		

			<xsl:if test="userregistry/task">			
				<h3>Övrigt</h3>
				<ul>
				<xsl:for-each select="userregistry/task">
					<li><xsl:copy-of select="."/></li>
				</xsl:for-each>
				</ul>
			</xsl:if>
		</xsl:if>
		
    </xsl:for-each>
    
    <xsl:for-each select="config/ad">
   		<h1>AD</h1>
		<h3>Grupper</h3>
		<ul>
			<xsl:for-each select="groups//group">
			<xsl:choose>
				<xsl:when test="../@name = ''">
					<li><xsl:value-of select="@name"/></li>
				</xsl:when>
				<xsl:otherwise>
					<li><xsl:value-of select="@name"/>,<xsl:value-of select="../@name"/></li>
				</xsl:otherwise>
			</xsl:choose>
			</xsl:for-each>
		</ul>

		<h3>Användare</h3>
		<xsl:for-each select="users/user">
			<span class="label">User: </span><span class="value"><xsl:value-of select="@name"/></span>
			<span class="label"> Lösen: </span><span class="value"><xsl:value-of select="@password"/></span>
		</xsl:for-each>

		<h3>Övrigt</h3>
		<ul>
		<xsl:for-each select="task">
			<li><xsl:copy-of select="."/></li>
		</xsl:for-each>
		</ul>
	</xsl:for-each>		
    
  </body>
  </html>
</xsl:template>


</xsl:stylesheet>