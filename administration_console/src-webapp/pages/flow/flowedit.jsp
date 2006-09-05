<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@page import="org.jmonitoring.console.flow.edit.FlowEditForm"%>
<%@page import="org.jmonitoring.core.dto.ExecutionFlowDTO"%>
<%@page import="org.jmonitoring.console.flow.stack.FlowAsStackUtil"%>
<%@page import="java.util.Date"%>

<H1>Flow Edition</H1>
<html:link action="DeleteFlow" paramName="floweditform" paramProperty="id" paramId="id">Delete</html:link>

<!--a href="/DeleteFlow.do?Id=< % =((FlowEditForm)request.getAttribute("floweditform").getId()% >">Delete</a-->
<table width="100%">
	<tr>	
		<td>JVM Name</td>
		<td><bean:write name="floweditform" property="executionFlow.jvmIdentifier"/></td>
		<td>Thread Name</td>
		<td><bean:write name="floweditform" property="executionFlow.threadName"/></td>
	</tr>
	<tr>
		<td>Begin Time</td>
		<td><bean:write name="floweditform" property="executionFlow.beginDateAsString"/></td>
		<td>End Time</td>
		<td><bean:write name="floweditform" property="executionFlow.endTimeAsString"/></td>
	</tr>
	<tr>
		<td>Duration</td>
		<td><bean:write name="floweditform" property="executionFlow.durationAsString"/></td>
		<td>Group Name</td>
		<td><bean:write name="floweditform" property="executionFlow.firstMethodCall.groupName"/></td>
	</tr>
	<tr>
		<td>Appel</td>
		<td colSpan="3"><bean:write name="floweditform" property="executionFlow.className"/>
			.<bean:write name="floweditform" property="executionFlow.methodName"/>()</td>
	</tr>
</table>

<!-- Chart -->
<table class="bidon">
	<tr>
		<% /* We use Bidon Param to be sure that or img can't be cached by proxy or browser */%>
		<% // @todo Refactor this mecanism may be with httpheader param... %>
		<td><img src="DynamicImageServlet.do?Id=DURATION_IN_GROUP&Bidon=<%=new Date().getTime()%>"/></td>
		<td><img src="DynamicImageServlet.do?Id=NB_CALL_TO_GROUP&Bidon=<%=new Date().getTime()%>"/></td>
	</tr>
	<tr>
		<bean:write name="floweditform" filter="false" property="imageMap"/>
		<td align="center" colSpan="2"><img src="DynamicImageServlet.do?Id=CHART_BAR_FLOWS&Bidon=<%=new Date().getTime()%>" USEMAP="#ChartBar"/></td>
	</tr>
</table>
<div id="menuDiv">
<%
	FlowEditForm tForm = (FlowEditForm)request.getAttribute("floweditform");
	if (tForm.getKindOfAction() != FlowEditForm.ACTION_ONLY_GRAPH)
	{
	ExecutionFlowDTO tFlow = tForm.getExecutionFlow();	
	FlowAsStackUtil tUtil = new FlowAsStackUtil( tFlow );
	StringBuffer tBuffer = tUtil.writeFlowAsHtml();
%>
	<%=tBuffer.toString()%>
<%	
	}
%>
</div>

<!--/html:form-->

