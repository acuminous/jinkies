function JobApi() {
	
	this.list = function() {
		
		var results;
		
		$.get('/api/job', function(jobs) {
			results = jobs;
		});	
		
		return results;
	}
	
	this.get = function(restId) {
		
		var results;
		
		$.get('/api/' + restId, function(job) {
			results = job;
		})
		
		return results;
	}
	
	this.create = function(job) {
		
		var response = null;
		
		$.ajax('/api/job', {
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
		
		$.ajax('/api/' + job.restId, {
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
		$.ajax('/api/' + restId, { 
			type: 'DELETE' 
		})
	}
	
	this.checkStatus = function(restId, onSuccess) {
		$.ajax('/api/event', {
			type: 'GET',
			data: { target: restId },
			async: true,
			success: onSuccess			
		});
	}
}