package uk.co.acuminous.jinkies.channel

import groovy.util.logging.Slf4j

import uk.co.acuminous.jinkies.event.ChainedEventHandler
import uk.co.acuminous.jinkies.event.EventHandler

@Slf4j
class ChannelSwitch extends ChainedEventHandler {

	EventHandler channel
	
	@Override
	public void handle(Map event) {
		
		log.debug "Received event: $event"
		
		if (event.selectedChannel == channel) {
			channel.handle event
		} else {
			log.debug "Bypassing $channel on this occaision"
			forward event
		}
	}	
}
