function findCustomerByIDFromTextInput() {
    var baseAddress = "CustomerLookup";
    var data = "customerId=" + getValue('customerID');
    var address = baseAddress + "?" + data;
    ajaxResult(address, 'findCustomerByIDResult');
}

function findCustomerByIDFromCombo() {
    var baseAddress = "CustomerLookup";
    var data = "customerId=" + getComboValue('customerID');
    var address = baseAddress + "?" + data;
    ajaxResult(address, 'findCustomerByIDResult');
}

function getComboValue(comboId) {
    var combo = document.getElementById(comboId);
    return combo.options[combo.selectedIndex].text;
}

function findCustomerByName() {
    var baseAddress = "LookupCustomerByName";
    var data = "firstName=" + getValue('firstName') + '&lastName=' + getValue('lastName');
    var address = baseAddress + "?" + data;
    ajaxResult(address, 'findCustomerByNameResult');
}