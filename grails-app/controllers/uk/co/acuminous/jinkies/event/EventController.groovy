package uk.co.acuminous.jinkies.event

import uk.co.acuminous.jinkies.content.*
import uk.co.acuminous.jinkies.util.JinkiesErrorRenderer
import static javax.servlet.http.HttpServletResponse.*
import grails.converters.JSON

// Mixins are currently working properly, therefore extending (yuck)
class EventController extends JinkiesErrorRenderer {

	EventHandler eventHandler
	EventHistory eventHistory

	/* "Listing" events doesn't make much sense since
	   the eventHistory only holds the most recent event for 
	   each target (usually a job). 
	
	   Since events don't have a unique id we can't 
	   simply GET them with a rest id like /api/event/123 
	   hence the following weird list implementation.
	*/
	def list() {
		Map event = eventHistory.get(params.target)
		List result = event ? [event] : []
		render(result as JSON)
	}
	
	def create(EventCommand cmd) {
		
		Map event = [:]
		
		event.target = cmd.target
		event.type = cmd.event
		event.theme = cmd.theme
		event.channels = cmd.channels
		
		eventHandler.handle(event)	
			
		report(cmd.errors) || render(status: SC_NO_CONTENT)		 
	}	
}

class EventCommand {
	
	String target
	List<String> channel
	
	static constraints = {
		target nullable: false, blank: false
		channel nullable: true
	}
	
	List<String> getChannels() {
		channel ?: []
	}
	
	Tag getTheme() {
		Tag.findByUri(Tag.generateUri(params.theme, TagType.theme))
	}
	
	Tag getEvent() {
		Tag.findByUri(Tag.generateUri(params.event, TagType.event))
	}
	
}