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
	person.setFirstName($F('firstName'));
	person.setLastName($F('lastName'));
	person.setAddress($F('address'));
	person.showAlert();
}