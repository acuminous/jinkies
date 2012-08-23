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

When (~'(?:sets the|the) text to "(.*)"') { String text ->
	page.dialog.uploadMethod = 'text'
	page.dialog.text = text	
	content.bytes = text.getBytes()
	content.type = 'text/plain'
}

When (~'(?:sets the|the) title to "(.*)"') { String title ->
	page.dialog.title = title
	content.title = title
}
When (~'(?:adds a|a) theme of (.*)') { String theme ->
	page.dialog.addTheme(theme)
	content.themes << new Tag(theme, TagType.theme)
}

When (~'(?:adds an|an) event type of (.*)') { String event ->
	page.dialog.addEvent(event)	
	content.events << new Tag(event, TagType.event)
}
