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

import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import modules.*
import pages.*

this.metaClass.mixin(cucumber.runtime.groovy.EN)

When (~'(?:sets the|the) server url to (.*)') { String serverUrl ->
	page.dialog.url = serverUrl	
	job.url = serverUrl
}

When (~'neglects to specify the server url') { ->
	// BDD Candy
}

When (~'(?:sets the|the) theme to (.*)') { String theme ->
	page.dialog.theme = theme	
	job.theme = new Tag(theme, TagType.theme)
}

When (~'(?:enables the|the) (.*) channel') { String channel ->
	page.dialog.assignChannel(channel)	
	job.channels << channel
}

When (~'(?:disables the|the) (.*) channel') { String channel ->
	page.dialog.unassignChannel(channel)	
	job.channels.remove(channel)
}

When (~'(?:changes the|the) theme from (.*) to (.*)') { String oldTheme, String newTheme ->
	assert page.dialog.theme.value() == oldTheme
	
	page.dialog.theme = newTheme		
	job.theme = new Tag(newTheme, TagType.theme)
}


