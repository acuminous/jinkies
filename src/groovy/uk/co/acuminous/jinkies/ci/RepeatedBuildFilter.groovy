package uk.co.acuminous.jinkies.ci

import groovy.util.logging.Slf4j

import java.util.concurrent.ConcurrentHashMap

import uk.co.acuminous.jinkies.event.ChainedEventHandler

@Slf4j
class RepeatedBuildFilter extends ChainedEventHandler {

	ConcurrentHashMap history = new ConcurrentHashMap()
	
	@Override
	public synchronized void handle(Map event) {
		
		log.debug "Received event: $event"		
						
		if (alreadyReported(event)) {
			log.debug "Build $event.build.job.displayName $event.build.number has already been processed"
		} else {			
			addBuildTo event
			forward event		
		}
	}

	private boolean alreadyReported(Map event) {
		history[event.build.job] == event.build
	}
	
	private void addBuildTo(Map event) {
		history[event.build.job] = event.build
	}
}
