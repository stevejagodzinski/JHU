function findCustomersByIDs(outputFormat) {
    var baseAddress = "CustomerLookup";
    var data = "customerIds=" + getValue('customerIds') + "&outputFormat=" + outputFormat;
    var address = baseAddress + "?" + data;
    ajaxResult(address, 'findCustomersByIdsResult', getAjaxResultHandler(outputFormat));
}

function getAjaxResultHandler(outputFormat) {
	if(outputFormat == 'JSON') {
		return jsonAjaxResultHandler;
	} else if(outputFormat == 'STRING') {
		return stringAjaxResultHandler;
	}  else if(outformat == 'XML') {
		return xmlAjaxResultHandler;
	}
}

function jsonAjaxResultHandler(request, resultRegion) {
	if ((request.readyState == 4) &&
		      (request.status == 200)) {
		var table = getTableBody(request.responseText);
		htmlInsert(resultRegion, table);
	}
}

function stringAjaxResultHandler() {
	
}

function xmlAjaxResultHandler() {
	
}

function buildTable(customerData) {
	String table = "<table>" +
			"<tr><th>Customer ID</th><th>First Name</th><th>Last Name</th><th>Current balance</th></tr>";
	
	for(customer in customerData) {
		table += "<tr><td>" + customer['customerId'] + "</td> " + 
					"<td>" + customer['firstName'] + "</td> " +
					"<td>" + customer['lastName'] + "</td> " +
					"<td>" + customer['accountBalance'] + "</td> ";
	};
	
	return table;
}