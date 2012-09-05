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

import java.util.Map;

import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType

class EventBuilder {

	Event build(Map data = [:]) {
		
		data.uuid = data.uuid ?: UUID.randomUUID().toString()
		data.resourceId = data.resourceId ?: randomResourceId
		data.type = data.type ?: new Tag('Success', TagType.event)
		data.timestamp = data.timestamp ?: System.currentTimeMillis()
		
		println data
		
		new Event(data)
	}
	
	String getRandomResourceId() {
		int number = Math.random() * Integer.MAX_VALUE 
		"job/$number"
	}
	
}
