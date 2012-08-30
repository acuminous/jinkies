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
package uk.co.acuminous.jinkies.ci

import java.util.List


import grails.converters.JSON
import uk.co.acuminous.jinkies.ci.Job
import uk.co.acuminous.jinkies.content.Tag;
import uk.co.acuminous.jinkies.content.TagService;
import uk.co.acuminous.jinkies.content.TagType
import uk.co.acuminous.jinkies.event.EventHistory
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT
import uk.co.acuminous.jinkies.util.JinkiesErrorRenderer


// Mixins are currently working properly, therefore extending (yuck)
class JobController extends JinkiesErrorRenderer {
	
	EventHistory eventHistory
	
	def show() {
		Job job = Job.get(params.id)
		reportMissing(job) || render(job as JSON)
	}
	
	def list() {
		List<Job> jobs = Job.list(sort: 'url', order: 'asc')	
		render jobs as JSON
	}
	
	def create(JobCommand cmd) {		
		Job job = new Job()
		save(job, cmd)
	}
	
	def update(JobCommand cmd) {
		Job job = Job.get(params.id)
		reportMissing(job) || save(job, cmd)
	}
	
	def delete() {
		Job job = Job.get(params.id)
		if (job) {
			job.delete(flush:true)
			eventHistory.remove(job.resourceId)
			println eventHistory
		}		
		render status: SC_NO_CONTENT 
	}
	
	private void save(job, JobCommand cmd) {
		job.properties = cmd.properties
		job.save(flush: true)
		report(job.errors) || render (job as JSON)
	}
}


class JobCommand {
	
	TagService tagService
		
	String displayName
	String url
	String type
	List<String> channel

	List<String> getChannels() {
		channel ?: []
	}
	
	Tag getTheme() {
		params.theme ? tagService.findOrCreateTag(params.theme, TagType.theme) : null
	}
}
