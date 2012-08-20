package uk.co.acuminous.jinkies.util

import java.util.Map;

class MapUtils {
	
	static Map serializableEntries(Map src) {
		Map dest = [:]
		src.each { k, v ->
			if (v instanceof Serializable) {
				dest[k] = v
			}
		}
		dest
	}	
}
