$(document).ready(function() {
	
	var baseUrl = $("base").attr("href") + '/api';
		
	var dataSource = new JobApi(baseUrl);
	var renderer = new JobWidget();
	
	var dialogElement = $('#job-dialog');
	var dialog = new JobDialog(dialogElement, dataSource);

	var panelElement = $('#jobs-panel');		
	var jobsPanel = new JobPanel(panelElement, dataSource, dialog, renderer);	

	jobsPanel.refresh();
	
	$(document).trigger('not-busy');	
});