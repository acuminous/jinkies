var Widget = Class.$extend({

	__init__ : function(element) {
		this.element = element;		
	},
	
	clone : function() {
		this.element = $('.widget.prototype').clone();	
		$('.widget').last().after(this.element);
	},	

	getResourceId : function() {
		return $('input[name=resourceId]', this.element).val();
	},

	setResourceId : function(resourceId) {
		$('input[name=resourceId]', this.element).val(resourceId);
	},

	getPrimaryField : function() {
		return $('.primary .text', this.element).text();	
	},

	show : function() {
		this.element.removeClass('prototype');				
	},
	
	filterIn : function() {
		this.element.removeClass('filter')
	},
	
	filterOut : function() {
		this.element.addClass('filter');
	}
	
});
