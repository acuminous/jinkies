$(document).ready(function() {
		
	var baseUrl = $("base").attr("href") + '/api';
	
	var dataSource = new ContentApi(baseUrl);
	var renderer = new ContentWidget();
	
	var dialogElement = $('#content-dialog');
	var dialog = new ContentDialog(dialogElement, dataSource);		
	
	var panelElement = $('#content-panel');		
	var contentPanel = new ContentPanel(panelElement, dataSource, dialog, renderer);	
	
	contentPanel.refresh();
	
	$(document).trigger('not-busy');
});