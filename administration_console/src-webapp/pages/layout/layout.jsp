<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
    <head>
        <title>Console d'administration de JavaMonitoring</title>
		<link href="css/global.css" rel="stylesheet" type="text/css" media="screen"/>

    	<!-- Optionnal Style sheet -->
		<tiles:importAttribute name="css1" ignore="true"/>
		<logic:present name="css1">
			<link href="<bean:write name="css1"/>" rel="stylesheet" type="text/css"/>
		</logic:present>
		<tiles:importAttribute name="css2" ignore="true"/>
		<logic:present name="css2">
			<link href="<bean:write name="css2"/>" rel="stylesheet" type="text/css"/>
		</logic:present>
		<tiles:importAttribute name="css3" ignore="true"/>
		<logic:present name="css3">
			<link href="<bean:write name="css3"/>" rel="stylesheet" type="text/css"/>
		</logic:present>
		
		<!-- Optionnal JavaScript file -->
		<tiles:importAttribute name="js1" ignore="true"/>
		<logic:present name="js1">
		    <script type="text/javascript" src="<bean:write name="js1"/>"></script>
		</logic:present>
		<tiles:importAttribute name="js2" ignore="true"/>
		<logic:present name="js2">
		    <script type="text/javascript" src="<bean:write name="js2"/>"></script>
		</logic:present>
		<tiles:importAttribute name="js3" ignore="true"/>
		<logic:present name="js3">
		    <script type="text/javascript" src="<bean:write name="js3"/>"></script>
		</logic:present>
	 
    </head>
    <body>
        <table class="bidon"  border="2" width="100%" height="100%">
            <tr height="40" bgcolor="#FFFF55">
                <td colSpan="2">
                    <tiles:insert attribute="head"/>
                </td>
            </tr>
            <tr>
                <td width="130" valign="top">
                    <tiles:insert attribute="menu"/>
                </td>
                <td  valign="top">
                    <tiles:insert attribute="body"/>                
                </td>
            </tr>
            <tr height="20" bgcolor="#FFFF55">
                <td align="center" colSpan="2">
                    <tiles:insert attribute="footer"/>
                </td>
            </tr>
        </table>
    </body>
</html>
