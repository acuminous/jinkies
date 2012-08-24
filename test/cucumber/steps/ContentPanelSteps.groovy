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

import pages.*
import modules.*
import uk.co.acuminous.jinkies.ci.Job
import uk.co.acuminous.jinkies.content.Content
import static fixtures.HttpStub.*

this.metaClass.mixin(cucumber.runtime.groovy.EN)


When (~'decides to add some content') { ->
	
	page.addContent()
	
	content = new Content()
	content.themes = []
	content.events = []
}

Then (~'display the content\'s (?:details|updated details)') { ->
	
	waitFor panelToLoad
	
	verifyContentWidget(content.title)
}

verifyContentWidget = { title ->
		
	def widget = page.widget(title)
	
	assert content.title == widget.title
	
	String themes = content.themes.collect { it.name }.join(', ')
	assert themes == widget.themes
	
}