var rpcClient;

window.onload = function() {
  rpcClient = new JSONRpcClient("JSON-RPC");
};

function findCustomerByID() {
	var customerId = getValue('customerId');
	
	var callback = function(customer, exception) {
	    if(exception) {
	      alert(exception.message);
	    } else {
	    	htmlInsert('findCustomerByIdResult', buildList(customer));
	    }
	};
	
	rpcClient.customerLookupService.getCustomerById(callback, customerId);	
}

function buildList(customer) {
	return '<ul>' + 
			'<li>' + customer['customerId'] + '</li>' +
			'<li>' + customer['firstName'] + '</li>' +
			'<li>' + customer['lastName'] + '</li>' +
			'<li>' + customer['accountBalance'] + '</li>' +
			'</ul>';
}

function findCustomersByIDs() {
	var customerIds = getRawValue('customerIds').split(',').map(function(s) {return String.prototype.trim.apply(s); });
	
	var callback = function(customers, exception) {
	    if(exception) {
	      alert(exception.message);
	    } else {
	    	htmlInsert('findCustomersByIdsResult', buildTable(customers));
	    }
	};
	
	rpcClient.customerLookupService.getAllCustomers(callback, customerIds);
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

function addCustomer() {
	var customer = createCustomer();
	
	var callback = function(result, exception) {
	    if(exception) {
	    	alert(exception.message);
	    } else if (result){
	    	htmlInsert('putCustomerResult', "Customer successfully created");
	    } else { 
	    	htmlInsert('putCustomerResult', "Customer Already Exists");
	    }
	};
	
	rpcClient.customerLookupService.addCustomer(callback, customer);
}

function createCustomer() {
	var customer = new Object();
	customer.customerId = getRawValue('customerId');
	customer.firstName = getRawValue('firstName');
	customer.lastName = getRawValue('lastName');
	customer.accountBalance = getRawValue('balance');
	return customer;
}