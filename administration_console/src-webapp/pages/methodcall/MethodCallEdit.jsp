<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@page import="org.jmonitoring.console.methodcall.MethodCallForm"%>
<%@page import="org.jmonitoring.core.configuration.Configuration"%>


<!-- Begin Body -->
<h1>Method Call Details</h1>

<h2><bean:write name="methodcallform" property="methodCall.className"/>.<bean:write name="methodcallform" property="methodCall.methodName"/>()</h2><br>


<table border="0" class="presentation">
	<tr>
		<td NOWRAP>Duration:</td>
		<td NOWRAP colSpan="2"><b><bean:write format="" name="methodcallform" property="methodCall.duration"/></b></td>
	</tr>
	<tr>
		<td NOWRAP>Group name:</td>
		<td NOWRAP class="group" bgcolor="<bean:write name="methodcallform" property="groupColor"/>">&nbsp;&nbsp;&nbsp;</td>
		<td NOWRAP><b><bean:write name="methodcallform" property="methodCall.groupName"/></b></td>
	</tr>
	<tr>
		<td NOWRAP>Flow Id:</td>
		<td NOWRAP colSpan="2">
			<html:link action="FlowEdit" paramId="id" paramName="methodcallform" paramProperty="methodCall.flowId" title="View full flow...">
				<b><bean:write format="####" name="methodcallform" property="methodCall.flowId"/></b></td>
			</html:link>
		
	</tr>
	<tr>
		<td NOWRAP>Sequence Id:</td>
		<td NOWRAP colSpan="2"><b><bean:write format="####" name="methodcallform" property="methodCall.sequenceId"/></b>
			<html:link action="MethodCallStat" name="methodcallform" property="methodCallIdsMap" title="View stats...">
				<IMG src="images/graphique.png"/>
			</html:link>
		</td>
	</tr>
	<tr>
		<td NOWRAP>Begin date:</td>
		<td NOWRAP colSpan="2"><b><bean:write name="methodcallform" property="methodCall.beginTimeAsString"/></b></td>
	</tr>
	<tr>
		<td NOWRAP>End date:</td>
		<td NOWRAP colSpan="2"><b><bean:write name="methodcallform" property="methodCall.endTimeAsString"/></b></td>
	</tr>
<logic:notEmpty name="methodcallform" property="methodCall.parent">
	<tr>
		<td NOWRAP>Parent SequenceId:</td>
		<td NOWRAP colSpan="2">
			<b><html:link action="MethodCallEdit" name="methodcallform" property="parentIdsMap">
				<bean:write format="####" name="methodcallform" property="methodCall.parent.sequenceId"/>
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
		<td colSpan="2"><b><bean:write name="methodcallform" property="methodCall.params"/></b></td>
	</tr>

	<!-- End of method Normal -->
	<logic:notEmpty name="methodcallform" property="methodCall.returnValue">
		<tr>
			<td NOWRAP>Result:</td>
			<td colSpan="2"><b><bean:write name="methodcallform" property="methodCall.returnValue"/></b></td>
		</tr>
	</logic:notEmpty> 
	
	<!-- End of method Throwable -->
	<logic:empty name="methodcallform" property="methodCall.returnValue">
		<tr>
			<td NOWRAP>ThrowableClass:</td>
			<td NOWRAP colSpan="2"><b><bean:write name="methodcallform" property="methodCall.throwableClassName"/></b></td>
		</tr>
		<tr>
			<td NOWRAP>ThrowableMsg:</td>
			<td colSpan="2"><b><bean:write name="methodcallform" property="methodCall.throwableMessage"/></b></td>
		</tr>
	</logic:empty> 
	
	<tr>
		<td colSpan="3">
			Children:
			<ul>
				<logic:iterate id="child" name="methodcallform" property="methodCall.children" indexId="ctr">
					<li>
						<html:link action="MethodCallEditIn" name="methodcallform" property='<%="childMethodCallIdsMap["+ctr+"]"%>' title="View details...">
							[<bean:write format="" name="child" property="duration"/>] &nbsp;
							<bean:write name="child" property="groupName"/> &nbsp;-->&nbsp;
							<bean:write name="child" property="className"/>.<bean:write name="child" property="methodName"/>()
						</html:link>
						<html:link action="MethodCallStatIn" name="methodcallform" property='<%="childMethodCallIdsMap["+ctr+"]"%>' title="View stats...">
							<IMG src="images/graphique.png"/>
						</html:link>
					</li>
				</logic:iterate>
			</ul>
		</td>
	</tr>
</table>

