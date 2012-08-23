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
import uk.co.acuminous.jinkies.channel.Channel
import uk.co.acuminous.jinkies.content.ContentProposer
import uk.co.acuminous.jinkies.content.ContentService
import uk.co.acuminous.jinkies.event.EventHandler


class ContentProposerSpec extends Specification {

	Channel audioChannel = [getContentTypes: {['audio/mpeg', 'audio/wav']}] as Channel
	Channel ipPowerChannel = [getContentTypes: {[]}] as Channel
	Tag theme = new Tag('Scooby Doo', TagType.theme)
	Tag eventType = new Tag('Success', TagType.event)

	EventHandler nextHandler = Mock(EventHandler)
	ContentService contentService = Mock(ContentService)
	ContentProposer proposer = new ContentProposer(nextHandler: nextHandler, contentService: contentService)
	
	def "Proposes candidate content from tags and content type"() {
		
		given:
			Map event = [theme: theme, type: eventType, selectedChannel: audioChannel]
			List results = [1, 2, 3]
			
		when:
			proposer.handle(event)
			
		then:
			1 * contentService.findAllEligibleContent(theme, eventType, audioChannel.contentTypes) >> results
			1 * nextHandler.handle(event)			
			event.eligibleContent == results
	}

	def "Uses prescribed content is specified"() {
		
		given:
			List prescribedContent = [1, 2, 3]
			Map event = [theme: theme, type: eventType, selectedChannel: audioChannel, prescribedContent: prescribedContent]
			
		when:
			proposer.handle(event)
			
		then:
			0 * contentService._
			1 * nextHandler.handle(event)			
			event.eligibleContent == prescribedContent
	}
	
	def "Does not attempt to propose content when no content types"() {
		
		given:
			Map event = [theme: theme, type: eventType, selectedChannel: ipPowerChannel]
			List results = [1, 2, 3]
			
		when:
			proposer.handle(event)
			
		then:
			0 * contentService._
			1 * nextHandler.handle(event)
			event.eligibleContent == []
	}
		
	def "Tollerates no eligible content"() {
		
		given:
			Map event = [theme: theme, type: eventType, selectedChannel: audioChannel]
			
		when:
			proposer.handle(event)
			
		then:
			1 * contentService.findAllEligibleContent(theme, eventType, audioChannel.contentTypes) >> []		
			1 * nextHandler.handle(event)
			event.eligibleContent == []			
	}
	
	
	def "Tollerates null theme"() {
		
		given:
			Map event = [type: eventType, selectedChannel: audioChannel]
			
		when:
			proposer.handle(event)
			
		then:
			1 * contentService.findAllEligibleContent(null, _, _)		
			1 * nextHandler.handle(event)			
	}
}
