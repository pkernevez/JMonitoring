<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@ taglib uri="displaytag.tld" prefix="display" %>
<%@page import="org.jmonitoring.console.flow.FlowSearchForm"%>
<%@page import="org.jmonitoring.core.configuration.SpringConfigurationUtil"%>
<%@page import="org.jmonitoring.core.configuration.FormaterBean"%>
<h1>Flow Search new</h1>
<%
FlowSearchForm tForm = (FlowSearchForm)request.getAttribute("flowsearchform");
%>  
<html:form action="/FlowSearchOut" focus="threadName">

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
			<td>Begin date (<%=((FormaterBean)SpringConfigurationUtil.getBean("formater")).getDateFormater().toPattern()%>)</td>
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
	<html:image styleId="but_submit" property="submit" page="/images/button/button_ok.gif" />

<% if (tForm.getListOfFlows() != null)
{//Not the first time
%>
		<!-- Table des resultats -->
		<display:table htmlId="results" name="flowsearchform.listOfFlows" sort="list" pagesize="20" requestURI="FlowSearchOut.do">
			<display:column property="id" sortable="true" title="Tech Id" href="FlowEditIn.do" paramId="id"/>
			<display:column property="threadName" title="Thread" />	
	  		<display:column property="jvmIdentifier" title="Server" />
	  		<display:column property="duration" sortable="true" title="Duration" />
	  		<display:column property="beginTime" sortable="true" title="Begin" />
	  		<display:column property="endTime" title="End"/>
	  		<display:column property="className" sortable="true" title="Class"/>
	  		<display:column property="methodName" sortable="true" title="Method"/>
		</display:table>
<% } %>
</html:form>

<!-- End Body -->
