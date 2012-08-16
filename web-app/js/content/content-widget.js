ContentWidget.prototype = new Widget();
ContentWidget.prototype.constructor = ContentWidget;

function ContentWidget(element) {
	
	this.element = element;
	
	this.render = function(content) {	
		this.clone();		
		this.setRestId(content.restId);
		this.setTitle(content.title);
		this.setPreviewLink(content.dataRestId, content.dataHashCode, content.type);
		this.setDescription(content.description);
		this.setThemes(content.themes);
		this.show();
	}
	
	this.setTitle = function(title) {
		$('.title .text', this.element).text(title);
	}
	
	this.setPreviewLink = function(dataRestId, dataHashCode, windowName) {
		$('.play a', this.element).attr('href', '/api/' + dataRestId + '?' + dataHashCode);
		$('.play a', this.element).attr('target', windowName);
	}
	
	this.setDescription = function(description) {
		$('.title .text', this.element).attr('title', description);
	}	
		
	this.setThemes = function(themes) {
		var themeElement = $('.theme .text', this.element);
				
		if (themes == null || themes.length == 0) {
			themeElement.parent().hide();
		} else {
			var themeNames = $.map(themes, function(theme) {
				return theme.name;
			});
			
			themeElement.text(themeNames.join(', '));	
		}		
	}
	
	this.show = function() {
		this.element.removeClass('prototype');				
	}
}
