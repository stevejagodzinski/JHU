var cities = ["Albany", "Atlanta", "Austin", "Baltimore", "Boston"];

window.onload = function() {
  new Autocompleter.Local("cityInput", "cityMenuDiv", cities);
};