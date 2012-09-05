$(document).ready(function() {
	
	var baseUrl = $("base").attr("href") + '/api';
		
	var dataSource = new JobApi(baseUrl);
	var renderer = new JobWidget();
	
	var dialogElement = $('#job-dialog');
	var dialog = new JobDialog(dialogElement, dataSource);

	var panelElement = $('#jobs-panel');		
	var jobsPanel = new JobPanel(panelElement, dataSource, dialog, renderer);	

	var statusChecker = new JobStatusChecker(panelElement, dataSource);	
	statusChecker.poll(5000);	
	
	jobsPanel.refresh();
	
	$(document).trigger('not-busy');	
});