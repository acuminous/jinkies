package uk.co.acuminous.jinkies.spec

import groovyx.net.http.RESTClient
import spock.lang.*

import uk.co.acuminous.jinkies.ci.Job
import uk.co.acuminous.jinkies.content.*
import uk.co.acuminous.jinkies.event.EventHistory
import static groovyx.net.http.ContentType.*

@Mixin(RemoteMixin)

class JobApiSpec extends Specification  {

	RESTClient client
	
	def setup() {
		nuke()
		client = new RESTClient('http://localhost:8080')
		client.defaultRequestContentType = URLENC
		client.handler.failure = { it }
	}
	
	def "Provides a json representation of a job"() {
		
		given:
			Job job = remote {
				Tag theme = new Tag('Star Wars', TagType.theme).save()
				Job.build(displayName: 'Julez', url: '../job/julez', type:'jenkins', theme: theme, channels: ['audio'])
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
				Job.build(displayName: 'B', url: '../job/b', type:'jenkins')
				Job.build(displayName: 'C', url: '../job/c', type:'jenkins')
				Job.build(displayName: 'A', url: '../job/a', type:'jenkins')
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
			
			result.restId == "job/$id"
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
				Job.build(displayName: 'Blah', url: '../job/blah', type: 'jenkins', theme: theme).id
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
				Job.build(displayName: 'Blah', url: '../job/blah', type: 'jenkins').id
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
				Job.build(displayName: 'Blah', url: '../job/blah', type: 'jenkins').id
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
				Job.build(displayName: 'Blah', url: '../job/blah', type: 'jenkins').id
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
				Job.build(displayName: 'Blah', url: '../job/blah', type: 'jenkins').id
			}
			String restId = "job/$id"
			
		expect:
			client.post(path: '/api/event', body: [target: restId, event:'Success']).status == 204
			client.delete(path: '/api/' + restId).status == 204
			
		when:			
			def response = client.get(path: '/api/event', query: [target: restId])
			def result = response.data			
					
		then:
			response.status == 200
			result.size() == 0

	}
}
