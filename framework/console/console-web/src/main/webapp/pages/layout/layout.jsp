<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<html>
    <head>
        <title>JMonitoring administration</title>
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
    	<table class="layout" border="0" height="100%" width="100%">
      	<tbody>
            	<tr class="header" height="40">
                    <tiles:insert attribute="head"/>
            	</tr>
            	<tr>
                <td class="leftMenu" width="130" valign="top">
                    <tiles:insert attribute="menu"/>
                </td>
                <td class="main"  valign="top">
                    <tiles:insert attribute="body"/>                
                </td>
            </tr>
            <tr height="20">
                <td class="footer" colspan="2" align="center">
                    <tiles:insert attribute="footer"/>
                </td>
            </tr>
          </tbody>
        </table>
    </body>
</html>
