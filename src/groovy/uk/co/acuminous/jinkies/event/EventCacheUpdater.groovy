package uk.co.acuminous.jinkies.event

import java.util.Map;

class EventCacheUpdater extends ChainedEventHandler {

	Map cache
	
	@Override
	public void handle(Map event) {		
		cache[event.sourceId] = event.type		
		forward event
	}
}
