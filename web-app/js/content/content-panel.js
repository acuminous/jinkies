var ContentPanel = WidgetPanel.$extend({

	__init__ : function(element, dataSource, dialog, renderer) {
		
		this.$super(element, dataSource, dialog, renderer);
		this.currentFilterName = 'all';
	},
	
	getWidgets : function() {		
		var widgets = this.$super()				
		this.createThemeFilters(widgets);				
		return widgets;		
	},	
	
	createThemeFilters : function(widgets) {
		var themes = this.getThemeNames(widgets);
		this.filters = this.createDynamicFilters(themes);				
	},
	
	getThemeNames : function(widgets) {
		
		var themes = [];		
		$.each(widgets, function(index, widget) {
			
			$.each(widget.themes, function(index, theme) {
				if (($.inArray(theme.name, themes)) == -1) {
					themes.push(theme.name)
				}				
			})
		});		
		return themes.sort();		
	},
	
	createDynamicFilters : function(displayNames) {
		
		var filters = ['all'];
		
		$.each(displayNames, function(index, displayName) {
			var filterName = 'filter-' + index;
			filters.push(filterName);
			$('#filter-name').data(filterName, displayName)
		});
		
		return filters;
	},
	
	applyThemeFilter: function(filterName) {
		
		var filterDisplayName = this.getCurrentFilterDisplayName();

		this.getRealWidgets().each(function() {
			var widget = new ContentWidget($(this));

			var themes = widget.getThemes().split(', ');
			
			if ($.inArray(filterDisplayName, themes) >= 0) {						
				widget.filterIn();
			} else {
				widget.filterOut();				
			}
		})				
	},	
	
	sort : function(widgets) {
		
		var panel = this;
		
		return widgets.sort(function(widget1, widget2) {
			
			var result = panel.compareTheme(widget1, widget2);
			if (result == 0) {
				result = panel.compareTitle(widget1, widget2);
			}
			
			return result;
		});
	},

	compareTheme : function(widget1, widget2) {
		var theme1 = widget1.themes[0];
		var theme2 = widget2.themes[0];			
		
		var themeName1 = theme1 == undefined ? '' : theme1.name
		var themeName2 = theme2 == undefined ? '' : theme2.name
		
		var result = themeName1 > themeName2 ? 1 : themeName1 < themeName2 ? -1 : 0;
		
		return result;
	},
	
	compareTitle : function(widget1, widget2) {
		var title1 = widget1.title;
		var title2 = widget2.title;		
		return title1 > title2 ? 1 : title1 < title2 ? -1 : 0;		
	},
	
		
	bindEventHandlers: function() {
		
		this.$super();
		
		var panel = this;
		
		this.element.on('filter', function(event, name) {
			if (name == 'all') {
				panel.clearFilter();
			} else {
				panel.applyThemeFilter(name);
			}
		});		
	}		
});



