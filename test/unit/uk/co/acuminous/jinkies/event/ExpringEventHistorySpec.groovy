package uk.co.acuminous.jinkies.event

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import spock.lang.Specification
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import uk.co.acuminous.jinkies.event.EventHandler
import uk.co.acuminous.jinkies.event.SimpleEventHistory


class ExpringEventHistorySpec extends Specification {

	ExpiringEventHistory filter = new ExpiringEventHistory(underlyingEventHistory: new SimpleEventHistory(), maxAgeInMillis: 1000)
		
	def "Tollerates new events"() {		
		expect:
			filter.get('some job') == null
	}
	
	def "Allows young events to be retrieved from history"() {
		
		given:
			Tag eventType = new Tag('Success', TagType.event)
			filter.update('some job', eventType)
		
		expect:
			filter.get('some job').type == eventType
			
		when:
			Thread.sleep(500)
			
		then:
			filter.get('some job').type == eventType
	}

	
	def "Prevents old events from being retrieved from history"() {
		
		given:
			Tag eventType = new Tag('Success', TagType.event)
			filter.update('some job', eventType)
		
		expect:
			filter.get('some job').type == eventType
			
		when:
			Thread.sleep(1100)
			
		then:
			filter.get('some job') == null
	}
}
