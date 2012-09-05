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
package uk.co.acuminous.jinkies.jenkins

import spock.lang.Specification
import uk.co.acuminous.jinkies.ci.*
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import uk.co.acuminous.jinkies.event.EventHandler

import grails.test.mixin.Mock


@Mock([Tag, Job])
class JenkinsBuildRetrieverSpec extends Specification {

	EventHandler nextHandler = Mock(EventHandler)
	JenkinsServer jenkinsServer = Mock(JenkinsServer)
	JenkinsBuildRetriever retriever = new JenkinsBuildRetriever(nextHandler: nextHandler, server: jenkinsServer)
	
	def "Updates build with full details"() {
		
		given:
			Build build = new BuildBuilder().build(result: null, timestamp:null)
			Map event = [build: build]
					
		when:
			retriever.handle(event)
		
		then:
			1 * jenkinsServer.populateMissingDetails(build)
	}
	
	def "Updates event with mandatory fields"() {
		
		given:
			Build build = new BuildBuilder().build(timestamp:123L)
			Map event = [build: build]
					
		when:
			retriever.handle(event)
		
		then:
			event.timestamp == 123L
	}
		
	def "Forwards event to next handler"() {
		
		given:
			Tag success = new Tag('Success', TagType.event).save()
			Map event = [build: new BuildBuilder().build(result: 'SUCCESS')]			
		
		when:
			retriever.handle(event)
		
		then:
			1 * nextHandler.handle(event)
	}
	
	
	def "Suppresses event if the event type is undefined"() {
		
		given:
			Map event = [build: new BuildBuilder().build()]
		
		when:
			retriever.handle(event)
		
		then:
			0 * nextHandler._
	}
	
}
