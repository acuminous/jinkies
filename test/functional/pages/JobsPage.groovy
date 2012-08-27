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
package pages

import geb.Page

import modules.JobWidget
import modules.JobDialog

class JobsPage extends Page {
		
	static url = "/"
	
	static at = {
		title ==~ /Jobs/
	}
	
	static content = {
		widgets(required: false) { moduleList JobWidget, $('.panel .widget').not('.fake, .prototype') }
		widget(required: false) { String name -> module JobWidget, $('.panel .widget').not('.fake, .prototype').find('.name', text: name).parent() }
		dialog { module JobDialog, $('#job-dialog') }
	}
	
	void addJenkinsJob() {
		$('.job.widget.add').click()
		waitFor { dialog.displayed }
	}
	
	void applyFilter(String filterDisplayName) {
		int counter = 0;
		while ($('#filter-name').text() != filterDisplayName) {
			$('#filter').click()
			counter++
			assert counter < 100, "Cannot apply filter $filterDisplayName"
		}	
	}

	void refresh() {
		$('#refresh').click()
	}
}
