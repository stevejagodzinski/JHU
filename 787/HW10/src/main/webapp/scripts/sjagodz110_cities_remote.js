window.onload = function() {
	new Ajax.Autocompleter("cityInputAjax", "cityMenuDivAjax", "GetCities");
};

function googleSearch(id) {
	var language = getValue(id);
	window.location.href = "http://www.google.com/search?q=" + language;
}

function getValue(id) {
	return (escape(document.getElementById(id).value));
}