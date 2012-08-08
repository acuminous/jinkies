package uk.co.acuminous.jinkies.event

import java.util.concurrent.ConcurrentHashMap;
import uk.co.acuminous.jinkies.content.Tag

class EventHistoryReadFilter implements EventHistory {
	
	// @Delegate doesn't work when implementing interfaces http://jira.codehaus.org/browse/GROOVY-4647
	EventHistory underlyingEventHistory
	Set<String> allowedEvents = []
	
	@Override
	public Map get(def key) {		
		Map entry = underlyingEventHistory.get(key)						
		allowedEvents.contains(entry?.type?.name) ? entry : null
	}
	
	@Override
	public void update(Object key, Tag event) {
		underlyingEventHistory.update(key,  event)		
	}
	
	@Override
	public void remove(Object key) {
		underlyingEventHistory.remove(key)		
	}
}
