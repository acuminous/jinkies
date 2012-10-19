var JobStatusChecker = Class.$extend({

	__init__ : function(element, dataSource) {
		this.element = element;
		this.dataSource = dataSource;
	},
	
	poll : function(interval) {
		var _this = this;
		setInterval(function() { 
			_this.run.call(_this) 
		}, interval);	
	},
	
	run : function() {
		this.dataSource.checkStatus(function(data) {
			_this = this;
			$.each(data, function(index, job) {
				var resourceIdElement = $('input[name=resourceId][value="' + job.resourceId + '"]', _this.element);
				var widgetElement = resourceIdElement.parent('.job.widget');
				var widget = new JobWidget(widgetElement);
				widget.setStatus(job.status);
			});
		});
	},

	getJobWidgets : function() {
		var elements = $('.widget').not('.prototype, .fake');
		var widgets = $.map(elements, function(element) {
			return new JobWidget(element);
		});
		return widgets;
	}	
})
