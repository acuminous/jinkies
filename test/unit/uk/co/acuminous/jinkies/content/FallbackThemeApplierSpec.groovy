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
import uk.co.acuminous.jinkies.content.FallbackThemeApplier
import uk.co.acuminous.jinkies.event.EventHandler

import spock.lang.Specification
import uk.co.acuminous.jinkies.content.Tag;
import grails.buildtestdata.mixin.Build
import static uk.co.acuminous.jinkies.util.AssertionUtils.*

@Build(Tag)
class FallbackThemeApplierSpec extends Specification {

	EventHandler nextHandler = Mock(EventHandler)
	FallbackThemeApplier fallback = new FallbackThemeApplier(nextHandler: nextHandler)
	
	def "Does nothing when event has a theme"() {
		
		given:
			Tag bespokeTheme = new Tag('Scooby Doo', TagType.theme).save(flush:true)
			Map event = [theme: bespokeTheme]
			
		when:
			fallback.handle(event)
			
		then:
			event.theme == bespokeTheme
			1 * nextHandler.handle(event)			
	}
	
	def "Applies default theme if necessary"() {
				
		given:
			Tag fallbackTheme = new Tag('Fallback', TagType.theme).save(flush:true)
			Map event = [:]
			
		when:
			fallback.handle(event)
			
		then:			
			event.theme == fallbackTheme 		
	}
}
