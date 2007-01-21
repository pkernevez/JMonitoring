<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@ taglib uri="displaytag.tld" prefix="display" %>
<%@page import="org.jmonitoring.console.methodcall.search.MethodCallUtil"%>
<%@page import="org.jmonitoring.console.methodcall.search.MethodCallSearchForm;"%>

<h1>Measure search</h1>
<div id="menuDiv">
<%
	MethodCallUtil tUtil = new MethodCallUtil();
	MethodCallSearchForm tForm = (MethodCallSearchForm)request.getAttribute("methodcallsearchform");

	tUtil.writeMeasureListAsMenu(tForm.getMapOfMethodCallExtractByFullName(), tForm.getTreeOfMethodCallExtract());

%>
	<%=tUtil.toString()%>


</div>