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
class ConsecutiveEventFilterSpec extends UnitSpec {

	EventHandler nextHandler = Mock(EventHandler)
	EventService eventService = Mock(EventService)
	ConsecutiveEventFilter filter
	
	Tag success
	Tag failure
	
	def setup() {
		success = new Tag('Success', TagType.event).save()
		failure = new Tag('Failure', TagType.event).save()
		filter = new ConsecutiveEventFilter(eventService: eventService, type: success.name, nextHandler: nextHandler)
	}
	
	
	def "Forwards events of an unmatched type"() {
		
		given:
			Map event = [sourceId: 'foo/bar', type: failure]
		
		when:
			filter.handle event
		
		then:
			1 * eventService.getLastEvent(event.sourceId) >> null
			1 * nextHandler.handle(event)
	}
	
	def "Forwards the first event with a matched type"() {
		
		given:
			Map event = [sourceId: 'foo/bar', type: success]
		
		when:
			filter.handle event
		
		then:
			1 * eventService.getLastEvent(event.sourceId) >> null
			1 * nextHandler.handle(event)
	}

	def "Suppresses second event of a matched type for the same resource"() {
		
		given:
			Event previous = new Event(uuid: '1', sourceId: 'foo/bar', type: success, timestamp: 1L).save()
			Map event = [sourceId: previous.sourceId, type: success]
								
		when:
			filter.handle event
		
		then:
			1 * eventService.getLastEvent(event.sourceId) >> previous
			0 * nextHandler.handle(_)
	}
			
	def "Forwards second event of a matched type for a different resources"() {
	
		given:
			Event previous = new Event(uuid: '1', sourceId: 'foo/bar', type: success, timestamp: 1L).save()
			Map event = [sourceId: 'foo/other', type: success]
								
		when:
			filter.handle event
		
		then:
			1 * eventService.getLastEvent(event.sourceId) >> null		
			1 * nextHandler.handle(event)
	}
	
	
	def "Suppresses second event of an unmatched type"() {
		
		given:
			Event previous = new Event(uuid: '1', sourceId: 'foo/bar', type: failure, timestamp: 1L).save()
			Map event = [sourceId: previous.sourceId, type: failure]
								
		when:
			filter.handle event
		
		then:
			1 * eventService.getLastEvent(event.sourceId) >> previous
			1 * nextHandler.handle(event)
	}
	
	def "Supresses second event of a matched type when the previous event has not expired"() {
		
		given:
			Event previous = new Event(uuid: '1', sourceId: 'foo/bar', type: success, timestamp: System.currentTimeMillis())
			Map event = [sourceId: previous.sourceId, type: success]
			filter.cutoff = 1000L
		
		when:
			filter.handle event
		
		then:
			1 * eventService.getLastEvent(event.sourceId) >> previous
			0 * nextHandler.handle(_)
	}
	
	def "Forwards second event of a machted type when the previous event has expired"() {
	
		given:
			Event previous = new Event(uuid: '1', sourceId: 'foo/bar', type: success, timestamp: System.currentTimeMillis() - 2000L)
			Map event = [sourceId: previous.sourceId, type: success]
			filter.cutoff = 1000L
		
		when:
			filter.handle event
		
		then:
			1 * eventService.getLastEvent(event.sourceId) >> previous
			1 * nextHandler.handle(event)				
	}
	
	def "Forwards event of matched type when the previous unmatched event has not expired"() {
		
			given:
				filter.cutoff = 1000L
				Event previous = new Event(uuid: '1', sourceId: 'foo/bar', type: failure, timestamp: System.currentTimeMillis())
				Map event = [sourceId: previous.sourceId, type: success]
			
			when:
				filter.handle event
			
			then:
				1 * eventService.getLastEvent(event.sourceId) >> previous
				1 * nextHandler.handle(event)
		}
}
