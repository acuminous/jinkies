package steps

import pages.*
import modules.*

this.metaClass.mixin(cucumber.runtime.groovy.EN)

Map pages = [
	jobs: JobsPage
]

When (~'a user opens the (.*) page') { String pageName ->
	destination = pages[pageName]
	to destination
	at destination
}

Then (~'take the user back to the (.*) page') { String pageName ->
		
	waitFor { !page.dialog.displayed }
	
	destination = pages[pageName]
	at destination
}

Then (~'inform the user that "(.*)"') { String message ->	
	assert Alert.message == message 
}
