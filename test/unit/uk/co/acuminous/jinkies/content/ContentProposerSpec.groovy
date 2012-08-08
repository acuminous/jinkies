package uk.co.acuminous.jinkies.content

import spock.lang.Specification
import uk.co.acuminous.jinkies.channel.Channel
import uk.co.acuminous.jinkies.content.ContentProposer
import uk.co.acuminous.jinkies.content.ContentService
import uk.co.acuminous.jinkies.event.EventHandler


class ContentProposerSpec extends Specification {

	Channel audioChannel = [getContentTypes: {['audio/mpeg', 'audio/wav']}] as Channel
	Channel nullChannel = [getContentTypes: {[]}] as Channel
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
