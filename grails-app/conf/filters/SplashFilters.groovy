package filters

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND
import org.codehaus.groovy.grails.commons.GrailsApplication
class SplashFilters {

	GrailsApplication grailsApplication
	
	def filters = {
		
		if (grailsApplication.config.jinkies.splash?.enabled) {		
			splash(uri: '/') {
				before = {
					render view:'/ui/about'
					false
				}
			}
			hide(uri: '/', invert: true) {
				before = {
					response.sendError SC_NOT_FOUND
					false 
				}
			}
		}
	}
}
