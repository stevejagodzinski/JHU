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
	return htmlContent + '<td colspan="2" class="simpleFireBug simpleFirebugSplitWidth simpleFirebugFullHeight"><div id="simpleFireBugOutputDiv" class="simpleFirebugFullWidth simpleFirebugFullHeight" /></td>';
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
