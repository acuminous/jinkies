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
package uk.co.acuminous.jinkies.json

import uk.co.acuminous.jinkies.ci.Job
import uk.co.acuminous.jinkies.content.Content
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.event.Event
import uk.co.acuminous.jinkies.event.EventService


public class CustomJsonMarshaller {
		
	static job = { Job job ->
		
		marshall {
			Map data = [
				displayName: job.displayName,
				url: job.url,
				type: job.type,
				theme: job.theme,
				channels: job.channels
			]
				
			if (job.id) {
				data.resourceId = job.resourceId
			}	
			
			data			
		}
	}
	
	static jobWithLastEvent = { EventService eventService, Job job ->
		
		Map data = CustomJsonMarshaller.job(job)
		if (data.resourceId) {
			Tag currentStatus = eventService.getCurrentStatus(data.resourceId)
			if (currentStatus) {
				data.status = currentStatus.name
			}
		}
				
		data
	}

	static content = { Content content ->
		
		marshall {
		
			Map data = [
				title: content.title,
				description: content.description,
				filename: content.filename,
				url: content.url,
				type: content.type,
				themes: content.themes ?: [], 
				events: content.events ?: []
			]
			
			data.resourceId = content.resourceId
			data.dataResourceId = content.dataResourceId
			if (content.bytes) {
				data.dataHashCode = content.bytes.hashCode()
			}
			
			data
		}
	}
	
	static tag = { Tag tag ->

		marshall {
			[
				name: tag.name,
				type: tag.type.name(),
				uri: tag.uri,
				resourceId: "tag/${tag.id}".toString()
			]
		}
	}
	
	static event = { Event event ->
		
		marshall {
			[
				uuid: event.uuid,
				sourceId: event.sourceId,				
				type: event.type,
				timestamp: event.timestamp,
				resourceId: event.resourceId
								
			]
		}
		
	}
	
	static def marshall(Closure marshaller) {		
		try {
			marshaller()
		} catch (Exception e) {
			e.message
		}
	}	
}
