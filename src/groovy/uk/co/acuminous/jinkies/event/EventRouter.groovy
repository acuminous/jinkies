package uk.co.acuminous.jinkies.event

import java.util.Map
import uk.co.acuminous.jinkies.content.Tag

class EventRouter implements EventHandler {

	EventHandler jobEventHandler
	EventHandler errorEventHandler
	EventHandler otherEventHandler
	
	@Override
	public void handle(Map event) {
		
		if (isError(event)) {
			errorEventHandler.handle event
		} else if (event.job) {
			jobEventHandler.handle event
		} else {
			otherEventHandler.handle event
		}		
	}
	
	boolean isError(event) {
		event.type == Tag.findEventTypeByName('Error')
	}

}
