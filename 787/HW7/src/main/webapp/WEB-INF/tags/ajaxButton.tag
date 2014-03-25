<%@ attribute name="value" required="true" %>
<%@ attribute name="url" required="true" %>
<%@ attribute name="resultRegion" required="true" %>
<input type="button" value="${value}" onclick="ajaxResult('${url}','${resultRegion}', showResponseText)" />
