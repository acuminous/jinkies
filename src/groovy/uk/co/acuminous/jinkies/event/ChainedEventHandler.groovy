package uk.co.acuminous.jinkies.event

abstract class ChainedEventHandler implements EventHandler {
	
	EventHandler nextHandler
	
	void forward(Map event) {
		nextHandler.handle event
	}
}
