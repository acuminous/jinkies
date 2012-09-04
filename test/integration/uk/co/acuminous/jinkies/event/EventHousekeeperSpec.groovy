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
package uk.co.acuminous.jinkies.event

import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import grails.plugin.spock.IntegrationSpec

class EventHousekeeperSpec extends IntegrationSpec {
	
	EventHousekeeper housekeeper
	Tag success
	Long baseTime
	Long cutoff

	
	def setup() {
		success = new Tag('Success', TagType.event).save()
		
		baseTime = System.currentTimeMillis()
		Long oneDay = 24 * 60 * 60 * 1000L
		cutoff = baseTime - oneDay
		
		housekeeper = new EventHousekeeper(cutoff: oneDay)
	}
	
	def "Deletes old events"() {
		
		given:
			[cutoff - 5000, cutoff, cutoff + 5000, baseTime, baseTime + 5000].each { long timestamp ->
				makeEvent('a/b', timestamp)
				makeEvent('x/y', timestamp)
			}
		
		when:
			housekeeper.run()
			
		then: 
			Event.count() == 6
			Event.list().findAll { it.timestamp < cutoff }.size() == 0
	}
	
	def "Always retains last event no matter what"() {

		given:
			[cutoff - 5000, cutoff].each { long timestamp ->
				makeEvent('a/b', timestamp)
				makeEvent('x/y', timestamp)
			}

		when:
			housekeeper.run()
			
		then:
			Event.count() == 2
			Event.list().findAll { it.timestamp <= cutoff }.size() == 2
	}
		
	void makeEvent(String resourceId, Long timestamp) {
		new Event(uuid: UUID.randomUUID().toString(), resourceId: resourceId, type: success, timestamp: timestamp).save()
	}
}
