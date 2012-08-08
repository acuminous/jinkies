package uk.co.acuminous.jinkies.jenkins

import groovy.util.logging.Slf4j
import uk.co.acuminous.jinkies.event.ChainedEventHandler

@Slf4j
class JenkinsBuildRetriever extends ChainedEventHandler {

	JenkinsServer server
	
	@Override
	public void handle(Map event) {
		
		log.debug "Received event: $event"		
		
		log.info "Retrieving build details for $event.build.job.displayName #$event.build.number"
		
		server.populateMissingDetailsIn event.build
		
		forward event
	}

		
	
}
