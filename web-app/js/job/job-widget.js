var JobWidget = Widget.$extend({
	
	__init__ : function(element) {		
		this.$super(element);
	},
	
	render : function(job) {		
		this.clone();		
		this.setRestId(job.restId);
		this.setDisplayName(job.displayName);
		this.setStatus(job.lastEvent);
		this.setTheme(job.theme);
		this.pruneChannels(job.channels);
		this.show();
	},
	
	getStatus : function() {		
		return this.element.data('status');		
	},
	
	setStatus : function(status) {
		var oldStatus = this.element.data('status');
		
		if (oldStatus) {
			this.element.removeClass(oldStatus);
		}
		
		if (status) {
			this.element.data('status', status.toLowerCase());
			this.element.addClass(status.toLowerCase());			
		}
	},
	
	setDisplayName : function(displayName) {
		$('.name .text', this.element).text(displayName);
	},
	
	setTheme : function(theme) {
		var themeElement = $('.theme .text', this.element);
		if (theme == null || theme == '') {
			themeElement.parent().hide();
		} else {
			themeElement.text(theme.name);	
		}		
	},
	
	pruneChannels : function(channels) {
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
})
