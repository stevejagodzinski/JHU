function closeSimpleFireBug() {
	document.getElementById('simpleFireBugDiv').style.visibility = 'hidden';
}

function launchSimpleFireBug() { 
	var htmlContent = "<table class='simpleFirebugFullWidth simpleFirebugFullHeight'>";
	htmlContent = "<tr class='simpleFirebugFullHeight'>" + addInputArea(htmlContent);
	htmlContent = addOutputArea(htmlContent) + "</tr>";
	htmlContent = "<tr>" + addButtons(htmlContent) + "</tr>" ;
	htmlContent += "</table>";
	
	var simpleFireBugDiv = document.getElementById('simpleFireBugDiv');
	simpleFireBugDiv.innerHTML = htmlContent;
	simpleFireBugDiv.className += ' simpleFireBugContentsExpandedHeight';
}

function addInputArea(htmlContent) {
	return htmlContent + '<td colspan="2" class="simpleFirebugSplitWidth simpleFirebugFullHeight"><textarea id="simpleFireBugInputTextArea" class="simpleFirebugFullWidth simpleFirebugFullHeight"></textarea></td>';
}

function addOutputArea(htmlContent) {
	return htmlContent + '<td colspan="2" class="simpleFirebugSplitWidth simpleFirebugFullHeight"><div id="simpleFireBugOutputDiv" class="simpleFirebugFullWidth simpleFirebugFullHeight" /></td>';
}

function addButtons(htmlContent) {
	return htmlContent +
		'<td><input type="button" value="Run" onclick="simpleFireBugRun();" /></td>' +
		'<td><input type="button" value="Clear" onclick="clearSimpleFireBugInputArea();" /></td>' +
		'<td colspan="2"><input type="button" value="Close" onclick="closeSimpleFireBug();" /></td>';
}

function simpleFireBugRun() {
	var inputArea = document.getElementById('simpleFireBugInputTextArea');
	var javascript = inputArea.value;	
	
	var result;
	
	try{
		result = eval(javascript);
	} catch(error) {
		result = error;
	}
	
	document.getElementById('simpleFireBugOutputDiv').innerHTML = result;
}
