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
package uk.co.acuminous.jinkies.ci

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import spock.lang.Specification
import uk.co.acuminous.jinkies.event.EventHandler
import grails.test.mixin.Mock as DomainMock


@DomainMock(Job) // Shouldn't be necessary, but getting some weird ServletContext null error when creating jobs
class RepeatedBuildFilterSpec extends Specification {

	EventHandler nextHandler = Mock(EventHandler)
	RepeatedBuildFilter filter = new RepeatedBuildFilter(nextHandler: nextHandler)
	
	def "Does not filter new events"() {
		
		given:
			Map event = [build: new BuildBuilder().build()]
			
		when:
			filter.handle(event)
			
		then:
			1 * nextHandler.handle(event)		
	}
	
	def "Filters duplicate events"() {
		
		given:
			Map event = [build: new BuildBuilder().build()]			
			
		when:
			filter.handle(event)
			filter.handle(event)
			
		then:
			1 * nextHandler.handle(_)
	}
		
	def "Does not filter subsequent events"() {
		
		given:
			Map event1 = [build: new BuildBuilder().build(number: 1)]
			Map event2 = [build: new BuildBuilder().build(number: 2)]
			
		when:
			filter.handle(event1)
			filter.handle(event2)
			
		then:
			2 * nextHandler.handle(_)
	}
	
	
	def "Does not confuse different jobs with the same build number"() {
		
		given:
			Map event1 = [build: new BuildBuilder().build(job: new Job(url: '.../job/Jinkies'), number: 1)]
			Map event2 = [build: new BuildBuilder().build(job: new Job(url: '.../job/Julez'), number: 1)]
			
		when:
			filter.handle(event1)
			filter.handle(event2)
			
		then:
			2 * nextHandler.handle(_)
	}
		
	def "Ignores event sequence because jobs might be deleted and recreated"() {
		
		given:
			Job job = new Job(url: '.../job/Jinkies')
			Map event1 = [build: new BuildBuilder().build(job: job, number: 1)]
			Map event2 = [build: new BuildBuilder().build(job: job, number: 2)]
			
		when:
			filter.handle(event2)
			filter.handle(event1)
			
		then:
			2 * nextHandler.handle(_)
	}
	
	def "Is thread safe"() {
		
		when:
			CountDownLatch latch = new CountDownLatch(100)
			(1..10).each { int threadIndex ->
				Thread.start {
					Job job = new Job(url: "../job/Jinkies-$threadIndex/")					
					(1..10).each { int buildIndex ->
						Map event = [build: new BuildBuilder().build(job: job, number: buildIndex)]						
						filter.handle(event)
						latch.countDown()
					}
				}
			}
			latch.await(10, TimeUnit.SECONDS)
			
		then:
			100 * nextHandler.handle(_)		
	}
	
}
