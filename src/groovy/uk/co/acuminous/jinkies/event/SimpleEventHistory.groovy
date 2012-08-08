/* 
 * Copyright 2012 Acuminous Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
