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

class EventPersistorSpec extends UnitSpec {
	
	EventService eventService
	EventHandler nextHandler = Mock(EventHandler)
	EventPersistor persistor
	
	def setup() {
		eventService = Mock(EventService)
		persistor = new EventPersistor(eventService: eventService, nextHandler: nextHandler)		
	}
	
	def "Persists events"() {
		
		given:
			Map event = [uuid: '123']
		
		when:
			persistor.handle event
			
		then: 
			1 * eventService.save(event)
			1 * nextHandler.handle(event)
	}
}
