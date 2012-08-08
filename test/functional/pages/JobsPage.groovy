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
	
	public void addJenkinsJob() {
		$('.job.widget.add').click()
		waitFor { dialog.displayed }
	}

}
