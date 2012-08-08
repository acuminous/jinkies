function Dialog() {
	this.bindEventHandlers();
}

Dialog.prototype.bindEventHandlers = function() {
	
	$('input.ok', this.element).on('click', function(event) {
		event.stopPropagation();
		
		$(document).trigger('busy').delay(100).queue(function() {
			Dialog.prototype.activeDialog.ok();
			$(document).trigger('not-busy');;
			$(document).dequeue();			
		});
	});	
	
	$('input.cancel', this.element).on('click', function(event) {
		event.stopPropagation();		
		Dialog.prototype.activeDialog.close();
	});
	
	$('input.ok, input.cancel', this.element).on('focusin', function(event) {
		event.stopPropagation();		
		$(event.target).addClass('hover');			
	});
	
	$('input.ok, input.cancel', this.element).on('focusout', function(event) {
		event.stopPropagation();		
		$(event.target).removeClass('hover');			
	});		
	
	Dialog.prototype.bindEventHandlers = $.noop;	
	
}	

Dialog.prototype.setActiveDialog = function(dialog) {
	Dialog.prototype.activeDialog = dialog;
}

Dialog.prototype.ok = function() {
	var data = this.getDataToSave();
	if (data && this.save(data)) {
		this.notifyListeners();
		this.close();
	}
}

Dialog.prototype.notifyListeners = function() {
	this.element.trigger('refresh');		
}

Dialog.prototype.close = function() {
	$.modal.close();		
}	

Dialog.prototype.show = function(restId) {
	
	this.setActiveDialog(this);	
	
	if (restId) {
		this.enableUpdateMode();
		this.getBean(restId);			
		this.populateForm();
	} else {
		this.enableCreateMode();
		this.clearBean();
		this.clearForm();
	}
	
	this.openDialogBox();
}

Dialog.prototype.openDialogBox = function() {
	this.element.modal({
		persist: true, 
		onOpen: this.onOpen,
		onClose: this.onClose,
		focus: false
	});
}

Dialog.prototype.enableCreateMode = function() {
	this.mode = 'create';
	this.element.removeClass('updateMode');
	this.element.addClass('createMode');
}
	
Dialog.prototype.enableUpdateMode = function() {
	this.mode = 'update';
	this.element.addClass('updateMode');
	this.element.removeClass('createMode');
}

Dialog.prototype.getBean = function(restId) {
	this.bean = this.dataSource.get(restId);
}

Dialog.prototype.clearBean = function() {
	this.bean = null;
}		

Dialog.prototype.ensureScheme = function(url) {
	var missingTheme = url.length > 0 && !url.match(/^\w+:\/\//);
	return missingTheme ? 'http://' + url : url;
}

Dialog.prototype.splitCommaSeparatedList = function(text) {
	var items = [];
	if (text) {
		$.each(text.split(/\s*,\s*/), function(index, item) {
			items[items.length] = $.trim(item);
		})
	}
	return items;
}
