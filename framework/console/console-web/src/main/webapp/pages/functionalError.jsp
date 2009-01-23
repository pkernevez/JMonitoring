<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.PrintWriter"%>

<html:html>
<head>
<title>Unexpected Error</title>
<html:base/>
</head>
<body>
<h3>An error as occurred</h3>
<logic:present name="<%=Globals.EXCEPTION_KEY%>">
<p><bean:write name="<%=Globals.EXCEPTION_KEY%>" property="message" /></p>
</logic:present>
<%
	Exception tException = (Exception)request.getAttribute(Globals.EXCEPTION_KEY);
	if ( tException != null )
	{
		ByteArrayOutputStream tOut = new ByteArrayOutputStream();
		PrintWriter tWriter = new PrintWriter(tOut);
		tException.printStackTrace(tWriter); 
%>Stack:<BR/><%=tOut.toString()%>
<%   }%>
</body>
</html:html>
