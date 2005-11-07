<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@page import="org.jmonitoring.console.flow.FlowSearchForm"%>
<%@page import="org.jmonitoring.core.configuration.Configuration"%>

<!-- Begin Body -->
<h1>Flow Search</h1>
<%
	FlowSearchForm tForm = (FlowSearchForm)request.getAttribute("flowsearchform");
%>  
<html:form action="/FlowSearchSubmit" focus="threadName">

	<!--IMG src="<html:rewrite page="/images/button/button_ok.gif"/>" width="10" height="20"/-->
	
	<table >
		<tr>	
			<td>Thread name</td>
			<td><html:text property="threadName" size="25" maxlength="25"/></td>
			<td>Minimum Duration</td>
			<td><html:text property="durationMin" size="20" maxlength="20"/></td>
		</tr>
		<tr>
			<td>Group name</td>
			<td><html:text property="firstMeasureGroupName" size="20" maxlength="20"/></td>
			<td>Begin date (<%=Configuration.getInstance().getDateFormater().toPattern()%>)</td>
			<td>
				<html:text property="beginDate" size="8" maxlength="8"/>
				<img class="mycalendar" src="images/img.gif" id="f_trigger_c" 
					style="cursor: pointer;" title="Date selector" 
					onmouseover="this.style.background='red';" onmouseout="this.style.background=''" />
			</td>
		</tr>
		<tr>
			<td>First Measure ClassName</td>
			<td><html:text property="firstMeasureClassName" size="40" maxlength="100"/></td>
			<td>First Measure MethodeName</td>
			<td><html:text property="firstMeasureMethodName" size="40" maxlength="80"/></td>
		</tr>
		<tr>
			<td colSpan="2"></td>
			<!--td>Minimum begin time (<%//Configuration.getTimeFormater().toPattern()%>)</td>
			<td><html:text property="beginTimeMin" size="8" maxlength="9"/></td-->
		</tr>
	</table>
<script type="text/javascript">
var field = document.all["beginDate"];
field.id = "beginDate";
Calendar.setup({
	inputField : "beginDate", // ID of the input field
	ifFormat : "%d/%m/%y", // the date format
	button : "f_trigger_c" // ID of the button
}
);
</script>
	<html:image property="submit" page="/images/button/button_ok.gif" />

<% if (tForm.getListOfFlows() != null)
{//Not the first time
%>
		<!-- Table des resultats -->
		<display:table name="flowsearchform.listOfFlows" sort="list" pagesize="20" requestURI="FlowSearchSubmit.do">
			<display:column property="id" sortable="true" title="Tech Id" href="FlowEdit.do" paramId="id"/>
			<display:column property="threadName" title="Thread" />	
	  		<display:column property="jvmIdentifier" title="Server" />
	  		<display:column property="duration" sortable="true" title="Duration" />
	  		<display:column property="beginDateAsString" sortable="true" title="Begin" />
	  		<display:column property="endTimeAsString" title="End"/>
	  		<display:column property="firstMeasure.className" sortable="true" title="Class"/>
	  		<display:column property="firstMeasure.methodName" sortable="true" title="Method"/>
		</display:table>
<% } %>
</html:form>

<!-- End Body -->
