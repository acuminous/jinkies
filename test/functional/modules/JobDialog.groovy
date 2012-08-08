package modules

import geb.Module

class JobDialog extends Module {

	static content = {
		form( { $('form') } )
		url( { $('#serverUrl') } )
		theme( { $('#theme') } )
	}
		
	public void assignChannel(String channel) {
		def checkbox = form.find("#$channel-checkbox")
		checkbox.value(true)
	}
	
	public void unassignChannel(String channel) {
		def checkbox = form.find("#$channel-checkbox")
		checkbox.value(false)
	}
	
	public void ok() {
		
		def okButton = form.find('.ok')
		if (Alert.expected) {
			Alert.message = withAlert(wait: true) { okButton.click() }
			Alert.expected = false
		} else {
			withNoAlert { okButton.click() }
		}
	}
	
	public void cancel() {
		form.find('.cancel').click()
	}
	
}
