package uk.co.acuminous.jinkies.channel

import uk.co.acuminous.jinkies.event.EventHandler

import spock.lang.Specification
import uk.co.acuminous.jinkies.channel.ChannelIterator;
import uk.co.acuminous.jinkies.content.Content
import uk.co.acuminous.jinkies.content.ContentPlayer

class ChannelSpec extends Specification {

	EventHandler nextHandler = Mock(EventHandler)
	
	def "Plays content using all available players"() {
				
		given:
			ContentPlayer player1 = Mock(ContentPlayer)
			ContentPlayer player2 = Mock(ContentPlayer)
			Content content = new Content()
			Channel channel = new Channel(name: 'audio', players: [player1, player2])
			
			Map event = [selectedContent: content]
			
		when:
			channel.handle(event)
			
		then:
			1 * player1.play(content)
			1 * player2.play(content)
		
	}

	def "Provides a list of supported content types"() {
		
		given:
			ContentPlayer player1 = Mock(ContentPlayer)
			ContentPlayer player2 = Mock(ContentPlayer)

			Channel channel = new Channel(name: 'audio', players: [player1, player2])
			
		when:
			List<String> contentTypes = channel.contentTypes
		
		then:
			1 * player1.getContentTypes() >> ['audio/mpeg', 'audio/wma']
			1 * player2.getContentTypes() >> ['audio/mpeg', 'audio/wav']
			
			contentTypes == ['audio/mpeg', 'audio/wma', 'audio/wav'] 			
	}
	
}
