package uk.co.acuminous.jinkies.spec

import groovyx.net.http.RESTClient

import uk.co.acuminous.jinkies.ci.JobBuilder
import spock.lang.Specification
import uk.co.acuminous.jinkies.ci.Job;

import betamax.Betamax
import betamax.Recorder
import org.junit.*
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

import spock.lang.Unroll

@Mixin(RemoteMixin)

@Ignore
class JenkinsApiSpec extends Specification {

	@Rule Recorder recorder = new Recorder()
		
	RESTClient client
		
	def setup() {
		nuke()
		client = new RESTClient('http://localhost:8080')
		client.defaultRequestContentType = URLENC
		client.handler.failure = { it }
	}
	
	@Betamax(tape="Jenkins Jobs List")
	@Unroll("List all jobs from #url")
	def "Lists all jobs from the specified CI server"() {
		
		when:
			def jobs = client.get(path:'/api/jenkins', query: [url: url]).data
									
		then:
			jobs.size() == 6
			jobs[0].displayName == 'books-publish'
			jobs[5].displayName == 'NoHistory'
			
		where:
			url << ['http://build.acuminous.meh:8080/', 'http://build.acuminous.meh:8080', 'http://build.ACUMINOUS.meh:8080']
	}		
		
	@Betamax(tape="Jenkins Job")
	@Unroll("List #url")
	def "Lists the specified job"() {
		
		when:
			def jobs = client.get(path:'/api/jenkins', query: [url: url]).data
									
		then:
			jobs.size() == 1
			jobs[0].displayName == 'Julez'	
			
		where:
			url << ['http://build.acuminous.meh:8080/job/Julez', 'http://build.acuminous.meh:8080/job/julez/', 'http://build.acuminous.meh:8080/job/JULEZ']
	}
	
	@Betamax(tape="Jenkins Jobs List")
	@Unroll("Merge existing jobs with those from #url")
	def "Merges CI server jobs with existing jobs"() {
		
		given: 
			remote {
				JobBuilder.build(displayName: 'Julez', url: 'http://build.acuminous.meh:8080/job/Julez/', theme: 'Scooby Doo', channels: ['audio']).save()				
			}
		
		when:
			def jobs = client.get(path:'/api/jenkins', query: [url: url]).data

		then:
			jobs.size() == 6
			
			jobs[3].displayName == 'Julez'
			jobs[3].theme == 'Scooby Doo'
			jobs[3].channels[0] == 'audio'
			
		where:
			url << ['http://build.acuminous.meh:8080/', 'http://build.acuminous.meh:8080', 'http://build.ACUMINOUS.meh:8080']		
	}
	
	@Betamax(tape="Jenkins Jobs List")
	@Unroll("Ignores jobs that have been deleted from CI server #url")
	def "Ignores jobs that have been deleted from CI server"() {
		
		given:
			remote {
				JobBuilder.build(displayName: 'NotPresentOnCiServer', url: 'http://build.acuminous.meh:8080/job/NotPresentOnCiServer/').save()
			}
		
		when:
			def jobs = client.get(path:'/api/jenkins', query: [url: url]).data

		then:
			jobs.size() == 6
			
		where:
			url << ['http://build.acuminous.meh:8080/', 'http://build.acuminous.meh:8080', 'http://build.ACUMINOUS.meh:8080']
	}
	
	@Betamax(tape="Jenkins Jobs List")
	@Unroll("Ignores jobs from different CI servers #url")
	def "Ignores jobs from different CI server"() {
		
		given:
			remote {
				JobBuilder.build(displayName: 'Julez', url: 'http://other.acuminous.meh:8080/job/Julez/', theme: 'Scooby Doo', channels: ['audio']).save()
			}
		
		when:
			def jobs = client.get(path:'/api/jenkins', query: [url: url]).data

		then:
			jobs.size() == 6
			
		where:
			url << ['http://build.acuminous.meh:8080/', 'http://build.acuminous.meh:8080', 'http://build.ACUMINOUS.meh:8080']
	}
	
	
	@Betamax(tape="Jenkins Job")
	@Unroll("Merges the specified job with a known job #url")
	def "Merges the specified job with a known job"() {
		
		given:
			remote {                                         
				JobBuilder.build(displayName: 'Julez', url: 'http://build.acuminous.meh:8080/job/Julez/', theme: 'Scooby Doo').save()
			}
		
		when:
			def jobs = client.get(path:'/api/jenkins', query: [url: url]).data
									
		then:
			jobs.size() == 1
			jobs[0].displayName == 'Julez'
			jobs[0].theme == 'Scooby Doo'
			
		where:
			url << ['http://build.acuminous.meh:8080/job/Julez', 'http://build.acuminous.meh:8080/job/Julez/', 'http://build.acuminous.meh:8080/job/JULEZ']
	}
	
	@Unroll("Responds with 400 if url not specified #url")
	def "Responds with 400 if url not specified"() {
		
		expect: 
			client.get(path:'/api/jenkins', query: [url: url]).status == 400
			
		where:
			url << ['', null]
	}
	
	@Unroll("Responds with 500 if jenkins cannot be reached #url")
	def "Responds with 500 if jenkins cannot be reached"() {
		
		expect:
			client.get(path:'/api/jenkins', query: [url: url]).status == 500
			
		where:
			url << ['http://doesnotexist.acuminous.meh/', 'http://localhost:8080/blah']

	}
}
