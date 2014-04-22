$j(function() {
    $j("#customerIdSearchButton").click(findCustomersById);
    showHighlightButton();
    $j("#highlightButton").click(highlightUnknownAndZeroRows);
});

function findCustomersById() {	
	var responseFormat = document.URL.toQueryParams()['responseFormat'];
	var responseHandler = responseFormat == 'json' ? createUnorderedList : insertHTML;
	
	$j.ajax({ url: "GetCustomerByID",
		data : {customerId : $j("#customerIdInput").val(),
			responseFormat : responseFormat },
			dataType: responseFormat,
        success : responseHandler,
        cache : false,
        type : "get" });
}

function createUnorderedList(jsonData) {	
	$j("#findCustomerByIdResult").html("<ul><li>" + jsonData.customerId + "</li><li>" + jsonData.firstName + "</li><li>" + jsonData.lastName + "</li><li>" + jsonData.accountBalance + "</li></ul>");
}

function insertHTML(htmlResult) {	
	$j("#findCustomerByIdResult").html(htmlResult);
}

function showHighlightButton() {
	if(document.URL.toQueryParams()['highlight'] == "true") {
		$j("#highlightButton").show();
	}
}

function highlightUnknownAndZeroRows() {
	$j(".unknown-name, .zero-balance").each(function(index, element){
		new Effect.Highlight(element.id);
	})
}