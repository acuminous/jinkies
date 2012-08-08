package uk.co.acuminous.jinkies.content

import uk.co.acuminous.jinkies.event.ChainedEventHandler

class NoCandidateContentFilter extends ChainedEventHandler {
	
	@Override
	void handle(Map event) {
		
		log.debug "Received event: $event"		
		
		if (!event.eligibleContent) {			
			log.info "No candidate content - supressing event"
		} else {
			forward event
		}
	}
	
}
