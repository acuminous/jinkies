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
