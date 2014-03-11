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
		var json = eval(request.responseText);
		var table = buildTable(json);
		htmlInsert(resultRegion, table);
	}
}

function stringAjaxResultHandler(request, resultRegion) {
	if ((request.readyState == 4) &&
		      (request.status == 200)) {
		var customersArray = new Array();
		var customerStrings = request.responseText.split('\n');
		for(var i = 0; i < customerStrings.length; i++) {
			customersArray.push(parseCustomerString(customerStrings[i]));
		}
		htmlInsert(resultRegion, buildTable(customersArray));
	}
}

function parseCustomerString(customerString) {
	var customerSubString = customerString.substring(customerString.indexOf('[')+1, customerString.indexOf(']'));
	var fields = customerSubString.split(',');
	var customer = new Object();
	for(var i = 0; i < fields.length; i++) {
		var keyValue = fields[i].split('=');
		customer[keyValue[0]] = keyValue[1];
	}
	return customer;
}


function xmlAjaxResultHandler() {
	
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