function findCustomersByIDs(resultRegion) {
	
	new Ajax.Request("GetCustomersByIDs", {
		method:'get',
		onSuccess : function(response) { buildTableFromProtoypeResponse(response, resultRegion); },
		parameters : {customerIds : $F("customerIds")}
	});
}

function buildTableFromProtoypeResponse(response, resultRegion) {
	var table = buildTable(response.responseJSON);
	$(resultRegion).update(table);
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