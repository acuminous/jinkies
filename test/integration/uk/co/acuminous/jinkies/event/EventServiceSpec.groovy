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
	
	def "Reports duplicate events"() {
		
		given:
			Tag tag = new Tag('Success', TagType.event).save()
			Event event = new Event(uuid: 'abc', resourceId: 'foo/bar', type: tag, timestamp: System.currentTimeMillis()).save()
			
		expect:
			eventService.exists(event.uuid)		
	}
	
	def "Reports new events"() {
		
		expect:
			!eventService.exists('abc')
	}
	
	def "Saves events"() {
	
		given:
			Tag tag = new Tag('Success', TagType.event).save()
			Map data = [uuid: 'abc', resourceId: 'foo/bar', type: tag, timestamp: System.currentTimeMillis()]
			
		when:
			eventService.save(data)
			
		then:
			Event event = Event.findByUuid('abc')
			event != null
			event.resourceId == data.resourceId
			event.type == data.type
			event.timestamp == data.timestamp			
	}
		
	def "Saving an event returns a domain object"() {
	
		given:
			Tag tag = new Tag('Success', TagType.event).save()
			Map data = [uuid: 'abc', resourceId: 'foo/bar', type: tag, timestamp: System.currentTimeMillis()]
			
		when:
			Event event = eventService.save(data)
			
		then:
			event != null
			event.uuid == data.uuid
			event.resourceId == data.resourceId
			event.type == data.type
			event.timestamp == data.timestamp
			
	}
	
	def "Save failures are reported"() {
		
			given:
				Tag tag = new Tag('Success', TagType.event).save()
				Map data = [uuid: 'abc', type: tag, timestamp: System.currentTimeMillis()]
				
			when:
				eventService.save(data)
				
			then:
				thrown ValidationException
		}
	
}
