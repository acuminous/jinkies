package uk.co.acuminous.jinkies.event

import java.util.concurrent.ConcurrentHashMap;
import uk.co.acuminous.jinkies.content.Tag

import groovy.util.logging.Slf4j

@Slf4j
class ExpiringEventHistory implements EventHistory {
	
	// @Delegate doesn't work when implementing interfaces http://jira.codehaus.org/browse/GROOVY-4647	
	EventHistory underlyingEventHistory
	long maxAgeInMillis
	
	@Override
	public Map get(def key) 
	{
		Map entry = underlyingEventHistory.get(key)	
		if (!entry) {
			log.debug "No event history for $key"
			entry
		} else if (isBelowMaxAge(entry.timestamp)) {
			log.debug "${entry?.event} is still current for $key"
			entry
		} else {
			log.debug "${entry?.event} has expired from history for $key"		
			null
		}
	}

	
	private boolean isBelowMaxAge(long lastEventTime) {
		(System.currentTimeMillis() - lastEventTime) <= maxAgeInMillis			
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
