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

import spock.lang.Ignore;
import spock.lang.Specification
import spock.lang.Unroll
import uk.co.acuminous.jinkies.ci.Build;
import uk.co.acuminous.jinkies.ci.Job;
import uk.co.acuminous.jinkies.jenkins.JenkinsServer
import uk.co.acuminous.jinkies.util.HttpClientsFactory
import groovyx.net.http.HttpResponseException

import betamax.Betamax
import betamax.Recorder
import org.junit.*


class JenkinsServerFunctionalSpec extends Specification {

	@Rule Recorder recorder = new Recorder()
	
	JenkinsServer jenkins = new JenkinsServer(httpClientsFactory: new HttpClientsFactory())	
	
	@Betamax(tape="Jenkins Build History")
	@Unroll("Gets #job.url build history")
	def "Gets a job's build history"() {
		
		when:
			List<Build> buildHistory = jenkins.getBuildHistory(job)
									
		then:
			buildHistory.size() == 6
			buildHistory[0].job == job
			buildHistory[0].number == 40
			buildHistory[0].url == 'http://build.acuminous.meh:8080/job/Jinkies/40/'
			
			buildHistory[5].job == job
			buildHistory[5].number == 27
			buildHistory[5].url == 'http://build.acuminous.meh:8080/job/Jinkies/27/'
			
		where:
			job << [
				new Job(displayName: 'Jinkies', url: 'http://build.acuminous.meh:8080/job/Jinkies/'),
				new Job(displayName: 'Jinkies', url: 'http://build.acuminous.meh:8080/job/Jinkies')
			]
	}	
	
	@Betamax(tape="Jenkins Build History")
	def "getBuildHistory handles jobs with no history"() {
		
		given:
			Job job = new Job(displayName: 'NoHistory', url: 'http://build.acuminous.meh:8080/job/NoHistory/')

		when:
			List buildHistory = jenkins.getBuildHistory(job)
									
		then:
			buildHistory.size() == 0
	}
	
	@Betamax(tape="Jenkins Build History")
	def "getBuildHistory reports missing jobs"() {
		
		given:
			Job job = new Job(displayName: 'Nada', url: 'http://build.acuminous.meh:8080/job/Nada/')
		
		when:
			jenkins.getBuildHistory(job)
									
		then:
			thrown HttpResponseException
	}
	
	@Betamax(tape="Jenkins Build")
	def "Reports build details"() {
		
		given:
			Job job = new Job(displayName: 'Jinkies', url: 'http://build.acuminous.meh:8080/job/Jinkies/')		
			Build build = new Build(job: job, number: 40, url: 'http://build.acuminous.meh:8080/job/Jinkies/40/')

		when:
			jenkins.populateMissingDetails(build)
									
		then:
			build.url == 'http://build.acuminous.meh:8080/job/Jinkies/40/'
			build.number == 40
			build.result == 'FAILURE'
			build.timestamp == 1338567880176
	}
		
	@Betamax(tape="Jenkins Build")
	def "getBuild reports missing jobs"() {
		
		given:
			Job job = new Job(displayName: 'Nada', url: 'http://build.acuminous.meh:8080/job/Nada/')		
			Build build = new Build(job: job, url: 'http://build.acuminous.meh:8080/job/Nada/1/')

		when:
			jenkins.populateMissingDetails(build)
									
		then:
			thrown HttpResponseException
	}
	
	@Betamax(tape="Jenkins Jobs")
	def "getJobs returns details of all jobs"() {
		
		when:
			List<Job> jobs = jenkins.getJobs('http://build.acuminous.meh:8080/')
									
		then:
			jobs.size() == 6
	}
	
	@Betamax(tape="Jenkins Build History")
	def "getJobs also supports a single job url"() {
		
		given:

		when:
			List<Job> jobs = jenkins.getJobs('http://build.acuminous.meh:8080/job/Jinkies/')
									
		then:
			jobs.size() == 1
	}
	
}
