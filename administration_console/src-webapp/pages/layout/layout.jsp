<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
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
	     
	<style type="text/css">
  	.smd-menu-top {0}font-family:verdana,arial;font-size:12px;font-weight:bold{1}
  	.smd-menu-item {0}font-family:verdana,arial;font-size:12px;{1}
  	img.smd-menu {0}border: 0{1}
  	a.smd-menu {0}text-decoration:none{1}
	</style>
    </head>
    <body>
    	<table class="layout" border="0" height="100%" width="100%">
      	<tbody>
            	<tr height="40">
                    <tiles:insert attribute="head"/>
            	</tr>
            	<tr>
                <td class="Leftmenu" width="130" valign="top">
                    <tiles:insert attribute="menu"/>
                </td>
                <td class="header"  valign="top">
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
