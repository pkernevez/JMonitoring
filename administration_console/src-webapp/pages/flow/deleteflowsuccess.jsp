<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@page import="org.jmonitoring.console.flow.FlowEditForm"%>

<%
	FlowEditForm tForm = (FlowEditForm)request.getAttribute("floweditform");
	if (tForm != null && tForm.getId() != -1 )
	{
%>	
<H1>Delete Flow Id=<bean:write name="floweditform" format="#" property="id"/></H1>
<% } else { %>
<H1>All Flows have been deleted.</H1>
<% } %>