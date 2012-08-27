var WidgetPanel = Class.$extend({
	
	__init__ : function(element, dataSource, dialog, renderer) {
	
		this.element = element;
		this.dataSource = dataSource;
		this.dialog = dialog;	
		this.renderer = renderer;
		this.filters = [];		
		this.currentFilterName;

		
		this.bindEventHandlers();
	},
	
	refresh : function() {
		
		this.freezeMinHeight();		
		this.clear();		
		var widgets = this.getWidgets();		
		this.render(widgets);		
		this.triggerFilter();				
		this.releaseMinHeight();
		
		$(document).trigger("refresh-complete");		
	},
	
	getRealWidgets : function() {
		return $('.widget', this.element).not('.fake, .prototype');
	},
	
	clear : function() {
		this.getRealWidgets().remove();			
	},
	
	getWidgets : function() {
		var widgets = this.dataSource.list();
		return this.sort(widgets);		
	}, 
	
	sort : function(widgets) {
		return widgets;
	},
	
	render : function(widgets) {
		var renderer = this.renderer;		
		$.each(widgets, function(index, widget) {
			renderer.render(widget);
		});				
	},
	
	freezeMinHeight : function() {
		var currentHeight = this.element.css('height');
		this.element.css('min-height', currentHeight);
	},
	
	releaseMinHeight : function() {
		this.element.css('min-height', '');
	},
	
	add : function(target) {		
		this.dialog.show();		
	},
	
	edit : function(target) {
		var panel = this;
		$(document).trigger('busy').delay(100).queue(function() {
			var restId = new Widget(target).getRestId();		
			panel.dialog.show(restId);	
			$(document).trigger('not-busy');
			$(this).dequeue();										
		});
	},
	
	confirmErasure : function(target) {
		var name = new Widget(target).getPrimaryField();
		var message = "Are you sure you want to delete '" + name + "'?";		
		return confirm(message);		
	},
	
	erase : function(target) {
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
	},
		
	applyNextFilter: function() {
		if (this.filters.length > 1) {
			this.nextFilter();
			this.renderFilterName();
			this.triggerFilter();
		}
	},
	
	nextFilter: function() {
		var currentIndex = $.inArray(this.currentFilterName, this.filters);
		if (currentIndex == -1) {
			this.currentFilterName = this.filters[0];
		} else {
			var newIndex = (currentIndex + 1) % this.filters.length;
			this.currentFilterName = this.filters[newIndex];
		}
	},
	
	renderFilterName: function() {
		var filterNameElement = $('#filter-name', this.element);
		var displayName = this.getCurrentFilterDisplayName();
		$(filterNameElement).text(displayName);
	},
	
	triggerFilter: function() {
		if (this.filters.length > 0) {			
			this.element.trigger('filter', this.currentFilterName);
		}
	},
	
	getCurrentFilterDisplayName: function() {
		var filterNameElement = $('#filter-name', this.element);
		var displayName = filterNameElement.data(this.currentFilterName);
		return displayName
	},	
	
	clearFilter: function() {
		this.getRealWidgets().each(function() {
			$(this).removeClass('filter');
		})
	},

	bindEventHandlers : function() {
						
		var panel = this;
		
		$('#refresh', this.element).on("click", function(event) {
			event.stopPropagation();
			panel.refresh();
		});
		
		$("#filter", this.element).on("click", function(event) {
			event.stopPropagation();
			panel.applyNextFilter();
		});
		
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
})