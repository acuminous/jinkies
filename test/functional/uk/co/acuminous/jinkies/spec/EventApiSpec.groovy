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
		
	def getRemoteEvent() {
		remote {
			TestChannel testChannel = app.mainContext.testSwitch.channel
			Map event = testChannel.events.isEmpty() ? null : testChannel.events.last()
			serializableEntries event
		}
	}

	def "Creates an event for specified resource"() {
		
		given:
			String uuid = '1234'		
			String resourceId = 'foo/bar'
			Map params = [uuid: uuid, resourceId: resourceId, event: success.name, channel: ['test'], timestamp: 123L]
		
		expect:
			client.post(path: '/api/event', body: params).status == 204
			
			Map event = remoteEvent
			event?.uuid == uuid
			event?.resourceId == resourceId	
			event?.timestamp == 123L	
	}
	
	def "Generates a UUID if not specified"() {
		given:
			Map params = [resourceId: 'foo/bar', event: success.name, channel: ['test']]
		
		expect:
			client.post(path: '/api/event', body: params).status == 204
			
			Map event = remoteEvent
			event?.uuid =~ /\w+-\w+-\w+-\w+-\w+/
	}	
	
	
	def "Generates a timestamp if not specified"() {
		
		given:
			Long currentTime = System.currentTimeMillis()
			Map params = [resourceId: 'foo/bar', event: success.name, channel: ['test']]
		
		expect:
			client.post(path: '/api/event', body: params).status == 204
			
			Map event = remoteEvent
			event?.timestamp >= currentTime
	}
	
	def "Events can specify content"() {
		
		given:
			def content = remote {
				Content zoinks = Content.build(title: 'Zoinks', filename: 'zoinks.mp3')
				Content jinkies = Content.build(title: 'Jinkies', filename: 'jinkies.mp3')
				[zoinks, jinkies]
			}
			
			Map params = [resourceId: 'foo/bar', event: success.name, channel: ['test'], content: ["content/${content[0].id}", "content/${content[1].id}"]]
		
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
			
			Map params = [resourceId: 'foo/bar', event: success.name, channel: ['test'], content: ["content/${content.id}", 'content/999']]
		
		when:
			def response = client.post(path: '/api/event', body: params)

		then:
			response.status == 400
			response.data[0] == "Content not found 'content/999'."	
	}
	
	def "Handles invalid content"() {
		
		given:
			Map params = [resourceId: 'foo/bar', event: success.name, channel: ['test'], content: ['123']]
		
		when:
			def response = client.post(path: '/api/event', body: params)

		then:
			response.status == 400
			response.data[0] == "Invalid content identifier '123'."			
	}
		
	def "Handles invalid events"() {
		
		given:
			Map params = [resourceId: 'foo/bar', channel: ['test']]
		
		when:
			def response = client.post(path: '/api/event', body: params)
			
		then:
			response.status == 400
			response.data[0] == "An event is required."				
	}
		
	@Unroll("Reports invalid resourceIds: #resourceId")
	def "Reports invalid resourceIds"() {
		
		given:
			Map params = [resourceId: resourceId, event: success.name]
		
		when:
			def response = client.post(path: '/api/event', body: params)

		then: 
			response.status == 400
			response.data[0] == "A resource id is required."
						
		where:
			resourceId << ['', null]		
	}
	
	def "Duplicate events are rejected"() {
		
		given:
			String resourceId = 'foo/bar'		
			Map params = [uuid: '123', resourceId: 'foo/bar', event: success.name, channel: ['test'], timestamp: 999L]
		
		expect:
			client.post(path: '/api/event', body: params).status == 204
			
		when:
			def response = client.post(path: '/api/event', body: params)
			
		then: 
			response.status == 400
			response.data[0] == "Duplicate uuid '${params.uuid}'."
	}
}
