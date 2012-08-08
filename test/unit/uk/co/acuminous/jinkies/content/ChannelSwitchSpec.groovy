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
