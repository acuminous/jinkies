/* 
 * Copyright 2012 Acuminous Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
