function ContentApi(baseUrl) {
	
	this.baseUrl = baseUrl;
		
	this.list = function() {
		
		var results;
		var url = this.baseUrl + '/content';
		
		$.get(url, function(content) {
			results = content;
		});	
		
		return results;
	}
	
	this.get = function(restId) {
		
		var response = null;		
		var url = this.baseUrl + '/' + restId;
		
		$.get(url, function(content) {
			response = content;
		})
	
		return response;
	}
	
	this.create = function(content) {
		
		var response = null;
		var url = this.baseUrl + '/content';
						
		$.ajax(url, {
			type: 'POST',
			processData: false,
			contentType: 'application/json',
			data: JSON.stringify({ 
				title: content.title,
				uploadMethod: content.uploadMethod,
				filename: content.filename,
				url: content.url,
				text: content.text,
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
		var url = this.baseUrl + '/' + content.restId;
				
		$.ajax(url, {
			type: 'PUT',
			processData: false,
			contentType: 'application/json',
			data: JSON.stringify({ 
				id: content.id, 
				title: content.title,
				uploadMethod: content.uploadMethod,
				filename: content.filename,
				url: content.url,
				text: content.text,
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
		
		var url = this.baseUrl + '/' + restId;
		
		$.ajax(url, { 
			type: 'DELETE' 
		})
	}
	
	this.play = function(restId) {
	
		var url = this.baseUrl + '/event';
		
		$.ajax(url, {
			type: 'POST',
			async: true,
			processData: false,
			contentType: 'application/json',
			data: JSON.stringify({
				target: restId,
				type: 'Preview',
				channel: ['audio', 'speech', 'test'],
				content: [restId]
			})
		})		
	}
}