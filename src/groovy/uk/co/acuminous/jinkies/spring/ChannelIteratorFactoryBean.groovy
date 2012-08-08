package uk.co.acuminous.jinkies.spring

import uk.co.acuminous.jinkies.channel.Channel
import uk.co.acuminous.jinkies.channel.ChannelIterator
import uk.co.acuminous.jinkies.channel.ChannelSwitch
import uk.co.acuminous.jinkies.event.EventHandler

import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class ChannelIteratorFactoryBean implements FactoryBean<ChannelIterator>, ApplicationContextAware {

	EventHandler nextHandler
	ApplicationContext applicationContext
	
	@Override
	public ChannelIterator getObject() throws Exception {
		Map channels = [:]
		
		Map switches = applicationContext.getBeansOfType(ChannelSwitch)
		switches.each { String beanName, ChannelSwitch channelSwitch ->
			Channel channel = channelSwitch.channel
			String name = channel.name
			channels[name] = channel
		}
		
		return new ChannelIterator(channels: channels, nextHandler:nextHandler);
	}

	@Override
	public Class<?> getObjectType() {
		return ChannelIterator
	}

	@Override
	public boolean isSingleton() {
		return true
	}
}
