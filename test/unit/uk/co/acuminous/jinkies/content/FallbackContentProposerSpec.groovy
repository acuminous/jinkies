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

import grails.test.mixin.Mock
import spock.lang.Specification
import uk.co.acuminous.jinkies.channel.Channel
import uk.co.acuminous.jinkies.content.ContentService
import uk.co.acuminous.jinkies.event.EventHandler

@Mock(Tag)
class FallbackContentProposerSpec extends Specification {

	Channel audioChannel
	Channel ipPowerChannel
	Tag theme
	Tag fallback
	Tag eventType

	EventHandler nextHandler
	ContentService contentService
	FallbackContentProposer proposer

	def setup() {
		audioChannel = [getContentTypes: {['audio/mpeg', 'audio/wav']}] as Channel
		ipPowerChannel = [getContentTypes: {[]}] as Channel
		theme = new Tag('Scooby Doo', TagType.theme).save()
		fallback = new Tag('Fallback', TagType.theme).save()
		eventType = new Tag('Success', TagType.event).save()
	
		nextHandler = Mock(EventHandler)
		contentService = Mock(ContentService)
		proposer = new FallbackContentProposer(fallbackTheme: fallback.name, nextHandler: nextHandler, contentService: contentService)
	}
	
	def "Proposes content from the fallback theme is eligible content has not already been proposed"() {
		
		given:
			Map event = [theme: theme, type: eventType, selectedChannel: audioChannel]
			List results = [1, 2, 3]
			
		when:
			proposer.handle event
			
		then:
			1 * contentService.findAllEligibleContent(fallback, eventType, audioChannel.contentTypes) >> results
			1 * nextHandler.handle(event)			
			event.eligibleContent == results
	}
	
	def "Proposes content from fallback theme when event has no theme"() {
		
		given:
			Map event = [type: eventType, selectedChannel: audioChannel]
			List results = [1, 2, 3]
			
		when:
			proposer.handle event
			
		then:
			1 * contentService.findAllEligibleContent(fallback, eventType, audioChannel.contentTypes) >> results
			1 * nextHandler.handle(event)
			event.eligibleContent == results
	}
	
	def "Does not propose content if eligible content has already been proposed"() {
		
		given:
			List eligibleContent = [1, 2, 3]
			Map event = [theme: theme, type: eventType, selectedChannel: audioChannel, eligibleContent: eligibleContent.clone()]
					
		when:
			proposer.handle event
						
		then:
			0 * contentService._
			1 * nextHandler.handle(event)			
			event.eligibleContent == eligibleContent
	}
	
	def "Does not attempt to propose content when no content types"() {
		
		given:
			Map event = [type: eventType, selectedChannel: ipPowerChannel]
			List results = [1, 2, 3]
			
		when:
			proposer.handle event
			
		then:
			0 * contentService._
			1 * nextHandler.handle(event)
			event.eligibleContent == []
	}
		
	def "Tollerates no eligible content"() {
		
		given:
			Map event = [type: eventType, selectedChannel: audioChannel]
			
		when:
			proposer.handle event
			
		then:
			1 * contentService.findAllEligibleContent(fallback, eventType, audioChannel.contentTypes) >> []		
			1 * nextHandler.handle(event)
			event.eligibleContent == []			
	}
}
