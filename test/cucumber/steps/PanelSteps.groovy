package steps

import pages.*
import modules.*

this.metaClass.mixin(cucumber.runtime.groovy.EN)

When (~'deletes (.*)') { String displayName ->
	page.widget(displayName).delete()
}

When (~'edits (.*)') { String displayName ->
	page.widget(displayName).update()
	waitFor { page.dialog.displayed }
}

Then (~'remove (.*) details from display') { String displayName ->
	waitFor { page.widget(displayName).empty }
}

panelToLoad = {
	page.widgets.size() > 0
}