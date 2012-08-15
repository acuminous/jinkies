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

import fixtures.HttpStub
import pages.*
import modules.*
import uk.co.acuminous.jinkies.ci.Job
import static fixtures.HttpStub.*

this.metaClass.mixin(cucumber.runtime.groovy.EN)


When (~'adds a new Jenkins server') { ->
	
	page.addJenkinsJob()
	
	job = new Job(type: 'jenkins', channels: [])
}

When (~'adds a new Jenkins job called (.*)') { String displayName ->

	page.addJenkinsJob()
		
	job = new Job(displayName: displayName, type: 'jenkins', channels: [])
}

Then (~'display (.*) jobs') { Integer n ->

	assert page.widgets.size() == n	
}

Then (~'display these details') { ->
	
	waitFor panelToLoad
	
	verifyJobWidget(job.displayName)
}

Then (~'display the job\'s (?:details|updated details)') { ->
	
	waitFor panelToLoad
	
	verifyJobWidget(job.displayName)
}

verifyJobWidget = { displayName ->
		
	def widget = page.widget(displayName)
	
	assert job.displayName == widget.displayName
	assert job.theme.name == widget.theme
	
	assert job.channels.size() == widget.channels.size()
	
	job.channels.each { String channelName ->
		assert widget.channel(channelName)
	}
}