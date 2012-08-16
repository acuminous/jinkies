package uk.co.acuminous.jinkies.admin

class VersionController {

	def grailsApplication
	
	def index = {
		render grailsApplication.metadata['app.version']
	}
	
}
