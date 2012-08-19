var ContentDialog = Dialog.$extend({

	__init__ : function(element, dataSource, file) {
		this.$super(element, dataSource);
		this.file = null;		
	},
	
	initialiseFileUpload : function() {
		var dialog = this;		
		this.element.fileupload({			
	        dataType: 'json',
	        type: 'POST',
	        add: function (e, data) {
                dialog.file = data;
                dialog.setFilename(data.files[0].name);
            }
	    });			
	},
	
	getUploadMethod : function() {
		return $('input[name=uploadMethod]:checked').val();
	},
	
	setUploadMethod : function(url) {
		var radioButton;
		if (url) {
			radioButton = $('#urlUploadMethod', this.element);			
		} else {
			radioButton = $('#fileUploadMethod', this.element);
		}
		
		// Clicking causes the event handler to fire and
		// results in the correct input element being displayed
		radioButton.click();  
	},
	
	uploadFromUrl : function() {
		return $('#urlUploadMethod', this.element).is(':checked');
	},
	
	uploadFromFile : function() {
		return $('#fileUploadMethod', this.element).is(':checked');
	},	
	
	getTitle : function() {
		return $('#title', this.element).val();
	},
	
	setTitle : function(title) {		
		$('#title', this.element).val(title);
		$('#titleInHeading', this.element).text(title);
	},

	getFilename : function() {
		return $('#filename', this.element).val();
	},
	
	setFilename : function(filename) {		
		$('#fileButton', this.element).siblings('.text').text(filename ? filename : '');		
		$('#filename', this.element).val(filename);
	},
	
	getContentUrl : function() {				
		var url = $('#contentUrl', this.element).val();
		return this.ensureScheme(url);
	},
	
	setContentUrl : function(url) {
		$('#contentUrl', this.element).val(url);
	},	
	
	getDescription : function() {
		return $('#description', this.element).val();
	},
	
	setDescription : function(description) {
		$('#description', this.element).val(description);
	},
		
	getThemes : function() {
		var text = $('#themes', this.element).val();
		return this.splitCommaSeparatedList(text);
	},
	
	setThemes : function(themes) {
		var themeNames = $.map(themes, function(theme) {
			return theme.name;
		});
		var text = themeNames.join(', ');
		$('#themes', this.element).val(text);
	},
	
	getEvents : function() {
		var specialEvents = this.getSpecialEvents();
		var otherEvents = this.getOtherEvents();
		return $.merge(specialEvents, otherEvents);
	},

	getSpecialEvents : function() {
		var events = [];
		var checkboxes = $('input[name=event]:checked', this.element);
		$.each(checkboxes, function(index, checkbox) {
			events[events.length] = $(checkbox).val();
		});
		return events;
	},
	
	getOtherEvents : function() {
		var text = $('#otherEvents', this.element).val();
		return this.splitCommaSeparatedList(text);
	},	
	
	
	setEvents : function(events) {

		this.clearEvents();
		
		var eventNames = $.map(events, function(event) {
			return event.name;
		});
		
		var otherEvents = [];
				
		$(eventNames).each(function(index, name) {
			
			var checkbox = $('input[name=event][value=' + name + ']', this.element);
			
			if (checkbox.length) {
				checkbox.attr('checked', true)
			} else {
				otherEvents[otherEvents.length] = name;
			}
		})
		var textbox = $('#otherEvents', this.element);
		textbox.val(otherEvents.join(', '))
	},
	
	clearEvents : function() {
		$('input[name=event]', this.element).each(function(index, checkbox) {
			$(checkbox).attr('checked', false);
		});
	},
	
	
	show : function(restId) {
		this.initialiseFileUpload();
		this.$super(restId);
	},
	
	populateForm : function() {
		this.setUploadMethod(this.bean.url)
		this.setTitle(this.bean.title);
		this.setFilename(this.bean.filename);
		this.setContentUrl(this.bean.url);		
		this.setDescription(this.bean.description);
		this.setThemes(this.bean.themes);
		this.setEvents(this.bean.events);
		this.file = null;		
	},
	
	clearForm : function() {		
		this.setTitle('');
		this.setContentUrl('');				
		this.setDescription('');
		this.setFilename('');		
		this.setThemes([]);
		this.setEvents([]);
		this.file = null;
	},
			
	getDataToSave : function() {
		if (this.mode == 'create') {
			return {};
		} else {
			return this.bean;
		}
	},	
	
	save : function(content) {
		
		/* File Content is saved in two steps, attributes (e.g. title, description) first,
		 * then the file is uploaded using a separate request. I did this because...
		 * 
		 *    a. Either my browser(s), the fileUpload plugin or Grails don't handle 
		 *       multipart PUT requests properly
		 *       
		 *    b. Attempting to diagnose + fix would have been very time consuming
		 *    
		 *    c. While I could have successfully used POST (instead of PUT) for 
		 *       updates, this would have broken RESTful principles
		 *       and meant mixing create and update behaviour using the same "action" 
		 *       on the server side.
		 *       
		 * Web Content is retrieved by the server and so only requires on step
		 */ 		
		
		content.title = this.getTitle();
		content.uploadMethod = this.getUploadMethod();
		content.url = this.getContentUrl();		
		content.filename = this.getFilename();
		content.description = this.getDescription();
		content.themes = this.getThemes();
		content.events = this.getEvents();
		
		var response;
		
		if (content.restId) {				
			response = this.dataSource.update(content);
		} else {				
			response = this.dataSource.create(content);
		}
		
		if (response && this.uploadFromFile() && this.file) {
			this.uploadFile('/api/' + response.dataRestId);
		}
		
		return response != null;
	},
	
	uploadFile : function(url) {
		this.element.fileupload('option', 'url', url);
		this.file.submit();				
	},
	
	hideInputMethods : function() {
		var rows = $('.form-row', this.element).has('#filename, #contentUrl');
		rows.each(function(index, row) {
			$(row).hide();
		})
	},
	
	showInputMethod : function(radioValue) {
		var selector = radioValue == 'url' ? '#contentUrl' : '#file'
		var row = $('.form-row', this.element).has(selector);
		row.show();
		row.next('.form-tip').show();
	},
	
	openDialogBox : function() {
		this.element.modal({
			persist: true, 
			onOpen: this.onOpen,
			onClose: this.onClose,
			focus: true
		});
	},	
	
	bindEventHandlers : function() {
		
		this.$super();
		
		var dialog = this;		
		
		$('#fileButton', this.element).on('click', function(event) {
			var fileElement = $(event.target).siblings('#file');
			fileElement.click();
			
		});
		
		$('input[name=uploadMethod]', this.element).on('change', function(event) {
			var radioButton = $(event.target);
			dialog.hideInputMethods();
			dialog.showInputMethod(radioButton.val());
		});	
		
		$('#otherEvents', this.element).on('click', function(event) {
			$('#otherEventsTextbox').focus();
		});				
	}
	
});



