package uk.co.acuminous.jinkies.event

import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import grails.plugin.spock.UnitSpec

class DuplicateEventFilterSpec extends UnitSpec {
	
	EventService eventService
	EventHandler nextHandler = Mock(EventHandler)
	DuplicateEventFilter filter
	
	def setup() {
		eventService = Mock(EventService)
		filter = new DuplicateEventFilter(eventService: eventService, nextHandler: nextHandler)		
	}
	
	def "Adds new events"() {
		
		given:
			Map event = [uuid: '123']
		
		when:
			filter.handle event
			
		then: 
			1 * eventService.exists(event.uuid) >> false
			1 * eventService.save(event)
			1 * nextHandler.handle(event)
	}
	
	def "Ignores duplicate events"() {
		
		given:
			Map event = [uuid: '123']
		
		when:
			filter.handle event
			
		then: 
			1 * eventService.exists(event.uuid) >> true
			0 * eventService.save(_)
			0 * nextHandler.handle(event)
		
	}
	
}
