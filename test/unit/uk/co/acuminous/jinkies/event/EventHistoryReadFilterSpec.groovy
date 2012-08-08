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
