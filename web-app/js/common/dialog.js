var Dialog = Class.$extend({
	
	__init__ : function(element, dataSource) {
				
		this.element = element;
		this.dataSource = dataSource;
				
		this.bindEventHandlers();		
	},
	
	bindEventHandlers : function() {
		
		var dialog = this;		
		
		$('input.ok', this.element).on('click', function(event) {
			event.stopPropagation();
			
			$(document).trigger('busy').delay(100).queue(function() {
				dialog.ok();
				$(document).trigger('not-busy');;
				$(document).dequeue();			
			});
		});	
		
		$('input.cancel', this.element).on('click', function(event) {
			event.stopPropagation();		
			dialog.close();
		});
		
		$('input.ok, input.cancel', this.element).on('focusin', function(event) {
			event.stopPropagation();		
			$(event.target).addClass('hover');			
		});
		
		$('input.ok, input.cancel', this.element).on('focusout', function(event) {
			event.stopPropagation();		
			$(event.target).removeClass('hover');			
		});		
	}, 
	
	ok : function() {

		var data = this.getDataToSave();
		
		if (data && this.save(data)) {
			this.notifyListeners();
			this.close();
		}
	}, 
	
	notifyListeners : function() {
		this.element.trigger('refresh');		
	},

	close : function() {
		$.modal.close();		
	},	

	show : function(resourceId) {
	
		if (resourceId) {
			this.enableUpdateMode();
			this.getBean(resourceId);			
			this.populateForm();
		} else {
			this.enableCreateMode();
			this.clearBean();
			this.clearForm();
		}
		
		this.openDialogBox();
	},
	
	openDialogBox : function() {
		this.element.modal({
			persist: true, 
			onOpen: this.onOpen,
			onClose: this.onClose,
			focus: false
		});
	},
	
	enableCreateMode : function() {
		this.mode = 'create';
		this.element.removeClass('updateMode');
		this.element.addClass('createMode');
	},
		
	enableUpdateMode : function() {
		this.mode = 'update';
		this.element.addClass('updateMode');
		this.element.removeClass('createMode');
	},
	
	getBean : function(resourceId) {
		this.bean = this.dataSource.get(resourceId);
	},
	
	clearBean : function() {
		this.bean = null;
	},	
	
	ensureScheme : function(url) {
		var missingTheme = url.length > 0 && !url.match(/^\w+:\/\//);
		return missingTheme ? 'http://' + url : url;
	},
	
	splitCommaSeparatedList : function(text) {
		var items = [];
		if (text) {
			$.each(text.split(/\s*,\s*/), function(index, item) {
				items[items.length] = $.trim(item);
			})
		}
		return items;
	}
});