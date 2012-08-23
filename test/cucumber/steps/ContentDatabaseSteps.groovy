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
package steps

import uk.co.acuminous.jinkies.content.Content
import fixtures.RemoteContentRepository

this.metaClass.mixin(cucumber.runtime.groovy.EN)

RemoteContentRepository contentRepository = new RemoteContentRepository()

Given (~'(?:that there are )?(\\d+) mp3s with the (.*) theme') { int number, String theme ->
	content = []
	number.times {
		content << contentRepository.buildRandomContent(theme)		
	}
}

Then (~'create the content in the database') { ->
	
	Content databaseContent
	waitFor {
		databaseContent = contentRepository.findByTitle(content.title)
	}
	verifyDatabaseContent(databaseContent)
}
	
verifyDatabaseContent = { Content databaseContent ->
	assert databaseContent != null
	assert databaseContent.title == content.title
	assert databaseContent.bytes == content.bytes
	assert databaseContent.type == content.type
	
	assert databaseContent.themes.collect { it.name } == content.themes.collect { it.name }
	assert databaseContent.events.collect { it.name } == content.events.collect { it.name }
}

