ContentWidget.prototype = new Widget();
ContentWidget.prototype.constructor = ContentWidget;

function ContentWidget(element) {
	
	this.element = element;
	
	this.render = function(content) {	
		this.clone();		
		this.setRestId(content.restId);
		this.setTitle(content.title);
		this.setPreviewLink(content.dataRestId, content.type);
		this.setDescription(content.description);
		this.setType(content.type);
		this.show();
	}
	
	this.setTitle = function(title) {
		$('.title .text', this.element).text(title);
	}
	
	this.setPreviewLink = function(dataRestId, windowName) {
		$('.play a', this.element).attr('href', '/api/' + dataRestId);
		$('.play a', this.element).attr('target', windowName);
	}
	
	this.setDescription = function(description) {
		$('.title .text', this.element).attr('title', description);
	}	
	
	this.setType = function(type) {
		$('.type .text', this.element).text(type ? type : '???');
	}
	
	this.show = function() {
		this.element.removeClass('prototype');				
	}
}
