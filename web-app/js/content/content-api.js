function ContentApi() {
	
	this.list = function() {
		
		var results;
		
		$.get('/api/content', function(content) {
			results = content;
		});	
		
		return results;
	}
	
	this.get = function(restId) {
		
		var response = null;
		
		$.get('/api/' + restId, function(content) {
			response = content;
		})
	
		return response;
	}
	
	this.create = function(content) {
		
		var response = null;
						
		$.ajax('/api/content', {
			type: 'POST',
			processData: false,
			contentType: 'application/json',
			data: JSON.stringify({ 
				title: content.title,
				uploadMethod: content.uploadMethod,
				filename: content.filename,
				url: content.url,
				description: content.description,
				theme: content.themes,
				event: content.events
			}),
			success: function(data, status, xhr){
				response = data;
			}
		});			
		
		return response;
	}
	
	this.update = function(content) {
		
		var response = null;
				
		$.ajax('/api/' + content.restId, {
			type: 'PUT',
			processData: false,
			contentType: 'application/json',
			data: JSON.stringify({ 
				id: content.id, 
				title: content.title,
				uploadMethod: content.uploadMethod,
				filename: content.filename,
				url: content.url,				
				description: content.description, 
				theme: content.themes, 
				event: content.events
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
}