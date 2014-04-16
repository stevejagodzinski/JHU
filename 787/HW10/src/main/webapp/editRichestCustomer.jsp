<%@ page import="com.jagodzinski.jhu.ajax.controller.simplebanking.CustomerLookupService" %>
<%@ page import="com.jagodzinski.jhu.ajax.model.simplebanking.CustomerAccountSummary" %>
<%! CustomerAccountSummary highestBalanceCustomer = CustomerLookupService.getInstance().findCustomerWithHighestBalance(); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>Customer Lookup</title>
    <link rel="stylesheet"
          href="./css/sjagodz110_styles.css"
          type="text/css"/>
	<script	src="https://ajax.googleapis.com/ajax/libs/prototype/1.6.1.0/prototype.js"
		type="text/javascript"></script>
	<script src="./scripts/scriptaculous/scriptaculous.js?load=effects,controls"
       type="text/javascript"></script>
</head>
<body>
<div align="center">
    <fieldset>
        <legend>Edit Richest Customer</legend>
        <form>
            <table>
	            <tr>
	            	<td class="inputFormLabel">Customer Id:</td>
	            	<td id="customerId"><%=highestBalanceCustomer.getCustomerId()%></td>
            	</tr>            
	            <tr>
	            	<td class="inputFormLabel">First Name:</td>
	            	<td id="firstName"><%=highestBalanceCustomer.getFirstName()%></td>
            	</tr>
	            <tr>
					<td class="inputFormLabel">Last Name:</td>
					<td id="lastName"><%=highestBalanceCustomer.getLastName()%></td>
				</tr>
	            <tr>
	            	<td class="inputFormLabel">Balance Name:</td>
	            	<td id="balance"><%=highestBalanceCustomer.getAccountBalance().toString()%></td>
            	</tr>	            
	        </table>
        </form>
    </fieldset>
</div>
</body></html>