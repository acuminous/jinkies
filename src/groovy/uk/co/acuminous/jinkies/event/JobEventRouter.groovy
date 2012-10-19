package uk.co.acuminous.jinkies.event

import java.util.Map;

class JobEventRouter implements EventHandler {

	EventHandler jobEventHandler
	EventHandler otherEventHandler
	
	@Override
	public void handle(Map event) {
		
		if (event.job) {
			jobEventHandler.handle event
		} else {
			otherEventHandler.handle event
		}
		
	}

}
