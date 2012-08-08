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
