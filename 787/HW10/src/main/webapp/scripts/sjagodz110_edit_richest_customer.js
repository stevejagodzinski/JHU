window.onload = function() {
	new Ajax.InPlaceEditor("firstName", "UpdateCustomerName?id=$f('customerId')");
};