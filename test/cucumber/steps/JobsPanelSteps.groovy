package steps

import pages.*
import modules.*
import uk.co.acuminous.jinkies.ci.Job

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