var ContentWidget = Widget.$extend({

	__init__ : function(element, dataSource) {	
		this.$super(element);		
		this.dataSource = dataSource;
	},
	
	render : function(content) {	
		this.clone();		
		this.setRestId(content.restId);
		this.setTitle(content.title);
		this.setDescription(content.description);
		this.setThemes(content.themes);
		this.bindPlayHandler(content);
		this.show();
	},
	
	setTitle : function(title) {
		$('.title .text', this.element).text(title);
	},
	
	bindPlayHandler : function(content) {
		var widget = this;
		$('.play img', this.element).on('click', function(event) {
			event.stopPropagation();
			widget.play(content.restId);
		});
	},
	
	play : function(restId) {
		this.dataSource.play(restId);
	},
	
	setDescription : function(description) {
		$('.title .text', this.element).attr('title', description);
	},	
		
	getThemes : function() {
		return $('.theme .text', this.element).text();
	},
	
	setThemes : function(themes) {
		var themeElement = $('.theme .text', this.element);
				
		if (themes == null || themes.length == 0) {
			themeElement.parent().hide();
		} else {
			var themeNames = $.map(themes, function(theme) {
				return theme.name;
			});
			
			themeElement.text(themeNames.join(', '));	
		}		
	},
	
	show : function() {
		this.element.removeClass('prototype');				
	}
});
