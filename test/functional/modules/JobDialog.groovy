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
