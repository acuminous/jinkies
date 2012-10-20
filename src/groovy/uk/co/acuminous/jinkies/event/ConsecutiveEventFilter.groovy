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

import groovy.util.logging.Slf4j

import uk.co.acuminous.jinkies.content.Tag

@Slf4j
class ConsecutiveEventFilter extends ChainedEventHandler {

	String type
	Long cutoff = Long.MAX_VALUE
	EventService eventService
		
	@Override
	public synchronized void handle(Map event) {
		
		log.debug "Received event: $event"		
					
		if (shouldSuppress(event)) {
			log.info "Suppressing ${event.type} event for ${event.sourceId}"
		} else {
			forward event
		}
	}
	
	boolean shouldSuppress(Map currentEvent) {
		
		boolean result = false
		
		if (isRequiredType(currentEvent.type)) {
			
			Event previousEvent = eventService.getLastEvent(currentEvent.sourceId)
			
			result = has(previousEvent) && 
					 isConsecutive(previousEvent, currentEvent) &&
					 isRecent(previousEvent)
		}
		
		result				
	}
	
	boolean isRequiredType(Tag tag) {
		tag.name == type
	}
		
	boolean has(Event previous) {
		previous != null
	}	
	
	boolean isRecent(Event previous) {
		previous.timestamp >= (System.currentTimeMillis() - cutoff)
	}
	
	boolean isConsecutive(Event previous, Map current) {
		current.type == previous.type
	}
}
