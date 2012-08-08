package uk.co.acuminous.jinkies.event

import java.util.Map;

class EventHistoryUpdater extends ChainedEventHandler {

	EventHistory eventHistory
	
	@Override
	public void handle(Map event) {
		log.debug "Received event: $event"
		eventHistory.update(event.target, event.type)		
		forward event
		
	}

}
