var AjaxRequest = Class.create({
	initialize : function(url, options) {
		this.url = url;
		this.options = options;
		
		this.doAjaxRequest();
	},
	getMethod : function() {
		if (this.options['method']) {
			return this.options['method'];
		} else { 
			return 'POST';
		}
	},
	buildParametersForURL : function() {
		if(this.options['parameters']) {
			return '?' + this.options['parameters'];
		} else {
			return '';
		}
	},
	doAjaxRequest : function() {
		var requestUrl = this.url + this.buildParametersForURL();
		var request = getRequestObject();
		
		request.onreadystatechange = (function() {
			if ((request.readyState == 4) && (request.status == 200)) {
				this.options['onSuccess'](request);
			}
		}).bind(this);
			
		request.open(this.getMethod(), requestUrl, true);
		request.send(null);	
	}
});

function doOnReadyStateChange(request, onSuccess) {
	if ((request.readyState == 4) && (request.status == 200)) {
		onSuccess(request);
	}
}