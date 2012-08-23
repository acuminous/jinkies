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

import org.springframework.validation.Errors

import uk.co.acuminous.jinkies.content.*
import uk.co.acuminous.jinkies.util.JinkiesErrorRenderer
import static javax.servlet.http.HttpServletResponse.*
import grails.converters.JSON
import static uk.co.acuminous.jinkies.util.CommonValidators.*

// Mixins are currently working properly, therefore extending (yuck)
class EventController extends JinkiesErrorRenderer {

	EventHandler eventHandler
	EventHistory eventHistory

	/* Since events don't have a unique id we can't 
	   simply GET them with a rest id like /api/event/123 
	   therefore using list.
	   
	   However "Listing" events doesn't make much sense since
	   the eventHistory only holds the most recent event for 
	   each target (usually a job). 
	*/
	def list() {
		Map event = eventHistory.get(params.target)
		List result = event ? [event] : []
		render(result as JSON)
	}
	
	def create(EventCommand cmd) {
		
		if (!report(cmd.errors)) { 
		
			Map event = [:]
			
			event.target = cmd.target
			event.type = cmd.event
			event.theme = cmd.theme
			event.channels = cmd.channels
			event.prescribedContent = cmd.prescribedContent
			
			if (!report(cmd.errors)) {
			
				eventHandler.handle(event)	
			
				render(status: SC_NO_CONTENT)
			}
		}		 
	}	
}

class EventCommand {
	
	String target
	List<String> channel
	List<String> content
	List<Content> prescribedContent = []
	
	static constraints = {
		target nullable: false, blank: false
		channel nullable: true
		content nullable: true, validator: contentValidator
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
	
	static def contentValidator = { List<String> restIds, EventCommand cmd ->
		
		def errorCode = null
		
		restIds.find { String restId ->
			if (!(restId ==~ /\w+\/\d+/)) {
				errorCode = ['eventCommand.content.invalid', restId]
			} else {
				Long id = Long.parseLong(restId.split('/').last())
				Content content = Content.get(id)
				if (!content) {
					errorCode = ['eventCommand.content.unknown', restId]					
				} else {
					cmd.prescribedContent << content
				}
			}
			errorCode != null
		}
		
		errorCode
	}
	
}