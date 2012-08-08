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

import uk.co.acuminous.jinkies.ci.Job
import uk.co.acuminous.jinkies.content.*
import uk.co.acuminous.jinkies.event.EventHistory
import static groovyx.net.http.ContentType.*

@Mixin(RemoteMixin)

class EventApiSpec extends Specification  {

	RESTClient client
	Tag success
	Tag failure
	
	def setup() {
		nuke()
		client = new RESTClient('http://localhost:8080')
		client.defaultRequestContentType = URLENC
		client.handler.failure = { it }
		
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
			Map params = [target: 'foo/bar', event: success.name]
		
		expect:
			client.post(path: '/api/event', body: params).status == 204
		
	}
	
	@Unroll("Reports invalid targets: #target")
	def "Reports invalid targets"() {
		
		given:
			Map params = [target: target, event: success.name]
		
		expect:
			client.post(path: '/api/event', body: params).status == 400
			
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
