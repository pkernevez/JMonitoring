<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@ taglib uri="displaytag.tld" prefix="display" %>
<%@page import="org.jmonitoring.console.methodcall.MethodCallEditForm"%>
<%@page import="org.jmonitoring.core.configuration.Configuration"%>


<!-- Begin Body -->
<h1>Method Call Details</h1>

<h2><bean:write name="methodcallform" property="methodCall.className"/>.<bean:write name="methodcallform" property="methodCall.methodName"/>()</h2><br>


<table border="0" class="presentation">
	<tr title="This class can be different of the full classname if the runtime instance is a child of the Weaving class." >
		<td class="title" NOWRAP>Runtime class name:</td>
		<td class="content" NOWRAP colSpan="2"><b><bean:write format="" name="methodcallform" property="methodCall.runtimeClassB"/></b></td>
	</tr>
	<tr>
		<td class="title" NOWRAP>Duration:</td>
		<td class="content" NOWRAP colSpan="2"><b><bean:write format="" name="methodcallform" property="methodCall.duration"/></b></td>
	</tr>
	<tr>
		<td NOWRAP>Group name:</td>
		<td NOWRAP class="group" bgcolor="<bean:write name="methodcallform" property="groupColor"/>">&nbsp;&nbsp;&nbsp;</td>
		<td NOWRAP><b><bean:write name="methodcallform" property="methodCall.groupName"/></b></td>
	</tr>
	<tr>
		<td NOWRAP>Flow Id:</td>
		<td NOWRAP colSpan="2">
			<html:link action="FlowEditIn" paramId="id" paramName="methodcallform" paramProperty="methodCall.flowId" title="View full flow...">
				<b><bean:write format="####" name="methodcallform" property="methodCall.flowId"/></b><img src="images/edit.png"/>
			</html:link>
		</td>
	</tr>
	<tr>
		<td NOWRAP>Id:</td>
		<td NOWRAP colSpan="2">
			<html:link action="MethodCallStatIn" name="methodcallform" property="methodCallIdsMap" title="View stats...">
				<b><bean:write format="####" name="methodcallform" property="methodCall.position"/></b><IMG src="images/graphique.png"/>
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
		<td NOWRAP>Parent id:</td>
		<td NOWRAP colSpan="2">
			<html:link action="MethodCallEditIn" name="methodcallform" property="parentIdsMap" title="View parent...">
				<b><bean:write format="####" name="methodcallform" property="methodCall.parent.position"/></b><IMG src="images/edit.png"/>
			</html:link>
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
						<span class="prevDuration" title="Duration since the prev MethodCall">[-><bean:write format="" name="child" property="durationFromPreviousCall"/>]</span>
						<span class="curDuration" title="Duration of this MethodCall">[<bean:write format="" name="child" property="duration"/>]</span>&nbsp;
							<html:link action="MethodCallEditIn" name="methodcallform" property='<%="childMethodCallIdsMap["+ctr+"]"%>' title="View details...">
							<bean:write name="child" property="groupName"/> &nbsp;-->&nbsp;
							<bean:write name="child" property="className"/>.<bean:write name="child" property="methodName"/>()<img src="images/edit.png"/>
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

