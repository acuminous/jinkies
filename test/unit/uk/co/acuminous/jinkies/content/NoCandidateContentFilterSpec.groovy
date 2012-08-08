package uk.co.acuminous.jinkies.content

import spock.lang.Specification
import spock.lang.Unroll
import uk.co.acuminous.jinkies.content.NoCandidateContentFilter
import uk.co.acuminous.jinkies.event.EventHandler

class NoCandidateContentFilterSpec extends Specification {

	EventHandler nextHandler = Mock(EventHandler)
	NoCandidateContentFilter filter = new NoCandidateContentFilter(nextHandler: nextHandler)	

	@Unroll("Supresses events with #content content")
	def "Suppresses events with no candidate or selected content"() {
		
		given:
			Map event = [eligibleContent: content]
			
		when:
			filter.handle(event)
			
		then:
			0 * nextHandler.handle(event)
			
		where: 
			content << [[], null]
		
	}
	
	@Unroll("Forwards events with #content content")
	def "Forwards events with content"() {
		
		given:
			Map event = [eligibleContent: content]
			
		when:
			filter.handle(event)
			
		then:
			1 * nextHandler.handle(event)
			
		where:
			content << [ 'some content' ]
		
	}
	
}
