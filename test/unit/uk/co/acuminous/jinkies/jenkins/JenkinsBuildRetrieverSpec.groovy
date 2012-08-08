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
