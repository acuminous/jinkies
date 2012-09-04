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

import groovy.util.logging.Slf4j;

import java.util.Map;

@Slf4j
class DuplicateEventFilter extends ChainedEventHandler {

	EventService eventService

	@Override
	public void handle(Map event) {
		
		log.debug "Received event: $event"		
		
		if (eventService.exists(event.uuid)) {
			log.debug "Event ${event.uuid} is a duplicate"			
		} else {
			forward event
		}
		
	}	
}
