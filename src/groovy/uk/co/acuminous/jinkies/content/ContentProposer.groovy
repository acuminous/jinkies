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
		
		event.eligibleContent = contentService.findAllEligibleContent(theme, eventType, contentTypes)
					
		forward event
	}
}
