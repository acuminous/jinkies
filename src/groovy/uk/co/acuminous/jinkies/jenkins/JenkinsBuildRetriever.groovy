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
package uk.co.acuminous.jinkies.jenkins

import groovy.util.logging.Slf4j
import uk.co.acuminous.jinkies.event.ChainedEventHandler

@Slf4j
class JenkinsBuildRetriever extends ChainedEventHandler {

	JenkinsServer server
	
	@Override
	public void handle(Map event) {
		
		log.debug "Received event: $event"		
		
		log.info "Retrieving build details for $event.build.job.displayName #$event.build.number"
		
		server.populateMissingDetails event.build

		freshen event
				
		forward event
	}

	void freshen(Map event) {
		event << event.build.toEvent()
	}
		
	
}
