package uk.co.acuminous.jinkies.content

import uk.co.acuminous.jinkies.event.ChainedEventHandler
import groovy.text.SimpleTemplateEngine


class SimpleTemplateRenderer {

	@Override
	String render(String template, Map binding) {
		
		SimpleTemplateEngine engine = new SimpleTemplateEngine()		
		template = engine.createTemplate(template).make(binding)
		template.toString()

	}
	
}
