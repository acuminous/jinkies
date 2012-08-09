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
package uk.co.acuminous.jinkies.spring

import uk.co.acuminous.jinkies.channel.ChannelIterator
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
