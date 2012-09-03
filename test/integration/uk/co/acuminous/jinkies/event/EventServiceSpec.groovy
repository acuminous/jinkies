package uk.co.acuminous.jinkies.event

import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import grails.plugin.spock.IntegrationSpec
import grails.validation.ValidationException

class EventServiceSpec extends IntegrationSpec {

	EventService eventService
	
	def "Reports duplicate events"() {
		
		given:
			Tag tag = new Tag('Success', TagType.event).save()
			Event event = new Event(uuid: 'abc', resourceId: 'foo/bar', type: tag, timestamp: System.currentTimeMillis()).save()
			
		expect:
			eventService.exists(event.uuid)		
	}
	
	def "Reports new events"() {
		
		expect:
			!eventService.exists('abc')
	}
	
	def "Saves events"() {
	
		given:
			Tag tag = new Tag('Success', TagType.event).save()
			Map data = [uuid: 'abc', resourceId: 'foo/bar', type: tag, timestamp: System.currentTimeMillis()]
			
		when:
			eventService.save(data)
			
		then:
			Event event = Event.findByUuid('abc')
			event != null
			event.resourceId == data.resourceId
			event.type == data.type
			event.timestamp == data.timestamp			
	}
		
	def "Saving an event returns a domain object"() {
	
		given:
			Tag tag = new Tag('Success', TagType.event).save()
			Map data = [uuid: 'abc', resourceId: 'foo/bar', type: tag, timestamp: System.currentTimeMillis()]
			
		when:
			Event event = eventService.save(data)
			
		then:
			event != null
			event.uuid == data.uuid
			event.resourceId == data.resourceId
			event.type == data.type
			event.timestamp == data.timestamp
			
	}
	
	def "Save failures are reported"() {
		
			given:
				Tag tag = new Tag('Success', TagType.event).save()
				Map data = [uuid: 'abc', type: tag, timestamp: System.currentTimeMillis()]
				
			when:
				eventService.save(data)
				
			then:
				thrown ValidationException
		}
	
}
