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

import uk.co.acuminous.jinkies.channel.Channel
import uk.co.acuminous.jinkies.channel.ChannelSwitch
import uk.co.acuminous.jinkies.event.EventHandler

import spock.lang.Specification


class ChannelSwitchSpec extends Specification {

	EventHandler nextHandler = Mock(EventHandler)
	EventHandler channel = Mock(EventHandler)
	ChannelSwitch channelSwitch = new ChannelSwitch(channel: channel, nextHandler: nextHandler)
	
	def "Forwards events destined for other channels"() {
		
		given:
			Map event = [selectedChannel: 'other']
		
		when:
			channelSwitch.handle(event)
		
		then:
			0 * channel._
			1 * nextHandler.handle(event)
	}

	def "Handles events destined for this channel"() {
		
		given:
			Map event = [selectedChannel: channel]
		
		when:
			channelSwitch.handle(event)
		
		then:		
			1 * channel.handle(event)
			0 * nextHandler._
	}
}
