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

import org.quartz.JobKey
import org.quartz.Scheduler

import groovyx.net.http.RESTClient
import spock.lang.*

import uk.co.acuminous.jinkies.content.*
import uk.co.acuminous.jinkies.event.Event
import uk.co.acuminous.jinkies.event.EventBuilder
import static uk.co.acuminous.jinkies.util.MapUtils.*
import static groovyx.net.http.ContentType.*


@Mixin(RemoteUtils)
@Mixin(TestUtils)

class HousekeepingSpec extends Specification  {

	def setup() {
		nuke()
		
		remote {
			new Tag('Success', TagType.event).save()
		}
	}

	def "Housekeeping removes old events"() {
		
		given:
			long baseTime = System.currentTimeMillis()
			long oneDay = 24 * 60 * 60 * 1000L
			long oneMinute = 60 * 1000L
			long cutoff = baseTime - oneDay 
		
			remote {
				[cutoff - oneDay, cutoff - oneMinute, cutoff + oneMinute, cutoff + oneDay].each { long timestamp ->
					new EventBuilder().build(sourceId : 'foo/bar', type: Tag.findByName('Success'), timestamp: timestamp).save()
				}
			}
		
		when:
			remote {
				Scheduler scheduler = ctx.getBean('quartzScheduler')
				scheduler.start()
				JobKey jobKey = new JobKey('EventHousekeeperJob', 'DEFAULT')
				scheduler.triggerJob(jobKey)
			}
			
		then:
			List events = remote {
				int attemptsRemaining = 10
				while (Event.count() != 3 && attemptsRemaining) {
					attemptsRemaining--
					Thread.sleep(1000)
				}
				Scheduler scheduler = ctx.getBean('quartzScheduler')
				scheduler.standby()
				
				Event.list()
			}
			
			events.size() == 2
			events.findAll { it.timestamp < cutoff }.isEmpty()			
	}
}
