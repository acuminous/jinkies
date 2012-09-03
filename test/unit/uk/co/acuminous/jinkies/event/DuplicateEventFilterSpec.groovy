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
import grails.plugin.spock.UnitSpec

class DuplicateEventFilterSpec extends UnitSpec {
	
	EventService eventService
	EventHandler nextHandler = Mock(EventHandler)
	DuplicateEventFilter filter
	
	def setup() {
		eventService = Mock(EventService)
		filter = new DuplicateEventFilter(eventService: eventService, nextHandler: nextHandler)		
	}
	
	def "Adds new events"() {
		
		given:
			Map event = [uuid: '123']
		
		when:
			filter.handle event
			
		then: 
			1 * eventService.exists(event.uuid) >> false
			1 * eventService.save(event)
			1 * nextHandler.handle(event)
	}
	
	def "Ignores duplicate events"() {
		
		given:
			Map event = [uuid: '123']
		
		when:
			filter.handle event
			
		then: 
			1 * eventService.exists(event.uuid) >> true
			0 * eventService.save(_)
			0 * nextHandler.handle(event)
		
	}
	
}
