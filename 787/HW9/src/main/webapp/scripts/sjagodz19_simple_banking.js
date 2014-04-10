function findCustomersByIDsUsingPrototypeJS(resultRegion) {
	
	new Ajax.Request("GetCustomersByIDs", {
		method:'get',
		onCreate  : function() { showWaiting(resultRegion); },
		onSuccess : function(response) { buildTableFromProtoypeResponse(response, resultRegion); },
		parameters : {
			customerIds : $F("customerIds"),
			simlateLongRunningServerResponse : true
		}
	});
}

function showWaiting(resultRegion) {
	$(resultRegion).update('Waiting');
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

function findCustomersByIDsUsingAjaxRequestObject(resultRegion) {
	var customerIds = "customerIds=" + getValue('customerIds');
	
	new AjaxRequest("GetCustomersByIDs", {
		method:'get',
		onSuccess : function(response) { buildTableFromJSONArray(response, resultRegion); },
		parameters : customerIds
	});
}

function buildTableFromJSONArray(request, resultRegion) {
	if ((request.readyState == 4) &&
		      (request.status == 200)) {
		var customers = fromJSONArray(request.responseText);
		var table = buildTable(customers);
		htmlInsert(resultRegion, table);
	}
}