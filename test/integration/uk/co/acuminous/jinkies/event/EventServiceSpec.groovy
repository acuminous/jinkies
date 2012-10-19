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

import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import grails.plugin.spock.IntegrationSpec
import grails.validation.ValidationException

class EventServiceSpec extends IntegrationSpec {

	EventService eventService
	Tag success
	Tag failure
	
	def setup() {		
		success = new Tag('Success', TagType.event).save()
		failure = new Tag('Failure', TagType.event).save()
	}
	
	def "Reports duplicate events"() {
		
		given:
			Event event = new Event(uuid: 'abc', sourceId: 'foo/bar', type: success, timestamp: System.currentTimeMillis()).save()
			
		expect:
			eventService.exists(event.uuid)		
	}
	
	def "Reports new events"() {
		
		expect:
			!eventService.exists('abc')
	}
	
	def "Saves events"() {
	
		given:
			Map data = [uuid: 'abc', sourceId: 'foo/bar', type: success, timestamp: System.currentTimeMillis()]
			
		when:
			eventService.save(data)
			
		then:
			Event event = Event.findByUuid('abc')
			event != null
			event.sourceId == data.sourceId
			event.type == data.type
			event.timestamp == data.timestamp			
	}
		
	def "Saving an event returns a domain object"() {
	
		given:
			Map data = [uuid: 'abc', sourceId: 'foo/bar', type: success, timestamp: System.currentTimeMillis()]
			
		when:
			Event event = eventService.save(data)
			
		then:
			event != null
			event.uuid == data.uuid
			event.sourceId == data.sourceId
			event.type == data.type
			event.timestamp == data.timestamp			
	}
	
	def "Save failures are reported"() {
		
		given:
			Map data = [uuid: 'abc', type: success, timestamp: System.currentTimeMillis()]
			
		when:
			eventService.save(data)
			
		then:
			thrown ValidationException
	}
	
	def "Returns last event for source id"() {
		
		given:
			Long baseTimestamp = System.currentTimeMillis()
			Event event1 = new Event(uuid: '1', sourceId: 'foo/bar', type: success, timestamp: baseTimestamp).save()
			Event event2 = new Event(uuid: '2', sourceId: 'foo/bar', type: success, timestamp: baseTimestamp + 1000).save()
		
		expect:
			eventService.getLastEvent('foo/bar') == event2
	}
	
	def "Purges all events for the given source id"() {
		
		given:
			Event event1 = new Event(uuid: '1', sourceId: 'foo/bar', type: success, timestamp: 1L).save()
			Event event2 = new Event(uuid: '2', sourceId: 'foo/bar', type: success, timestamp: 2L).save()

		when:
			eventService.purge('foo/bar')
			
		then:
			Event.count() == 0
			
	}
		
	def "Does not purges events for other source ids"() {
		
		given:
			Event event1 = new Event(uuid: '1', sourceId: 'foo/bar', type: success, timestamp: 1L).save()
			Event event2 = new Event(uuid: '2', sourceId: 'foo/bar', type: success, timestamp: 2L).save()

		when:
			eventService.purge('foo/other')
			
		then:
			Event.count() == 2
			
	}
	
	
	def "Purges events from cache for given source id"() {
		
		given:
			eventService.cache = ['foo/bar': 'some-event']

		when:
			eventService.purge('foo/bar')
			
		then:
			eventService.getCurrentStatus('foo/bar') == null
			
	}
	
	def "Get last event tollerates no events"() {
		
		expect:
			eventService.getLastEvent('foo') == null		
	}
}
