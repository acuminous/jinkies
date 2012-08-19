function JobStatusChecker(element, dataSource) {
	
	this.element = element;
	this.dataSource = dataSource;
	
	this.poll = function(interval) {
		var _this = this;
		setInterval(function() { 
			_this.run.call(_this) 
		}, interval);	
	}
	
	this.bind = function(event) {
		var _this = this;
		$(document).on(event, function() { 
			_this.run.call(_this); 
		});
	}
	
	this.run = function() {
		var widgets = this.getJobWidgets();
		$.each(widgets, function(index, widget) {
			var restId = widget.getRestId();
			dataSource.checkStatus(restId, function(data) {
				var status = data.length > 0 ? data[0].type.name : ''
				widget.setStatus(status);
			});
			widget.setStatus(status);
		});
	}
	
	this.getJobWidgets = function() {
		var elements = $('.widget').not('.prototype, .fake');
		var widgets = $.map(elements, function(element) {
			return new JobWidget(element);
		});
		return widgets;
	}
}