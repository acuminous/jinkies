package uk.co.acuminous.jinkies.channel

import java.util.Map;

import groovy.util.logging.Slf4j
import uk.co.acuminous.jinkies.content.ContentPlayer
import uk.co.acuminous.jinkies.event.EventHandler

@Slf4j
class Channel implements EventHandler {

	String name
	List<ContentPlayer> players = []
	
	@Override
	public void handle(Map event) {
		
		log.debug "Received event: $event"
		
		players.each { ContentPlayer player ->
			player.play(event.selectedContent)
		}		
	}
	
	List<String> getContentTypes() {
		Set<String> results = new LinkedHashSet()
		
		players.each { ContentPlayer player ->
			results += player.contentTypes
		}
		
		results as List
	}
	
	@Override
	String toString() {
		"Channel[name=$name,contentTypes=${contentTypes}]"
	}
}
