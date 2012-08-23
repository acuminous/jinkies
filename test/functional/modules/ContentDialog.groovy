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

class ContentDialog extends Module {

	static content = {
		form( { $('form') } )
		source( { $('#uploadMethod') } )
		text( { $('#text') } )
		title( { $('#title') } )
		description( { $('#description') } )		
		themes( { $('#themes') } )
	}
		
	public void assignChannel(String channel) {
		def checkbox = form.find("#$channel-checkbox")
		checkbox.value(true)
	}
	
	public void unassignChannel(String channel) {
		def checkbox = form.find("#$channel-checkbox")
		checkbox.value(false)
	}
	
	public void addTheme(String theme) {
		def themesElement = form.find("#themes")
		List themes = themesElement.value() ? themesElement.value().split(', ') : []
		themes << theme
		themesElement.value(themes.join(', '))
	}
	
	public void addEvent(String event) {
		if (event == 'Failure') {
			form.find("#failureEvent").value(true)
		} else if (event == 'Success') {
			form.find("#successEvent").value(true)
		} else {
			def otherEventsElement = form.find('#otherEvents')
			List otherEvents = otherEventsElement.value() ? otherEventsElement.value().split(', ') : []
			otherEvents << event
			otherEventsElement.value(otherEvents.join(', '))
		}
		
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
