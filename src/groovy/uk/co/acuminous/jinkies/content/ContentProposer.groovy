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

import uk.co.acuminous.jinkies.event.ChainedEventHandler
import uk.co.acuminous.jinkies.util.UriBuilder

@Slf4j
class ContentProposer extends ChainedEventHandler {

	ContentService contentService
	
	@Override
	void handle(Map event) {
		
		log.debug "Received event: $event"
			
		Tag theme = event.theme
		Tag eventType = event.type
		List<String> contentTypes = event.selectedChannel.contentTypes	
		
		if (event.prescribedContent) {
			event.eligibleContent = event.prescribedContent
		} else if (contentTypes) {
			event.eligibleContent = contentService.findAllEligibleContent(theme, eventType, contentTypes)
		} else {
			event.eligibleContent = []
		}
					
		forward event
	}
}
