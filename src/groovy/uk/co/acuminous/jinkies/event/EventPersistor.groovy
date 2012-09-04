package uk.co.acuminous.jinkies.event

import groovy.util.logging.Slf4j;

import java.util.Map;

@Slf4j
class EventPersistor extends ChainedEventHandler {

	EventService eventService
	
	@Override
	public void handle(Map event) {
		
		log.debug "Received event: $event"		

		eventService.save(event)
		forward event
	}

}
