package uk.co.acuminous.jinkies.ci

import groovy.util.logging.Slf4j

import uk.co.acuminous.jinkies.event.ChainedEventHandler
import uk.co.acuminous.jinkies.content.*

@Slf4j
class BuildTagCollator extends ChainedEventHandler {
	
	@Override
	void handle(Map event) {
		
		log.debug "Received event: $event"
							
		event.type = Tag.findEventTypeByName(event.build.result)
		event.theme = event.build.job.theme
		event.channels = event.build.job.channels

		forward event
	}
}
