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

class EventCacheUpdaterSpec extends UnitSpec {
	
	EventHandler nextHandler = Mock(EventHandler)
	EventCacheUpdater updater = new EventCacheUpdater(cache: [:], nextHandler: nextHandler)
	
	def "Caches events for new source"() {
		
		given:
			Map event = [sourceId: 'A', type: 'Error']
		
		when:
			updater.handle event
			
		then: 
			updater.cache[event.sourceId] == event.type
	}
	
	def "Updates events for existing source"() {
		
		given:	
			Map event = [sourceId: 'A', type: 'Error']
			updater.cache[event.sourceId] = 'Success'
		
		when:
			updater.handle event
			
		then: 
			updater.cache[event.sourceId] == event.type	
	}
	
	def "Forwards event to next handler"() {
		
		given:
			Map event = [sourceId: 'A', type: 'Error']
	
		when:
			updater.handle event
			
		then:
			1 * nextHandler.handle(event)
		
	}
	
}
