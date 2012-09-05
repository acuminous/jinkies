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
	
	this.get = function(resourceId) {
		
		var results;
		var url = this.baseUrl + '/' + resourceId;
		
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
		var url = this.baseUrl + '/' + job.resourceId;
		
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
	
	this.erase = function(resourceId) {
		
		var url = this.baseUrl + '/' + resourceId;
		
		$.ajax(url, { 
			type: 'DELETE' 
		})
	}
	
	this.listCiServerJobs = function(ciServerUrl) {
		
		var results;				
		var url = this.baseUrl + '/jenkins';
			
		$.get(url, { url: ciServerUrl }, function(jobs) {
	    	results = jobs;
		});					
		
		return results;				
	}		
	
	this.checkStatus = function(onSuccess) {
		
		var url = this.baseUrl + '/event';
		
		$.ajax(url, {
			type: 'GET',
			async: true,
			success: onSuccess			
		});
	}
}