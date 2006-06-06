<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/MeasurePointList">
	<table>
		<tr>	
			<td>Method name:</td>
			<td colSpan="3">
				<bean:write name="measurepointlistform" property="className"
				/>.<bean:write name="measurepointlistform" property="methodName"/>()
				<a href="MeasurePointStat.do?className=<bean:write name="measurepointlistform" property="className"
				/>&methodName=<bean:write name="measurepointlistform" property="methodName"/>">
				<html:img page="/images/graphique.png"/></a>
			</td>
		</tr>
		<tr>
			<td>Minimum Duration</td>
			<td><html:text property="durationMin" size="6" maxlength="8"/></td>
			<td>Maximum Duration</td>
			<td><html:text property="durationMax" size="6" maxlength="8"/></td>
		</tr>
	</table>
	<html:hidden property="className"/>
	<html:hidden property="methodName"/>
	<html:image property="submit" page="/images/button/button_ok.gif" />
	
</html:form>
<!-- Table des resultats -->
<display:table name="results.rows" sort="list" pagesize="20" requestURI="MeasurePointList.do" 
	decorator="net.kernevez.javamonitoring.console.measurepoint.list.DisplayTagDecorator">
  		<display:column property="urlFlow" title="View Flow" />
  		<display:column property="urlMeasurePoint" title="View MeasurePoint"/>
  		<display:column property="FLOW_ID" sortable="true" title="FlowId" />
  		<display:column property="THREAD_NAME" sortable="true" title="Thread name"/>
  		<display:column property="FLOW_DURATION" sortable="true" title="Flow Duration"/>
  		<display:column property="BEGIN_TIME_AS_DATE" sortable="true" title="Date"/>
  		<display:column property="DURATION" sortable="true" title="Method duration"/>
  		<display:column property="JVM" sortable="true" title="Server"/>
</display:table>
