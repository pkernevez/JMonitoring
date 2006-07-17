<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<html>
    <head>
        <title>JMonitoring sample</title>
    	<link href="css/screen.css" rel="stylesheet" type="text/css">    
    	<link href="css/global.css" rel="stylesheet" type="text/css" media="screen"/>
 
    </head>
    <body>
        <table border="2" width="100%" height="100%">
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
    <body>
</html>
