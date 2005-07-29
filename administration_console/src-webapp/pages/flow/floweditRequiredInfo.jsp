<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>

<html:form action="/FlowEdit">
	<H1>Flow Edition may be long...</H1>
	<BR/>
	<p>
	The ExecutionFlow is very big (<bean:write name="floweditform" format="#" property="executionFlow.measureCount"/> MeasurePoints).<BR/>
	The display of the MeasurePoint tree may be very long.
	</p>
	<p>
	What do you want to do ?<BR/>
	<html:hidden property="id"/>
	<html:radio value="1" property="kindOfAction">Only display the graphics</html:radio><BR/>
	<html:radio value="2" property="kindOfAction">Force the display of the graphics and MeasurePoint tree.</html:radio><BR/>
	<html:radio value="3" property="kindOfAction">Limit the MeasurePoint tree to those that have duration greater than
	<input type="text" name="durationMin" maxlength="5" size="5"/> ms.</html:radio>
	</p>
	<html:image property="submit" page="/images/button/button_ok.gif" />
</html:form>