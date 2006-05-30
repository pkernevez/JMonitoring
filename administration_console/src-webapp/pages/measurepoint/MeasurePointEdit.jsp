<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@page import="org.jmonitoring.console.measurepoint.MeasurePointForm"%>
<%@page import="org.jmonitoring.core.configuration.Configuration"%>
<%@page import="org.jmonitoring.core.dao.ExecutionFlowDAO"%>
<%@page import="org.jmonitoring.core.persistence.HibernateManager"%>


<!-- Begin Body -->
<h1>MeasurePoint Details</h1>

<%
	MeasurePointForm tForm = (MeasurePointForm)request.getAttribute("measurepointform");
	Connection tConnection = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
    ExecutionFlowDAO tDao = new ExecutionFlowDAO(HibernateManager.getSession());
	tForm.setMeasurePoint( tDao.readFullMethodCall( tForm.getFlowId(), tForm.getSequenceId() ) );
%>

<h2><bean:write name="measurepointform" property="measurePoint.className"/>.<bean:write name="measurepointform" property="measurePoint.methodName"/>()</h2><br>


<table border="0" class="presentation">
	<tr>
		<td NOWRAP>Duration:</td>
		<td NOWRAP colSpan="2"><b><bean:write format="" name="measurepointform" property="measurePoint.duration"/></b></td>
	</tr>
	<tr>
		<td NOWRAP>Group name:</td>
		<td NOWRAP class="group" bgcolor="<%=Configuration.getInstance().getGroupAsColorString(tForm.getMeasurePoint().getGroupName())%>">&nbsp;&nbsp;&nbsp;</td>
		<td NOWRAP><b><bean:write name="measurepointform" property="measurePoint.groupName"/></b></td>
	</tr>
	<tr>
		<td NOWRAP>Flow Id:</td>
		<td NOWRAP colSpan="2">
			<html:link action="FlowEdit" paramId="id" paramName="measurepointform" paramProperty="measurePoint.flowId" title="View full flow...">
				<b><bean:write format="####" name="measurepointform" property="measurePoint.flowId"/></b></td>
			</html:link>
		
	</tr>
	<tr>
		<td NOWRAP>Sequence Id:</td>
		<td NOWRAP colSpan="2"><b><bean:write format="####" name="measurepointform" property="measurePoint.sequenceId"/></b>
			<html:link action="MeasurePointStat" name="measurepointform" property="measureIdsMap" title="View stats...">
				<IMG src="images/graphique.png"/>
			</html:link>
		</td>
	</tr>
	<tr>
		<td NOWRAP>Begin date:</td>
		<td NOWRAP colSpan="2"><b><bean:write name="measurepointform" property="measurePoint.beginTimeAsString"/></b></td>
	</tr>
	<tr>
		<td NOWRAP>End date:</td>
		<td NOWRAP colSpan="2"><b><bean:write name="measurepointform" property="measurePoint.endTimeAsString"/></b></td>
	</tr>
<logic:notEmpty name="measurepointform" property="measurePoint.parent">
	<tr>
		<td NOWRAP>Parent SequenceId:</td>
		<td NOWRAP colSpan="2">
			<b><html:link action="MeasurePointEdit" name="measurepointform" property="parentIdsMap">
				<bean:write format="####" name="measurepointform" property="measurePoint.parent.sequenceId"/>
			</html:link></b>
		</td>
	</tr>
</logic:notEmpty> 
	<tr>
		<td NOWRAP></td>
		<td NOWRAP colSpan="2"></td>
	</tr></>
	<tr>
		<td NOWRAP>Param:</td>
		<td colSpan="2"><b><bean:write name="measurepointform" property="measurePoint.params"/></b></td>
	</tr>

	<!-- End of method Normal -->
	<logic:notEmpty name="measurepointform" property="measurePoint.returnValue">
		<tr>
			<td NOWRAP>Result:</td>
			<td colSpan="2"><b><bean:write name="measurepointform" property="measurePoint.returnValue"/></b></td>
		</tr>
	</logic:notEmpty> 
	
	<!-- End of method Throwable -->
	<logic:empty name="measurepointform" property="measurePoint.returnValue">
		<tr>
			<td NOWRAP>ThrowableClass:</td>
			<td NOWRAP colSpan="2"><b><bean:write name="measurepointform" property="measurePoint.throwableClassName"/></b></td>
		</tr>
		<tr>
			<td NOWRAP>ThrowableMsg:</td>
			<td colSpan="2"><b><bean:write name="measurepointform" property="measurePoint.throwableMessage"/></b></td>
		</tr>
	</logic:empty> 
	
	<tr>
		<td colSpan="3">
			Children:
			<ul>
				<logic:iterate id="child" name="measurepointform" property="measurePoint.children" indexId="ctr">
					<li>
						<html:link action="MeasurePointEdit" name="measurepointform" property='<%="childMeasureIdsMap["+ctr+"]"%>' title="View details...">
							[<bean:write format="" name="child" property="duration"/>] &nbsp;
							<bean:write name="child" property="groupName"/> &nbsp;-->&nbsp;
							<bean:write name="child" property="className"/>.<bean:write name="child" property="methodName"/>()
						</html:link>
						<html:link action="MeasurePointStat" name="measurepointform" property='<%="childMeasureIdsMap["+ctr+"]"%>' title="View stats...">
							<IMG src="images/graphique.png"/>
						</html:link>
					</li>
				</logic:iterate>
			</ul>
		</td>
	</tr>
</table>

