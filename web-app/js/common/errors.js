$(document).ready(function() {
	$(document).ajaxError(function(event, xhr) {
		var renderers = { html: new BodyRenderer('ajax-error-page', 'error status-' + xhr.status), json: new AlertRenderer() }
		var errorHandler = new AjaxErrorHandler(renderers);
		errorHandler.handleError(event, xhr);
		$(document).trigger('not-busy');
	});		
});

function AjaxErrorHandler(renderers) {
	
	this.xhr = null;
	this.event = null;
	this.renderers = renderers;
	this.matchAfterSlashAndBeforeSemi = /\/(.*);/;
	
	this.handleError = function(event, xhr) {
		
		this.event = event;
		this.xhr = xhr;
		
		var name = this.getRendererName();
		var renderer = this.getRenderer(name);
		if (renderer) {
			renderer.render(this.xhr.responseText, 'errors');
		}
	};
	
	this.getContentType = function() {
		return this.xhr.getResponseHeader('Content-Type');
	}
	
	this.getRendererName = function() {
		var contentType = this.getContentType();
		var match = this.matchAfterSlashAndBeforeSemi.exec(contentType);
		return match[1];
	}
	
	this.getRenderer = function(name) {
		return this.renderers[name];
	}	
}

function AlertRenderer() {
	
	this.render = function(content) {
		try {
			var messages = $.parseJSON(content);
			$.each(messages, function(index, message) {
				alert(message);			
			});
		} catch (err) {
			alert("An unexpected error occured");
		}
	}
}

function BodyRenderer(id, classNames) {
	this.id = id;
	this.render = function(content) {
		var body = new BodyExtractor(content).extractBody();
		$('body').html(body);
		$('body').attr('id', id);
		$('body').addClass(classNames);
	}
}

function BodyExtractor(html) {
	
	this.html = html;
	this.upToAndIncludingBodyTag = /[\s\S]*<body.*>/;
	this.afterAndIncludingBodyTag = /<\/body>[\s\S].*/;
	
	this.extractBody = function() {
		this.removePreBodyHtml();
		this.removePostBodyHtml();
		return this.html;
	}
	
	this.removePreBodyHtml = function() {
		this.html = this.html.replace(this.upToAndIncludingBodyTag, '<body>');
	}
	
	this.removePostBodyHtml = function() {
		this.html = this.html.replace(this.afterAndIncludingBodyTag, '</body>');
	}
}