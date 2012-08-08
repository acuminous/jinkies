function Widget(element) {	
	this.element = element;
}

Widget.prototype.clone = function() {
	this.element = $('.widget.prototype').clone();	
	$('.widget').last().after(this.element);
}	

Widget.prototype.getRestId = function() {
	return $('input[name=restId]', this.element).val();
}

Widget.prototype.setRestId = function(restId) {
	$('input[name=restId]', this.element).val(restId);
}

Widget.prototype.getPrimaryField = function() {
	return $('.primary .text', this.element).text();	
}

Widget.prototype.show = function() {
	this.element.removeClass('prototype');				
}
