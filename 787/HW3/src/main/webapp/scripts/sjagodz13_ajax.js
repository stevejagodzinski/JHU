// Ajax methods are from instructor's sample code from the module 3 lectures.

// Get the browser-specific request object, either for
// Firefox, Safari, Opera, Mozilla, Netscape, IE 8, or IE 7 (top entry);
// or for Internet Explorer 5 and 6 (bottom entry). 

function getRequestObject() {
  if (window.XMLHttpRequest) {
    return(new XMLHttpRequest());
  } else if (window.ActiveXObject) { 
    return(new ActiveXObject("Microsoft.XMLHTTP"));
  } else {
    // Don't throw Error: this part is for very old browsers,
    // and Error was implemented starting in JavaScript 1.5.
    return(null); 
  }
}

// Make an HTTP request to the given address. 
// Display result in an alert box.

function ajaxAlert(address) {
  var request = getRequestObject();
  request.onreadystatechange =
    function() { showResponseAlert(request); };
  request.open("GET", address, true);
  request.send(null);
}

// Put response text in alert box.

function showResponseAlert(request) {
  if ((request.readyState == 4) &&
      (request.status == 200)) {
    alert(request.responseText);
  }
}

// Make an HTTP request to the given address. 
// Display result in the HTML element that has given ID.

function ajaxResult(address, resultRegion) {
  var request = getRequestObject();
  request.onreadystatechange =
    function() { showResponseText(request,
                                  resultRegion); };
  request.open("GET", address, true);
  request.send(null);
}

// Put response text in the HTML element that has given ID.

function showResponseText(request, resultRegion) {
  if ((request.readyState == 4) &&
      (request.status == 200)) {
    htmlInsert(resultRegion, request.responseText);
  }
}

//Insert the html data into the element that has the specified id.

function htmlInsert(id, htmlData) {
  document.getElementById(id).innerHTML = htmlData;
}

// Demonstrates the incorrect way to do ajax. Using a shared request object instead of annonomous functions.

var sharedRequest;

function sendAjaxRequestUsingSharedRequestObject() {
    sharedRequest = getRequestObject();
    sharedRequest.onreadystatechange = appendResultToDivUsingSharedRequestObject;
    sharedRequest.open("GET", "DemonstrateRaceCondition", true);
    sharedRequest.send(null);
}

function appendResultToDivUsingSharedRequestObject() {
    if (sharedRequest.readyState == 4 && sharedRequest.status == 200) {
        var div = document.getElementById('raceConditionResult');
        div.innerHTML = div.innerHTML + sharedRequest.responseText + '<br/>';
    }
}