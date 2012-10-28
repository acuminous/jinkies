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

import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

import grails.test.mixin.Mock

import spock.lang.Unroll
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import uk.co.acuminous.jinkies.event.EventHandler

@Mock([Tag, Event])
class RecoveryEventGeneratorSpec extends UnitSpec {

	EventHandler nextHandler = Mock(EventHandler)
	EventService eventService = Mock(EventService)
	RecoveryEventGenerator generator
	
	Tag success, failure, error, recovery
	
	def setup() {
		success = new Tag('Success', TagType.event).save()
		failure = new Tag('Failure', TagType.event).save()
		error = new Tag('Error', TagType.event).save()
		recovery = new Tag('Recovery', TagType.event).save()
		generator = new RecoveryEventGenerator(eventService: eventService, nextHandler: nextHandler)
	}
		
	def "Inserts a new recovery event when the build has recovered from an error"() {
		
		given:
			Event previous = new Event(uuid: '1', sourceId: 'foo/bar', type: error, timestamp: 1L).save()		
			Map event = [uuid: '123', sourceId: 'foo/bar', type: failure, timestamp: 3L, theme: 'Foo', channels: ['a', 'b']]
		
		when:
			generator.handle event
		
		then:
			1 * eventService.getLastEvent(event.sourceId) >> previous
			
		then:
			1 * nextHandler.handle({ Map recoveryEvent ->
				recoveryEvent.uuid != null &&
				recoveryEvent.uuid != event.uuid &&
				recoveryEvent.sourceId == event.sourceId &&
				recoveryEvent.theme == event.theme &&
				recoveryEvent.channels == event.channels &&
				recoveryEvent.timestamp == (previous.timestamp + 1) &&
				recoveryEvent.type == recovery
			})
			
		then:
			1 * nextHandler.handle(event)
	}
	
	def "Just forwards original event when there is no previous event"() {

		given:
			Map event = [uuid: '123', sourceId: 'foo/bar', type: failure, timestamp: 2L, theme: 'Foo', channels: ['a', 'b']]
		
		when:
			generator.handle event
	
		then:
			1 * eventService.getLastEvent(event.sourceId) >> null
			1 * nextHandler.handle(event)
			0 * nextHandler.handle({ Map recoveryEvent ->
				recoveryEvent.type == recovery
			})
	}
	
	def "Just forwards the original event when the previous event was not an error"() {
		given:
			Event previous = new Event(uuid: '1', sourceId: 'foo/bar', type: success, timestamp: 1L).save()
			Map event = [uuid: '123', sourceId: 'foo/bar', type: failure, timestamp: 2L, theme: 'Foo', channels: ['a', 'b']]
		
		when:
			generator.handle event
		
		then:
			1 * eventService.getLastEvent(event.sourceId) >> previous
			1 * nextHandler.handle(event)
			0 * nextHandler.handle({ Map recoveryEvent ->
				recoveryEvent.type == recovery
			})
	}
	
	def "Just forwards the original event when the current event is also an error"() {
		given:
			Event previous = new Event(uuid: '1', sourceId: 'foo/bar', type: error, timestamp: 1L).save()
			Map event = [uuid: '123', sourceId: 'foo/bar', type: error, timestamp: 2L, theme: 'Foo', channels: ['a', 'b']]
		
		when:
			generator.handle event
		
		then:
			1 * eventService.getLastEvent(event.sourceId) >> previous
			1 * nextHandler.handle(event)
			0 * nextHandler.handle({ Map recoveryEvent ->
				recoveryEvent.type == recovery
			})
	}
	

}
