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
import grails.test.mixin.Mock

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
	
	def "Does not filter first event"() {
		
		given:
			Map event = [resourceId: 'foo/bar', type: success]
		
		when:
			filter.handle event
		
		then:
			1 * eventService.getLastEvent(event.resourceId) >> null
			1 * nextHandler.handle(event)
	}
	
	def "Does not filter different events"() {
	
		given:
			Event previous = new Event(uuid: '1', resourceId: 'foo/bar', type: failure, timestamp: 1L)
			Map event = [resourceId: previous.resourceId, type: success]
								
		when:
			filter.handle event
		
		then:
			1 * eventService.getLastEvent(event.resourceId) >> previous		
			1 * nextHandler.handle(event)
	}
	
	def "Filters consecutive events"() {
		
		given:
			Event previous = new Event(uuid: '1', resourceId: 'foo/bar', type: success, timestamp: 1L)		
			Map event = [resourceId: previous.resourceId, type: success]
		
		when:
			filter.handle event
		
		then:
			1 * eventService.getLastEvent(event.resourceId) >> previous		
			0 * nextHandler.handle(_)
	}
}
