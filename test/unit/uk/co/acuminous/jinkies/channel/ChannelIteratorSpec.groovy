package uk.co.acuminous.jinkies.channel

import uk.co.acuminous.jinkies.event.EventHandler

import spock.lang.Specification
import uk.co.acuminous.jinkies.channel.ChannelIterator;


class ChannelIteratorSpec extends Specification {

	Channel audio = new Channel()
	Channel www = new Channel()
	EventHandler nextHandler = Mock(EventHandler)
	ChannelIterator channelIterator = new ChannelIterator(channels: [audio: audio, www: www], nextHandler: nextHandler)
	
	def "Repeats the workflow for each channel"() {
		
		given:
			Map event = [channels: ['audio', 'www']]
			
		when:
			channelIterator.handle(event)
			
		then:
			1 * nextHandler.handle({ Map e -> e.selectedChannel == audio })
			1 * nextHandler.handle({ Map e -> e.selectedChannel == www })
			
	}
}
