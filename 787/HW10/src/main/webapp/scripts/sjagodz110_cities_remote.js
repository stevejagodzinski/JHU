window.onload = function() {
	new Ajax.Autocompleter("cityInputAjax", "cityMenuDivAjax", "GetCities");
};