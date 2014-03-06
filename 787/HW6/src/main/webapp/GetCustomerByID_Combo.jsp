<%@ page import="com.jagodzinski.simplebanking.controller.CustomerLookupService" %>
<%@ page import="java.util.List" %>
<%! List<String> customerIds = CustomerLookupService.getInstance().getCustomerIds(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>Get Customer By Id</title>
    <link rel="stylesheet"
          href="./css/sjagodz13_styles.css"
          type="text/css"/>
    <script src="./scripts/sjagodz14_ajax_utils.js"
            type="text/javascript"></script>
    <script src="./scripts/sjagodz14.js"
            type="text/javascript"></script>
</head>
<body>
<div align="center">
    <fieldset>
        <legend>Get Customer By Id</legend>
        <form>
            <label>Customer ID:
                <select id="customerID">
                <% for(int i = 0; i < customerIds.size(); i++) { %>
                    <option value="<%=i%>"><%=customerIds.get(i)%></option>
                <% } %>
                </select>
            </label>
            <br/>
            <input type="button" value="Find Customer by ID" onclick="findCustomerByIDFromCombo()"/>
            <div id="findCustomerByIDResult"></div>
        </form>
    </fieldset>
</div>
</body></html>