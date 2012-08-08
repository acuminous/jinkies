function WidgetPanel(element, dataSource, dialog, renderer) {
	
	this.element = element;
	this.dataSource = dataSource;
	this.dialog = dialog;	
	this.renderer = renderer;
	
	this.refresh = function() {
		
		this.freezeMinHeight();
				
		$('.widget', this.element).not('.fake, .prototype').remove();
		
		var widgets = dataSource.list();
		var renderer = this.renderer;
			
		$.each(widgets, function(index, widget) {
			renderer.render(widget);
		});
		
		this.releaseMinHeight();
		
		$(document).trigger("refresh-complete");		
	}
	
	this.freezeMinHeight = function() {
		var currentHeight = this.element.css('height');
		this.element.css('min-height', currentHeight);
	}
	
	this.releaseMinHeight = function() {
		this.element.css('min-height', '');
	}
	
	this.add = function(target) {		
		this.dialog.show();		
	}
	
	this.edit = function(target) {
		var panel = this;
		$(document).trigger('busy').delay(100).queue(function() {
			var restId = new Widget(target).getRestId();		
			panel.dialog.show(restId);	
			$(document).trigger('not-busy');
			$(this).dequeue();										
		});
	}
	
	this.confirmErasure = function(target) {
		var name = new Widget(target).getPrimaryField();
		var message = "Are you sure you want to delete '" + name + "'?";		
		return confirm(message);		
	}
	
	this.erase = function(target) {
		if (this.confirmErasure(target)) {
			var panel = this;
			$(document).trigger('busy').delay(100).queue(function() {
				var restId = new Widget(target).getRestId();
				panel.dataSource.erase(restId);
				panel.element.trigger('refresh');
				$(document).trigger('not-busy');
				$(this).dequeue();							
			});
		}
	}

	this.bindEventHandlers = function(panel) {
						
		$(".widget.add", this.element).on("click", function(event) {
			event.stopPropagation();			
			var target = $(event.target).closest('.widget');
			panel.add(target);
		});	
		
		this.element.on("click", ".widget.edit", function(event) {
			event.stopPropagation();	
			var target = $(event.target).closest('.widget');								
			panel.edit(target);
		});		
		
		this.element.on("click", ".widget .delete", function(event) {
			event.stopPropagation();
			var target = $(event.target).closest('.widget');
			panel.erase(target);
		});			
		
		this.element.on("click", ".widget .circular-button a", function(event) {
			event.stopPropagation();
		});
	
		$(document).on("refresh", function(event) {
			event.stopPropagation();
			panel.refresh();
		});
		
		$(document).on("busy", function(event) {			
			$('body').addClass('busy');
		});
		
		$(document).on("not-busy", function(event) {
			$('body').removeClass('busy');
		});		
	}
	
	this.bindEventHandlers(this);
}