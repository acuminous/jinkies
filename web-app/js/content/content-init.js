$(document).ready(function() {
		
	var dataSource = new ContentApi();
	var renderer = new ContentWidget();
	
	var dialogElement = $('#content-dialog');
	var dialog = new ContentDialog(dialogElement, dataSource);		
	
	var panelElement = $('#content-panel');		
	var contentPanel = new WidgetPanel(panelElement, dataSource, dialog, renderer);	
	
	contentPanel.refresh();
	
	$(document).trigger('not-busy');
});