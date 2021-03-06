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

import uk.co.acuminous.jinkies.ci.Job

import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import grails.plugin.spock.UnitSpec
import grails.test.mixin.Mock

@Mock(Tag)
class EventRouterSpec extends UnitSpec {
	
	EventHandler jobEventHandler = Mock(EventHandler)
	EventHandler errorEventHandler = Mock(EventHandler)
	EventHandler otherEventHandler = Mock(EventHandler)
	EventRouter router = new EventRouter(jobEventHandler: jobEventHandler, errorEventHandler: errorEventHandler, otherEventHandler: otherEventHandler)
	
	Tag error
	
	def setup() {
		error = new Tag('Error', TagType.event).save()
	}
	
	def "Routes job events to job handler"() {
		
		given:
			Map event = [job: new Job()]
		
		when:
			router.handle event
			
		then: 
			1 * jobEventHandler.handle(event)
			0 * errorEventHandler._
			0 * otherEventHandler._
	}
	
	def "Routes error events to the error handler"() {
		
		given:
			Map event = [type: error]
		
		when:
			router.handle event
			
		then:
			1 * errorEventHandler.handle(event)
			0 * jobEventHandler._			
			0 * otherEventHandler._
		
	}
	
	def "Routes other events to other handler"() {
		
		given:
			Map event = [:]
		
		when:
			router.handle event
			
		then: 
			1 * otherEventHandler.handle(event)
			0 * jobEventHandler._
			0 * errorEventHandler._
	}
}
