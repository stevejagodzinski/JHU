window.onload = function() {
	new Ajax.Autocompleter("cityInputAjax", "cityMenuDivAjax", "GetCities");
};

function googleSearch(id) {
	var language = $F(id);
	window.location.href = "http://www.google.com/search?q=" + language;
}