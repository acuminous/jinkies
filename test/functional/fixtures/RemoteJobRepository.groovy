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
import uk.co.acuminous.jinkies.event.EventHistory
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;


import uk.co.acuminous.jinkies.spec.RemoteUtils

class RemoteJobRepository {

	Job buildRandomJob() {
		buildJob([
			displayName: randomAlphabetic(10),
			url: '.../job/' + randomAlphabetic(10).toLowerCase(),
			type: 'jenkins'
		])
	}	
	
	Job buildJob(Map data) {
		new RemoteUtils().remote {
			Job job = JobBuilder.build(data).save(flush:true)
			assert job.id, job.errors
			job
		}
	}
	
	void failJob(Job job) {
		new RemoteUtils().remote {
			TagService tagService = app.mainContext.getBean('tagService')
			Tag tag = tagService.findOrCreateEvents(['Failure'])[0]
			
			EventHistory eventHistory = app.mainContext.getBean('eventHistory')
			eventHistory.update("job/${job.id}".toString(), tag)			
		}
	}
	
	Job addChannel(String displayName, String channel) {
		new RemoteUtils().remote {
			List results = Job.findAllByDisplayName(displayName)
			Job job = results ? results[0] : null
			job.addToChannels(channel)
			assert job.save(flush:true)
			return job
		}
	}
	
	Job setTheme(String displayName, String themeName) {
		new RemoteUtils().remote {
			
			List results = Job.findAllByDisplayName(displayName)
			Job job = results ? results[0] : null

			TagService tagService = new TagService()
			job.theme = tagService.findOrCreateTag(themeName, TagType.theme)
			
			assert job.save(flush:true)
			return job
		}
	}
	
	List<Job> findAllByUrl(String url) {
		new RemoteUtils().remote {
			Job.findAllByUrlIlike("$url%")
		}
	}
	
	Job findByDisplayName(String displayName) {
		new RemoteUtils().remote {
			// http://jira.grails.org/browse/GRAILS-8915?focusedCommentId=71362#comment-71362
			List results = Job.findAllByDisplayName(displayName)		
			
			// Avoid LazyInitializationException
			results.each {
				"${it.theme}"
			}
				
			results ? results[0] : null
		}	
	}
	
	Integer count() {
		new RemoteUtils().remote {
			Job.count()
		}
	}

}
