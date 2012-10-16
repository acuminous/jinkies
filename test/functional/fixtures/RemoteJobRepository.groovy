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
package fixtures

import uk.co.acuminous.jinkies.ci.Job
import uk.co.acuminous.jinkies.ci.JobBuilder
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagService
import uk.co.acuminous.jinkies.content.TagType
import uk.co.acuminous.jinkies.event.Event
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;


import uk.co.acuminous.jinkies.spec.RemoteUtils

class RemoteJobRepository {

	TagService tagService = new TagService()
	
	Job buildRandomJob() {
		buildJob([
			displayName: randomAlphabetic(10),
			url: '.../job/' + randomAlphabetic(10).toLowerCase(),
			type: 'jenkins'
		])
	}	
	
	Job buildJob(Map data) {
		Job job = JobBuilder.build(data)
		job.save(flush:true, failOnError:true)
	}
	
	void fail(Job job) {
		Tag tag = tagService.findOrCreateEvents(['Failure'])[0]
		
		Event event = new Event(uuid: UUID.randomUUID().toString(), sourceId: job.resourceId, type: tag, timestamp: System.currentTimeMillis())
		event.save(flush: true, failOnError: true)			
	}
	
	Job addChannel(String displayName, String channel) {
		
		// http://jira.grails.org/browse/GRAILS-8915?focusedCommentId=71362#comment-71362
		List results = Job.findAllByDisplayName(displayName)
		Job job = results ? results[0] : null
		
		job.addToChannels(channel)
		job.save(flush:true, failOnError: true)
	}
	
	Job setTheme(String displayName, String themeName) {

		// http://jira.grails.org/browse/GRAILS-8915?focusedCommentId=71362#comment-71362
		List results = Job.findAllByDisplayName(displayName)
		Job job = results ? results[0] : null

		Tag theme = tagService.findOrCreateTag(themeName, TagType.theme)
		job.theme = theme						
		job.save(flush:true, failOnError: true)
	}

}
