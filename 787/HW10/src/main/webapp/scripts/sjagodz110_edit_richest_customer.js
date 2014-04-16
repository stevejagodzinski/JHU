window.onload = function() {
	new Ajax.InPlaceEditor("element-to-edit", "show-value.jsp");
};

function findCustomersById(resultRegion) {
	
	new Ajax.Request("GetCustomerByID", {
		method:'get',
		onSuccess : function(response) { handleCustomerLookupResponse(response, resultRegion); },
		parameters : {
			customerId : $F("customerIdInput")
		}
	});
}

function handleCustomerLookupResponse(response, resultRegion) {	
	buildTableFromProtoypeResponse(response, resultRegion);
	
	var highlight = document.URL.toQueryParams()['highlight'] == "true";
	
	if(highlight) {
		new Effect.Highlight(resultRegion);
	}
}

function buildTableFromProtoypeResponse(response, resultRegion) {
	// GetCustomerByID returns a single customer. Adding to an array.
	var table = buildTable(new Array(response.responseJSON));
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