package uk.co.acuminous.jinkies.json

import uk.co.acuminous.jinkies.ci.Job
import uk.co.acuminous.jinkies.content.Content
import uk.co.acuminous.jinkies.content.Tag


public class CustomJsonMarshaller {
	
	static Closure job = { Job job ->
		
		marshall {
			Map data = [
				displayName: job.displayName,
				url: job.url,
				type: job.type,
				theme: job.theme,
				channels: job.channels
			]
				
			if (job.id) {
				data.restId = "job/${job.id}".toString()
			}	
			
			data			
		}
	}

	static Closure content = { Content content ->
		
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
			
			if (content.id) {
				data.restId = "content/${content.id}".toString()
				data.dataRestId = "content/${content.id}/data".toString()
			}
			
			data
		}
	}
	
	static Closure tag = { Tag tag ->

		marshall {
			Map data = [
				name: tag.name,
				type: tag.type.name(),
				uri: tag.uri
			]
			
			if (tag.id) {
				data.restId = "tag/${tag.id}".toString()
			}
			
			data
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
