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
	
	def "Returns last event for each distinct source id"() {
		given: 
			new EventBuilder().build(sourceId: 'a/1', type: success, timestamp: 1L).save()
			new EventBuilder().build(sourceId: 'a/1', type: failure, timestamp: 2L).save()
			new EventBuilder().build(uuid: 'this-one', sourceId: 'a/1', type: success, timestamp: 3L).save()
			
			new EventBuilder().build(sourceId: 'a/2', type: success, timestamp: 1L).save()
			new EventBuilder().build(uuid: 'and-this-one', sourceId: 'a/2', type: failure, timestamp: 2L).save()
		
		when:
			List events = eventService.getLastEvents()
			
		then:
			events.size() == 2
			events[0].uuid == 'this-one'
			events[1].uuid == 'and-this-one'
	}
	
	def "Returns last event for source id"() {
		
		given:
			Long baseTimestamp = System.currentTimeMillis()
			Event event1 = new Event(uuid: '1', sourceId: 'foo/bar', type: success, timestamp: baseTimestamp).save()
			Event event2 = new Event(uuid: '2', sourceId: 'foo/bar', type: success, timestamp: baseTimestamp + 1000).save()
		
		expect:
			eventService.getLastEvent('foo/bar') == event2
	}
	
	
	def "Get last event tollerates no events"() {
		
		expect:
			eventService.getLastEvent('foo') == null		
	}
}
