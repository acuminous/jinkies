package uk.co.acuminous.jinkies.channel

import java.util.List;

import uk.co.acuminous.jinkies.content.Content
import uk.co.acuminous.jinkies.content.ContentPlayer;

class ContentChannel extends BaseChannel {

	List<ContentPlayer> players = []
	
	@Override
	public void handle(Map event) {
		
		log.debug "Received event: $event"
				
		if (event.selectedContent) {							
			players.each { ContentPlayer player ->	
				Content content = event.selectedContent
				if (player.isSupported(content)) {							
					player.play(content, event)
				}
			}
		}
	}
	
	@Override	
	List<String> getContentTypes() {
		Set<String> results = new LinkedHashSet()
		
		players.each { ContentPlayer player ->
			results += player.contentTypes
		}
		
		results as List
	}
}
