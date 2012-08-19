var ContentPanel = WidgetPanel.$extend({

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
	
});



