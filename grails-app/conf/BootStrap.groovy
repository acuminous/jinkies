import org.codehaus.groovy.grails.commons.env.GrailsEnvironment;

import grails.util.Environment
import uk.co.acuminous.jinkies.content.*
import uk.co.acuminous.jinkies.json.CustomJsonMarshaller
import grails.converters.JSON

import uk.co.acuminous.jinkies.ci.Job



class BootStrap {

    def init = { servletContext ->
		JSON.registerObjectMarshaller(Job, CustomJsonMarshaller.job)
		JSON.registerObjectMarshaller(Content, CustomJsonMarshaller.content)
		JSON.registerObjectMarshaller(Tag, CustomJsonMarshaller.tag)
    }
	
    def destroy = {
    }
}
