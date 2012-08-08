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

import java.util.concurrent.ConcurrentHashMap;
import uk.co.acuminous.jinkies.content.Tag

import groovy.util.logging.Slf4j

@Slf4j
class ExpiringEventHistory implements EventHistory {
	
	// @Delegate doesn't work when implementing interfaces http://jira.codehaus.org/browse/GROOVY-4647	
	EventHistory underlyingEventHistory
	long maxAgeInMillis
	
	@Override
	public Map get(def key) 
	{
		Map entry = underlyingEventHistory.get(key)	
		if (!entry) {
			log.debug "No event history for $key"
			entry
		} else if (isBelowMaxAge(entry.timestamp)) {
			log.debug "${entry?.event} is still current for $key"
			entry
		} else {
			log.debug "${entry?.event} has expired from history for $key"		
			null
		}
	}

	
	private boolean isBelowMaxAge(long lastEventTime) {
		(System.currentTimeMillis() - lastEventTime) <= maxAgeInMillis			
	}
	
	@Override
	public void update(Object key, Tag event) {
		underlyingEventHistory.update(key,  event)		
	}
	
	@Override
	public void remove(Object key) {
		underlyingEventHistory.remove(key)		
	}
	
}
