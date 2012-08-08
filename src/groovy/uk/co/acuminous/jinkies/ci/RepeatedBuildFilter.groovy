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

import groovy.util.logging.Slf4j

import java.util.concurrent.ConcurrentHashMap

import uk.co.acuminous.jinkies.event.ChainedEventHandler

@Slf4j
class RepeatedBuildFilter extends ChainedEventHandler {

	ConcurrentHashMap history = new ConcurrentHashMap()
	
	@Override
	public synchronized void handle(Map event) {
		
		log.debug "Received event: $event"		
						
		if (alreadyReported(event)) {
			log.debug "Build $event.build.job.displayName $event.build.number has already been processed"
		} else {			
			addBuildTo event
			forward event		
		}
	}

	private boolean alreadyReported(Map event) {
		history[event.build.job] == event.build
	}
	
	private void addBuildTo(Map event) {
		history[event.build.job] = event.build
	}
}
