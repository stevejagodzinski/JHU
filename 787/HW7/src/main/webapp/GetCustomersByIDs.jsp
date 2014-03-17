<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>Get Customer By Id</title>
    <link rel="stylesheet"
          href="./css/sjagodz16_styles.css"
          type="text/css"/>
    <script src="./scripts/sjagodz16_ajax_utils.js"
            type="text/javascript"></script>
    <script src="./scripts/sjagodz16.js"
            type="text/javascript"></script>
</head>
<body>
<div align="center">
    <fieldset>
        <legend>Get Customer By Id</legend>
        <form>
            <label>Customer ID:<input id="customerIds" type="text"/></label><br/>
            <input type="button" value="Find Customer by IDs" onclick="findCustomersByIDs('<%= request.getParameter("outputFormat") %>');"/>
            <div id="findCustomersByIdsResult"></div>
        </form>
    </fieldset>
</div>
</body></html>