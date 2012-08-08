package uk.co.acuminous.jinkies.jenkins

import spock.lang.Specification
import uk.co.acuminous.jinkies.ci.*
import uk.co.acuminous.jinkies.event.EventHandler

class JenkinsBuildRetrieverSpec extends Specification {

	EventHandler nextHandler = Mock(EventHandler)
	JenkinsServer jenkinsServer = Mock(JenkinsServer)
	JenkinsBuildRetriever retriever = new JenkinsBuildRetriever(nextHandler: nextHandler, server: jenkinsServer)
	
	def "Updates build with full details"() {
		
		given:
			Build build = new BuildBuilder().build(result: null, timestamp:null)
			Map event = [build: build]
					
		when:
			retriever.handle(event)
		
		then:
			1 * jenkinsServer.populateMissingDetailsIn(build)
	}
		
	def "Forwards event to next handler"() {
		
		given:
			Map event = [build: new BuildBuilder().build()]			
		
		when:
			retriever.handle(event)
		
		then:
			1 * nextHandler.handle(event)
	}
	
}
