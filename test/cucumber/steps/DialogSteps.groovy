package steps

import pages.*
import modules.*

this.metaClass.mixin(cucumber.runtime.groovy.EN)

When (~'proceeds') { ->
	page.dialog.ok()
}

When (~'attempts to proceed') { ->
	Alert.expected = true
	page.dialog.ok()	
}

When (~'cancels') { ->
	page.dialog.cancel()
}