package uk.co.acuminous.jinkies.event

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import spock.lang.Specification
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import uk.co.acuminous.jinkies.event.EventHandler
import uk.co.acuminous.jinkies.event.SimpleEventHistory


class ConsecutiveEventFilterSpec extends Specification {

	EventHandler nextHandler = Mock(EventHandler)
	EventHistory underlyingEventHistory = new SimpleEventHistory()
	ConsecutiveEventFilter filter = new ConsecutiveEventFilter(eventHistory: underlyingEventHistory, nextHandler: nextHandler)

	
	def "Does not filter first event"() {
		
		given:
			Tag success = new Tag('Success', TagType.event)
			Map event = [target: 'some job', type: success]
		
		when:
			filter.handle(event)
		
		then:
			1 * nextHandler.handle(event)
	}
	
	def "Does not filter different events"() {
	
		given:
			Tag success = new Tag('Success', TagType.event)
			Tag failure = new Tag('Failure', TagType.event)
			underlyingEventHistory.update('some job', failure)
			Map event = [target: 'some job', type: success]
		
		when:
			filter.handle(event)
		
		then:
			1 * nextHandler.handle(event)
	}
	
	def "Filters repeated events"() {
		
		given:
			Tag success = new Tag('Success', TagType.event)
			underlyingEventHistory.update('some job', success)
			
			Map event = [target: 'some job', type: success]
		
		when:
			filter.handle(event)
		
		then:
			0 * nextHandler.handle(_)
	}
	
	def "Does not confuse events from different jobs"() {
		
		given:
			Tag success = new Tag('Success', TagType.event)
			underlyingEventHistory.update('job1', success)
			
			Map event = [target: 'job2', type: success]
		
		when:
			filter.handle(event)
		
		then:
			1 * nextHandler.handle(event)
	}
}
