function findCustomersByIDs() {
    var baseAddress = "GetCustomersByIDs";
    var data = "customerIds=" + getValue('customerIds');
    var address = baseAddress + "?" + data;
    ajaxResult(address, 'findCustomersByIdsResult', buildTableFromJSONArray);
}

function buildTableFromJSONArray(request, resultRegion) {
	if ((request.readyState == 4) &&
		      (request.status == 200)) {
		var customers = fromJSONArray(request.responseText);
		var table = buildTable(customers);
		htmlInsert(resultRegion, table);
	}
}

function buildTable(customers) {
	var table = "<table>" +
			"<tr><th>Customer ID</th><th>First Name</th><th>Last Name</th><th>Current balance</th></tr>";
	
	for(var i = 0; i < customers.length; i++) {
		table += "<tr><td>" + customers[i]['customerId'] + "</td> " + 
					"<td>" + customers[i]['firstName'] + "</td> " +
					"<td>" + customers[i]['lastName'] + "</td> " +
					"<td>" + customers[i]['accountBalance'] + "</td> ";
	};
	
	return table;
}