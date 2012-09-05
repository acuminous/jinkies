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

class EventHousekeeper {
	
	long timeToLive
	
	void run() {				
		sourceIds.each { String sourceId ->
			List doomed = listOldEvents(sourceId)
			pardonLastEvent(doomed)
			deleteOldEvents(doomed)
		}
	}
	
	List getSourceIds() {
		Event.executeQuery('select distinct e.sourceId from Event e')
	}
	
	List listOldEvents(String sourceId) {
		def c = Event.createCriteria()
		c.list {
			eq('sourceId', sourceId)
			lt('timestamp', System.currentTimeMillis() - timeToLive)
			order('timestamp', 'desc')
		}
	}
	
	void pardonLastEvent(List doomed) {
		if (doomed) {
			String sourceId = doomed.first().sourceId
			List allEvents = Event.findAllBySourceId(sourceId)
			if (doomed.size() == allEvents.size()) {
				doomed.remove(0)
			}
		}
	}
	
	void deleteOldEvents(List doomed) {
		Event.withSession { def session ->		
			doomed*.delete()
			session.flush()
		}		
	}

}
