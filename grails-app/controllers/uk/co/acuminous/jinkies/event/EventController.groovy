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

import uk.co.acuminous.jinkies.content.*
import uk.co.acuminous.jinkies.util.JinkiesErrorRenderer
import static javax.servlet.http.HttpServletResponse.*
import grails.converters.JSON

// Mixins are currently working properly, therefore extending (yuck)
class EventController extends JinkiesErrorRenderer {

	EventHandler eventHandler
	EventHistory eventHistory

	/* "Listing" events doesn't make much sense since
	   the eventHistory only holds the most recent event for 
	   each target (usually a job). 
	
	   Since events don't have a unique id we can't 
	   simply GET them with a rest id like /api/event/123 
	   hence the following weird list implementation.
	*/
	def list() {
		Map event = eventHistory.get(params.target)
		List result = event ? [event] : []
		render(result as JSON)
	}
	
	def create(EventCommand cmd) {
		
		Map event = [:]
		
		event.target = cmd.target
		event.type = cmd.event
		event.theme = cmd.theme
		event.channels = cmd.channels
		
		eventHandler.handle(event)	
			
		report(cmd.errors) || render(status: SC_NO_CONTENT)		 
	}	
}

class EventCommand {
	
	String target
	List<String> channel
	
	static constraints = {
		target nullable: false, blank: false
		channel nullable: true
	}
	
	List<String> getChannels() {
		channel ?: []
	}
	
	Tag getTheme() {
		Tag.findByUri(Tag.generateUri(params.theme, TagType.theme))
	}
	
	Tag getEvent() {
		Tag.findByUri(Tag.generateUri(params.event, TagType.event))
	}
	
}