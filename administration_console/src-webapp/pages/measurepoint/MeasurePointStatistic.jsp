<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@page import="net.kernevez.javamonitoring.console.measurepoint.MeasurePointForm"%>
<%@page import="net.kernevez.performance.measure.MeasurePoint"%>
<%@page import="java.util.Date"%>

<!-- Begin Body -->
<h1>MeasurePoint statistics</h1>

<!-- details of the measure point -->
<h2>Statistics generated for all the MeasurePoint made on :</h2>
<table>
	<tr>
		<td>Call:</td>
		<td colSpan="3"><b><bean:write name="measurepointstatform" property="className"
		/>.<bean:write name="measurepointstatform" property="methodName"/>(..)</b></td>
	</tr>
	<tr>
		<td>Number of occurences found:</td>
		<td><b><bean:write name="measurepointstatform" format="#" property="nbMeasures"/></b></td>
	</tr>
	<tr>
		<td colSpan="4"><b>Duration:</b></td>
	</tr>
	<tr>
		<td>Min :</td>
		<td><bean:write name="measurepointstatform" format="#" property="durationMin"/></td>
		<td>Max :</td>
		<td><bean:write name="measurepointstatform" format="#" property="durationMax"/></td>
	</tr>
	<tr>
		<td>Avg :</td>
		<td><bean:write name="measurepointstatform" format="#" property="durationAvg"/></td>
		<td>Deviance :</td>
		<td><bean:write name="measurepointstatform" format="#" property="durationDev"/></td>
	</tr>
</table>
<br>
<html:form action="/MeasurePointStat" >
Value to use for grouping duration value:&nbsp;<html:text property="interval" size="7" maxlength="7"/>ms
<html:image property="submit" page="/images/button/button_refresh.png" />
<html:hidden property="className"/>
<html:hidden property="methodName"/>
</html:form>
<br><br>
<hr>
<br>
<!-- Chart -->
<% /* We use Bidon Param to be sure that or img can't be cached by proxy or browser */%>
<% // @todo Refactor this mecanism may be with httpheader param... %>
<bean:write name="measurepointstatform" filter="false" property="imageMap"/>
<img src="DynamicImageServlet.do?Id=FULL_DURATION_STAT&Bidon=<%=new Date().getTime()%>" USEMAP="#chart"/>
