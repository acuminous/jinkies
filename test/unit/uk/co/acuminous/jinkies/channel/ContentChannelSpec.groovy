/* 
] * Copyright 2012 Acuminous Ltd
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
package uk.co.acuminous.jinkies.channel

import uk.co.acuminous.jinkies.event.EventHandler

import spock.lang.Specification
import uk.co.acuminous.jinkies.channel.ChannelIterator;
import uk.co.acuminous.jinkies.content.Content
import uk.co.acuminous.jinkies.content.ContentPlayer

class ContentChannelSpec extends Specification {

	EventHandler nextHandler = Mock(EventHandler)
	
	def "Plays content using all available players"() {
				
		given:
			ContentPlayer player1 = Mock(ContentPlayer)
			ContentPlayer player2 = Mock(ContentPlayer)
			Content content = new Content()
			ContentChannel channel = new ContentChannel(name: 'audio', players: [player1, player2])
			
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

			ContentChannel channel = new ContentChannel(name: 'audio', players: [player1, player2])
			
		when:
			List<String> contentTypes = channel.contentTypes
		
		then:
			1 * player1.getContentTypes() >> ['audio/mpeg', 'audio/wma']
			1 * player2.getContentTypes() >> ['audio/mpeg', 'audio/wav']
			
			contentTypes == ['audio/mpeg', 'audio/wma', 'audio/wav'] 			
	}
	
	
	def "Tollerates no content"() {
				
		given:
			ContentPlayer player = Mock(ContentPlayer)
			ContentChannel channel = new ContentChannel(name: 'audio', players: [player])
			
		when:
			channel.handle(event)
			
		then:
			0 * player._
			
		where: 
			event << [ [:], [selectedContent: null] ]
		
	}
	
}
