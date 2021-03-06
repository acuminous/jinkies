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

class EventService {

	Map cache
	
	synchronized boolean exists(String uuid) {
		Event.findByUuid(uuid)
	}
	
	synchronized Event save(Map data) {
		Event event = new Event(data)
		event.save(flush:true, failOnError:true)
	}
	
	void purge(String sourceId) {
		Event.withNewSession {
			Event.findAllBySourceId(sourceId)*.delete()
		}
		cache.remove(sourceId)
	}
	
	Tag getCurrentStatus(String sourceId) {
		cache.get(sourceId)
	}
		
	Event getLastEvent(String sourceId) {
		def c = Event.createCriteria()
		Event previous = c.get {
			eq('sourceId', sourceId)
			maxResults(1)
			order('timestamp', 'desc')
		}
	}	
}
