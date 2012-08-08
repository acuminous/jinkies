package uk.co.acuminous.jinkies.ui

class UserInterfaceController {

	static defaultAction = "jobs"
	
	def jobs() {
		render(view: '/ui/jobs')
	}
	
	def themes() {
		render(view: '/ui/themes')
	}
	
	def about() {
		render(view: '/ui/about')
	}	

	def fail() {
		throw new RuntimeException()
	}
}
