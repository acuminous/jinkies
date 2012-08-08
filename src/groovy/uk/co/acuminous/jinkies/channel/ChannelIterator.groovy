package uk.co.acuminous.jinkies.channel

import groovy.util.logging.Slf4j

import uk.co.acuminous.jinkies.event.ChainedEventHandler

@Slf4j
class ChannelIterator extends ChainedEventHandler {

	Map<String, Channel> channels
	
	@Override
	void handle(Map event) {
		
		log.debug "Received event: $event"
					
		event.channels.each { String channelName ->	
					
			log.debug "Received event: $event"			
			
			event.selectedChannel = channels[channelName]
			
			forward event
		}
		
	}
}
