package uk.co.acuminous.jinkies.spring

import uk.co.acuminous.jinkies.content.ContentPlayer
import uk.co.acuminous.jinkies.content.ContentService

import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class ContentServiceFactoryBean implements FactoryBean<ContentService>, ApplicationContextAware {

	ApplicationContext applicationContext
	
	@Override
	public ContentService getObject() throws Exception {
		List players = []
		
		applicationContext.getBeansOfType(ContentPlayer).each { String beanName, ContentPlayer player ->
			players << player
		}
		
		return new ContentService(players: players);
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
