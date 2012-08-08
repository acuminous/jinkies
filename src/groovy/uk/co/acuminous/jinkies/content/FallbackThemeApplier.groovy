package uk.co.acuminous.jinkies.content

import java.util.Map;

import groovy.util.logging.Slf4j
import uk.co.acuminous.jinkies.event.ChainedEventHandler

@Slf4j
class FallbackThemeApplier extends ChainedEventHandler {

	String fallbackTheme = 'Fallback'
	
	@Override
	void handle(Map event) {
		
		log.debug "Received event: $event"
			
		if (!event.theme) {
			Tag example = new Tag(fallbackTheme, TagType.theme)
			event.theme = Tag.find(example)
		}
					
		forward event
	}		
}
