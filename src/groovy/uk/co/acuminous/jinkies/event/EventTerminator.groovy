package uk.co.acuminous.jinkies.event

import groovy.util.logging.Slf4j

@Slf4j
class EventTerminator implements EventHandler {

	@Override
	void handle(Map event) {	
		log.debug "Received event: $event"
		log.info "Terminiating"
	}
	
}
