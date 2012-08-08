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
package uk.co.acuminous.jinkies.content

import groovy.util.logging.Slf4j

import org.apache.commons.lang.math.RandomUtils

import uk.co.acuminous.jinkies.event.ChainedEventHandler


@Slf4j
class RandomContentSelector extends ChainedEventHandler {

	ContentService contentService
	
	@Override
	void handle(Map event) {
		
		log.debug "Received event: $event"
		
		List<Content> eligibleContent = event.eligibleContent
						
		if (eligibleContent) {
			int index = RandomUtils.nextInt(eligibleContent.size())
			event.selectedContent = eligibleContent[index] 
			
			log.info "Using content: $event.eligibleContent"
		}
		
		forward event
	}
	
}
