function JobApi(baseUrl) {
	
	this.baseUrl = baseUrl;	
	
	this.list = function() {
		
		var results;
		var url = this.baseUrl + '/job'; 
		
		$.get(url, function(jobs) {
			results = jobs;
		});	
		
		return results;
	}
	
	this.get = function(restId) {
		
		var results;
		var url = this.baseUrl + '/' + restId;
		
		$.get(url, function(job) {
			results = job;
		})
		
		return results;
	}
	
	this.create = function(job) {
		
		var response = null;
		var url = this.baseUrl + '/job';
		
		$.ajax(url, {
			type: 'POST',
			processData: false,
			contentType: 'application/json',
			data: JSON.stringify({  
				url: job.url, 
				displayName: job.displayName, 
				type: job.type, 
				theme: job.theme, 
				channel: job.channels 
			}),
			success: function(data, status, xhr){
				response = data;
			}			
		});		
		
		return response;
	}
	
	this.update = function(job) {
		
		var response = null;
		var url = this.baseUrl + '/' + job.restId;
		
		$.ajax(url, {
			type: 'PUT',
			processData: false,
			contentType: 'application/json',
			data: JSON.stringify({ 
				id: job.id, 
				url: job.url, 
				displayName: job.displayName, 
				type: job.type, 
				theme: job.theme, 
				channel: job.channels 
			}),
			success: function(data, status, xhr){
				response = data;
			}			
		});		
		
		return response;	
	}
	
	this.erase = function(restId) {
		
		var url = this.baseUrl + '/' + restId;
		
		$.ajax(url, { 
			type: 'DELETE' 
		})
	}
	
	this.listJenkinsJobs = function() {
		
		var results;				
		var url = this.baseUrl + '/jenkins';
			
		$.get(url, { url: this.getCiServerUrl() }, function(jobs) {
	    	results = jobs;
		});					
		
		return results;				
	}		
	
	this.checkStatus = function(restId, onSuccess) {
		
		var url = this.baseUrl + '/event';
		
		$.ajax(url, {
			type: 'GET',
			data: { target: restId },
			async: true,
			success: onSuccess			
		});
	}
}