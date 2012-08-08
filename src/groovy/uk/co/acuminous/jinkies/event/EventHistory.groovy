package uk.co.acuminous.jinkies.event

import uk.co.acuminous.jinkies.content.Tag

interface EventHistory {
	Map get(def key)
	void update(def key, Tag event)
	void remove(def key)
}
