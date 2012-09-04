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
import static uk.co.acuminous.jinkies.util.CommonValidators.*

// Mixins are currently working properly, therefore extending (yuck)
class EventController extends JinkiesErrorRenderer {

	EventHandler eventHandler
	
	def create(EventCommand cmd) {
		
		if (!report(cmd.errors)) { 
		
			Map event = [:]
			
			event.uuid = cmd.uuid
			event.resourceId = cmd.resourceId
			event.type = cmd.retrieveEvent()
			event.theme = cmd.retrieveTheme()
			event.channels = cmd.channels
			event.prescribedContent = cmd.prescribedContent
			event.timestamp = cmd.timestamp
			
			eventHandler.handle event	
		
			render status: SC_NO_CONTENT
		}		 
	}	
}

class EventCommand {
	
	String uuid
	String resourceId
	String event
	String theme
	List<String> channel
	List<String> content
	List<Content> prescribedContent = []
	Long timestamp
	
	TagService tagService
	EventService eventService
	
	static constraints = {
		uuid nullable: true, validator: duplicateEvent
		resourceId nullable: false, blank: false
		event nullable: false
		theme nullable: true
		channel nullable: true		
		content nullable: true, validator: contentValidator
	}
	
	String getUuid() {
		this.uuid ?: UUID.randomUUID().toString()
	}
	
	List<String> getChannels() {
		channel ?: []
	}
	
	Tag retrieveEvent() {
		event != null ? tagService.findOrCreateTag(event, TagType.event) : null
	}
	
	Tag retrieveTheme() {
		theme != null ? tagService.findOrCreateTag(theme, TagType.theme) : null
	}
	
	Long getTimestamp() {
		this.timestamp ?: System.currentTimeMillis()
	}
	
	static def duplicateEvent = { String uuid, EventCommand cmd ->		
		if (cmd.eventService.exists(uuid)) {
			'eventCommand.uuid.unique'
		}		
	}
	
	static def contentValidator = { List<String> resourceIds, EventCommand cmd ->
		
		def errorCode = null
		
		resourceIds.find { String resourceId ->
			if (!(resourceId ==~ /\w+\/\d+/)) {
				errorCode = ['eventCommand.content.invalid', resourceId]
			} else {
				Long id = Long.parseLong(resourceId.split('/').last())
				Content content = Content.get(id)
				if (!content) {
					errorCode = ['eventCommand.content.unknown', resourceId]					
				} else {
					cmd.prescribedContent << content
				}
			}
			errorCode != null
		}
		
		errorCode
	}
	
}