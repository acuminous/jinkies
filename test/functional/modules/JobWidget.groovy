package modules

import geb.Module

class JobWidget extends Widget {

	static content = {
		displayName { $('.name .text').text() }
		theme { $('.theme .text').text() }
		channels(required: false) { $('.channel') }
		channel { String name -> $('.channel.' + name) }
	}
}
