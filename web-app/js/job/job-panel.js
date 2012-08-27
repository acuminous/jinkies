var JobPanel = WidgetPanel.$extend({
	
	__init__ : function(element, dataSource, dialog, renderer) {
		
		this.$super(element, dataSource, dialog, renderer);	
		this.filters = ['all', 'failure', 'error'];
		this.currentFilterName = 'all';
	},		
	
	applyStatusFilter: function(status) {
		
		this.getRealWidgets().each(function() {
			var widget = new JobWidget($(this));
			
			if (widget.getStatus() == status) {
				widget.filterIn();
			} else {
				widget.filterOut();				
			}
		})		
	},
	
	bindEventHandlers: function() {
		
		this.$super();
		
		var panel = this;
		
		this.element.on('filter', function(event, name) {
			if (name == 'all') {
				panel.clearFilter();
			} else {
				panel.applyStatusFilter(name);
			}
		});		
	}
	
});
