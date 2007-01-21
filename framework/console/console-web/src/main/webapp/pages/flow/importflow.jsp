<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@ taglib uri="displaytag.tld" prefix="display" %>

<!-- Begin Body -->
<h1>Import Flow</h1>
<html:form action="/ImportFlowOut" method="post" enctype="multipart/form-data">

<h2>Please Enter the Following Details</h2><BR/>
File Name &nbsp;<html:file property="theFile"/><BR/><BR/>
<html:submit>Upload File</html:submit>
</html:form>