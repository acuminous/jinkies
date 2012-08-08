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
import uk.co.acuminous.jinkies.content.TagService
import uk.co.acuminous.jinkies.content.TagType
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;


import uk.co.acuminous.jinkies.spec.RemoteMixin

class RemoteJobRepository {

	Job buildRandomJob() {
		buildJob([
			displayName: randomAlphabetic(10),
			url: '.../job/' + randomAlphabetic(10).toLowerCase(),
			type: 'jenkins'
		])
	}	
	
	Job buildJob(Map data) {
		new RemoteMixin().remote {
			Job job = Job.build(data)
			assert job
			return job
		}
	}
	
	Job addChannel(String displayName, String channel) {
		new RemoteMixin().remote {
			List results = Job.findAllByDisplayName(displayName)
			Job job = results ? results[0] : null
			job.addToChannels(channel)
			assert job.save(flush:true)
			return job
		}
	}
	
	Job setTheme(String displayName, String themeName) {
		new RemoteMixin().remote {
			
			List results = Job.findAllByDisplayName(displayName)
			Job job = results ? results[0] : null

			TagService tagService = new TagService()
			job.theme = tagService.findOrCreateTag(themeName, TagType.theme)
			
			assert job.save(flush:true)
			return job
		}
	}
	
	List<Job> findAllByUrl(String url) {
		new RemoteMixin().remote {
			Job.findAllByUrlIlike("$url%")
		}
	}
	
	Job findByDisplayName(String displayName) {
		new RemoteMixin().remote {
			// http://jira.grails.org/browse/GRAILS-8915?focusedCommentId=71362#comment-71362
			List results = Job.findAllByDisplayName(displayName)		
			
			results.each {
				"${it.theme}"
			}
				
			results ? results[0] : null
		}	
	}
	
	Integer count() {
		new RemoteMixin().remote {
			Job.count()
		}
	}

}
