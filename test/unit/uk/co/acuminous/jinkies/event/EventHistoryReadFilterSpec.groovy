package uk.co.acuminous.jinkies.event

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import spock.lang.Specification
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import uk.co.acuminous.jinkies.event.EventHandler
import uk.co.acuminous.jinkies.event.SimpleEventHistory


class EventHistoryReadFilterSpec extends Specification {

	SimpleEventHistory eventHistory = new SimpleEventHistory()	
	EventHistoryReadFilter filter = new EventHistoryReadFilter(underlyingEventHistory: eventHistory, allowedEvents: ['Success', 'Failure'])
	
	def "Allows specified events to be retrieved from history"() {
		
		given:
			Tag eventType = new Tag('Success', TagType.event)
			eventHistory.update('some job', eventType)
		
		expect:
			filter.get('some job').type == eventType
	}

	
	def "Tollerates allowed events not present in history"() {
		
		expect:
			filter.get('some job') == null
	}
		
	def "Filters other events from history"() {
	
		given:
			Tag eventType = new Tag('Error', TagType.event)
			eventHistory.update('some job', eventType)
		
		expect:
			filter.get('some job') == null
	}
}
