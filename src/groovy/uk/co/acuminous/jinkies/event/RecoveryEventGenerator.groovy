package uk.co.acuminous.jinkies.event

import java.util.Map
import uk.co.acuminous.jinkies.content.Tag

class RecoveryEventGenerator extends ChainedEventHandler {

	EventService eventService	
	
	@Override
	public void handle(Map event) {
						
		log.debug "Received event: $event"
		
		Event previousEvent = eventService.getLastEvent(event.sourceId)
						
		if (hasRecovered(event, previousEvent)) {
			Map recoveryEvent = makeRecoveryEvent(event, previousEvent)			
			forward recoveryEvent
		}
		
		forward event		
	}
	
	boolean hasRecovered(Map currentEvent, Event previousEvent) {
		Tag error = Tag.findEventTypeByName('Error')	
		currentEvent.type != error && previousEvent && previousEvent.type == error
	}
	
	Map makeRecoveryEvent(Map currentEvent, Event previousEvent) {
		[
			uuid: UUID.randomUUID().toString(),
			sourceId: currentEvent.sourceId,
			type: Tag.findEventTypeByName('Recovery'),
			theme: currentEvent.theme,
			channels: currentEvent.channels,
			timestamp: previousEvent.timestamp + 1
		]
	}
	
	


}
