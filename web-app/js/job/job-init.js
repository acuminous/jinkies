$(document).ready(function() {
	
	var baseUrl = $("base").attr("href") + '/api';
		
	var dataSource = new JobApi(baseUrl);
	var renderer = new JobWidget();
	
	var dialogElement = $('#job-dialog');
	var dialog = new JobDialog(dialogElement, dataSource);

	var panelElement = $('#jobs-panel');		
	var jobsPanel = new WidgetPanel(panelElement, dataSource, dialog, renderer);	

	var statusChecker = new JobStatusChecker(panelElement, dataSource);	
	statusChecker.poll(60000);
	statusChecker.bind('refresh-complete');
	
	jobsPanel.refresh();
	$(document).trigger('not-busy');	
});

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