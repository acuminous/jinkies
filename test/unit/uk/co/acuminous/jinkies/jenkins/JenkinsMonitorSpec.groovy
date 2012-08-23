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

import grails.plugin.spock.UnitSpec
import grails.test.mixin.Mock
import uk.co.acuminous.jinkies.ci.Build;
import uk.co.acuminous.jinkies.ci.Job;
import uk.co.acuminous.jinkies.content.*
import uk.co.acuminous.jinkies.event.EventHandler
import uk.co.acuminous.jinkies.test.QuietException
import spock.lang.*


@Mock([Job, Tag])
class JenkinsMonitorSpec extends UnitSpec {

	JenkinsServer jenkinsServer = Mock(JenkinsServer)
	EventHandler eventHandler = Mock(EventHandler)
	EventHandler errorHandler = Mock(EventHandler)
	JenkinsMonitor jenkinsMonitor = new JenkinsMonitor(server: jenkinsServer, eventHandler: eventHandler, errorHandler: errorHandler)

	def "Checks all known Jenkins jobs"() {
		
		given:
			Job job1 = new Job(displayName: 'Jinkies', type: 'jenkins', url: '.../job/Jinkies/').save()
			Job job2 = new Job(displayName: 'Julez', type: 'jenkins', url: '.../job/Julez/').save()
			
		when:
			jenkinsMonitor.check()
			
		then:
			1 * jenkinsServer.getBuildHistory(job1) >> [ ]
			1 * jenkinsServer.getBuildHistory(job2) >> [ ]
	}
	
	def "Excludes non Jenkins jobs"() {
		
		given:
			Job job1 = new Job(displayName: 'Jinkies', type: 'hudson', url: '.../job/Jinkies/').save()
			
		when:
			jenkinsMonitor.check()
			
		then:
			0 * jenkinsServer._
	}
	
	def "Continues if one job errors"() {
		
		given:
			Job job1 = new Job(displayName: 'Jinkies', type: 'jenkins', url: '.../job/Jinkies/').save()
			Job job2 = new Job(displayName: 'Julez', type: 'jenkins', url: '.../job/Julez/').save()
			
		when:
			jenkinsMonitor.check()
			
		then:
			1 * jenkinsServer.getBuildHistory(job1) >> { throw new QuietException() }			
			1 * jenkinsServer.getBuildHistory(job2) >> [ ]
	}
	
	def "Rasies error events for failures"() {
		
		given:
			Tag error = new Tag('Error', TagType.event).save(flush:true)
			Tag scoobyDoo = new Tag('Scooby Doo', TagType.theme).save(flush:true)
			
			Job job = new Job(displayName: 'Jinkies', type: 'jenkins', url: '.../job/Jinkies/')
			job.channels = ['audio']
			job.theme = scoobyDoo
			job.save(flush: true)
			
			Build build = new Build(job: job, number: 5, url: '.../job/Jinkies/5/')
			
		when:
			jenkinsMonitor.check()
			
		then:
			1 * jenkinsServer.getBuildHistory(_) >> [ build ]
			1 * eventHandler.handle(_) >> { throw new QuietException() }
			1 * errorHandler.handle({ Map event ->
				event.job == job &&
				event.error == 'This is a test exception. Please ignore.' &&
				event.target == "job/$job.id" &&
				event.type == error && 
				event.theme == scoobyDoo &&
				event.channels == job.channels
			})
	}
		
	def "Raises an event for the most recent build"() {
		
		given:			
			Job job = new Job(displayName: 'Jinkies', type: 'jenkins', url: '.../job/Jinkies/').save()
			Build build5 = new Build(job: job, number: 5, url: '.../job/Jinkies/5/')
			Build build6 = new Build(job: job, number: 6, url: '.../job/Jinkies/6/')
			
		when:
			jenkinsMonitor.check()
			
		then:
			1 * jenkinsServer.getBuildHistory(job) >> [ build6, build5 ]		
			1 * eventHandler.handle({ Map event ->
				event.target == "job/$job.id" &&
				event.build == build6 
			})
	}

	def "Handles jobs with no build history"() {

		given:
			Job job = new Job(displayName: 'Jinkies', type: 'jenkins', url: '.../job/Jinkies/').save()
	
		when:
			jenkinsMonitor.check()
			
		then:
			1 * jenkinsServer.getBuildHistory(job) >> [ ]
			0 * eventHandler.handle(_)
	}
}
