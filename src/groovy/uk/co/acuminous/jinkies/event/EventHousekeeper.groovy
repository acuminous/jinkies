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
	
	long cutoff
	
	void run() {				
		resourceIds.each { String resourceId ->
			List doomed = listOldEvents(resourceId)
			pardonLastEvent(doomed)
			deleteOldEvents(doomed)
		}
	}
	
	List getResourceIds() {
		Event.executeQuery('select distinct e.resourceId from Event e')
	}
	
	List listOldEvents(String resourceId) {
		println "Purging resourceId: $resourceId"
		def c = Event.createCriteria()
		c.list {
			eq('resourceId', resourceId)
			lt('timestamp', System.currentTimeMillis() - cutoff)
			order('timestamp', 'desc')
		}
	}
	
	void pardonLastEvent(List doomed) {
		String resourceId = doomed.first().resourceId
		List allEvents = Event.findAllByResourceId(resourceId)
		if (doomed.size() == allEvents.size()) {
			doomed.remove(0)
		}
	}
	
	void deleteOldEvents(List doomed) {
		doomed*.delete()		
	}

}
