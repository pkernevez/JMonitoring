<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.PrintWriter"%>

<!--  We don't use Tiles for limiting the risk of error -->
<%@page import="org.apache.commons.logging.LogFactory"%>
<html:html>
<head>
   <title>Unexpected Error</title>
  <link href="css/global.css" rel="stylesheet" type="text/css" media="screen"/>
</head>

    <body>
    	<table class="layout" border="0" height="100%" width="100%">
      	<tbody>
            	<tr class="header" height="40">
                  <td class="header" width="138" height="40"><h1><img src="images/jmonitoring.gif" alt="" width="120" height="56" border="0"></h1></td>
                  <td class="header-right" height="40">JMonitoring Tool</td>
            	</tr>
            	<tr>
                <td class="leftMenu" width="130" valign="top">
<%try{ %>
	<menu:useMenuDisplayer name="Simple"
 		bundle="org.apache.struts.action.MESSAGE">
  		<menu:displayMenu name="mainMenu"/>
	</menu:useMenuDisplayer>
<% } catch(Throwable t){
    //We dont take care of a Menu Exception on the error page
	LogFactory.getLog(this.getClass()).error("Unable to create menu for error page", t);
}%>
                </td>
                <td class="main"  valign="top">
					<h3>An unexpected error has occured</h3>
					<logic:present name="<%=Globals.EXCEPTION_KEY%>"> 
					<p><bean:write name="<%=Globals.EXCEPTION_KEY%>" /></p>
					</logic:present>
					<%
						Exception tException = (Exception)request.getAttribute(Globals.EXCEPTION_KEY);
						if ( tException != null )
						{
							ByteArrayOutputStream tOut = new ByteArrayOutputStream();
							PrintWriter tWriter = new PrintWriter(tOut);
							tException.printStackTrace(tWriter); 
							tWriter.flush();
					%>Stack:<BR/><%=tOut.toString()%>
					<%   } %>
                </td>
            </tr>
            <tr height="20">
                <td class="footer" colspan="2" align="center">
					<I><a href="http://forge.octo.com/confluence/display/JMO/Home">JMonitoring</a> is developed by Philippe Kernevez and <a href="www.octo.com">OCTO Technology</a></I>                </td>
            </tr>
          </tbody>
        </table>
    </body>
</html:html>
