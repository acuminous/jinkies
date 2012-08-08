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
