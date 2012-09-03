package uk.co.acuminous.jinkies.event

class EventService {

	synchronized boolean exists(String uuid) {
		Event.findByUuid(uuid)
	}
	
	synchronized Event save(Map data) {
		Event event = new Event(data)
		event.save(flush:true, failOnError:true)
	}
	
	Event getLastEvent(String resourceId) {
		def c = Event.createCriteria()
		Event previous = c.get {
			eq('resourceId', resourceId)
			maxResults(1)
			order('timestamp', 'desc')
		}
	}
	
}
