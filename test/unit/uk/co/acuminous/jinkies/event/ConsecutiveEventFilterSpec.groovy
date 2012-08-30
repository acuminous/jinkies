/* 
 * Copyright 2012 Acuminous Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
			Map event = [resourceId: 'some job', type: success]
		
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
			Map event = [resourceId: 'some job', type: success]
		
		when:
			filter.handle(event)
		
		then:
			1 * nextHandler.handle(event)
	}
	
	def "Filters repeated events"() {
		
		given:
			Tag success = new Tag('Success', TagType.event)
			underlyingEventHistory.update('some job', success)
			
			Map event = [resourceId: 'some job', type: success]
		
		when:
			filter.handle(event)
		
		then:
			0 * nextHandler.handle(_)
	}
	
	def "Does not confuse events from different jobs"() {
		
		given:
			Tag success = new Tag('Success', TagType.event)
			underlyingEventHistory.update('job1', success)
			
			Map event = [resourceId: 'job2', type: success]
		
		when:
			filter.handle(event)
		
		then:
			1 * nextHandler.handle(event)
	}
}
