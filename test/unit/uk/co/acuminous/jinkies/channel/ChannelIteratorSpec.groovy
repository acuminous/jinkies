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
package uk.co.acuminous.jinkies.channel

import uk.co.acuminous.jinkies.event.EventHandler

import spock.lang.Specification
import uk.co.acuminous.jinkies.channel.ChannelIterator;


class ChannelIteratorSpec extends Specification {

	ContentChannel audio = new ContentChannel()
	ContentChannel www = new ContentChannel()
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
