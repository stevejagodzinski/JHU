var Alertable = {};

Alertable.showAlert = function() {
	alert(this.getFirstName() + ' ' + this.getLastName());
};

var Person = Class.create({
	initialize : function() {
		Object.extend(this, Alertable);
	},
	getAddress : function() {
		return this.address;
	},
	setAddress : function(address) {
		this.address = address;
	},
	getFirstName : function() {
		return this.firstName;
	},
	setFirstName : function(firstName) {
		this.firstName = firstName;
	},
	getLastName : function() {
		return this.lastName;
	},
	setLastName : function(lastName) {
		this.lastName = lastName;
	}
});

var Employee = Class.create(Person, {
	getEmployeeId : function() {
		return this.employeeId;
	},
	setEmployeeId : function(employeeId) {
		this.employeeId = employeeId;
	}
});

var Contractor = Class.create(Person, {
	getContractingCompany : function() {
		return this.contractingCompany;
	},
	setContractingCompany : function(contractingCompany) {
		this.contractingCompany = contractingCompany;
	},
	getContractorId : function() {
		return this.contractorId;
	},
	setContractorId : function(contractorId) {
		this.contractorId = contractorId;
	}
});

function createPerson() {
	var person = new Person();
	person.setFirstName($F('personFirstName'));
	person.setLastName($F('personLastName'));
	person.setAddress($F('personAddress'));
	person.showAlert();
}

function createEmployee() {
	var contractor = new Employee();
	contractor.setFirstName($F('employeeFirstName'));
	contractor.setLastName($F('employeeLastName'));
	contractor.setAddress($F('employeeAddress'));
	contractor.setEmployeeId($F('employeeId'));
	contractor.showAlert();
}

function createContractor() {
	var contractor = new Contractor();
	contractor.setFirstName($F('contractorFirstName'));
	contractor.setLastName($F('contractorLastName'));
	contractor.setAddress($F('contractorAddress'));
	contractor.setContractingCompany($F('contractorContractingCompany'));
	contractor.setContractorId($F('contractorId'));
	contractor.showAlert();
}