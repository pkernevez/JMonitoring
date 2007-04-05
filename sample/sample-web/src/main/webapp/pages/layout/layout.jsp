<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<html>
    <head>
        <title>JMonitoring sample</title>
    	<link href="css/global.css" rel="stylesheet" type="text/css" media="screen"/>
 
    </head>
    <body>
    	<table class="layout" border="0" height="100%" width="100%">
      	<tbody>
            	<tr class="header" height="40">
                    <tiles:insert attribute="head"/>
            	</tr>
            	<tr>
                <td class="leftMenu" width="200" valign="top">
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
