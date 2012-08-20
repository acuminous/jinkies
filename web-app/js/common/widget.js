var Widget = Class.$extend({

	__init__ : function(element) {
		this.element = element;		
	},
	
	clone : function() {
		this.element = $('.widget.prototype').clone();	
		$('.widget').last().after(this.element);
	},	

	getRestId : function() {
		return $('input[name=restId]', this.element).val();
	},

	setRestId : function(restId) {
		$('input[name=restId]', this.element).val(restId);
	},

	getPrimaryField : function() {
		return $('.primary .text', this.element).text();	
	},

	show : function() {
		this.element.removeClass('prototype');				
	}
	
});
