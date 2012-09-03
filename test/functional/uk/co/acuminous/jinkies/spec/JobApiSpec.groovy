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
import uk.co.acuminous.jinkies.event.Event
import static groovyx.net.http.ContentType.*

@Mixin(RemoteUtils)
@Mixin(TestUtils)

class JobApiSpec extends Specification  {

	RESTClient client
	
	def setup() {
		nuke()
		
		client = getRestClient(baseUrl)	
	}
	
	def "Provides a json representation of a job"() {
		
		given:
			Job job = remote {
				Tag theme = new Tag('Star Wars', TagType.theme).save()
				new Job(displayName: 'Julez', url: '../job/julez', type:'jenkins', theme: theme, channels: ['audio']).save()
			}
		
		when:
			def response = client.get(path: "/api/job/${job.id}")
			def result = response.data
		
		then:
			response.status == 200
			
			result.displayName == job.displayName
			result.url == job.url
			result.type == job.type
			result.theme.uri == job.theme.uri
			result.channels.collect { it } == job.channels 		
	}
	
	def "Responds with 404 if job does not exist"() {
		
		expect:
			client.get(path: '/api/job/999').status == 404
	}
	
	def "Lists jobs"() {
		
		given:
			remote {
				new Job(displayName: 'B', url: '../job/b', type:'jenkins').save()
				new Job(displayName: 'C', url: '../job/c', type:'jenkins').save()
				new Job(displayName: 'A', url: '../job/a', type:'jenkins').save()
			}
			
		when:
			def response = client.get(path: "/api/job")
			def results = response.data
			
		then:
			response.status == 200
		
			results.size() == 3
			results[0].displayName == 'A'
			results[2].displayName == 'C'		
	}
	
	def "Creates a job"() {
		
		given:
			Map params = [displayName: 'Julez', url: '../job/julez', type: 'jenkins', channel: ['audio', 'video']]
		
		when:
			def response = client.post(path: '/api/job', body: params)
		
		then:
			response.status == 200
			
			Job job = remote {
				List results = Job.findAllByDisplayName(params.displayName) // http://jira.grails.org/browse/GRAILS-8915?focusedCommentId=71362#comment-71362
				results ? results[0] : null
			}
			
			job.displayName == params.displayName
			job.url == params.url
			job.type == params.type
			job.theme == null
			
			job.channels.size() == 2			
			job.channels[0] == params.channel[0]
			job.channels[1] == params.channel[1]
	}
	
	def "Will create themes if necessary"() {
		
		given:
			Map params = [displayName: 'Julez', url: '../job/julez', type: 'jenkins', theme: 'Star Wars', channel: ['audio', 'video']]
		
		when:
			def response = client.post(path: '/api/job', body: params)
		
		then:
			response.status == 200
			
			Job job = remote {
				List results = Job.findAllByDisplayName(params.displayName) // http://jira.grails.org/browse/GRAILS-8915?focusedCommentId=71362#comment-71362
				Job job = results ? results[0] : null
				job.theme
				job
			}
			
			job.displayName == params.displayName
			job.url == params.url
			job.type == params.type
			job.theme?.name == 'Star Wars'
			
			job.channels.size() == 2
			job.channels[0] == params.channel[0]
			job.channels[1] == params.channel[1]
	}
	
	
	def "Creatinga job responds with a json represenation"() {
		
		given:
			Map params = [displayName: 'Julez', url: '../job/julez', type: 'jenkins', theme: 'Star Wars', channel: ['audio', 'video']]
		
		when:
			def response = client.post(path: '/api/job', body: params)
			def result = response.data
					
		then:
			response.status == 200
			result == response.data
			
			def id = remote {
				Job.findByDisplayName(params.displayName).id
			}
			
			result.resourceId == "job/$id"
			result.displayName == params.displayName
			result.url == params.url
			result.type == params.type
			result.theme?.name == params.theme
			
			result.channels.size() == 2
			result.channels[0] == params.channel[0]
			result.channels[1] == params.channel[1]
	}
	
	@Unroll("Validation errors while creating a job result in 400")
	def "Validation errors while creating a job result in 400"() {
		
		given:
			Map params = [displayName: displayName, url: url, type: type]
		
		expect:
			client.post(path: '/api/job', body: params).status == 400
			
		where:
			displayName | url      | type
		    null        | '/valid' | 'valid'
			''          | '/valid' | 'valid'
			'valid'		| null	   | 'valid'
			'valid'		| ''	   | 'valid'
			'valid'		| '/valid' | null
			'valid'		| '/valid' | ''		
	}
	
	def "Updates a job"() {
		
		given:
			def id = remote {
				Tag theme = new Tag('Scooby Doo', TagType.theme).save(flush:true)				
				Job job = new Job(displayName: 'Blah', url: '../job/blah', type: 'jenkins', theme: theme).save(flush:true)
				job.id
			}
			
			Map params = [displayName: 'Julez', url: '../job/julez', type: 'jenkins', theme: 'Star Wars', channel: ['audio', 'video']]
	
		when:
			def response = client.put(path: "/api/job/$id", body: params)
			def result = response.data
		
		then:
			response.status == 200
			
			Job job = remote { 
				Job job = Job.get(id)
				job.theme
				job
				 
			}
			
			job.displayName == params.displayName
			job.url == params.url
			job.type == params.type
			job.theme?.name == params.theme
			job.channels[0] == params.channel[0]
			job.channels[1] == params.channel[1]
	}
	
	def "Updating a job responds with a json representation"() {
		
		given:
			def id = remote {
				Job job = new Job(displayName: 'Blah', url: '../job/blah', type: 'jenkins').save(flush:true)
				job.id
			}
			
			Map params = [displayName: 'Julez', url: '../job/julez', type: 'jenkins', theme: 'Star Wars', channel: ['audio', 'video']]
	
		when:
			def response = client.put(path: "/api/job/$id", body: params)
			def result = response.data
		
		then:
			response.status == 200	
		
			result.displayName == params.displayName
			result.url == params.url
			result.type == params.type
			result.theme?.name == params.theme
			result.channels[0] == params.channel[0]
			result.channels[1] == params.channel[1]	
	}
	
	def "Validation errors that occur while updating a job result in 400"() {
	
		given:
			def id = remote {
				Job job = new Job(displayName: 'Blah', url: '../job/blah', type: 'jenkins').save(flush:true)
				job.id
			}
			
			Map params = [displayName: displayName, url: url, type: type]

		expect:
			client.put(path: "/api/job/$id", body: params).status == 400
			
		where:
			displayName | url      | type
		    null        | '/valid' | 'valid'
			''          | '/valid' | 'valid'
			'valid'		| null	   | 'valid'
			'valid'		| ''	   | 'valid'
			'valid'		| '/valid' | null
			'valid'		| '/valid' | ''		
	}
	
	
	def "Deletes jobs"() {
		given:
			def id = remote {
				Job job = new Job(displayName: 'Blah', url: '../job/blah', type: 'jenkins').save(flush:true)
				job.id
			}
			
		expect:
			client.delete(path: "/api/job/$id").status == 204
	}
	
	def "Deleting jobs that doesn't exist is tollerated"() {
			
		expect:
			client.delete(path: "/api/content/999").status == 204
	}
	
	def "Deleting jobs removes their event history"() {
		
		given:
			def id = remote {
				new Tag('Success', TagType.event).save()				
				Job job = new Job(displayName: 'Blah', url: '../job/blah', type: 'jenkins').save(flush:true)
				job.id
			}
			String resourceId = "job/$id"
			
		expect:
			client.post(path: '/api/event', body: [resourceId: resourceId, event:'Success']).status == 204
			client.delete(path: '/api/' + resourceId).status == 204
			
		when:			
			def response = client.get(path: "/api/job/$id")
			def result = response.data			
					
		then:
			int count = remote {
				Event.count()
			}
			count == 0
			
	}
}
