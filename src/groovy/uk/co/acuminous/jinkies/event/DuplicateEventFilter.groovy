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
			eventService.save(event)
			forward event
		}
		
	}	
}
