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
package uk.co.acuminous.jinkies.spec

import groovyx.net.http.RESTClient
import spock.lang.*

import uk.co.acuminous.jinkies.channel.TestChannel
import uk.co.acuminous.jinkies.content.*
import static uk.co.acuminous.jinkies.util.MapUtils.*
import static groovyx.net.http.ContentType.*


@Mixin(RemoteUtils)
@Mixin(TestUtils)

class EventApiSpec extends Specification  {

	RESTClient client
	Tag success
	Tag failure
	
	def setup() {
		nuke()
		
		client = getRestClient(baseUrl)	
		
		success = remote {
			new Tag('Success', TagType.event).save()
		}
		
		failure = remote {
			new Tag('Failure', TagType.event).save()
		}
	}

	def "Creates an event for specified target"() {
		
		given:
			String target = 'foo/bar'		
			Map params = [target: 'foo/bar', event: success.name, channel: ['test']]
		
		expect:
			client.post(path: '/api/event', body: params).status == 204
			
			Map event = remoteEvent
			event?.target == target		
	}
	
	def "Events can specify content"() {
		
		given:
			def content = remote {
				Content zoinks = Content.build(title: 'Zoinks', filename: 'zoinks.mp3')
				Content jinkies = Content.build(title: 'Jinkies', filename: 'jinkies.mp3')
				[zoinks, jinkies]
			}
			
			Map params = [target: 'foo/bar', event: success.name, channel: ['test'], content: ["content/${content[0].id}", "content/${content[1].id}"]]
		
		expect:
			client.post(path: '/api/event', body: params).status == 204
			
			Map event = remoteEvent
			event?.eligibleContent.size() == content.size()
			event?.eligibleContent[0].title == content[0].title
			event?.eligibleContent[1].title == content[1].title
	}
		
	def "Handles missing content"() {
		
		given:
			def content = remote {				
				Content.build(title: 'Zoinks', filename: 'zoinks.mp3')
			}
			
			Map params = [target: 'foo/bar', event: success.name, channel: ['test'], content: ["content/${content.id}", 'content/999']]
		
		when:
			def response = client.post(path: '/api/event', body: params)

		then:
			response.status == 400
			response.data[0] == "Content not found 'content/999'."	
	}
	
	def "Handles invalid content"() {
		
		given:
			Map params = [target: 'foo/bar', event: success.name, channel: ['test'], content: ['123']]
		
		when:
			def response = client.post(path: '/api/event', body: params)

		then:
			response.status == 400
			response.data[0] == "Invalid content identifier '123'."			
	}
	
	def getRemoteEvent() {
		remote {
			TestChannel testChannel = app.mainContext.testSwitch.channel
			Map event = testChannel.events.isEmpty() ? null : testChannel.events.last()
			serializableEntries event
		}
	}
		
	@Unroll("Reports invalid targets: #target")
	def "Reports invalid targets"() {
		
		given:
			Map params = [target: target, event: success.name]
		
		when:
			def response = client.post(path: '/api/event', body: params)

		then: 
			response.status == 400
			response.data[0] == "A target is required."
						
		where:
			target << ['', null]
		
	}
	
	def "Renders events for specified target"() {
		
		given:			
			String target = 'foo/bar'
			long serverTime = remote { System.currentTimeMillis() }
			
		expect:
			client.post(path: '/api/event', body: [target: target, event: success.name]).status == 204
			
		when:			
			def response = client.get(path: '/api/event', query: [target: target])
			def result = response.data			
					
		then:
			response.status == 200
			result.size() == 1
			result[0].type.name == success.name
			result[0].timestamp >= serverTime
	}
	
	
	def "Only ever returns the latest result ( thanks to the implementation of SimpleEventHistory )"() {
		
		given:
			String target = 'foo/bar'
			
		expect:
			client.post(path: '/api/event', body: [target: target, event: success.name]).status == 204	

			long serverTime = remote { System.currentTimeMillis() }
			client.post(path: '/api/event', body: [target: target, event: failure.name]).status == 204
						
		when:			
			def response = client.get(path: "/api/event", query: [target: target])
			def result = response.data
					
		then:
			response.status == 200
			result.size() == 1
			result[0].type.name == failure.name
			result[0].timestamp >= serverTime
	}
	
	def "Get returns empty list for unknow targets"() {		
			
		when:
			def response = client.get(path: '/api/event', query: [target: 'unknown'])
			def result = response.data			
			
		then:
			response.status == 200
			result.size() == 0
	}

}
