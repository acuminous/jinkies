package uk.co.acuminous.jinkies.event

import java.util.concurrent.ConcurrentHashMap;
import uk.co.acuminous.jinkies.content.Tag

class SimpleEventHistory implements EventHistory {

	Map events = new ConcurrentHashMap()
	
	public Map get(def key) {
		events.get(key)
	}
	
	public void update(def key, Tag event) {
		events.put(key,  [type: event, timestamp: System.currentTimeMillis()])
	}
	
	public void remove(def key) {
		events.remove(key)
	}
	
	public String toString() {
		events
	}
	
}
