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

function ajaxResult(address, resultRegion, responseHandler) {
  var request = getRequestObject();
  request.onreadystatechange =
    function() { responseHandler(request,
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

// Return escaped value of textfield that has given id.
// The builtin "escape" function converts < to &lt;, etc.

function getValue(id) {
  return(escape(document.getElementById(id).value));
}

//Given a doc and the name of an XML element, returns an 
//array of the values of all elements with that name.
//E.g., for 
//<foo><a>one</a><q>two</q><a>three</a></foo>
//getXmlValues(doc, "a") would return 
//["one", "three"].

function getXmlValues(xmlDocument, xmlElementName) {
var elementArray = 
  xmlDocument.getElementsByTagName(xmlElementName);
var valueArray = new Array();
for(var i=0; i<elementArray.length; i++) {
  valueArray[i] = getBodyContent(elementArray[i]);
}
return(valueArray);
}

//Given an element, returns the body content.
function getBodyContent(element) {
  element.normalize();
  return(element.childNodes[0].nodeValue);
}