JobWidget.prototype = new Widget();
JobWidget.prototype.constructor = JobWidget;

function JobWidget(element) {
	
	this.element = element;
	
	this.render = function(job) {		
		this.clone();		
		this.setRestId(job.restId);
		this.setDisplayName(job.displayName);
		this.setTheme(job.theme);
		this.pruneChannels(job.channels);
		this.show();
	}
	
	this.setStatus = function(status) {
		var statusClass = status.toLowerCase();		
		var displayNameText = $('.name .text', this.element);
		
		if (!displayNameText.hasClass(statusClass)) {
			displayNameText.attr('class', 'text ' + statusClass);
		}
	}
	
	this.setDisplayName = function(displayName) {
		$('.name .text', this.element).text(displayName);
	}
	
	this.setTheme = function(theme) {
		var themeElement = $('.theme .text', this.element);
		if (theme == null || theme == '') {
			themeElement.parent().hide();
		} else {
			themeElement.text(theme.name);	
		}		
	}
	
	this.pruneChannels = function(channels) {
		$('.channel', this.element).each(function(i, channelElement) {		
			var hasChannel = false;
			$.each(channels, function(j, channel) {
				if ($(channelElement).hasClass(channel)) {
					hasChannel = true;
				}
			})
			if (!hasChannel) {
				$(channelElement).remove();
			}
		});			
	}
}