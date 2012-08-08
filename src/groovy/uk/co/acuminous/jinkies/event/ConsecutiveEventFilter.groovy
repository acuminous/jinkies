package uk.co.acuminous.jinkies.event

import groovy.util.logging.Slf4j

import uk.co.acuminous.jinkies.content.Tag

@Slf4j
class ConsecutiveEventFilter extends ChainedEventHandler {

	EventHistory eventHistory
		
	@Override
	public synchronized void handle(Map event) {
		
		log.debug "Received event: $event"		
		
		Tag previousEventType = eventHistory.get(event.target)?.type
		
		if (event.type == previousEventType) {
			log.info "Suppressing ${event.type} event for ${event.target}"		
		} else {
			forward event
		}
	}
}
