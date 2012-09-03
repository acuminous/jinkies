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

class EventService {

	synchronized boolean exists(String uuid) {
		Event.findByUuid(uuid)
	}
	
	synchronized Event save(Map data) {
		Event event = new Event(data)
		event.save(flush:true, failOnError:true)
	}
	
	Event getLastEvent(String resourceId) {
		def c = Event.createCriteria()
		Event previous = c.get {
			eq('resourceId', resourceId)
			maxResults(1)
			order('timestamp', 'desc')
		}
	}
	
}