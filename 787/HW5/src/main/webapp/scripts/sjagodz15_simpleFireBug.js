
window.onload=new function(){
	addBodyToDivIfDocumentReady(0);
};

function addBodyToDivIfDocumentReady(attempts) {
	var WAIT_FOR_DOCUMENT_READY_TIMEOUT_MS = 10000;
	var WAIT_FOR_DOCUMENT_READY_RETRY_INTERVAL_MS = 100;
	
	if(window.document.readyState === "complete") {
		addDivToBody();
	} else {
		var attemptAgain = (attempts * WAIT_FOR_DOCUMENT_READY_RETRY_INTERVAL_MS) < WAIT_FOR_DOCUMENT_READY_TIMEOUT_MS;
		
		if(attemptAgain) {
			window.setTimeout(function(){addBodyToDivIfDocumentReady(attempts + 1);}, WAIT_FOR_DOCUMENT_READY_RETRY_INTERVAL_MS);
		};
	};
}

function closeSimpleFireBug() {
	document.getElementById('simpleFireBugDiv').style.visibility = 'hidden';
}

function launchSimpleFireBug() { 
	var htmlContent = "<table class='simpleFireBug simpleFirebugFullWidth simpleFirebugFullHeight'>";
	htmlContent = "<tr class='simpleFireBug simpleFirebugFullHeight'>" + addInputArea(htmlContent);
	htmlContent = addOutputArea(htmlContent) + "</tr>";
	htmlContent = "<tr class='simpleFireBug'>" + addButtons(htmlContent) + "</tr>" ;
	htmlContent += "</table>";
	
	var simpleFireBugDiv = document.getElementById('simpleFireBugDiv');
	simpleFireBugDiv.innerHTML = htmlContent;
	simpleFireBugDiv.className += ' simpleFireBugContentsExpandedHeight';
}

function addInputArea(htmlContent) {
	return htmlContent + '<td colspan="2" class="simpleFireBug simpleFirebugSplitWidth simpleFirebugFullHeight"><textarea id="simpleFireBugInputTextArea" class="simpleFireBug simpleFirebugFullWidth simpleFirebugFullHeight"></textarea></td>';
}

function addOutputArea(htmlContent) {
	return htmlContent + '<td colspan="2" class="simpleFireBug simpleFirebugSplitWidth simpleFirebugFullHeight"><div id="simpleFireBugOutputDiv" class="simpleFirebugFullWidth simpleFirebugFullHeight simpleFireBugOutputDiv" /></td>';
}

function addButtons(htmlContent) {
	return htmlContent +
		'<td class="simpleFireBug hideRightBorder"><input type="button" value="Run" onclick="simpleFireBugRun();" /></td>' +
		'<td class="simpleFireBug hideLeftBorder"><input type="button" value="Clear" onclick="clearSimpleFireBugInputArea();" /></td>' +
		'<td class="simpleFireBug" colspan="2"><input type="button" class="simpleFireBugCloseButton" value="Close" onclick="closeSimpleFireBug();" /></td>';
}

function simpleFireBugRun() {
	var javascript = getJavascriptStringFromInputArea();
	var evaluationResult = evaluateJavascript(javascript);	
	
	var innerHTMLForOutputDiv = buildResult(javascript, evaluationResult);
	document.getElementById('simpleFireBugOutputDiv').innerHTML = innerHTMLForOutputDiv;
}

function getJavascriptStringFromInputArea() {
	return document.getElementById('simpleFireBugInputTextArea').value;
}

function evaluateJavascript(javascriptString) {
	var result;
	
	try{
		result = eval(javascriptString);
	} catch(error) {
		result = error;
	}
	
	return result;
}

function buildResult(inputJavascript, evaluationResult) {
	return "<span class='inputJS'>" + inputJavascript + "</span>" +
			"<br /><br />" +
			"<span class='evaluationResult'>" + evaluationResult + "</span>";
}

function clearSimpleFireBugInputArea() {
	document.getElementById('simpleFireBugInputTextArea').value = "";
}

function addDivToBody() {
	var div = 	'<div id="simpleFireBugDiv" class="simpleFireBugContents">' +
					'<input id="launchSimpleFireBugButton" class="simpleFireBugCloseButton" type="button" value="Close"  onclick="closeSimpleFireBug();"/>' +
					'<input id="closeSimpleFireBugButton" class="simpleFireBugLaunchButton" type="button" value="Launch Simple Fire Bug" onclick="launchSimpleFireBug();"/>' +
				'</div>';
	
	document.body.innerHTML += div;
}