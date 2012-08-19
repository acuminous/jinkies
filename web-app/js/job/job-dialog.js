JobDialog.prototype = new Dialog();
JobDialog.prototype.constructor = JobDialog;
JobDialog.prototype.parent = Dialog.prototype;

function JobDialog(element, dataSource) {

	this.element = element;
	this.dataSource = dataSource;
	
	this.setDisplayName = function(displayName) {
		$('#displayNameInTitle').text(displayName);
	}
	
	this.getCiServerUrl = function() {
		var url = $('#serverUrl', this.element).val();
		return this.ensureScheme(url);
	}
	
	this.setCiServerUrl = function(url) {
		var serverUrl = $('#serverUrl', this.element)
		$('#serverUrl', this.element).val(url);
		
		var serverUrlLink = $('#serverUrl', this.element).siblings('a');
		serverUrlLink.attr('href', url);
		$('.text', serverUrlLink).text(url);
	}	
		
	this.getTheme = function() {
		return $('#theme', this.element).val();
	}
	
	this.setTheme = function(theme) {
		$('#theme', this.element).val(theme ? theme.name : '');
	}
	
	this.getChannels = function() {
		var channels = [];
		$('input[name=channel]', this.element).each(function(index, channel) {
			if ($(channel).val() != "") {
				channels[channels.length] = $(channel).val();
			}
		})		
		return channels;
	}
	
	this.setChannels = function(channels) {		
		$('input.channel', this.element).attr('checked', false);
		$.each(channels, function(index, channel) {
			$('input[name=' + channel + ']', this.element).attr('checked', true);
		});
	}

	this.toggleChannel = function(checkbox) {
		var backingField = $('#' + checkbox.attr('name'), this.element);
		var value = checkbox.is(':checked') ? backingField.attr('id') : '';			
		backingField.val(value);
	}		
	
	this.populateForm = function() {
		this.setDisplayName(this.bean.displayName);
		this.setCiServerUrl(this.bean.url);
		this.setTheme(this.bean.theme);
		this.setChannels(this.bean.channels);
	}
	
	this.clearForm = function() {		
		this.setCiServerUrl('');
		this.setTheme('');
		this.setChannels(['audio']);		
	}
		
	this.getDataToSave = function() {
		var data;
		if (this.mode == 'create') {
			var ciServerUrl = this.getCiServerUrl();
			data = this.dataSource.listCiServerJobs(ciServerUrl);
		} else {
			data = [ this.bean ];			
		}		
		return data;
	}	
	
	this.save = function(jobs) {
		
		var dialog = this;
		var theme = this.getTheme();
		var channels = this.getChannels();
		var dataSource = this.dataSource;

		var response = null;
		
		$.each(jobs, function(index, job) {
			
			job.theme = theme;
			job.channels = channels;
			
			if (job.restId) {				
				response = dataSource.update(job);
			} else {				
				response = dataSource.create(job);					
			}
			
			return response != null
		});
		
		return response != null;
	}
	
	this.bindEventHandlers = function(dialog) {
		
		this.parent.bindEventHandlers.call(this, dialog);
		
		$(document).on('click', '.dialog input[type=checkbox].channel', function(event) {
			event.stopPropagation(); // using preventDefault prevents the checkbox from appearing to be selected			
			var checkbox = $(event.target);
			dialog.toggleChannel(checkbox);			
		});
		
	}		
	
	this.bindEventHandlers(this);
}



