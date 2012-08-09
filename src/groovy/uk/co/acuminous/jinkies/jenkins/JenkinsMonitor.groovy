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

import groovy.util.logging.Slf4j
import uk.co.acuminous.jinkies.channel.ChannelIterator
import uk.co.acuminous.jinkies.ci.Build
import uk.co.acuminous.jinkies.ci.Job
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import uk.co.acuminous.jinkies.event.EventHandler
import uk.co.acuminous.jinkies.json.CustomJsonMarshaller


@Slf4j
class JenkinsMonitor {

	JenkinsServer server
	EventHandler eventHandler
	EventHandler errorHandler
	
	void check() {
		List<Job> jobs = Job.findAllByType('jenkins')
		jobs.each checkLatestBuild
	}
	
	def checkLatestBuild = { Job job ->
		
		String restId = CustomJsonMarshaller.job(job).restId
		 
		try {
			log.debug("Checking for new $job.displayName builds")
			
			List<Build> history = server.getBuildHistory(job)
			
			if (history) {				
				Map event = [target: restId, build: history.first()]
				eventHandler.handle event
			}
			
		} catch (Exception e) {
			log.error("Error checking job: $job", e)
						
			Map event = [
				target: restId,
				type: Tag.findEventTypeByName('Error'),
				theme: job.theme,
				channels: job.channels
			]
			
			println event
			
			errorHandler.handle event				
		}
	}
	
}