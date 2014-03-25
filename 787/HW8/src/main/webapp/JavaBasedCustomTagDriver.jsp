<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>Java-Based JSP Custom Tag Driver</title>
    <link rel="stylesheet"
          href="./css/sjagodz17_styles.css"
          type="text/css"/>
    <script src="./scripts/sjagodz17_ajax_utils.js"
            type="text/javascript"></script>
</head>
<body>
<%@ taglib uri="/WEB-INF/tlds/sjagodz17-taglib.tld" prefix="sjagodz17" %>
<div align="center">
    <fieldset>
        <legend>Java-Based JSP Custom Tag Driver</legend>
        <form>
            <sjagodz17:ajaxButton url="AjaxButtonTestURL" resultRegion="ajaxButtonResultRegion" value="AJAX Button"/>
            <div id="ajaxButtonResultRegion"></div>
        </form>
    </fieldset>
</div>
</body></html>