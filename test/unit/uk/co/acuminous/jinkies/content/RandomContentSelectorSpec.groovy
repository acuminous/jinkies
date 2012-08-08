package uk.co.acuminous.jinkies.content

import spock.lang.Specification
import uk.co.acuminous.jinkies.ci.*
import uk.co.acuminous.jinkies.content.RandomContentSelector;
import uk.co.acuminous.jinkies.event.EventHandler;


class RandomContentSelectorSpec extends Specification {

	EventHandler nextHandler = Mock(EventHandler)
	RandomContentSelector selector = new RandomContentSelector(nextHandler: nextHandler,)
	
	def "Randomly selects content from candidates"() {
		
		given:
			Map event = [eligibleContent: ['A', 'B', 'C']]
			Map stats = [A: 0, B: 0, C: 0]
			
		when:
			(1..1000).each {
				selector.handle(event)
				stats[event.selectedContent]++
			}
			
		then:
			stats.A + stats.B + stats.C == 1000
			stats.A > 200
			stats.B > 200
			stats.C > 200
			
	}
		
	def "Forwards event to next handler"() {
		
		given:
			Map event = [eligibleContent: ['A', 'B', 'C']]

		when:
			selector.handle(event)
			
		then:
			1 * nextHandler.handle(event)
	}
}
