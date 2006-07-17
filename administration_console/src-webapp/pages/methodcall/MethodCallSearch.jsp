<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@page import="org.jmonitoring.console.methodcall.MethodCallUtil"%>

<!-- Begin Body -->
<h1>Measure search</h1>
<div id="menuDiv">
<%
	StringBuffer tBuffer = new StringBuffer();
	MethodCallUtil tUtil = new MethodCallUtil( tBuffer );
	tUtil.writeMeasureListAsMenu();

%>
	<%=tBuffer.toString()%>


</div>